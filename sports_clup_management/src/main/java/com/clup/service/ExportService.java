package com.clup.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.clup.model.*;
import com.clup.repo.*;

public class ExportService {

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

    public ExportService(MemberRepository memberRepo, CoachRepository coachRepo, SportRepository sportRepo,
            ScheduleRepository scheduleRepo, CompetitionRepository competitionRepo,
            ResultRepository resultRepo, AchievementRepository achievementRepo,
            MembershipFeeRepository feeRepo, AttendanceRepository attendanceRepo,
            EquipmentRepository equipmentRepo) {
        this.memberRepo = memberRepo;
        this.coachRepo = coachRepo;
        this.sportRepo = sportRepo;
        this.scheduleRepo = scheduleRepo;
        this.competitionRepo = competitionRepo;
        this.resultRepo = resultRepo;
        this.achievementRepo = achievementRepo;
        this.feeRepo = feeRepo;
        this.attendanceRepo = attendanceRepo;
        this.equipmentRepo = equipmentRepo;
    }

    // Xuất báo cáo tổng hợp thành viên
    public void exportMembersReport(String outputPath) throws IOException {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(outputPath), StandardCharsets.UTF_8))) {
            pw.println("=== BÁO CÁO THÀNH VIÊN ===");
            pw.println("Ngày xuất: " + LocalDate.now());
            pw.println("Tổng số thành viên: " + memberRepo.all().size());
            pw.println();
            pw.println("ID,Tên,SĐT,Phí hiện tại,Ngày hết hạn");
            for (Member m : memberRepo.all()) {
                MembershipFee fee = feeRepo.getLatestByMember(m.getId());
                String feeInfo = fee != null ? fee.getExpiryDate().toString() : "Chưa có";
                pw.println(String.join(",", m.getId(), m.getName(), m.getPhone(),
                        fee != null ? String.valueOf(fee.getAmount()) : "0", feeInfo));
            }
        }
    }

    // Xuất báo cáo giải đấu
    public void exportCompetitionReport(String competitionId, String outputPath) throws IOException {
        Competition comp = competitionRepo.get(competitionId);
        if (comp == null) {
            throw new IllegalArgumentException("Giải đấu không tồn tại");
        }

        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(outputPath), StandardCharsets.UTF_8))) {
            pw.println("=== BÁO CÁO GIẢI ĐẤU ===");
            pw.println("Tên giải: " + comp.getName());
            pw.println("Ngày: " + comp.getDate());
            pw.println("Loại: " + comp.getType());
            pw.println();
            pw.println("Hạng,Thành viên,Điểm");

            List<Result> results = resultRepo.all().stream()
                    .filter(r -> r.getCompetitionId().equals(competitionId))
                    .sorted(Comparator.comparingInt(Result::getRank))
                    .toList();

            for (Result r : results) {
                Member m = memberRepo.get(r.getMemberId());
                String memberName = m != null ? m.getName() : r.getMemberId();
                pw.println(r.getRank() + "," + memberName + "," + r.getScore());
            }
        }
    }

    // Xuất báo cáo thiết bị
    public void exportEquipmentReport(String outputPath) throws IOException {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(outputPath), StandardCharsets.UTF_8))) {
            pw.println("=== BÁO CÁO THIẾT BỊ ===");
            pw.println("Ngày xuất: " + LocalDate.now());
            pw.println("Tổng số thiết bị: " + equipmentRepo.getTotalQuantity());
            pw.println();
            pw.println("ID,Tên,Danh mục,Số lượng,Trạng thái,Vị trí,Giá");
            for (Equipment e : new ArrayList<>(equipmentRepo.all())) {
                pw.println(String.join(",", e.getId(), e.getName(), e.getCategory(),
                        String.valueOf(e.getQuantity()), e.getStatus().name(),
                        e.getLocation(), String.valueOf(e.getPrice())));
            }
        }
    }

    // Xuất báo cáo tham dự
    public void exportAttendanceReport(String memberId, String outputPath) throws IOException {
        Member m = memberRepo.get(memberId);
        if (m == null) {
            throw new IllegalArgumentException("Thành viên không tồn tại");
        }

        List<Attendance> records = attendanceRepo.getByMember(memberId);
        double rate = attendanceRepo.getAttendanceRate(memberId);

        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(outputPath), StandardCharsets.UTF_8))) {
            pw.println("=== BÁO CÁO THAM DỰ ===");
            pw.println("Thành viên: " + m.getName());
            pw.println("ID: " + memberId);
            pw.println("Tỷ lệ tham dự: " + String.format("%.2f", rate) + "%");
            pw.println();
            pw.println("Ngày,Giờ vào,Giờ ra,Trạng thái,Ghi chú");
            for (Attendance a : records) {
                String checkOut = a.getCheckOutTime() != null ? a.getCheckOutTime().toString() : "";
                pw.println(String.join(",", a.getDate().toString(), a.getCheckInTime().toString(),
                        checkOut, a.getStatus().name(), a.getNote()));
            }
        }
    }

    // Xuất tất cả dữ liệu dạng JSON đơn giản
    public void exportAllDataJson(String outputPath) throws IOException {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(outputPath), StandardCharsets.UTF_8))) {
            pw.println("{");
            pw.println("  \"exportDate\": \"" + LocalDate.now() + "\",");
            pw.println("  \"members\": " + memberRepo.all().size() + ",");
            pw.println("  \"coaches\": " + coachRepo.all().size() + ",");
            pw.println("  \"sports\": " + sportRepo.all().size() + ",");
            pw.println("  \"schedules\": " + scheduleRepo.all().size() + ",");
            pw.println("  \"competitions\": " + competitionRepo.all().size() + ",");
            pw.println("  \"results\": " + resultRepo.all().size() + ",");
            pw.println("  \"achievements\": " + achievementRepo.all().size() + ",");
            pw.println("  \"fees\": " + new ArrayList<>(feeRepo.all()).size() + ",");
            pw.println("  \"attendances\": " + new ArrayList<>(attendanceRepo.all()).size() + ",");
            pw.println("  \"equipment\": " + new ArrayList<>(equipmentRepo.all()).size());
            pw.println("}");
        }
    }
}
