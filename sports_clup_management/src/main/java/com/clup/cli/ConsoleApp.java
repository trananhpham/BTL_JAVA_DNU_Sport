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
            System.out.println("\nĐã lưu dữ liệu, tạm biệt!");
        }));

        while (true) {
            // Hiển thị thông báo quan trọng
            int importantCount = notificationService.getImportantNotificationCount();
            if (importantCount > 0) {
                System.out.println("\n⚠️ Bạn có " + importantCount + " thông báo quan trọng!");
            }

            System.out.println("\n=== QUẢN LÝ CLB THỂ THAO ===");
            System.out.println("1. Quản lý thành viên");
            System.out.println("2. Quản lý huấn luyện viên");
            System.out.println("3. Quản lý môn thể thao");
            System.out.println("4. Lịch tập");
            System.out.println("5. Giải đấu & Kết quả");
            System.out.println("6. Báo cáo & Thống kê");
            System.out.println("7. Quản lý phí thành viên");
            System.out.println("8. Điểm danh & Tham dự");
            System.out.println("9. Quản lý thiết bị");
            System.out.println("10. Xuất báo cáo");
            System.out.println("11. Thông báo");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
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
                    System.out.println("Lựa chọn không hợp lệ!");
                    break;
            }
        }
    }

    private void membersMenu() {
        System.out.println("\n-- Thành viên --");
        System.out.println("1. Thêm");
        System.out.println("2. Sửa");
        System.out.println("3. Xóa");
        System.out.println("4. Tìm kiếm");
        System.out.println("5. Danh sách");
        System.out.print("Chọn: ");
        String ch = sc.nextLine();
        switch (ch) {
            case "1": {
                System.out.print("Tên: ");
                String name = sc.nextLine();
                System.out.print("SĐT: ");
                String phone = sc.nextLine();
                Member m = new Member(null, name, phone);
                memberRepo.add(m);
                System.out.println("Đã thêm: " + m.getId());
                break;
            }
            case "2": {
                System.out.print("ID: ");
                String id = sc.nextLine();
                Member m = memberRepo.get(id);
                if (m == null) {
                    System.out.println("Không tìm thấy");
                    return;
                }
                System.out.print("Tên mới: ");
                m.setName(sc.nextLine());
                System.out.print("SĐT mới: ");
                m.setPhone(sc.nextLine());
                System.out.println("Đã cập nhật.");
                break;
            }
            case "3": {
                System.out.print("ID: ");
                String id = sc.nextLine();
                memberRepo.remove(id);
                System.out.println("Đã xóa.");
                break;
            }
            case "4": {
                System.out.print("Từ khóa (tên/mã/sđt): ");
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
        System.out.println("\n-- Huấn luyện viên --");
        System.out.println("1. Thêm");
        System.out.println("2. Sửa");
        System.out.println("3. Xóa");
        System.out.println("4. Phân công môn");
        System.out.println("5. Danh sách");
        System.out.print("Chọn: ");
        String ch = sc.nextLine();
        switch (ch) {
            case "1": {
                System.out.print("Tên: ");
                String name = sc.nextLine();
                System.out.print("SĐT: ");
                String phone = sc.nextLine();
                Coach c = new Coach(null, name, phone);
                coachRepo.add(c);
                System.out.println("Đã thêm: " + c.getId());
                break;
            }
            case "2": {
                System.out.print("ID: ");
                String id = sc.nextLine();
                Coach c = coachRepo.get(id);
                if (c == null) {
                    System.out.println("Không tìm thấy");
                    return;
                }
                System.out.print("Tên mới: ");
                c.setName(sc.nextLine());
                System.out.print("SĐT mới: ");
                c.setPhone(sc.nextLine());
                System.out.println("Đã cập nhật.");
                break;
            }
            case "3": {
                System.out.print("ID: ");
                String id = sc.nextLine();
                coachRepo.remove(id);
                System.out.println("Đã xóa.");
                break;
            }
            case "4": {
                System.out.print("Coach ID: ");
                String cid = sc.nextLine();
                Coach c = coachRepo.get(cid);
                if (c == null) {
                    System.out.println("Không tìm thấy");
                    return;
                }
                System.out.print("Sport ID: ");
                String sid = sc.nextLine();
                c.addSport(sid);
                System.out.println("Đã phân công.");
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
        System.out.println("\n-- Môn thể thao --");
        System.out.println("1. Thêm");
        System.out.println("2. Sửa");
        System.out.println("3. Xóa");
        System.out.println("4. Danh sách");
        System.out.print("Chọn: ");
        String ch = sc.nextLine();
        switch (ch) {
            case "1": {
                System.out.println("Loại: 1-Football 2-Basketball 3-Tennis 4-Badminton");
                String t = sc.nextLine();
                System.out.print("Tên: ");
                String name = sc.nextLine();
                System.out.print("Mô tả: ");
                String desc = sc.nextLine();
                System.out.print("Coach ID (có thể trống): ");
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
                System.out.println("Đã thêm: " + s.getId());
                break;
            }
            case "2": {
                System.out.print("Sport ID: ");
                String id = sc.nextLine();
                Sport s = sportRepo.get(id);
                if (s == null) {
                    System.out.println("Không tìm thấy");
                    return;
                }
                System.out.print("Coach ID mới (trống = bỏ qua): ");
                String cid = sc.nextLine();
                if (!cid.isBlank()) {
                    s.setCoachId(cid);
                }
                System.out.println("Lịch mặc định: " + s.getTrainingSchedule());
                break;
            }
            case "3": {
                System.out.print("Sport ID: ");
                sportRepo.remove(sc.nextLine());
                System.out.println("Đã xóa.");
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
        System.out.println("\n-- Lịch tập --");
        System.out.println("1. Đặt lịch");
        System.out.println("2. Danh sách");
        System.out.print("Chọn: ");
        String ch = sc.nextLine();
        switch (ch) {
            case "1": {
                System.out.print("Sport ID: ");
                String sid = sc.nextLine();
                System.out.print("Coach ID: ");
                String cid = sc.nextLine();
                System.out.print("Member IDs (phân tách ;): ");
                String mids = sc.nextLine();
                System.out.print("Ngày (yyyy-MM-dd): ");
                String d = sc.nextLine();
                System.out.print("Giờ (HH:mm): ");
                String t = sc.nextLine();
                System.out.print("Thời lượng (phút): ");
                int dur = Integer.parseInt(sc.nextLine());
                Schedule s = new Schedule(null, sid, cid, mids, LocalDate.parse(d), LocalTime.parse(t), dur, ScheduleStatus.SCHEDULED);
                try {
                    schedulingService.createSchedule(s);
                    System.out.println("Đã đặt: " + s.getId());
                } catch (Exception e) {
                    System.out.println("Lỗi: " + e.getMessage());
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
        System.out.println("\n-- Giải đấu & Kết quả --");
        System.out.println("1. Tạo giải đấu");
        System.out.println("2. Thêm kết quả");
        System.out.println("3. Danh sách giải đấu");
        System.out.print("Chọn: ");
        String ch = sc.nextLine();
        switch (ch) {
            case "1": {
                System.out.print("Tên: ");
                String name = sc.nextLine();
                System.out.print("Ngày (yyyy-MM-dd): ");
                String d = sc.nextLine();
                System.out.print("Loại (L=League, T=Tournament): ");
                String tp = sc.nextLine();
                Competition c = "L".equalsIgnoreCase(tp) ? new League(null, name, LocalDate.parse(d))
                        : new Tournament(null, name, LocalDate.parse(d));
                competitionRepo.add(c);
                System.out.println("Đã tạo: " + c.getId());
                break;
            }
            case "2": {
                System.out.print("Competition ID: ");
                String cid = sc.nextLine();
                System.out.print("Member ID: ");
                String mid = sc.nextLine();
                System.out.print("Điểm: ");
                int score = Integer.parseInt(sc.nextLine());
                System.out.print("Hạng: ");
                int rank = Integer.parseInt(sc.nextLine());
                resultRepo.add(new Result(cid, mid, score, rank));
                System.out.println("Đã ghi kết quả.");
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
        System.out.println("\n-- Báo cáo & Thống kê --");
        System.out.println("1. Thành viên / môn thể thao");
        System.out.println("2. Kết quả giải gần nhất");
        System.out.println("3. Top 5 thành viên (thành tích)");
        System.out.print("Chọn: ");
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
        System.out.println("\n-- Quản lý Phí Thành Viên --");
        System.out.println("1. Thêm phí mới");
        System.out.println("2. Xem phí của thành viên");
        System.out.println("3. Danh sách phí sắp hết hạn");
        System.out.println("4. Cập nhật trạng thái phí");
        System.out.print("Chọn: ");
        String ch = sc.nextLine();
        switch (ch) {
            case "1": {
                System.out.print("Member ID: ");
                String mid = sc.nextLine();
                System.out.print("Số tiền: ");
                double amount = Double.parseDouble(sc.nextLine());
                System.out.print("Ngày thanh toán (yyyy-MM-dd): ");
                String payDate = sc.nextLine();
                System.out.print("Ngày hết hạn (yyyy-MM-dd): ");
                String expDate = sc.nextLine();
                System.out.print("Phương thức (CASH/CARD/TRANSFER): ");
                String method = sc.nextLine();
                MembershipFee fee = new MembershipFee(null, mid, amount, LocalDate.parse(payDate),
                        LocalDate.parse(expDate), method, FeeStatus.PAID);
                feeRepo.add(fee);
                System.out.println("Đã thêm phí: " + fee.getId());
                break;
            }
            case "2": {
                System.out.print("Member ID: ");
                String mid = sc.nextLine();
                List<MembershipFee> fees = feeRepo.getByMember(mid);
                if (fees.isEmpty()) {
                    System.out.println("Không có phí nào.");
                } else {
                    fees.forEach(System.out::println);
                }
                break;
            }
            case "3": {
                System.out.print("Số ngày cảnh báo (mặc định 7): ");
                String input = sc.nextLine();
                int days = input.isBlank() ? 7 : Integer.parseInt(input);
                for (Member m : memberRepo.all()) {
                    MembershipFee fee = feeRepo.getLatestByMember(m.getId());
                    if (fee != null && fee.isExpiringSoon(days)) {
                        System.out.println(m.getName() + " - Hết hạn: " + fee.getExpiryDate());
                    }
                }
                break;
            }
            case "4": {
                System.out.print("Fee ID: ");
                String fid = sc.nextLine();
                MembershipFee fee = feeRepo.get(fid);
                if (fee == null) {
                    System.out.println("Không tìm thấy");
                    return;
                }
                System.out.println("Trạng thái hiện tại: " + fee.getStatus());
                System.out.print("Trạng thái mới (PAID/PENDING/OVERDUE/CANCELLED): ");
                String status = sc.nextLine();
                fee.setStatus(FeeStatus.valueOf(status));
                System.out.println("Đã cập nhật.");
                break;
            }
            default:
                break;
        }
    }

    private void attendanceMenu() {
        System.out.println("\n-- Điểm danh & Tham dự --");
        System.out.println("1. Điểm danh");
        System.out.println("2. Xem lịch sử tham dự");
        System.out.println("3. Tỷ lệ tham dự của thành viên");
        System.out.print("Chọn: ");
        String ch = sc.nextLine();
        switch (ch) {
            case "1": {
                System.out.print("Schedule ID: ");
                String sid = sc.nextLine();
                System.out.print("Member ID: ");
                String mid = sc.nextLine();
                System.out.print("Ngày (yyyy-MM-dd): ");
                String d = sc.nextLine();
                System.out.print("Giờ vào (HH:mm): ");
                String t = sc.nextLine();
                System.out.print("Trạng thái (PRESENT/ABSENT/LATE/EXCUSED): ");
                String st = sc.nextLine();
                Attendance att = new Attendance(null, sid, mid, LocalDate.parse(d),
                        LocalTime.parse(t), null, AttendanceStatus.valueOf(st), "");
                attendanceRepo.add(att);
                System.out.println("Đã điểm danh: " + att.getId());
                break;
            }
            case "2": {
                System.out.print("Member ID: ");
                String mid = sc.nextLine();
                List<Attendance> records = attendanceRepo.getByMember(mid);
                if (records.isEmpty()) {
                    System.out.println("Không có lịch sử.");
                } else {
                    records.forEach(System.out::println);
                }
                break;
            }
            case "3": {
                System.out.print("Member ID: ");
                String mid = sc.nextLine();
                double rate = attendanceRepo.getAttendanceRate(mid);
                System.out.println("Tỷ lệ tham dự: " + String.format("%.2f", rate) + "%");
                break;
            }
            default:
                break;
        }
    }

    private void equipmentMenu() {
        System.out.println("\n-- Quản lý Thiết bị --");
        System.out.println("1. Thêm thiết bị");
        System.out.println("2. Cập nhật trạng thái");
        System.out.println("3. Bảo trì thiết bị");
        System.out.println("4. Danh sách thiết bị");
        System.out.println("5. Thiết bị cần bảo trì");
        System.out.print("Chọn: ");
        String ch = sc.nextLine();
        switch (ch) {
            case "1": {
                System.out.print("Tên: ");
                String name = sc.nextLine();
                System.out.print("Danh mục: ");
                String cat = sc.nextLine();
                System.out.print("Số lượng: ");
                int qty = Integer.parseInt(sc.nextLine());
                System.out.print("Vị trí: ");
                String loc = sc.nextLine();
                System.out.print("Giá: ");
                double price = Double.parseDouble(sc.nextLine());
                Equipment eq = new Equipment(null, name, cat, qty, EquipmentStatus.AVAILABLE,
                        LocalDate.now(), null, loc, price);
                equipmentRepo.add(eq);
                System.out.println("Đã thêm: " + eq.getId());
                break;
            }
            case "2": {
                System.out.print("Equipment ID: ");
                String eid = sc.nextLine();
                Equipment eq = equipmentRepo.get(eid);
                if (eq == null) {
                    System.out.println("Không tìm thấy");
                    return;
                }
                System.out.println("Trạng thái hiện tại: " + eq.getStatus());
                System.out.print("Trạng thái mới (AVAILABLE/IN_USE/MAINTENANCE/DAMAGED/RETIRED): ");
                String status = sc.nextLine();
                eq.setStatus(EquipmentStatus.valueOf(status));
                System.out.println("Đã cập nhật.");
                break;
            }
            case "3": {
                System.out.print("Equipment ID: ");
                String eid = sc.nextLine();
                Equipment eq = equipmentRepo.get(eid);
                if (eq == null) {
                    System.out.println("Không tìm thấy");
                    return;
                }
                eq.setLastMaintenanceDate(LocalDate.now());
                eq.setStatus(EquipmentStatus.AVAILABLE);
                System.out.println("Đã hoàn thành bảo trì.");
                break;
            }
            case "4":
                equipmentRepo.all().forEach(System.out::println);
                break;
            case "5": {
                System.out.print("Số tháng ngưỡng (mặc định 6): ");
                String input = sc.nextLine();
                int months = input.isBlank() ? 6 : Integer.parseInt(input);
                List<Equipment> needs = equipmentRepo.getNeedingMaintenance(months);
                if (needs.isEmpty()) {
                    System.out.println("Không có thiết bị nào cần bảo trì.");
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
        System.out.println("\n-- Xuất Báo cáo --");
        System.out.println("1. Xuất danh sách thành viên");
        System.out.println("2. Xuất kết quả giải đấu");
        System.out.println("3. Xuất báo cáo thiết bị");
        System.out.println("4. Xuất báo cáo tham dự");
        System.out.println("5. Xuất tổng quan (JSON)");
        System.out.print("Chọn: ");
        String ch = sc.nextLine();
        try {
            switch (ch) {
                case "1": {
                    System.out.print("Đường dẫn file (ví dụ: reports/members.txt): ");
                    String path = sc.nextLine();
                    exportService.exportMembersReport(path);
                    System.out.println("Đã xuất thành công!");
                    break;
                }
                case "2": {
                    System.out.print("Competition ID: ");
                    String cid = sc.nextLine();
                    System.out.print("Đường dẫn file: ");
                    String path = sc.nextLine();
                    exportService.exportCompetitionReport(cid, path);
                    System.out.println("Đã xuất thành công!");
                    break;
                }
                case "3": {
                    System.out.print("Đường dẫn file: ");
                    String path = sc.nextLine();
                    exportService.exportEquipmentReport(path);
                    System.out.println("Đã xuất thành công!");
                    break;
                }
                case "4": {
                    System.out.print("Member ID: ");
                    String mid = sc.nextLine();
                    System.out.print("Đường dẫn file: ");
                    String path = sc.nextLine();
                    exportService.exportAttendanceReport(mid, path);
                    System.out.println("Đã xuất thành công!");
                    break;
                }
                case "5": {
                    System.out.print("Đường dẫn file: ");
                    String path = sc.nextLine();
                    exportService.exportAllDataJson(path);
                    System.out.println("Đã xuất thành công!");
                    break;
                }
                default:
                    break;
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi xuất: " + e.getMessage());
        }
    }

    private void notificationMenu() {
        System.out.println("\n-- Thông báo --");
        System.out.println("1. Xem tất cả thông báo");
        System.out.println("2. Phí sắp hết hạn");
        System.out.println("3. Phí đã hết hạn");
        System.out.println("4. Lịch tập sắp tới");
        System.out.println("5. Thiết bị cần bảo trì");
        System.out.print("Chọn: ");
        String ch = sc.nextLine();
        switch (ch) {
            case "1": {
                List<String> all = notificationService.getAllNotifications();
                if (all.isEmpty()) {
                    System.out.println("Không có thông báo nào.");
                } else {
                    all.forEach(System.out::println);
                }
                break;
            }
            case "2": {
                List<String> expiring = notificationService.getExpiringFeeNotifications(7);
                if (expiring.isEmpty()) {
                    System.out.println("Không có phí nào sắp hết hạn.");
                } else {
                    expiring.forEach(System.out::println);
                }
                break;
            }
            case "3": {
                List<String> expired = notificationService.getExpiredFeeNotifications();
                if (expired.isEmpty()) {
                    System.out.println("Không có phí nào đã hết hạn.");
                } else {
                    expired.forEach(System.out::println);
                }
                break;
            }
            case "4": {
                List<String> upcoming = notificationService.getUpcomingScheduleNotifications(3);
                if (upcoming.isEmpty()) {
                    System.out.println("Không có lịch tập nào sắp tới.");
                } else {
                    upcoming.forEach(System.out::println);
                }
                break;
            }
            case "5": {
                List<String> maintenance = notificationService.getEquipmentMaintenanceNotifications(6);
                if (maintenance.isEmpty()) {
                    System.out.println("Không có thiết bị nào cần bảo trì.");
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
