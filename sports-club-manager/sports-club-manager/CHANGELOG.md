# Nhật ký thay đổi / Changelog

## Version 2.0 - Cập nhật tính năng mở rộng

### Tính năng mới

#### 1. Hệ thống quản lý phí thành viên (Membership Fee Management)
- **Model**: `MembershipFee`, `FeeStatus`
- **Repository**: `MembershipFeeRepository`
- **Chức năng**:
  - Thêm, xem, cập nhật phí thành viên
  - Theo dõi ngày thanh toán và ngày hết hạn
  - Kiểm tra phí sắp hết hạn và đã hết hạn
  - Hỗ trợ nhiều phương thức thanh toán (CASH, CARD, TRANSFER)
  - Trạng thái phí: PAID, PENDING, OVERDUE, CANCELLED

#### 2. Hệ thống điểm danh & tham dự (Attendance Tracking)
- **Model**: `Attendance`, `AttendanceStatus`
- **Repository**: `AttendanceRepository`
- **Chức năng**:
  - Ghi nhận điểm danh cho từng buổi tập
  - Theo dõi giờ vào, giờ ra
  - Trạng thái: PRESENT, ABSENT, LATE, EXCUSED
  - Tính tỷ lệ tham dự của thành viên
  - Xem lịch sử tham dự theo thành viên hoặc lịch tập
  - Lọc theo khoảng thời gian

#### 3. Hệ thống quản lý thiết bị (Equipment Management)
- **Model**: `Equipment`, `EquipmentStatus`
- **Repository**: `EquipmentRepository`
- **Chức năng**:
  - Quản lý trang thiết bị thể thao
  - Theo dõi số lượng, vị trí, giá trị
  - Trạng thái: AVAILABLE, IN_USE, MAINTENANCE, DAMAGED, RETIRED
  - Lịch sử bảo trì thiết bị
  - Cảnh báo thiết bị cần bảo trì
  - Thống kê theo danh mục và trạng thái

#### 4. Dịch vụ xuất báo cáo (Export Service)
- **Service**: `ExportService`
- **Chức năng**:
  - Xuất danh sách thành viên (CSV/TXT)
  - Xuất kết quả giải đấu
  - Xuất báo cáo thiết bị
  - Xuất báo cáo tham dự theo thành viên
  - Xuất tổng quan hệ thống (JSON)

#### 5. Hệ thống thông báo thông minh (Notification Service)
- **Service**: `NotificationService`
- **Chức năng**:
  - Cảnh báo phí sắp hết hạn (cấu hình số ngày)
  - Thông báo phí đã hết hạn
  - Nhắc nhở lịch tập sắp tới
  - Cảnh báo thiết bị cần bảo trì
  - Thông báo thiết bị hư hỏng
  - Dashboard hiển thị số thông báo quan trọng

### Cải tiến menu CLI

Menu chính được mở rộng từ 6 lên 11 tùy chọn:
1. Quản lý thành viên
2. Quản lý huấn luyện viên
3. Quản lý môn thể thao
4. Lịch tập
5. Giải đấu & Kết quả
6. Báo cáo & Thống kê
7. **Quản lý phí thành viên** *(mới)*
8. **Điểm danh & Tham dự** *(mới)*
9. **Quản lý thiết bị** *(mới)*
10. **Xuất báo cáo** *(mới)*
11. **Thông báo** *(mới)*

### Cải tiến Tests

Tăng số lượng test từ 10 lên 20:
- `test11_add_membership_fee`: Thêm phí thành viên
- `test12_check_fee_expiring_soon`: Kiểm tra phí sắp hết hạn
- `test13_add_attendance`: Thêm bản ghi điểm danh
- `test14_calculate_attendance_rate`: Tính tỷ lệ tham dự
- `test15_add_equipment`: Thêm thiết bị
- `test16_equipment_needs_maintenance`: Kiểm tra thiết bị cần bảo trì
- `test17_get_equipment_by_category`: Lọc thiết bị theo danh mục
- `test18_get_latest_fee_by_member`: Lấy phí mới nhất của thành viên
- `test19_get_attendance_by_member`: Lấy lịch sử tham dự
- `test20_equipment_total_quantity`: Thống kê tổng số lượng thiết bị

### Dữ liệu mẫu

DataBootstrap được cập nhật để tạo dữ liệu mẫu cho:
- 15 bản ghi phí thành viên với ngày hết hạn khác nhau
- 20 bản ghi điểm danh với các trạng thái khác nhau
- 5 thiết bị thể thao với trạng thái và lịch bảo trì

### Files CSV mới

Thêm 3 file CSV để lưu trữ dữ liệu:
- `data/fees.csv`: Phí thành viên
- `data/attendance.csv`: Điểm danh
- `data/equipment.csv`: Thiết bị

### Kiến trúc

- Tuân thủ nguyên lý OOP: Kế thừa, Đa hình, Interface, Encapsulation
- Sử dụng Enum cho các trạng thái
- Repository Pattern cho data access
- Service Layer cho business logic
- Console UI với menu phân cấp

---

## Version 1.0 - Ban đầu

- Quản lý thành viên, huấn luyện viên, môn thể thao
- Lịch tập với kiểm tra xung đột
- Giải đấu (League/Tournament) và kết quả
- Thành tích thành viên
- Báo cáo cơ bản
- 10 test cases JUnit

