# Java_Mini_Compiler #
Java Mini Compiler for Variable Assignment in compliance with our Theory of Programming Class Project.

## About
**How To use**
1. Run using run.bat batch file - run.bat batch file will compile and run the program.
2. Open File Button - Open file to read the java or text file to be read.
3. Lexical Analysis Button - Opens after opening a file, starts the Lexical Analysis phase.
4. Syntax Analysis Button - Opens after Lexical Analysis, starts the Syntax Analysis phase.
5. Semantic Analysis Button - Opens after Syntax Analysis, starts the Semantic Analysis phase.

**Mini Compiler Phases**
1. Lexical Analysis - Read the input file, turn it into string, read it char per char, and turn it into a list of tokens.
2. Syntax Analysis - Read the list of tokens, checks if the syntax is valid through recursive descent parsing.
3. Semantic Analysis - Read the list of tokens, and checks if there are any semantic errors while parsing.

**Type of Semantic Errors**
1. Type Mismatch - When the type of the variable that is declared is not the same as the type of the value.
2. Undeclared Variable - When the variable is not declared but has been tried to assign value to.
3. Multiple Declaration - When the variable is declared more than once.
