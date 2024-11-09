import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Lexer{
    BufferedReader br;
    int currentChar;
    List<Token> tokens;
    
    public List<Token> getTokens(){
        return tokens;
    }

    public Lexer(File file) throws IOException {
        FileReader fr = new FileReader(file);
        br = new BufferedReader(fr);
        tokenize();
    }

    private String buildWord() throws IOException{
        // if char is letter, will append the string until 
        // the reader reads something that is not a letter
        // or a number because identifiers can have a number
        // but should always start as a letter
        StringBuilder str = new StringBuilder();

        while(Character.isAlphabetic(currentChar) || Character.isDigit(currentChar)){
            str.append((char) currentChar);
            currentChar = br.read();
        }

        return str.toString();
    } 

    private Integer buildNumber() throws IOException{
        // if char is number, will append the string until
        // the reader reads something that is not a number
        StringBuilder num = new StringBuilder();

        while(Character.isDigit(currentChar)){
            num.append((char) currentChar);
            currentChar = br.read();
        }

        return Integer.valueOf(num.toString());
    }


    private void tokenizeLetters(String str){
        switch(str){
            case "int":
                tokens.add(new Token(Token.Type.DATA_TYPES, str));
                break;
            case "String":
                tokens.add(new Token(Token.Type.DATA_TYPES, str));
                break;
            case "boolean":
                tokens.add(new Token(Token.Type.DATA_TYPES, str));
                break;   
            case "char":
                tokens.add(new Token(Token.Type.DATA_TYPES, str));
                break;
            case "float":
                tokens.add(new Token(Token.Type.DATA_TYPES, str));
                break;
            default:
                tokens.add(new Token(Token.Type.IDENTIFIER, str));
                break;
        }
    }

    public List<Token> tokenize() throws IOException{
        tokens = new ArrayList<>();

        while((currentChar = br.read()) != -1){
            if(Character.isAlphabetic(currentChar)){
                String str = buildWord();

                tokenizeLetters(str);

            } else if(Character.isDigit(currentChar)){
                Integer num = buildNumber();

                tokens.add(new Token(Token.Type.CONSTANT, num.toString()));

            } else if(currentChar == '=') {
                tokens.add(new Token(Token.Type.ASSIGN_OP, "="));
            }
        }
        return tokens;
    }
}