# Script để xóa target cũ và biên dịch lại từ đầu
# Sử dụng khi gặp lỗi UnsupportedClassVersionError

# Set UTF-8 encoding
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$PSDefaultParameterValues['*:Encoding'] = 'utf8'

# Navigate to script directory
Set-Location $PSScriptRoot

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  CLEAN BUILD - Xóa và biên dịch lại" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Step 1: Remove old target directory
Write-Host "[1/4] Đang xóa thư mục target cũ..." -ForegroundColor Yellow
if (Test-Path "target") {
    Remove-Item -Recurse -Force "target" -ErrorAction SilentlyContinue
    Write-Host "      ✅ Đã xóa thư mục target" -ForegroundColor Green
} else {
    Write-Host "      ℹ️  Thư mục target không tồn tại" -ForegroundColor Gray
}

# Step 2: Create new target directory
Write-Host "[2/4] Đang tạo thư mục target mới..." -ForegroundColor Yellow
New-Item -ItemType Directory -Force -Path "target\classes" | Out-Null
Write-Host "      ✅ Đã tạo thư mục target/classes" -ForegroundColor Green

# Step 3: Find JDK 17
Write-Host "[3/4] Đang tìm JDK 17..." -ForegroundColor Yellow
$jdk17Path = "C:\Program Files\Java\jdk-17"
if (Test-Path "$jdk17Path\bin\javac.exe") {
    $javac = "$jdk17Path\bin\javac.exe"
    $java = "$jdk17Path\bin\java.exe"
    Write-Host "      ✅ Tìm thấy JDK 17 tại: $jdk17Path" -ForegroundColor Green
} else {
    $javac = "javac"
    $java = "java"
    Write-Host "      ⚠️  Không tìm thấy JDK 17, sử dụng Java từ PATH" -ForegroundColor Yellow
    Write-Host "      ⚠️  Đảm bảo Java version >= 14" -ForegroundColor Yellow
}

# Step 4: Compile
Write-Host "[4/4] Đang biên dịch với JDK 17..." -ForegroundColor Yellow
$javaFiles = Get-ChildItem -Path "src\main\java" -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName
& $javac -d target/classes -encoding UTF-8 -sourcepath src/main/java $javaFiles

if ($LASTEXITCODE -eq 0) {
    Write-Host "      ✅ Biên dịch thành công!" -ForegroundColor Green
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "  ✅ HOÀN TẤT! Bây giờ chạy: .\run.ps1" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
} else {
    Write-Host "      ❌ Biên dịch thất bại!" -ForegroundColor Red
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "  ❌ LỖI BIÊN DỊCH" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    exit 1
}


