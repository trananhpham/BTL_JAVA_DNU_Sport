# 🔧 Hướng dẫn Fix lỗi Java Version

## ❌ Các lỗi thường gặp

### Lỗi 1: UnsupportedClassVersionError
```
Exception in thread "main" java.lang.UnsupportedClassVersionError: 
com/club/model/Competition has been compiled by a more recent version 
of the Java Runtime (class file version 69.0), this version of the 
Java Runtime only recognizes class file versions up to 61.0
```

**Nghĩa là gì?**
- File `.class` được biên dịch bằng Java 21 (version 69.0)
- Nhưng bạn đang chạy bằng Java 17 (version 61.0)
- Java 17 không thể chạy code đã biên dịch bằng Java 21

### Lỗi 2: Arrow in case statement
```
error: arrow in case statement supported from Java 14 onwards only
```

**Nghĩa là gì?**
- Code dùng switch expression (Java 14+)
- Nhưng IDE biên dịch với Java < 14

## ✅ Giải pháp: 1 lệnh duy nhất

### Bước 1: Chạy Clean Build

**PowerShell:**
```powershell
cd E:\sports-club-manager\sports-club-manager
.\clean-build.ps1
```

**CMD:**
```cmd
cd E:\sports-club-manager\sports-club-manager
clean-build.bat
```

Script này sẽ:
1. ✅ Xóa hoàn toàn thư mục `target/` (chứa file .class cũ)
2. ✅ Tạo thư mục mới
3. ✅ Tìm JDK 17 tại `C:\Program Files\Java\jdk-17`
4. ✅ Biên dịch lại toàn bộ với JDK 17

### Bước 2: Chạy chương trình

**PowerShell:**
```powershell
.\run.ps1
```

**CMD:**
```cmd
run.bat
```

## 🎯 Output thành công

```
========================================
  CLEAN BUILD - Xóa và biên dịch lại
========================================

[1/4] Đang xóa thư mục target cũ...
      ✅ Đã xóa thư mục target
[2/4] Đang tạo thư mục target mới...
      ✅ Đã tạo thư mục target/classes
[3/4] Đang tìm JDK 17...
      ✅ Tìm thấy JDK 17 tại: C:\Program Files\Java\jdk-17
[4/4] Đang biên dịch với JDK 17...
      ✅ Biên dịch thành công!

========================================
  ✅ HOÀN TẤT! Bây giờ chạy: .\run.ps1
========================================
```

## 🔍 Tại sao lỗi này xảy ra?

### Nguyên nhân:
1. **IDE (Eclipse/IntelliJ) tự động biên dịch** khi bạn save file
2. IDE có thể dùng **Java version khác** với version bạn muốn chạy
3. File `.class` được lưu trong `target/classes/`
4. Khi chạy, JVM load file `.class` cũ → **không tương thích** → lỗi

### Ví dụ:
- IDE dùng Java 21 → tạo file `.class` version 69.0
- Bạn chạy với Java 17 → chỉ đọc được version 61.0
- → **CRASH!**

## 📋 Bảng tra cứu Class File Version

| Class Version | Java Version | Ghi chú |
|--------------|--------------|---------|
| **69.0** | **Java 21** | IDE có thể dùng version này |
| 65.0 | Java 21 (preview) | |
| 64.0 | Java 20 | |
| 63.0 | Java 19 | |
| 62.0 | Java 18 | |
| **61.0** | **Java 17** | **✅ Project này dùng version này** |
| 60.0 | Java 16 | |
| 59.0 | Java 15 | |
| 58.0 | Java 14 | Switch expression được thêm vào |
| 55.0 | Java 11 | LTS |
| 52.0 | Java 8 | LTS |

## 🛡️ Ngăn chặn lỗi trong tương lai

### 1. Tắt auto-build trong IDE

**Eclipse:**
- Project → Build Automatically → **BỎ CHỌN**

**IntelliJ IDEA:**
- File → Settings → Build, Execution, Deployment → Compiler
- Build project automatically → **BỎ CHỌN**

### 2. Luôn dùng script để build và chạy

**Đừng:**
- ❌ Nhấn Run trong IDE
- ❌ Dùng Maven/Gradle từ IDE
- ❌ Để IDE tự động build

**Hãy:**
- ✅ Dùng `.\clean-build.ps1` để build
- ✅ Dùng `.\run.ps1` để chạy
- ✅ Kiểm soát hoàn toàn Java version

### 3. Kiểm tra Java version

```powershell
# Kiểm tra Java đang dùng
java -version

# Kiểm tra JDK 17
& 'C:\Program Files\Java\jdk-17\bin\java.exe' -version
```

Output mong muốn:
```
java version "17.0.x"
Java(TM) SE Runtime Environment (build 17.0.x+...)
Java HotSpot(TM) 64-Bit Server VM (build 17.0.x+..., mixed mode, sharing)
```

## 🚨 Troubleshooting

### Vấn đề: Không tìm thấy JDK 17

**Lỗi:**
```
⚠️ Không tìm thấy JDK 17, sử dụng Java từ PATH
```

**Giải pháp:**
1. Tải JDK 17 từ: https://www.oracle.com/java/technologies/downloads/#java17
2. Cài đặt vào: `C:\Program Files\Java\jdk-17`
3. Chạy lại `.\clean-build.ps1`

### Vấn đề: Vẫn lỗi sau khi clean build

**Giải pháp:**
1. Xóa thủ công:
   ```powershell
   Remove-Item -Recurse -Force target
   ```

2. Kiểm tra không có process nào đang giữ file:
   ```powershell
   # Đóng tất cả terminal/IDE
   # Mở terminal mới
   ```

3. Chạy lại:
   ```powershell
   .\clean-build.ps1
   .\run.ps1
   ```

### Vấn đề: IDE vẫn tự động build

**Giải pháp:**
1. Đóng IDE hoàn toàn
2. Xóa thư mục target:
   ```powershell
   Remove-Item -Recurse -Force target
   ```
3. Chạy clean build:
   ```powershell
   .\clean-build.ps1
   ```
4. Mở lại IDE (nhớ tắt auto-build)

## 📚 Tài liệu liên quan

- `README.md` - Hướng dẫn tổng quan
- `SOLUTION.md` - Giải pháp chi tiết
- `QUICK_START.md` - Hướng dẫn nhanh

## ✅ Checklist

Trước khi chạy chương trình:
- [ ] Đã tắt auto-build trong IDE
- [ ] Đã chạy `.\clean-build.ps1`
- [ ] Thấy message "✅ Biên dịch thành công!"
- [ ] Chạy `.\run.ps1`
- [ ] Thấy menu hiển thị đầy đủ

Nếu tất cả đều ✅ → **Chương trình sẵn sàng!** 🎉


