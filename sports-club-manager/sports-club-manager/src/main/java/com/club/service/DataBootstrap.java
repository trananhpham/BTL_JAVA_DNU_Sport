
package com.club.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.club.model.Achievement;
import com.club.model.Attendance;
import com.club.model.AttendanceStatus;
import com.club.model.Badminton;
import com.club.model.Basketball;
import com.club.model.Coach;
import com.club.model.Competition;
import com.club.model.Equipment;
import com.club.model.EquipmentStatus;
import com.club.model.FeeStatus;
import com.club.model.Football;
import com.club.model.League;
import com.club.model.Member;
import com.club.model.MembershipFee;
import com.club.model.Result;
import com.club.model.Schedule;
import com.club.model.ScheduleStatus;
import com.club.model.Sport;
import com.club.model.Tennis;
import com.club.model.Tournament;
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

public class DataBootstrap {
    private final MemberRepository memberRepo;
    private final CoachRepository coachRepo;
    private final SportRepository sportRepo;
    private final ScheduleRepository scheduleRepo;
    private final CompetitionRepository competitionRepo;
    private final ResultRepository resultRepo;
    private final AchievementRepository achievementRepo;
    private final MembershipFeeRepository feeRepo;
    private final AttendanceRepository attendanceRepo;
    private final EquipmentRepository equipmentRepo;

    public DataBootstrap(MemberRepository memberRepo, CoachRepository coachRepo, SportRepository sportRepo,
                         ScheduleRepository scheduleRepo, CompetitionRepository competitionRepo,
                         ResultRepository resultRepo, AchievementRepository achievementRepo,
                         MembershipFeeRepository feeRepo, AttendanceRepository attendanceRepo,
                         EquipmentRepository equipmentRepo){
        this.memberRepo = memberRepo; this.coachRepo = coachRepo; this.sportRepo = sportRepo;
        this.scheduleRepo = scheduleRepo; this.competitionRepo = competitionRepo; this.resultRepo = resultRepo;
        this.achievementRepo = achievementRepo; this.feeRepo = feeRepo; this.attendanceRepo = attendanceRepo;
        this.equipmentRepo = equipmentRepo;
    }

    public void loadAll(){
        memberRepo.load(); coachRepo.load(); sportRepo.load(); scheduleRepo.load();
        competitionRepo.load(); resultRepo.load(); achievementRepo.load();
        feeRepo.load(); attendanceRepo.load(); equipmentRepo.load();
    }
    public void saveAll(){
        memberRepo.save(); coachRepo.save(); sportRepo.save(); scheduleRepo.save();
        competitionRepo.save(); resultRepo.save(); achievementRepo.save();
        feeRepo.save(); attendanceRepo.save(); equipmentRepo.save();
    }

    public void ensureSampleData(){
        if (memberRepo.all().size() >= 20) return; // already have dataset

        // 20 members
        for (int i=1;i<=20;i++){
            memberRepo.add(new Member(null, "Member"+i, "090"+String.format("%07d", i)));
        }
        // 5 coaches
        List<Coach> coaches = new ArrayList<>();
        for (int i=1;i<=5;i++){
            Coach c = new Coach(null, "Coach"+i, "098"+String.format("%07d", i));
            coachRepo.add(c); coaches.add(c);
        }
        // 4 sports
        Sport fb = new Football(null, "Football", "Đá bóng", coaches.get(0).getId());
        Sport bb = new Basketball(null, "Basketball", "Bóng rổ", coaches.get(1).getId());
        Sport tn = new Tennis(null, "Tennis", "Quần vợt", coaches.get(2).getId());
        Sport bm = new Badminton(null, "Badminton", "Cầu lông", coaches.get(3).getId());
        sportRepo.add(fb); sportRepo.add(bb); sportRepo.add(tn); sportRepo.add(bm);

        coaches.get(0).addSport(fb.getId());
        coaches.get(1).addSport(bb.getId());
        coaches.get(2).addSport(tn.getId());
        coaches.get(3).addSport(bm.getId());

        // 10 schedules
        List<Member> ms = new ArrayList<>(memberRepo.all());
        for (int i=0;i<10;i++){
            Sport s;
            switch (i%4){
                case 0:
                    s = fb;
                    break;
                case 1:
                    s = bb;
                    break;
                case 2:
                    s = tn;
                    break;
                default:
                    s = bm;
            }
            Coach c = coaches.get(i%4);
            String members = ms.get(i).getId()+";"+ms.get((i+1)%ms.size()).getId();
            scheduleRepo.add(new Schedule(null, s.getId(), c.getId(), members,
                LocalDate.now().plusDays(i%5), LocalTime.of(17+(i%3), 0), 60, ScheduleStatus.SCHEDULED));
        }

        // 3 competitions
        Competition c1 = new League(null, "League Spring", LocalDate.now().minusDays(30));
        Competition c2 = new Tournament(null, "Open Cup", LocalDate.now().minusDays(10));
        Competition c3 = new Tournament(null, "Autumn Cup", LocalDate.now().minusDays(3));
        competitionRepo.add(c1); competitionRepo.add(c2); competitionRepo.add(c3);

        Random rnd = new Random(42);
        // results for latest competition c3
        int rank=1;
        for (int i=0;i<8;i++){
            String mid = ms.get(i).getId();
            resultRepo.add(new Result(c3.getId(), mid, rnd.nextInt(100), rank++));
            achievementRepo.add(new Achievement(null, mid, "Thành tích "+i, rnd.nextInt(50)+10, c3.getId(), LocalDate.now().minusDays(rnd.nextInt(20))));
        }

        // Sample membership fees
        for (int i=0;i<15;i++){
            String mid = ms.get(i).getId();
            LocalDate payDate = LocalDate.now().minusMonths(rnd.nextInt(12));
            LocalDate expDate = payDate.plusMonths(6);
            feeRepo.add(new MembershipFee(null, mid, 500000 + rnd.nextInt(500000), payDate, expDate, "CASH", FeeStatus.PAID));
        }

        // Sample attendance records
        for (int i=0;i<20;i++){
            Schedule sched = scheduleRepo.all().stream().findFirst().orElse(null);
            if (sched != null){
                String mid = ms.get(i%ms.size()).getId();
                AttendanceStatus status = i%5==0 ? AttendanceStatus.ABSENT : AttendanceStatus.PRESENT;
                attendanceRepo.add(new Attendance(null, sched.getId(), mid, LocalDate.now().minusDays(rnd.nextInt(30)),
                                                 LocalTime.of(17, rnd.nextInt(60)), null, status, ""));
            }
        }

        // Sample equipment
        equipmentRepo.add(new Equipment(null, "Bóng đá", "Football", 20, EquipmentStatus.AVAILABLE, 
                                       LocalDate.now().minusMonths(12), LocalDate.now().minusMonths(6), "Kho A", 50000));
        equipmentRepo.add(new Equipment(null, "Bóng rổ", "Basketball", 15, EquipmentStatus.AVAILABLE, 
                                       LocalDate.now().minusMonths(10), LocalDate.now().minusMonths(5), "Kho A", 80000));
        equipmentRepo.add(new Equipment(null, "Vợt tennis", "Tennis", 10, EquipmentStatus.IN_USE, 
                                       LocalDate.now().minusMonths(8), LocalDate.now().minusMonths(2), "Sân B", 200000));
        equipmentRepo.add(new Equipment(null, "Lưới cầu lông", "Badminton", 5, EquipmentStatus.MAINTENANCE, 
                                       LocalDate.now().minusMonths(24), null, "Sân C", 150000));
        equipmentRepo.add(new Equipment(null, "Máy đo thời gian", "Equipment", 3, EquipmentStatus.AVAILABLE, 
                                       LocalDate.now().minusMonths(18), LocalDate.now().minusMonths(1), "Phòng quản lý", 300000));

        saveAll();
    }
}
