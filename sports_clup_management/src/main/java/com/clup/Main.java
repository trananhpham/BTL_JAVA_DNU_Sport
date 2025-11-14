package com.clup;

import com.clup.cli.ConsoleApp;
import com.clup.repo.AchievementRepository;
import com.clup.repo.AttendanceRepository;
import com.clup.repo.CoachRepository;
import com.clup.repo.CompetitionRepository;
import com.clup.repo.EquipmentRepository;
import com.clup.repo.MemberRepository;
import com.clup.repo.MembershipFeeRepository;
import com.clup.repo.ResultRepository;
import com.clup.repo.ScheduleRepository;
import com.clup.repo.SportRepository;

public class Main {

    public static void main(String[] args) {
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
        new ConsoleApp(m, c, s, sch, comp, res, ach, fee, att, eq).start();
    }
}
