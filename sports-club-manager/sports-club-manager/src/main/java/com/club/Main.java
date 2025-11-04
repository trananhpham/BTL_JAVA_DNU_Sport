
package com.club;

import com.club.cli.ConsoleApp;
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

public class Main {
    public static void main(String[] args){
        String base = System.getProperty("user.dir") + "/data";
        MemberRepository m = new MemberRepository(base + "/members.csv");
        CoachRepository c = new CoachRepository(base + "/coaches.csv");
        SportRepository s = new SportRepository(base + "/sports.csv");
        ScheduleRepository sch = new ScheduleRepository(base + "/schedules.csv");
        CompetitionRepository comp = new CompetitionRepository(base + "/competitions.csv");
        ResultRepository res = new ResultRepository(base + "/results.csv");
        AchievementRepository ach = new AchievementRepository(base + "/achievements.csv");
        MembershipFeeRepository fee = new MembershipFeeRepository(base + "/fees.csv");
        AttendanceRepository att = new AttendanceRepository(base + "/attendance.csv");
        EquipmentRepository eq = new EquipmentRepository(base + "/equipment.csv");
        new ConsoleApp(m,c,s,sch,comp,res,ach,fee,att,eq).start();
    }
}
