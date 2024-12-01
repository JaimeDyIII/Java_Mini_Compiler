@echo java
:: Compile all codes in src folder
echo Compiling Code...
javac -d bin src/*

:: Check if compilation is successful
IF ERRORLEVEL 0 (
    echo Compiling Successful.
    java -cp ./bin Main
) ELSE (
    echo Compiling Failed.
)

pause