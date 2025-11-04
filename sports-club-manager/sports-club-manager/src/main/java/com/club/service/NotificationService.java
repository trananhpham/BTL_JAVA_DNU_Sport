
package com.club.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.club.model.Equipment;
import com.club.model.EquipmentStatus;
import com.club.model.FeeStatus;
import com.club.model.Member;
import com.club.model.MembershipFee;
import com.club.model.Schedule;
import com.club.model.ScheduleStatus;
import com.club.repo.EquipmentRepository;
import com.club.repo.MemberRepository;
import com.club.repo.MembershipFeeRepository;
import com.club.repo.ScheduleRepository;

public class NotificationService {
    private final MemberRepository memberRepo;
    private final MembershipFeeRepository feeRepo;
    private final ScheduleRepository scheduleRepo;
    private final EquipmentRepository equipmentRepo;

    public NotificationService(MemberRepository memberRepo, MembershipFeeRepository feeRepo,
                              ScheduleRepository scheduleRepo, EquipmentRepository equipmentRepo){
        this.memberRepo = memberRepo;
        this.feeRepo = feeRepo;
        this.scheduleRepo = scheduleRepo;
        this.equipmentRepo = equipmentRepo;
    }

    // Thông báo phí sắp hết hạn
    public List<String> getExpiringFeeNotifications(int daysThreshold){
        List<String> notifications = new ArrayList<>();
        for (Member m : memberRepo.all()){
            MembershipFee fee = feeRepo.getLatestByMember(m.getId());
            if (fee != null && fee.isExpiringSoon(daysThreshold)){
                long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), fee.getExpiryDate());
                notifications.add("⚠️ Phí của " + m.getName() + " sẽ hết hạn sau " + daysLeft + " ngày");
            }
        }
        return notifications;
    }

    // Thông báo phí đã hết hạn
    public List<String> getExpiredFeeNotifications(){
        List<String> notifications = new ArrayList<>();
        for (Member m : memberRepo.all()){
            MembershipFee fee = feeRepo.getLatestByMember(m.getId());
            if (fee != null && fee.isExpired() && fee.getStatus() == FeeStatus.PAID){
                long daysOverdue = ChronoUnit.DAYS.between(fee.getExpiryDate(), LocalDate.now());
                notifications.add("❌ Phí của " + m.getName() + " đã quá hạn " + daysOverdue + " ngày");
            }
        }
        return notifications;
    }

    // Thông báo lịch tập sắp tới
    public List<String> getUpcomingScheduleNotifications(int daysAhead){
        List<String> notifications = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(daysAhead);
        
        for (Schedule s : scheduleRepo.all()){
            if (s.getStatus() == ScheduleStatus.SCHEDULED && 
                !s.getDate().isBefore(today) && !s.getDate().isAfter(endDate)){
                // We don't have direct access to SportRepository here, so just use ID
                notifications.add("📅 Lịch tập: " + s.getDate() + " " + s.getTime() + 
                                " - Sport ID: " + s.getSportId());
            }
        }
        return notifications;
    }

    // Thông báo thiết bị cần bảo trì
    public List<String> getEquipmentMaintenanceNotifications(int monthsThreshold){
        List<String> notifications = new ArrayList<>();
        List<Equipment> needMaintenance = equipmentRepo.getNeedingMaintenance(monthsThreshold);
        
        for (Equipment e : needMaintenance){
            if (e.getStatus() != EquipmentStatus.RETIRED && e.getStatus() != EquipmentStatus.DAMAGED){
                notifications.add("🔧 Thiết bị \"" + e.getName() + "\" cần bảo trì");
            }
        }
        return notifications;
    }

    // Thông báo thiết bị hỏng
    public List<String> getDamagedEquipmentNotifications(){
        List<String> notifications = new ArrayList<>();
        List<Equipment> damaged = equipmentRepo.getByStatus(EquipmentStatus.DAMAGED);
        
        for (Equipment e : damaged){
            notifications.add("⚠️ Thiết bị \"" + e.getName() + "\" đang hư hỏng - Vị trí: " + e.getLocation());
        }
        return notifications;
    }

    // Lấy tất cả thông báo
    public List<String> getAllNotifications(){
        List<String> all = new ArrayList<>();
        all.addAll(getExpiredFeeNotifications());
        all.addAll(getExpiringFeeNotifications(7));
        all.addAll(getUpcomingScheduleNotifications(3));
        all.addAll(getEquipmentMaintenanceNotifications(6));
        all.addAll(getDamagedEquipmentNotifications());
        return all;
    }

    // Đếm số thông báo quan trọng
    public int getImportantNotificationCount(){
        return getExpiredFeeNotifications().size() + getDamagedEquipmentNotifications().size();
    }
}

