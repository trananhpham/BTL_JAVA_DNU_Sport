package com.clup.cli;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.clup.model.*;
import com.clup.model.AttendanceStatus;
import com.clup.model.Badminton;
import com.clup.model.Basketball;
import com.clup.model.Coach;
import com.clup.model.Competition;
import com.clup.model.Equipment;
import com.clup.model.EquipmentStatus;
import com.clup.model.FeeStatus;
import com.clup.model.Football;
import com.clup.model.League;
import com.clup.model.MembershipFee;
import com.clup.model.Schedule;
import com.clup.model.ScheduleStatus;
import com.clup.model.Sport;
import com.clup.model.Tennis;
import com.clup.model.Tournament;
import com.clup.repo.*;
import com.clup.repo.AttendanceRepository;
import com.clup.repo.CoachRepository;
import com.clup.repo.CompetitionRepository;
import com.clup.repo.EquipmentRepository;
import com.clup.repo.MemberRepository;
import com.clup.repo.MembershipFeeRepository;
import com.clup.repo.ResultRepository;
import com.clup.repo.ScheduleRepository;
import com.clup.repo.SportRepository;
import com.clup.service.*;
import com.clup.service.ExportService;
import com.clup.service.NotificationService;
import com.clup.service.ReportService;
import com.clup.service.SchedulingService;

public class ConsoleApp {

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
    private final SchedulingService schedulingService;
    private final ReportService reportService;
    private final ExportService exportService;
    private final NotificationService notificationService;
    private final DataBootstrap bootstrap;
    private final Scanner sc = new Scanner(System.in);

    public ConsoleApp(MemberRepository m, CoachRepository c, SportRepository s, ScheduleRepository sch,
            CompetitionRepository comp, ResultRepository res, AchievementRepository ach,
            MembershipFeeRepository fee, AttendanceRepository att, EquipmentRepository eq) {
        this.memberRepo = m;
        this.coachRepo = c;
        this.sportRepo = s;
        this.scheduleRepo = sch;
        this.competitionRepo = comp;
        this.resultRepo = res;
        this.achievementRepo = ach;
        this.feeRepo = fee;
        this.attendanceRepo = att;
        this.equipmentRepo = eq;
        this.schedulingService = new SchedulingService(scheduleRepo);
        this.reportService = new ReportService(memberRepo, coachRepo, sportRepo, scheduleRepo, competitionRepo, resultRepo, achievementRepo);
        this.exportService = new ExportService(memberRepo, coachRepo, sportRepo, scheduleRepo, competitionRepo, resultRepo, achievementRepo, feeRepo, attendanceRepo, equipmentRepo);
        this.notificationService = new NotificationService(memberRepo, feeRepo, scheduleRepo, equipmentRepo);
        this.bootstrap = new DataBootstrap(memberRepo, coachRepo, sportRepo, scheduleRepo, competitionRepo, resultRepo, achievementRepo, feeRepo, attendanceRepo, equipmentRepo);
    }

    public void start() {
        bootstrap.loadAll();
        bootstrap.ensureSampleData();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            bootstrap.saveAll();
            System.out.println("\nDa luu du lieu, tam biet!");
        }));

        while (true) {
            // Hiển thị thông báo quan trọng
            int importantCount = notificationService.getImportantNotificationCount();
            if (importantCount > 0) {
                System.out.println("\n Ban co " + importantCount + " thong bao quan trong!");
            }

            System.out.println("\n=== QUAN LY CLB THE THAO ===");
            System.out.println("1. Quan ly thanh vien");
            System.out.println("2. Quan ly huan luyen vien");
            System.out.println("3. Quan ly mon the thao");
            System.out.println("4. LLich tap");
            System.out.println("5. Giai dau & KKet qua");
            System.out.println("6. BBao cao & Thong ke");
            System.out.println("7. Quan ly phi thanh vien");
            System.out.println("8. Diem danh & Tham du");
            System.out.println("9. Quan ly thiet bi");
            System.out.println("10. Xuat bao cao");
            System.out.println("11. Thong bao");
            System.out.println("0. Thoat");
            System.out.print("Chon: ");
            String ch = sc.nextLine();
            switch (ch) {
                case "1":
                    membersMenu();
                    break;
                case "2":
                    coachesMenu();
                    break;
                case "3":
                    sportsMenu();
                    break;
                case "4":
                    scheduleMenu();
                    break;
                case "5":
                    competitionMenu();
                    break;
                case "6":
                    reportMenu();
                    break;
                case "7":
                    membershipFeeMenu();
                    break;
                case "8":
                    attendanceMenu();
                    break;
                case "9":
                    equipmentMenu();
                    break;
                case "10":
                    exportMenu();
                    break;
                case "11":
                    notificationMenu();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Lua chon khong hop le!");
                    break;
            }
        }
    }

    private void membersMenu() {
        System.out.println("\n-- Thanh vien --");
        System.out.println("1. Them");
        System.out.println("2. Sua");
        System.out.println("3. Xoa");
        System.out.println("4. Tim kiem");
        System.out.println("5. Danh sach");
        System.out.print("Chon: ");
        String ch = sc.nextLine();
        switch (ch) {
            case "1": {
                System.out.print("Ten: ");
                String name = sc.nextLine();
                System.out.print("SDT: ");
                String phone = sc.nextLine();
                Member m = new Member(null, name, phone);
                memberRepo.add(m);
                System.out.println("Da them: " + m.getId());
                break;
            }
            case "2": {
                System.out.print("ID: ");
                String id = sc.nextLine();
                Member m = memberRepo.get(id);
                if (m == null) {
                    System.out.println("Khong tim thay");
                    return;
                }
                System.out.print("Ten moi: ");
                m.setName(sc.nextLine());
                System.out.print("SDT moi: ");
                m.setPhone(sc.nextLine());
                System.out.println("Da cap nhat.");
                break;
            }
            case "3": {
                System.out.print("ID: ");
                String id = sc.nextLine();
                memberRepo.remove(id);
                System.out.println("Da xoa.");
                break;
            }
            case "4": {
                System.out.print("Tu khoa (ten/ma/sdt): ");
                String q = sc.nextLine().toLowerCase();
                for (Member m : memberRepo.all()) {
                    if (m.getId().contains(q) || m.getName().toLowerCase().contains(q) || m.getPhone().contains(q)) {
                        System.out.println(m);
                    }
                }
                break;
            }
            case "5":
                memberRepo.all().forEach(System.out::println);
                break;
            default:
                break;
        }
    }

    private void coachesMenu() {
        System.out.println("\n-- Huan luyen vien --");
        System.out.println("1. Them");
        System.out.println("2. Sua");
        System.out.println("3. Xoa");
        System.out.println("4. Phan cong mon");
        System.out.println("5. Danh sach");
        System.out.print("Chon: ");
        String ch = sc.nextLine();
        switch (ch) {
            case "1": {
                System.out.print("Ten: ");
                String name = sc.nextLine();
                System.out.print("SDT: ");
                String phone = sc.nextLine();
                Coach c = new Coach(null, name, phone);
                coachRepo.add(c);
                System.out.println("Da them: " + c.getId());
                break;
            }
            case "2": {
                System.out.print("ID: ");
                String id = sc.nextLine();
                Coach c = coachRepo.get(id);
                if (c == null) {
                    System.out.println("Khong tim thay");
                    return;
                }
                System.out.print("Ten moi: ");
                c.setName(sc.nextLine());
                System.out.print("SDT moi: ");
                c.setPhone(sc.nextLine());
                System.out.println("Da cap nhat.");
                break;
            }
            case "3": {
                System.out.print("ID: ");
                String id = sc.nextLine();
                coachRepo.remove(id);
                System.out.println("Da xoa.");
                break;
            }
            case "4": {
                System.out.print("Coach ID: ");
                String cid = sc.nextLine();
                Coach c = coachRepo.get(cid);
                if (c == null) {
                    System.out.println("Khong tim thay");
                    return;
                }
                System.out.print("Sport ID: ");
                String sid = sc.nextLine();
                c.addSport(sid);
                System.out.println("Da phan cong.");
                break;
            }
            case "5":
                coachRepo.all().forEach(System.out::println);
                break;
            default:
                break;
        }
    }

    private void sportsMenu() {
        System.out.println("\n-- MMon the thao --");
        System.out.println("1. Them");
        System.out.println("2. Sua");
        System.out.println("3. Xoa");
        System.out.println("4. Danh sach");
        System.out.print("Chon: ");
        String ch = sc.nextLine();
        switch (ch) {
            case "1": {
                System.out.println("Loai: 1-Football 2-Basketball 3-Tennis 4-Badminton");
                String t = sc.nextLine();
                System.out.print("TTen: ");
                String name = sc.nextLine();
                System.out.print("Mo ta: ");
                String desc = sc.nextLine();
                System.out.print("Coach ID (co the trong): ");
                String coachInput = sc.nextLine();
                String coachId = coachInput.isBlank() ? null : coachInput; // đọc 1 lần tránh nuốt dòng

                Sport s;
                if ("1".equals(t)) {
                    s = new Football(null, name, desc, coachId);
                } else if ("2".equals(t)) {
                    s = new Basketball(null, name, desc, coachId);
                } else if ("3".equals(t)) {
                    s = new Tennis(null, name, desc, coachId);
                } else {
                    s = new Badminton(null, name, desc, coachId);
                }

                sportRepo.add(s);
                System.out.println("Da them: " + s.getId());
                break;
            }
            case "2": {
                System.out.print("Sport ID: ");
                String id = sc.nextLine();
                Sport s = sportRepo.get(id);
                if (s == null) {
                    System.out.println("Khong tim thay");
                    return;
                }
                System.out.print("Coach ID moi (trong = bo qua): ");
                String cid = sc.nextLine();
                if (!cid.isBlank()) {
                    s.setCoachId(cid);
                }
                System.out.println("Lich mac dinh: " + s.getTrainingSchedule());
                break;
            }
            case "3": {
                System.out.print("Sport ID: ");
                sportRepo.remove(sc.nextLine());
                System.out.println("Da xoa.");
                break;
            }
            case "4": {
                for (Sport s : sportRepo.all()) {
                    System.out.println(s.getId() + " | " + s.getName() + " | coach=" + s.getCoachId() + " | " + s.getTrainingSchedule());
                }
                break;
            }
            default:
                break;
        }
    }

    private void scheduleMenu() {
        System.out.println("\n-- Lich tap --");
        System.out.println("1. Dat lich");
        System.out.println("2. Danh sach");
        System.out.print("Chon: ");
        String ch = sc.nextLine();
        switch (ch) {
            case "1": {
                System.out.print("Sport ID: ");
                String sid = sc.nextLine();
                System.out.print("Coach ID: ");
                String cid = sc.nextLine();
                System.out.print("Member IDs (phan tach ;): ");
                String mids = sc.nextLine();
                System.out.print("Ngay (yyyy-MM-dd): ");
                String d = sc.nextLine();
                System.out.print("Gio (HH:mm): ");
                String t = sc.nextLine();
                System.out.print("Thoi luong (phut): ");
                int dur = Integer.parseInt(sc.nextLine());
                Schedule s = new Schedule(null, sid, cid, mids, LocalDate.parse(d), LocalTime.parse(t), dur, ScheduleStatus.SCHEDULED);
                try {
                    schedulingService.createSchedule(s);
                    System.out.println("Da dat: " + s.getId());
                } catch (Exception e) {
                    System.out.println("Loi: " + e.getMessage());
                }
                break;
            }
            case "2":
                scheduleRepo.all().forEach(System.out::println);
                break;
            default:
                break;
        }
    }

    private void competitionMenu() {
        System.out.println("\n-- Giai dau & Ket qua --");
        System.out.println("1. TTao giai dau");
        System.out.println("2. Them ket qua");
        System.out.println("3. Danh sach giai dau");
        System.out.print("Chon: ");
        String ch = sc.nextLine();
        switch (ch) {
            case "1": {
                System.out.print("Ten: ");
                String name = sc.nextLine();
                System.out.print("Ngay (yyyy-MM-dd): ");
                String d = sc.nextLine();
                System.out.print("Loai (L=League, T=Tournament): ");
                String tp = sc.nextLine();
                Competition c = "L".equalsIgnoreCase(tp) ? new League(null, name, LocalDate.parse(d))
                        : new Tournament(null, name, LocalDate.parse(d));
                competitionRepo.add(c);
                System.out.println("Da tao: " + c.getId());
                break;
            }
            case "2": {
                System.out.print("Competition ID: ");
                String cid = sc.nextLine();
                System.out.print("Member ID: ");
                String mid = sc.nextLine();
                System.out.print("Diem: ");
                int score = Integer.parseInt(sc.nextLine());
                System.out.print("Hang: ");
                int rank = Integer.parseInt(sc.nextLine());
                resultRepo.add(new Result(cid, mid, score, rank));
                System.out.println("Da ghi ket qua.");
                break;
            }
            case "3": {
                for (Competition c : competitionRepo.all()) {
                    System.out.println(c.getId() + " | " + c.getName() + " | " + c.getDate() + " | " + c.getType());
                }
                break;
            }
            default:
                break;
        }
    }

    private void reportMenu() {
        System.out.println("\n-- BBao cao & Thong ke --");
        System.out.println("1. Thanh vien / mon the thao");
        System.out.println("2. KKet qua giai gan nhat");
        System.out.println("3. Top 5 thanh vien (thanh tich)");
        System.out.print("Chon: ");
        String ch = sc.nextLine();
        switch (ch) {
            case "1": {
                // tránh dùng 'var' để tương thích mức ngôn ngữ thấp
                // Giả định hàm trả về Map<String, Number> (in ra bằng toString)
                reportService.membersPerSport().forEach((k, v) -> System.out.println(k + " -> " + v));
                break;
            }
            case "2": {
                List<Result> list = reportService.latestCompetitionRanking();
                for (Result r : list) {
                    System.out.println("Member " + r.getMemberId() + " | Rank " + r.getRank() + " | Score " + r.getScore());
                }
                break;
            }
            case "3": {
                // Chấp nhận mọi kiểu điểm số (Integer/Long/Double):
                for (Map.Entry<?, ?> e : reportService.top5MembersAchievements()) {
                    System.out.println("Member " + e.getKey() + " -> Points " + e.getValue());
                }
                break;
            }
            default:
                break;
        }
    }

    private void membershipFeeMenu() {
        System.out.println("\n-- Quan ly Phi Thanh Vien --");
        System.out.println("1. Them phi moi");
        System.out.println("2. Xem phi cua thanh vien");
        System.out.println("3. Danh sach phi sap het han");
        System.out.println("4. CCap nhat trang thai phi");
        System.out.print("Chon: ");
        String ch = sc.nextLine();
        switch (ch) {
            case "1": {
                System.out.print("Member ID: ");
                String mid = sc.nextLine();
                System.out.print("So tien: ");
                double amount = Double.parseDouble(sc.nextLine());
                System.out.print("Ngay thanh toan (yyyy-MM-dd): ");
                String payDate = sc.nextLine();
                System.out.print("Ngay het han (yyyy-MM-dd): ");
                String expDate = sc.nextLine();
                System.out.print("Phuong thuc (CASH/CARD/TRANSFER): ");
                String method = sc.nextLine();
                MembershipFee fee = new MembershipFee(null, mid, amount, LocalDate.parse(payDate),
                        LocalDate.parse(expDate), method, FeeStatus.PAID);
                feeRepo.add(fee);
                System.out.println("Da them phi: " + fee.getId());
                break;
            }
            case "2": {
                System.out.print("Member ID: ");
                String mid = sc.nextLine();
                List<MembershipFee> fees = feeRepo.getByMember(mid);
                if (fees.isEmpty()) {
                    System.out.println("Khong co phi nao.");
                } else {
                    fees.forEach(System.out::println);
                }
                break;
            }
            case "3": {
                System.out.print("So ngay canh bao (mac dinh 7): ");
                String input = sc.nextLine();
                int days = input.isBlank() ? 7 : Integer.parseInt(input);
                for (Member m : memberRepo.all()) {
                    MembershipFee fee = feeRepo.getLatestByMember(m.getId());
                    if (fee != null && fee.isExpiringSoon(days)) {
                        System.out.println(m.getName() + " - Het han: " + fee.getExpiryDate());
                    }
                }
                break;
            }
            case "4": {
                System.out.print("Fee ID: ");
                String fid = sc.nextLine();
                MembershipFee fee = feeRepo.get(fid);
                if (fee == null) {
                    System.out.println("Khong tim thay");
                    return;
                }
                System.out.println("Trang thai hien tai: " + fee.getStatus());
                System.out.print("Trang thai moi (PAID/PENDING/OVERDUE/CANCELLED): ");
                String status = sc.nextLine();
                fee.setStatus(FeeStatus.valueOf(status));
                System.out.println("Da cap nhat.");
                break;
            }
            default:
                break;
        }
    }

    private void attendanceMenu() {
        System.out.println("\n-- Diem danh & Tham du --");
        System.out.println("1. Diem danh");
        System.out.println("2. Xem lich su tham du");
        System.out.println("3. Ty le tham du cua thanh vien");
        System.out.print("Chon: ");
        String ch = sc.nextLine();
        switch (ch) {
            case "1": {
                System.out.print("Schedule ID: ");
                String sid = sc.nextLine();
                System.out.print("Member ID: ");
                String mid = sc.nextLine();
                System.out.print("Ngay (yyyy-MM-dd): ");
                String d = sc.nextLine();
                System.out.print("Gio vao (HH:mm): ");
                String t = sc.nextLine();
                System.out.print("Trang thai (PRESENT/ABSENT/LATE/EXCUSED): ");
                String st = sc.nextLine();
                Attendance att = new Attendance(null, sid, mid, LocalDate.parse(d),
                        LocalTime.parse(t), null, AttendanceStatus.valueOf(st), "");
                attendanceRepo.add(att);
                System.out.println("Da diem danh: " + att.getId());
                break;
            }
            case "2": {
                System.out.print("Member ID: ");
                String mid = sc.nextLine();
                List<Attendance> records = attendanceRepo.getByMember(mid);
                if (records.isEmpty()) {
                    System.out.println("Khong co lich su.");
                } else {
                    records.forEach(System.out::println);
                }
                break;
            }
            case "3": {
                System.out.print("Member ID: ");
                String mid = sc.nextLine();
                double rate = attendanceRepo.getAttendanceRate(mid);
                System.out.println("Ty le tham du: " + String.format("%.2f", rate) + "%");
                break;
            }
            default:
                break;
        }
    }

    private void equipmentMenu() {
        System.out.println("\n-- Quan ly Thiet bi --");
        System.out.println("1. Them thiet bi");
        System.out.println("2. CCap nhat trang thai");
        System.out.println("3. BBao tri thiet bi");
        System.out.println("4. Danh sach thiet bi");
        System.out.println("5. Thiet bi can bao tri");
        System.out.print("Chon: ");
        String ch = sc.nextLine();
        switch (ch) {
            case "1": {
                System.out.print("TTen: ");
                String name = sc.nextLine();
                System.out.print("Danh muc: ");
                String cat = sc.nextLine();
                System.out.print("So luong: ");
                int qty = Integer.parseInt(sc.nextLine());
                System.out.print("Vi tri: ");
                String loc = sc.nextLine();
                System.out.print("Gia: ");
                double price = Double.parseDouble(sc.nextLine());
                Equipment eq = new Equipment(null, name, cat, qty, EquipmentStatus.AVAILABLE,
                        LocalDate.now(), null, loc, price);
                equipmentRepo.add(eq);
                System.out.println("Da them: " + eq.getId());
                break;
            }
            case "2": {
                System.out.print("Equipment ID: ");
                String eid = sc.nextLine();
                Equipment eq = equipmentRepo.get(eid);
                if (eq == null) {
                    System.out.println("Khong tim thay");
                    return;
                }
                System.out.println("Trang thai hien tai: " + eq.getStatus());
                System.out.print("Trang thai moi (AVAILABLE/IN_USE/MAINTENANCE/DAMAGED/RETIRED): ");
                String status = sc.nextLine();
                eq.setStatus(EquipmentStatus.valueOf(status));
                System.out.println("Da cap nhat.");
                break;
            }
            case "3": {
                System.out.print("Equipment ID: ");
                String eid = sc.nextLine();
                Equipment eq = equipmentRepo.get(eid);
                if (eq == null) {
                    System.out.println("Khong tim thay");
                    return;
                }
                eq.setLastMaintenanceDate(LocalDate.now());
                eq.setStatus(EquipmentStatus.AVAILABLE);
                System.out.println("Da hoan thanh bao tri.");
                break;
            }
            case "4":
                equipmentRepo.all().forEach(System.out::println);
                break;
            case "5": {
                System.out.print("So thang nguong (mac dinh 6): ");
                String input = sc.nextLine();
                int months = input.isBlank() ? 6 : Integer.parseInt(input);
                List<Equipment> needs = equipmentRepo.getNeedingMaintenance(months);
                if (needs.isEmpty()) {
                    System.out.println("Khong co thiet bi nao can bao tri.");
                } else {
                    needs.forEach(System.out::println);
                }
                break;
            }
            default:
                break;
        }
    }

    private void exportMenu() {
        System.out.println("\n-- Xuat Bao cao --");
        System.out.println("1. Xuat danh sach thanh vien");
        System.out.println("2. Xuat ket qua giai dau");
        System.out.println("3. Xuat bao cao thiet bi");
        System.out.println("4. Xuat bao cao tham du");
        System.out.println("5. Xuat tong quan (JSON)");
        System.out.print("Chon: ");
        String ch = sc.nextLine();
        try {
            switch (ch) {
                case "1": {
                    System.out.print("Duong dan file (vi du: reports/members.txt): ");
                    String path = sc.nextLine();
                    exportService.exportMembersReport(path);
                    System.out.println("Da xuat thanh cong!");
                    break;
                }
                case "2": {
                    System.out.print("Competition ID: ");
                    String cid = sc.nextLine();
                    System.out.print("Duong dan file: ");
                    String path = sc.nextLine();
                    exportService.exportCompetitionReport(cid, path);
                    System.out.println("Da xuat thanh cong!");
                    break;
                }
                case "3": {
                    System.out.print("Duong dan file: ");
                    String path = sc.nextLine();
                    exportService.exportEquipmentReport(path);
                    System.out.println("Da xuat thanh cong!");
                    break;
                }
                case "4": {
                    System.out.print("Member ID: ");
                    String mid = sc.nextLine();
                    System.out.print("Duong dan file: ");
                    String path = sc.nextLine();
                    exportService.exportAttendanceReport(mid, path);
                    System.out.println("Da xuat thanh cong!");
                    break;
                }
                case "5": {
                    System.out.print("Duong dan file: ");
                    String path = sc.nextLine();
                    exportService.exportAllDataJson(path);
                    System.out.println("Da xuat thanh cong!");
                    break;
                }
                default:
                    break;
            }
        } catch (Exception e) {
            System.out.println("LLoi khi xuat: " + e.getMessage());
        }
    }

    private void notificationMenu() {
        System.out.println("\n-- Thong Bao --");
        System.out.println("1. Xem tat ca thong bao");
        System.out.println("2. Phi sap het han");
        System.out.println("3. Phi da het han");
        System.out.println("4. LLich tap sap toi");
        System.out.println("5. Thiet bi can bao tri");
        System.out.print("Chon: ");
        String ch = sc.nextLine();
        switch (ch) {
            case "1": {
                List<String> all = notificationService.getAllNotifications();
                if (all.isEmpty()) {
                    System.out.println("Khong co thong bao nao.");
                } else {
                    all.forEach(System.out::println);
                }
                break;
            }
            case "2": {
                List<String> expiring = notificationService.getExpiringFeeNotifications(7);
                if (expiring.isEmpty()) {
                    System.out.println("Khong co phi nao sap het han.");
                } else {
                    expiring.forEach(System.out::println);
                }
                break;
            }
            case "3": {
                List<String> expired = notificationService.getExpiredFeeNotifications();
                if (expired.isEmpty()) {
                    System.out.println("Khong co phi nao da het han.");
                } else {
                    expired.forEach(System.out::println);
                }
                break;
            }
            case "4": {
                List<String> upcoming = notificationService.getUpcomingScheduleNotifications(3);
                if (upcoming.isEmpty()) {
                    System.out.println("Khong co lich tap nao sap toi.");
                } else {
                    upcoming.forEach(System.out::println);
                }
                break;
            }
            case "5": {
                List<String> maintenance = notificationService.getEquipmentMaintenanceNotifications(6);
                if (maintenance.isEmpty()) {
                    System.out.println("Khong co thiet bi nao can bao tri.");
                } else {
                    maintenance.forEach(System.out::println);
                }
                break;
            }
            default:
                break;
        }
    }
}
