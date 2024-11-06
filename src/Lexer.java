import java.util.ArrayList;
import java.util.List;

public class Lexer{
    /* 
       Lexer could either use Reader or Regex.
       Reader is theoretically better for error handling and flexibility. 
    */

    private String input;
    private int currentPosition;

    public Lexer(String input){
        this.input = input;
        this.currentPosition = 0;
    }

    public List<Token> tokenize(){
        List<Token> tokens = new ArrayList<>();

        // turn lexeme into token

        return tokens;
    }

    private Token nextToken(){

        // move to the next lexeme

        return null;
    }
}