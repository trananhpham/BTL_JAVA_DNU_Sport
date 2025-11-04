# Hướng dẫn nhanh / Quick Start Guide

## 🚀 Cách chạy nhanh nhất

### Windows

**Chạy bằng PowerShell (Khuyến nghị):**
```powershell
.\run.ps1
```

**Hoặc chạy bằng Command Prompt:**
```cmd
run.bat
```

### Linux/Mac

```bash
# Tạo thư mục target
mkdir -p target/classes

# Biên dịch
javac -d target/classes -encoding UTF-8 -sourcepath src/main/java $(find src/main/java -name "*.java")

# Chạy
java -Dfile.encoding=UTF-8 -cp target/classes com.club.Main
```

## ✅ Kiểm tra Java

Đảm bảo bạn đã cài đặt Java 17 trở lên:

```bash
java -version
javac -version
```

Nếu chưa có, tải Java tại: https://www.oracle.com/java/technologies/downloads/

## 📋 Menu chính

Khi chạy chương trình, bạn sẽ thấy menu với 11 tùy chọn:

1. **Quản lý thành viên** - CRUD thành viên
2. **Quản lý huấn luyện viên** - CRUD huấn luyện viên
3. **Quản lý môn thể thao** - Thêm môn thể thao (Football, Basketball, Tennis, Badminton)
4. **Lịch tập** - Xem, thêm, cập nhật lịch tập
5. **Giải đấu & Kết quả** - Quản lý giải đấu và ghi nhận kết quả
6. **Báo cáo & Thống kê** - Xem các báo cáo chi tiết
7. **Quản lý phí thành viên** ⭐ - Theo dõi phí và ngày hết hạn
8. **Điểm danh & Tham dự** ⭐ - Ghi nhận tham dự buổi tập
9. **Quản lý thiết bị** ⭐ - Quản lý trang thiết bị và bảo trì
10. **Xuất báo cáo** ⭐ - Export dữ liệu ra file
11. **Thông báo** ⭐ - Xem thông báo quan trọng

⭐ = Tính năng mới trong phiên bản 2.0

## 📁 Dữ liệu mẫu

Lần chạy đầu tiên, chương trình sẽ tự động tạo:
- 5 thành viên mẫu
- 3 huấn luyện viên
- 4 môn thể thao
- 6 lịch tập
- 2 giải đấu
- Dữ liệu phí, điểm danh, thiết bị

Tất cả dữ liệu được lưu trong thư mục `data/` dạng CSV.

## 🧪 Chạy tests

```bash
# Với Maven
mvn test

# Hoặc biên dịch và chạy thủ công
javac -cp "target/classes" -d target/test-classes src/test/java/com/club/AppTest.java
java -cp "target/classes:target/test-classes:junit-5.jar" org.junit.runner.JUnitCore com.club.AppTest
```

## ❓ Gặp vấn đề?

### Lỗi: "mvn not found"
→ Sử dụng script `run.ps1` hoặc `run.bat` thay vì Maven

### Lỗi: "Cannot find symbol" hoặc compilation error
→ Đảm bảo bạn dùng Java 17+: `java -version`

### Tiếng Việt hiển thị sai ký tự
→ Chạy bằng `run.ps1` hoặc `run.bat` (đã cấu hình UTF-8)

### Lỗi: "Arrow in case statement"
→ Cần Java 14+. Kiểm tra: `javac -version`

## 📞 Liên hệ

Nếu gặp lỗi khác, vui lòng kiểm tra:
1. Java version >= 17
2. Đã xóa thư mục `target` cũ: `Remove-Item -Recurse -Force target`
3. Biên dịch lại: `.\run.ps1`

