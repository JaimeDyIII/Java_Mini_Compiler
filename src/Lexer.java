import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Lexer{
    private List<Token> tokens;
    
    public List<Token> getTokens(){
        return tokens;
    }

    public Lexer(File file) throws IOException {
        String fileString = Files.readString(file.toPath());
        tokenize(fileString);
    }


    public List<Token> tokenize(String input) throws IOException{
        tokens = new ArrayList<>();

        String dataTypesPattern = "byte|short|int|String|boolean|char|float|Double";
        String identifierPattern = "[a-zA-Z_][a-zA-Z0-9_]*";
        String stringLitPattern = "\"[^\"]*\"";
        String assignOpPattern = "=";
        String delimiterPattern = ";";
        String constantPattern = "-?(?:\\d+(\\.\\d*)?|\\.\\d+)([eE][-+]?\\d+)?[fF]?";

        String tokenPatterns = "(\\s+)|(" + dataTypesPattern + 
                             ")|(" + identifierPattern +
                             ")|(" + stringLitPattern +
                             ")|(" + assignOpPattern + 
                             ")|(" + delimiterPattern +
                             ")|(" + constantPattern + ")";

        Pattern pattern = Pattern.compile(tokenPatterns);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            if(matcher.group(1) != null){
                continue;
            } else if(matcher.group(2) != null) {
                tokens.add(new Token(Token.Type.DATA_TYPES, matcher.group()));
            } else if (matcher.group(3) != null) {
                tokens.add(new Token(Token.Type.IDENTIFIER, matcher.group()));
            } else if (matcher.group(4) != null) {
                tokens.add(new Token(Token.Type.STRING_LIT, matcher.group()));
            } else if (matcher.group(5) != null) {
                tokens.add(new Token(Token.Type.ASSIGN_OP, matcher.group()));
            } else if (matcher.group(6) != null) {
                tokens.add(new Token(Token.Type.DELIMITER, matcher.group()));
            } else if (matcher.group(7) != null) {
                tokens.add(new Token(Token.Type.CONSTANT, matcher.group()));
            }
        }

        String nonTokenPattern = "((?!" + tokenPatterns +").)+";

        pattern = Pattern.compile(nonTokenPattern);
        matcher = pattern.matcher(input);

        while(matcher.find()){
            System.out.println("Unexpected token found: " + matcher.group());
        }

        tokens.add(new Token(Token.Type.EOF, "eof"));
        return tokens;
    }
}