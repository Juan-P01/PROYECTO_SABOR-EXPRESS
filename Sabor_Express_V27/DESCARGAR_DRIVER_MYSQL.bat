@echo off
setlocal
set "DIR=%~dp0web\WEB-INF\lib"
set "JAR=%DIR%\mysql-connector-j-8.0.33.jar"
set "URL=https://repo.maven.apache.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar"
if not exist "%DIR%" mkdir "%DIR%"
if exist "%JAR%" (
  echo MySQL Connector/J ya existe:
  echo %JAR%
  exit /b 0
)
echo Descargando MySQL Connector/J 8.0.33...
powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -Uri '%URL%' -OutFile '%JAR%'"
if errorlevel 1 (
  echo.
  echo ERROR: no se pudo descargar el driver.
  echo Puedes descargarlo desde la pagina oficial de MySQL y copiar el JAR a:
  echo %DIR%
  exit /b 1
)
echo.
echo Driver instalado correctamente:
echo %JAR%
endlocal
