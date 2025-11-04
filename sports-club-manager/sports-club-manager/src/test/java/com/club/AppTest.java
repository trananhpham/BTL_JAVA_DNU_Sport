
package com.club;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.club.model.Attendance;
import com.club.model.AttendanceStatus;
import com.club.model.Coach;
import com.club.model.Competition;
import com.club.model.Equipment;
import com.club.model.EquipmentStatus;
import com.club.model.FeeStatus;
import com.club.model.League;
import com.club.model.Member;
import com.club.model.MembershipFee;
import com.club.model.Result;
import com.club.model.Schedule;
import com.club.model.ScheduleStatus;
import com.club.model.Sport;
import com.club.repo.AchievementRepository;
import com.club.repo.AttendanceRepository;
import com.club.repo.CoachRepository;
import com.club.repo.CompetitionRepository;
import com.club.repo.EquipmentRepository;
import com.club.repo.MemberRepository;
import com.club.repo.MembershipFeeRepository;
import com.club.repo.ResultRepository;
import com.club.repo.ScheduleRepository;
import com.club.repo.SportRepository;
import com.club.service.DataBootstrap;
import com.club.service.ReportService;
import com.club.service.SchedulingService;

public class AppTest {
    static String tmp = System.getProperty("user.dir") + "/data_test";
    MemberRepository m; CoachRepository c; SportRepository s; ScheduleRepository sch;
    CompetitionRepository comp; ResultRepository res; AchievementRepository ach;
    MembershipFeeRepository fee; AttendanceRepository att; EquipmentRepository eq;
    SchedulingService scheduling; ReportService report; DataBootstrap boot;

    @BeforeEach
    void setup(){
        new java.io.File(tmp).mkdirs();
        m = new MemberRepository(tmp+"/members.csv");
        c = new CoachRepository(tmp+"/coaches.csv");
        s = new SportRepository(tmp+"/sports.csv");
        sch = new ScheduleRepository(tmp+"/schedules.csv");
        comp = new CompetitionRepository(tmp+"/competitions.csv");
        res = new ResultRepository(tmp+"/results.csv");
        ach = new AchievementRepository(tmp+"/achievements.csv");
        fee = new MembershipFeeRepository(tmp+"/fees.csv");
        att = new AttendanceRepository(tmp+"/attendance.csv");
        eq = new EquipmentRepository(tmp+"/equipment.csv");
        boot = new DataBootstrap(m,c,s,sch,comp,res,ach,fee,att,eq);
        boot.loadAll(); boot.ensureSampleData();
        scheduling = new SchedulingService(sch);
        report = new ReportService(m,c,s,sch,comp,res,ach);
    }

    @Test
    void test1_add_member(){
        int before = m.all().size();
        m.add(new Member(null,"New","090"));
        assertEquals(before+1, m.all().size());
    }

    @Test
    void test2_add_coach(){
        int before = c.all().size();
        c.add(new Coach(null,"NewCoach","091"));
        assertEquals(before+1, c.all().size());
    }

    @Test
    void test3_assign_coach_to_sport(){
        Coach coach = c.all().iterator().next();
        Sport sport = s.all().iterator().next();
        coach.addSport(sport.getId());
        assertTrue(coach.getSportIds().contains(sport.getId()));
    }

    @Test
    void test4_schedule_ok(){
        Sport sport = s.all().iterator().next();
        String coachId = sport.getCoachId();
        String memberId = m.all().iterator().next().getId();
        Schedule sc = new Schedule(null, sport.getId(), coachId, memberId,
                LocalDate.now().plusDays(1), LocalTime.of(9,0), 60, ScheduleStatus.SCHEDULED);
        scheduling.createSchedule(sc);
        assertTrue(sch.all().stream().anyMatch(new java.util.function.Predicate<Schedule>() {
            @Override
            public boolean test(Schedule x) {
                return x.getId().equals(sc.getId());
            }
        }));
    }

    @Test
    void test5_schedule_conflict(){
        Sport sport = s.all().iterator().next();
        String coachId = sport.getCoachId();
        String memberId = m.all().iterator().next().getId();
        Schedule sc1 = new Schedule(null, sport.getId(), coachId, memberId,
                LocalDate.now().plusDays(2), LocalTime.of(10,0), 60, ScheduleStatus.SCHEDULED);
        scheduling.createSchedule(sc1);
        Schedule sc2 = new Schedule(null, sport.getId(), coachId, memberId,
                sc1.getDate(), sc1.getTime().plusMinutes(30), 60, ScheduleStatus.SCHEDULED);
        assertThrows(com.club.exceptions.ScheduleConflictException.class, new org.junit.jupiter.api.function.Executable() {
            @Override
            public void execute() throws Throwable {
                scheduling.createSchedule(sc2);
            }
        });
    }

    @Test
    void test6_create_competition(){
        int before = comp.all().size();
        comp.add(new League(null,"Test League", LocalDate.now()));
        assertEquals(before+1, comp.all().size());
    }

    @Test
    void test7_add_member_to_competition_and_result(){
        Competition latest = comp.all().stream().findFirst().get();
        String mid = m.all().iterator().next().getId();
        res.add(new Result(latest.getId(), mid, 88, 2));
        assertTrue(res.all().stream().anyMatch(new java.util.function.Predicate<Result>() {
            @Override
            public boolean test(Result r) {
                return r.getMemberId().equals(mid) && r.getCompetitionId().equals(latest.getId());
            }
        }));
    }

    @Test
    void test8_update_competition_results_sorted(){
        Competition latest = comp.all().stream().max(java.util.Comparator.comparing(Competition::getDate)).get();
        List<Result> ranking = report.latestCompetitionRanking();
        for (int i=1;i<ranking.size();i++){
            assertTrue(ranking.get(i-1).getRank() <= ranking.get(i).getRank());
        }
    }

    @Test
    void test9_report_members_per_sport(){
        Map<String, Long> mp = report.membersPerSport();
        assertFalse(mp.isEmpty());
    }

    @Test
    void test10_top5_members(){
        var top = report.top5MembersAchievements();
        assertTrue(top.size() <= 5);
    }

    @Test
    void test11_add_membership_fee(){
        Member member = m.all().iterator().next();
        int before = fee.all().size();
        MembershipFee mFee = new MembershipFee(null, member.getId(), 500000, 
                                               LocalDate.now(), LocalDate.now().plusMonths(6), "CASH", FeeStatus.PAID);
        fee.add(mFee);
        assertEquals(before + 1, fee.all().size());
    }

    @Test
    void test12_check_fee_expiring_soon(){
        Member member = m.all().iterator().next();
        MembershipFee mFee = new MembershipFee(null, member.getId(), 500000, 
                                               LocalDate.now(), LocalDate.now().plusDays(3), "CASH", FeeStatus.PAID);
        fee.add(mFee);
        assertTrue(mFee.isExpiringSoon(7));
        assertFalse(mFee.isExpired());
    }

    @Test
    void test13_add_attendance(){
        Schedule schedule = sch.all().iterator().next();
        Member member = m.all().iterator().next();
        int before = att.all().size();
        Attendance attendance = new Attendance(null, schedule.getId(), member.getId(), 
                                              LocalDate.now(), LocalTime.of(17, 0), null, AttendanceStatus.PRESENT, "");
        att.add(attendance);
        assertEquals(before + 1, att.all().size());
    }

    @Test
    void test14_calculate_attendance_rate(){
        Member member = m.all().iterator().next();
        Schedule schedule = sch.all().iterator().next();
        
        // Add multiple attendance records
        att.add(new Attendance(null, schedule.getId(), member.getId(), 
                              LocalDate.now(), LocalTime.of(17, 0), null, AttendanceStatus.PRESENT, ""));
        att.add(new Attendance(null, schedule.getId(), member.getId(), 
                              LocalDate.now().minusDays(1), LocalTime.of(17, 0), null, AttendanceStatus.PRESENT, ""));
        att.add(new Attendance(null, schedule.getId(), member.getId(), 
                              LocalDate.now().minusDays(2), LocalTime.of(17, 0), null, AttendanceStatus.ABSENT, ""));
        
        double rate = att.getAttendanceRate(member.getId());
        assertTrue(rate >= 0 && rate <= 100);
    }

    @Test
    void test15_add_equipment(){
        int before = eq.all().size();
        Equipment equipment = new Equipment(null, "Test Ball", "Football", 10, EquipmentStatus.AVAILABLE,
                                           LocalDate.now(), null, "Storage", 50000);
        eq.add(equipment);
        assertEquals(before + 1, eq.all().size());
    }

    @Test
    void test16_equipment_needs_maintenance(){
        Equipment oldEquip = new Equipment(null, "Old Ball", "Football", 5, EquipmentStatus.AVAILABLE,
                                          LocalDate.now().minusYears(2), LocalDate.now().minusMonths(12), "Storage", 30000);
        eq.add(oldEquip);
        assertTrue(oldEquip.needsMaintenance(6));
    }

    @Test
    void test17_get_equipment_by_category(){
        Equipment ball1 = new Equipment(null, "Ball 1", "Football", 10, EquipmentStatus.AVAILABLE,
                                       LocalDate.now(), null, "Storage", 50000);
        Equipment ball2 = new Equipment(null, "Ball 2", "Football", 5, EquipmentStatus.AVAILABLE,
                                       LocalDate.now(), null, "Storage", 40000);
        eq.add(ball1);
        eq.add(ball2);
        
        List<Equipment> footballEquip = eq.getByCategory("Football");
        assertTrue(footballEquip.size() >= 2);
    }

    @Test
    void test18_get_latest_fee_by_member(){
        Member member = m.all().iterator().next();
        MembershipFee fee1 = new MembershipFee(null, member.getId(), 500000, 
                                               LocalDate.now().minusMonths(6), LocalDate.now(), "CASH", FeeStatus.PAID);
        MembershipFee fee2 = new MembershipFee(null, member.getId(), 600000, 
                                               LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(5), "CARD", FeeStatus.PAID);
        fee.add(fee1);
        fee.add(fee2);
        
        MembershipFee latest = fee.getLatestByMember(member.getId());
        assertNotNull(latest);
        assertEquals(600000, latest.getAmount());
    }

    @Test
    void test19_get_attendance_by_member(){
        Member member = m.all().iterator().next();
        Schedule schedule = sch.all().iterator().next();
        
        Attendance att1 = new Attendance(null, schedule.getId(), member.getId(), 
                                        LocalDate.now(), LocalTime.of(17, 0), null, AttendanceStatus.PRESENT, "");
        Attendance att2 = new Attendance(null, schedule.getId(), member.getId(), 
                                        LocalDate.now().minusDays(1), LocalTime.of(17, 0), null, AttendanceStatus.LATE, "");
        att.add(att1);
        att.add(att2);
        
        List<Attendance> records = att.getByMember(member.getId());
        assertTrue(records.size() >= 2);
    }

    @Test
    void test20_equipment_total_quantity(){
        Equipment eq1 = new Equipment(null, "Item 1", "Test", 10, EquipmentStatus.AVAILABLE,
                                     LocalDate.now(), null, "Storage", 10000);
        Equipment eq2 = new Equipment(null, "Item 2", "Test", 20, EquipmentStatus.AVAILABLE,
                                     LocalDate.now(), null, "Storage", 20000);
        eq.add(eq1);
        eq.add(eq2);
        
        int total = eq.getTotalQuantity();
        assertTrue(total >= 30);
    }
}
