import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Lexer{
    // need to add boolean literal
    private int currentChar = 0;
    private List<Token> tokens;
    private GUI gui;
    String fileString;
    StringBuilder stringBuilder;
    boolean encounteredError = false;
    String state;
    public List<Token> getTokens(){
        return tokens;
    }

    public Lexer(File file, GUI gui) throws IOException {
        this.gui = gui;
        fileString = Files.readString(file.toPath());
        tokenize(fileString);
    }

    private boolean isLetter(char c){   
        return Character.isLetter(c);
    }

    private boolean isDigit(char c){
        return Character.isDigit(c);
    }

    private boolean isWhitespace(char c){
        return Character.isWhitespace(c);
    }

    private char peekPreviousChar(int numChar){
        if(numChar <= 0){
            return '\0';
        }

        return fileString.charAt(currentChar - numChar);
    }

    private char peekNextChar(int numChar){
        if((currentChar + numChar) >= fileString.length()){
            return '\0';
        }

        return fileString.charAt(currentChar + numChar);
    }

    private char getChar(){
        if(isEndOfFile()){
            return '\0';    
        }
        return fileString.charAt(currentChar);
    }

    private char nextChar(){
        currentChar++;
        return getChar();
    }

    private boolean isEndOfFile(){
        return currentChar >= fileString.length();
    }
    
    private String buildLetters(){
        stringBuilder = new StringBuilder();
        while((isLetter(getChar()) || isDigit(getChar()) || getChar() == '_' || getChar() == '-') && !isEndOfFile()){
            stringBuilder.append(getChar());
            nextChar();
        }
        return stringBuilder.toString();
    }

    private String buildNumber(){   
        stringBuilder = new StringBuilder();
        state = "";

        // Alternative floating-point without integer with optional positive or negative sign
        if(peekPreviousChar(1) == '.'){
            if(peekPreviousChar(2) == '+' || peekPreviousChar(2) == '-'){
                stringBuilder.append(peekPreviousChar(2));
            }
            stringBuilder.append(peekPreviousChar(1));

            while(isDigit(getChar())){
                stringBuilder.append(getChar());
                nextChar();
            }
            state = "float";
        }

        // for number with integers and optional floating-point
        // Optional sign before the number
        if(peekPreviousChar(1) == '-' || peekPreviousChar(1) == '+'){
            stringBuilder.append(peekPreviousChar(1));
        }

        // Integer or Floating-point
        if(isDigit(getChar())){
            while(isDigit(getChar())){
                stringBuilder.append(getChar());
                nextChar();
            }

            state = "int";

            if(getChar() == '.'){
                state = "float";
                stringBuilder.append(getChar());  
                nextChar();
            }

            while(isDigit(getChar())){
                stringBuilder.append(getChar());
                nextChar();
            }
        }

        // optional scientific notation
        if(getChar() == 'e' || getChar() == 'E'){
            state = "double";
            stringBuilder.append(getChar());
            nextChar();

            if(getChar() == '+' || getChar() == '-'){
                stringBuilder.append(getChar());
            }

            while(isDigit(getChar())){
                stringBuilder.append(getChar());
                nextChar();
            }
        }

        // optional float or double suffix
        if(getChar() == 'f' || getChar() == 'F'){
            state = "float";
            stringBuilder.append(getChar());
            nextChar();
        } else if(getChar() == 'd' || getChar() == 'D'){
            state = "double";
            stringBuilder.append(getChar());
            nextChar();
        }


        // return immediately
        if(isLetter(getChar())){
            state = "error";
            return stringBuilder.toString();
        }
        
        return stringBuilder.toString();
    }


    private void tokenizeLetter(){
        String str = buildLetters();
        
        switch(str){
            case "short":
            case "int":
            case "byte":
            case "float":
            case "double":
            case "boolean":
            case "char":
            case "String":
                tokens.add(new Token(Token.Type.DATA_TYPES, str));
                break;
            case "true":
            case "false":
                tokens.add(new Token(Token.Type.BOOLEAN, str));

            default:
                tokens.add(new Token(Token.Type.IDENTIFIER, str));
                break;
        }
        currentChar--;
    }

    private void tokenizeDigit(){
        String str = buildNumber();
        if(isWhitespace(getChar()) || getChar() == ';'){
            switch(state){
                case "int":
                    tokens.add(new Token(Token.Type.INT, str));
                    break;
                case "float":
                    tokens.add(new Token(Token.Type.FLOAT, str));
                    break;
                case "double":
                    tokens.add(new Token(Token.Type.DOUBLE, str));
                    break;
                case "error":
                case "":
                default:
                    System.out.println("error");
            } 
            currentChar--;  
        } else {
            encounteredError = true;
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);

            while(!isWhitespace(peekNextChar(1)) && peekNextChar(1) != ';' && !isEndOfFile()){
                nextChar();
                stringBuilder.append(getChar());
            }

            System.out.println("Unexpected token found: " + stringBuilder.toString());
        }
    }

    private void tokenizeStringLiteral(){
        stringBuilder = new StringBuilder();
        stringBuilder.append(getChar());
        nextChar();

        while (getChar() != '"' && !isEndOfFile()){
            stringBuilder.append(getChar());
            nextChar();
        }

        if(getChar() == '"'){
            stringBuilder.append(getChar());
        }

        if(isEndOfFile()){
            encounteredError = true;
            System.out.println("Unclosed String literal found: " + stringBuilder.toString());
        } else {
            tokens.add(new Token(Token.Type.STRING_LIT, stringBuilder.toString()));
        }
    }

    private void tokenizeCharLiteral(){
        stringBuilder = new StringBuilder();
        stringBuilder.append(getChar());
        nextChar();

        while (getChar() != '\'' && !isEndOfFile()){
            stringBuilder.append(getChar());
            nextChar();
        }

        if(getChar() == '\''){
            stringBuilder.append(getChar());
        }

        if(isEndOfFile()){
            encounteredError = true;
            System.out.println("Unclosed Character literal found: " + stringBuilder.toString());
        } else {
            tokens.add(new Token(Token.Type.CHAR_LIT, stringBuilder.toString()));
        }
    }

    public void tokenize(String fileString) throws IOException{
        tokens = new ArrayList<>();

        while(!isEndOfFile()){
            char c = getChar();
            switch(c){
                case '"':
                    tokenizeStringLiteral();
                    break;
                case '\'':
                    tokenizeCharLiteral();
                    break;
                case '=':
                    tokens.add(new Token(Token.Type.ASSIGN_OP, "="));
                    break;
                case ';':
                    tokens.add(new Token(Token.Type.DELIMITER, ";"));
                    break;
                default:
                    if(isWhitespace(c)){
                        // ignore
                    } else if(isLetter(c)){
                        tokenizeLetter();
                    } else if(isDigit(c)){
                        tokenizeDigit();
                    } else {
                        encounteredError = true;
                        System.out.println("Unexpected character found: " + c);
                    }
            }
            nextChar();
        }
        tokens.add(new Token(Token.Type.EOF, "eof"));

        if(encounteredError == false){
            gui.updateStatus("Lexical Analysis Successful!");
        } else {
            gui.updateStatus("Lexical Analysis Failed!");
        }
    }
}