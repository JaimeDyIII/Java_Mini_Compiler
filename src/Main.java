import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main{
    public static void main(String[] args) throws IOException{
        File file = new File("./code.txt");

        Lexer lexer = new Lexer(file);
        List<Token> Tokens = lexer.getTokens();

        for(Token token: Tokens){
            System.out.println(token.toString());
        }
    }
}