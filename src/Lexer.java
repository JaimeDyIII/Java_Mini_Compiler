import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Lexer{
    List<Token> tokens;
    public List<Token> getTokens(){
        return tokens;
    }

    public Lexer(File file) throws IOException {
        String fileString = Files.readString(file.toPath());
        tokenize(fileString);
    }


    public List<Token> tokenize(String input) throws IOException{
        tokens = new ArrayList<>();

        String dataTypesPattern = "int|String|boolean|char|float|double";
        String identifierPattern = "[a-zA-Z_][a-zA-Z0-9_]*";
        String constantPattern = "\\d+(\\.\\d+)?";
        String stringLitPattern = "I'm gonna cry, I'm just gonna add this to follow";
        String assignOpPattern = "=";

        String tokenPatterns = "(" + dataTypesPattern + 
                             ")|(" + identifierPattern + 
                             ")|(" + constantPattern + 
                             ")|(" + stringLitPattern + 
                             ")|(" + assignOpPattern + ")";

        Pattern pattern = Pattern.compile(tokenPatterns);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                tokens.add(new Token(Token.Type.DATA_TYPES, matcher.group()));
            } else if (matcher.group(2) != null) {
                tokens.add(new Token(Token.Type.IDENTIFIER, matcher.group()));
            } else if (matcher.group(3) != null) {
                tokens.add(new Token(Token.Type.CONSTANT, matcher.group()));
            } else if (matcher.group(4) != null) {
                tokens.add(new Token(Token.Type.STRING_LIT, matcher.group()));
            } else if (matcher.group(5) != null) {
                tokens.add(new Token(Token.Type.ASSIGN_OP, matcher.group()));
            }
        }
        return tokens;
    }
}