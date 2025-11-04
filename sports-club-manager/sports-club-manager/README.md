
# Quản lý Câu Lạc Bộ Thể Thao — Java OOP Console

> Java 17 • Maven • Lưu trữ CSV • OOP (Kế thừa/Đa hình/Interface) • Không dùng CSDL

## Cách chạy

### Phương pháp 1: Sử dụng script (Khuyến nghị)

**Windows PowerShell:**
```powershell
cd sports-club-manager
.\run.ps1
```

**Windows Command Prompt:**
```cmd
cd sports-club-manager
run.bat
```

### Phương pháp 2: Sử dụng Maven

```bash
cd sports-club-manager
mvn -q -DskipTests exec:java
```

### Phương pháp 3: Biên dịch và chạy thủ công

```bash
cd sports-club-manager
javac -d target/classes -encoding UTF-8 -sourcepath src/main/java src/main/java/com/club/**/*.java
java -Dfile.encoding=UTF-8 -cp target/classes com.club.Main
```

**Lưu ý:** 
- Dữ liệu khởi tạo được nạp từ thư mục `data/` (tự động tạo nếu thiếu).
- Ứng dụng **tự động ghi tệp** khi thoát (Shutdown Hook).
- Script `run.ps1` và `run.bat` đã cấu hình UTF-8 để hiển thị tiếng Việt chính xác.

## Cấu trúc

- `com.club.model`: Person, Member, Coach, Sport (Football/Basketball/Tennis/Badminton), Schedule, Competition (League/Tournament), Result, Achievement, MembershipFee, Attendance, Equipment.
- `com.club.repo`: Repository cho Member/Coach/Sport/Schedule/Competition/Result/Achievement/MembershipFee/Attendance/Equipment (CSV + `Persistable`).
- `com.club.service`: Dịch vụ logic (`SchedulingService`, `ReportService`, `DataBootstrap`, `ExportService`, `NotificationService`).
- `com.club.cli`: Menu CLI (CRUD, lịch tập, giải đấu, báo cáo, phí thành viên, điểm danh, thiết bị, xuất báo cáo, thông báo).
- `data/`: Thư mục CSV (members.csv, coaches.csv, sports.csv, schedules.csv, competitions.csv, results.csv, achievements.csv, fees.csv, attendance.csv, equipment.csv).

## Định dạng

- ID: UUID.
- Ngày: `yyyy-MM-dd`, Giờ: `HH:mm`.
- Trạng thái lịch tập: `SCHEDULED|COMPLETED|CANCELLED`.

## Tính năng

### Chức năng cốt lõi
- Quản lý thành viên, huấn luyện viên, môn thể thao
- Quản lý lịch tập với kiểm tra xung đột
- Quản lý giải đấu (League/Tournament) và kết quả
- Báo cáo và thống kê

### Tính năng mới
- **Quản lý phí thành viên**: Theo dõi phí, ngày hết hạn, trạng thái thanh toán
- **Điểm danh & Tham dự**: Ghi nhận tham dự buổi tập, tính tỷ lệ tham dự
- **Quản lý thiết bị**: Theo dõi trang thiết bị, trạng thái, bảo trì
- **Xuất báo cáo**: Export dữ liệu ra file (CSV/TXT/JSON)
- **Thông báo thông minh**: Cảnh báo phí hết hạn, thiết bị cần bảo trì, lịch tập sắp tới

## Test

```bash
mvn -q test
```

- Có sẵn 20 test JUnit trong `src/test/java/com/club/`.

## Báo cáo mẫu

- Thống kê số thành viên tham gia từng môn thể thao.
- Kết quả & xếp hạng giải đấu gần nhất.
- Top 5 thành viên đạt nhiều thành tích nhất.
- Tỷ lệ tham dự của từng thành viên.
- Báo cáo thiết bị theo trạng thái và danh mục.
- Danh sách phí sắp hết hạn và đã hết hạn.

## Kiến trúc OOP

Dự án sử dụng các nguyên lý OOP:
- **Kế thừa**: Person → Member/Coach; Sport → Football/Basketball/Tennis/Badminton; Competition → League/Tournament
- **Đa hình**: Các phương thức `getTrainingSchedule()`, `getType()` được override
- **Interface**: `Persistable`, `Schedulable` cho tính mở rộng
- **Encapsulation**: Các field private với getter/setter
- **Enum**: FeeStatus, AttendanceStatus, EquipmentStatus, ScheduleStatus

## Gợi ý mở rộng

- Gợi ý môn thể thao phù hợp theo lịch sử tham gia.
- Tích hợp xuất PDF (hiện có CSV/TXT/JSON).
- Dashboard với biểu đồ thống kê.
- Hệ thống xếp hạng thành viên.
- Email/SMS tự động cho thông báo.
