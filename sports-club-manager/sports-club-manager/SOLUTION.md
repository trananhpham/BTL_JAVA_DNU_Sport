# ✅ Giải pháp hoàn chỉnh cho lỗi Java Version

## 🔍 Các lỗi thường gặp

### Lỗi 1: "Arrow in case statement supported from Java 14 onwards only"
- **Nguyên nhân:** IDE biên dịch với Java < 14, nhưng code dùng switch expression (Java 14+)
- **Giải pháp:** Xóa target và biên dịch lại với JDK 17

### Lỗi 2: "UnsupportedClassVersionError: class file version 69.0"
- **Nguyên nhân:** IDE biên dịch với Java 21 (version 69.0), nhưng chạy với Java 17 (version 61.0)
- **Giải pháp:** Xóa target và biên dịch lại với JDK 17

### Nguyên nhân chung:
1. **IDE (Eclipse/IntelliJ) biên dịch với Java version khác** với version chạy
2. Các file `.class` cũ được lưu trong `target/classes/`
3. Khi chạy, JVM load các file `.class` không tương thích → gây lỗi

## ✨ Giải pháp đã áp dụng

### 1. Xóa các file .class cũ
```powershell
Remove-Item -Recurse -Force target
```

### 2. Biên dịch lại với JDK 17
```powershell
& 'C:\Program Files\Java\jdk-17\bin\javac.exe' -d target\classes -encoding UTF-8 -sourcepath src\main\java (Get-ChildItem -Recurse -Path src\main\java -Filter *.java).FullName
```

### 3. Chạy với JDK 17 và UTF-8 encoding
```powershell
& 'C:\Program Files\Java\jdk-17\bin\java.exe' "-Dfile.encoding=UTF-8" "-cp" "target/classes" "com.club.Main"
```

### 4. Cập nhật script run.ps1

Script được cập nhật để:
- ✅ Tự động tìm và sử dụng JDK 17 từ `C:\Program Files\Java\jdk-17`
- ✅ Fallback sang Java từ PATH nếu không tìm thấy JDK 17
- ✅ Biên dịch với đúng encoding UTF-8
- ✅ Chạy với parameters được quote đúng cách

## 🚀 Cách chạy ngay bây giờ

### ⚠️ QUAN TRỌNG: Nếu gặp lỗi UnsupportedClassVersionError

Chạy script clean build trước:

```powershell
cd E:\sports-club-manager\sports-club-manager
.\clean-build.ps1
```

Hoặc dùng CMD:
```cmd
cd E:\sports-club-manager\sports-club-manager
clean-build.bat
```

Script này sẽ:
1. ✅ Xóa hoàn toàn thư mục `target/` cũ
2. ✅ Tạo thư mục mới
3. ✅ Biên dịch lại với JDK 17

### Cách 1: Dùng script PowerShell (KHUYẾN NGHỊ)
```powershell
cd E:\sports-club-manager\sports-club-manager
.\run.ps1
```

### Cách 2: Dùng CMD
```cmd
cd E:\sports-club-manager\sports-club-manager
run.bat
```

### Cách 3: Chạy thủ công
```powershell
# Xóa target cũ (chỉ cần 1 lần)
Remove-Item -Recurse -Force target

# Tạo thư mục
mkdir -Force target\classes

# Biên dịch
& 'C:\Program Files\Java\jdk-17\bin\javac.exe' -d target\classes -encoding UTF-8 -sourcepath src\main\java (Get-ChildItem -Recurse -Path src\main\java -Filter *.java).FullName

# Chạy
& 'C:\Program Files\Java\jdk-17\bin\java.exe' "-Dfile.encoding=UTF-8" "-cp" "target/classes" "com.club.Main"
```

## ✅ Kết quả

Sau khi áp dụng giải pháp:
- ✅ Biên dịch thành công với JDK 17
- ✅ Chạy được chương trình
- ✅ Menu hiển thị đầy đủ 11 tùy chọn
- ✅ Tiếng Việt hiển thị đúng
- ✅ Không còn lỗi "Arrow in case statement"

## 📸 Output mẫu

```
Using JDK 17 from: C:\Program Files\Java\jdk-17
Compiling Java files...
Compilation successful!
Running application...

⚠️ Bạn có 8 thông báo quan trọng!

=== QUẢN LÝ CLB THỂ THAO ===
1. Quản lý thành viên
2. Quản lý huấn luyện viên
3. Quản lý môn thể thao
4. Lịch tập
5. Giải đấu & Kết quả
6. Báo cáo & Thống kê
7. Quản lý phí thành viên
8. Điểm danh & Tham dự
9. Quản lý thiết bị
10. Xuất báo cáo
11. Thông báo
0. Thoát
Chọn:
```

## 🛠️ Lưu ý quan trọng

### Nếu vẫn gặp lỗi tương tự:

1. **Chạy clean build:**
   ```powershell
   .\clean-build.ps1
   ```
   
   Script này sẽ tự động:
   - Xóa hoàn toàn thư mục target
   - Tạo lại thư mục mới
   - Biên dịch với JDK 17

2. **Đảm bảo IDE không tự động biên dịch:**
   - Eclipse: Project → Build Automatically → **TẮT**
   - IntelliJ: File → Settings → Build → Compiler → Build project automatically → **TẮT**

3. **Chạy lại chương trình:**
   ```powershell
   .\run.ps1
   ```

### Bảng tra cứu Class File Version:

| Class Version | Java Version | Mô tả |
|--------------|--------------|-------|
| 69.0 | Java 21 | Latest LTS |
| 65.0 | Java 21 (preview) | |
| 64.0 | Java 20 | |
| 63.0 | Java 19 | |
| 62.0 | Java 18 | |
| 61.0 | **Java 17** | **LTS - Dùng cho project này** |
| 60.0 | Java 16 | |
| 59.0 | Java 15 | |
| 58.0 | Java 14 | Switch expression |
| 55.0 | Java 11 | LTS |
| 52.0 | Java 8 | LTS |

**Lưu ý:** Project này yêu cầu **Java 17 (version 61.0)** để tương thích với switch expression.

### Ngăn chặn vấn đề trong tương lai:

1. **Luôn biên dịch bằng command line** thay vì dùng IDE
2. **Sử dụng script `run.ps1`** để đảm bảo nhất quán
3. **Xóa target trước khi build mới** nếu nghi ngờ có vấn đề

## 📚 Tài liệu liên quan

- `README.md` - Hướng dẫn chi tiết
- `QUICK_START.md` - Hướng dẫn nhanh
- `FIX_SUMMARY.md` - Tóm tắt các vấn đề đã sửa
- `CHANGELOG.md` - Lịch sử thay đổi

## 🎉 Tổng kết

Lỗi **đã được sửa hoàn toàn**! Chương trình giờ đây:
- ✅ Biên dịch và chạy được với JDK 17
- ✅ Hiển thị tiếng Việt đúng
- ✅ Code sạch, không warning
- ✅ Có script tự động hóa
- ✅ Tài liệu đầy đủ

**Sẵn sàng để sử dụng! 🚀**

