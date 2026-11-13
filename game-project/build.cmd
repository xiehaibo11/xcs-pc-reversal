@echo off
REM One-shot build: full javac compile + resource jar. See README.md.
REM Requires javac/jar/jimage (JDK21) on PATH, repo-root game-jars and the
REM JRE-9 modules file under .sandbox-game (path below uses GBK bytes).
setlocal enabledelayedexpansion
cd /d "%~dp0"

set "MODULES=..\.sandbox-game\ÓÎÏ·¶Ë\jre-9\lib\modules"
if not exist "%MODULES%" (
  echo [build] missing %MODULES% - cannot extract JavaFX 9 classes
  exit /b 1
)

REM ---- 1) dependency jars ----
if not exist out\lib mkdir out\lib
copy /y ..\game-jars\*.jar out\lib\ >nul
del /f /q "out\lib\QueshengDaPai.jar" >nul 2>&1
echo [build] dependency jars copied to out\lib

REM ---- 2) JavaFX 9 classes via jimage (javafx9\ is git-ignored) ----
if not exist javafx9\javafx9-classes.jar (
  echo [build] extracting JavaFX 9 classes ...
  if exist javafx9\_extract rmdir /s /q javafx9\_extract
  mkdir javafx9\_extract
  jimage extract --include "regex:/javafx\.(base|graphics|controls|fxml|swing|media|web)/.*" --dir javafx9\_extract "%MODULES%"
  if errorlevel 1 exit /b 1
  for /d %%m in (javafx9\_extract\*) do del /f /q "%%m\module-info.class" >nul 2>&1
  set "ARGS="
  for /d %%m in (javafx9\_extract\*) do set "ARGS=!ARGS! -C %%m ."
  jar cf javafx9\javafx9-classes.jar !ARGS!
  rmdir /s /q javafx9\_extract
)
echo [build] javafx9\javafx9-classes.jar OK

REM ---- 3) compile ----
if exist out\classes rmdir /s /q out\classes
mkdir out\classes
dir /s /b src\*.java > out\sources.txt
javac --release 8 -encoding UTF-8 -Xlint:-options -J-Duser.language=en -cp "javafx9\javafx9-classes.jar;out\lib\*" -d out\classes @out\sources.txt
set RC=%ERRORLEVEL%
echo javac exit=%RC%
if not "%RC%"=="0" exit /b %RC%
set N=0
for /r out\classes %%f in (*.class) do set /a N+=1
echo compiled classes=%N%

REM ---- 4) package (manifest.mf keeps original Main-Class and Class-Path) ----
if exist out\quesheng.jar del /f /q out\quesheng.jar
jar cfm out\quesheng.jar manifest.mf -C out\classes . -C resources .
for %%F in (out\quesheng.jar) do echo jar=out\quesheng.jar size=%%~zF bytes
echo [build] done. run: cd out ^&^& java -jar quesheng.jar
endlocal
