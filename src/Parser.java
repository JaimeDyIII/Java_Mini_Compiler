import java.util.List;

public class Parser{
    // improvement needed, need to allow variable declaration and variable assignment, 
    // need to allow int a = 0;, int a;, a = 0; but not a;
    private int position;
    private List<Token> tokens;
    private GUI gui;

    public Parser(List<Token> tokens, GUI gui){
        this.gui = gui;
        this.tokens = tokens;
        position = 0;
        parse();
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
            if(currentToken().getTokenType() == Token.Type.ASSIGN_OP){
                assignmentOperator();
            }
        } else {
            syntaxErrorMessage(currentToken());
        }
    }

    private void assignmentOperator(){
        if(currentToken().getTokenType() == Token.Type.ASSIGN_OP){
            nextToken();
            if(currentToken().getTokenType() == Token.Type.STRING_LIT){
                stringLiteral();
            } else if (currentToken().getTokenType() == Token.Type.CHAR_LIT) {
                charLiteral();
            }else if (currentToken().getTokenType() == Token.Type.CONSTANT) {
                constant();
            }
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

    private void charLiteral(){
        if(currentToken().getTokenType() == Token.Type.CHAR_LIT){
            nextToken();
            delimeter();
        } else {
            syntaxErrorMessage(currentToken());
        }
    }

    private void constant(){
        if(currentToken().getTokenType() == Token.Type.CONSTANT){
            nextToken();
            delimeter();
        } else {
            syntaxErrorMessage(currentToken());
        }
    }

    private void delimeter(){
        if(currentToken().getTokenType() == Token.Type.DELIMITER){
            nextToken();
            if (currentToken().getTokenType() == Token.Type.DATA_TYPES){
              dataType();
            } else if(currentToken().getTokenType() == Token.Type.IDENTIFIER) {
              identifier();
            } else if (isEndOfFile()) {
                endOfFile();  
            }
        } else {
            syntaxErrorMessage(currentToken());
        }
    }

    private void endOfFile(){
        System.out.println("End of file reached!");
    }

    public void parse(){
        dataType();
        if(currentToken().getTokenType() == Token.Type.EOF){
            gui.updateStatus("Syntax Analysis Successful");
        } else {
            gui.updateStatus("Syntax Analysis Unsuccessful! Syntax Error Found");
        }
    }
}