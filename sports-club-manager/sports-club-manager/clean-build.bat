@echo off
chcp 65001 > nul
echo ========================================
echo   CLEAN BUILD - Xoa va bien dich lai
echo ========================================
echo.

echo [1/4] Dang xoa thu muc target cu...
if exist target (
    rmdir /s /q target
    echo       [OK] Da xoa thu muc target
) else (
    echo       [i] Thu muc target khong ton tai
)

echo [2/4] Dang tao thu muc target moi...
mkdir target\classes > nul 2>&1
echo       [OK] Da tao thu muc target/classes

echo [3/4] Dang tim JDK 17...
set JDK17_PATH=C:\Program Files\Java\jdk-17
if exist "%JDK17_PATH%\bin\javac.exe" (
    set JAVAC="%JDK17_PATH%\bin\javac.exe"
    set JAVA="%JDK17_PATH%\bin\java.exe"
    echo       [OK] Tim thay JDK 17 tai: %JDK17_PATH%
) else (
    set JAVAC=javac
    set JAVA=java
    echo       [!] Khong tim thay JDK 17, su dung Java tu PATH
)

echo [4/4] Dang bien dich voi JDK 17...
%JAVAC% -d target/classes -encoding UTF-8 -sourcepath src/main/java src/main/java/com/club/*.java src/main/java/com/club/model/*.java src/main/java/com/club/service/*.java src/main/java/com/club/cli/*.java src/main/java/com/club/util/*.java

if %ERRORLEVEL% EQU 0 (
    echo       [OK] Bien dich thanh cong!
    echo.
    echo ========================================
    echo   [OK] HOAN TAT! Bay gio chay: run.bat
    echo ========================================
) else (
    echo       [X] Bien dich that bai!
    echo.
    echo ========================================
    echo   [X] LOI BIEN DICH
    echo ========================================
    exit /b 1
)


