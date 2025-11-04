@echo off
chcp 65001 > nul
cd /d %~dp0
javac -d target/classes -encoding UTF-8 -sourcepath src/main/java src/main/java/com/club/Main.java src/main/java/com/club/**/*.java 2>nul
if %errorlevel% neq 0 (
    echo Compiling all source files...
    for /r src\main\java %%f in (*.java) do @javac -d target/classes -encoding UTF-8 -cp target/classes %%f
)
java -Dfile.encoding=UTF-8 -cp target/classes com.club.Main
pause

