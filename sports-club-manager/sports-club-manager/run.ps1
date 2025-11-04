# Set UTF-8 encoding
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$PSDefaultParameterValues['*:Encoding'] = 'utf8'

# Navigate to script directory
Set-Location $PSScriptRoot

# Find Java compiler and runtime
$jdk17Path = "C:\Program Files\Java\jdk-17"
if (Test-Path "$jdk17Path\bin\javac.exe") {
    $javac = "$jdk17Path\bin\javac.exe"
    $java = "$jdk17Path\bin\java.exe"
    Write-Host "Using JDK 17 from: $jdk17Path" -ForegroundColor Cyan
} else {
    $javac = "javac"
    $java = "java"
    Write-Host "Using Java from PATH (make sure it is Java 14+)" -ForegroundColor Yellow
}

# Create target directory if not exists
if (-not (Test-Path "target\classes")) {
    New-Item -ItemType Directory -Force -Path "target\classes" | Out-Null
}

# Compile Java files
Write-Host "Compiling Java files..." -ForegroundColor Green
$javaFiles = Get-ChildItem -Path "src\main\java" -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName
& $javac -d target/classes -encoding UTF-8 -sourcepath src/main/java $javaFiles

if ($LASTEXITCODE -eq 0) {
    Write-Host "Compilation successful!" -ForegroundColor Green
    Write-Host "Running application..." -ForegroundColor Cyan
    Write-Host ""
    
    # Run the application
    & $java "-Dfile.encoding=UTF-8" "-cp" "target/classes" "com.club.Main"
} else {
    Write-Host "Compilation failed!" -ForegroundColor Red
    exit 1
}
