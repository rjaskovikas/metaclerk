@echo off

setlocal

rem set JAVA_HOME=c:\...
set path=%JAVA_HOME%\bin;%path%
set JAVA_OPTS=-Dfile.encoding=utf-8
set MAIN_CLASS=org.rola.metaclerk.MetaClerk
set LIBS="lib/*"

java -cp %LIBS% %JAVA_OPTS% %MAIN_CLASS% %*

endlocal
