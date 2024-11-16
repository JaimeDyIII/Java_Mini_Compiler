import java.util.ArrayList;
import java.util.List;

public class Parser{
    private int position;
    private List<Token> tokens;
        
    public Parser(List<Token> tokens){
        tokens = new ArrayList<>();
        position = 0;
    }
    
    private Token currentToken(){
        return tokens.get(position);
    }
    
    private void nextToken(){
        position++;
    }

    private boolean isEndOfFile(){
        return currentToken().getTokenType() == Token.Type.EOF;
    }

    private void syntaxErrorMessage(Token token){
        System.out.println("Syntax Error: " + token);
    }

    private void dataType(){
        if(currentToken().getTokenType() == Token.Type.DATA_TYPES){
            nextToken();
            identifier();
        } else {
            syntaxErrorMessage(currentToken());
        }
    }

    private void identifier(){
        if(currentToken().getTokenType() == Token.Type.IDENTIFIER){
            nextToken();
            assignmentOperator();
        } else {
            syntaxErrorMessage(currentToken());
        }
    }

    private void assignmentOperator(){
        if(currentToken().getTokenType() == Token.Type.ASSIGN_OP){
            nextToken();
            stringLiteral();
        } else {
            syntaxErrorMessage(currentToken());
        }
    }

    private void stringLiteral(){
        if(currentToken().getTokenType() == Token.Type.STRING_LIT){
            nextToken();
            delimeter();
        } else {
            syntaxErrorMessage(currentToken());
        }
    }

    private void delimeter(){
        if(currentToken().getTokenType() == Token.Type.DELIMITER){
            nextToken();
            if (isEndOfFile()) {
              endOfFile();  
            } else{
                dataType();
            }
        } else {
            syntaxErrorMessage(currentToken());
        }
    }

    // Stopped here, finish later
    private void endOfFile(){
        if(isEndOfFile()){
            
        }
    }

    public void Parse(List<Token> tokens){
        dataType();
    }
}