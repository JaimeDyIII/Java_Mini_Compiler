import java.util.List;

public class Parser{
    private int position;
    private List<Token> tokens;
    private GUI gui;
    private boolean encounteredError = false;


    public Parser(List<Token> tokens, GUI gui){
        this.gui = gui;
        this.tokens = tokens;
        position = 0;
        parse();
    }
    
    private Token currentToken(){
        return tokens.get(position);
    }
    
    private Token.Type currentTokenType(){
        return currentToken().getTokenType(); 
    }
    
    private void nextToken(){
        if(position < tokens.size() - 1){
            position++;
        }
    }

    private boolean isEndOfFile(){
        return currentTokenType() == Token.Type.EOF;

    }

    private boolean isDelimiter(){
        return currentTokenType() == Token.Type.DELIMITER;
    }

    private boolean isAssignment(){
        return currentTokenType() == Token.Type.ASSIGN_OP;
    }

    private void syntaxError(Token token){
        encounteredError = true;
        if(isEndOfFile()){
            System.out.println("Reached end of file while parsing!");
        } else {
            System.out.println("Syntax Error: " + token);
        }
    }

    private boolean isData(){
        return  currentTokenType() == Token.Type.CONSTANT   || 
                currentTokenType() == Token.Type.CHAR_LIT   || 
                currentTokenType() == Token.Type.STRING_LIT;
    }

    private void expression(){
        switch(currentTokenType()){
            case DATA_TYPES:
                nextToken();
                variableDeclaration();
                break;
            case IDENTIFIER:
                nextToken();
                variableReassignment();
                break;
            case EOF:
                syntaxError(currentToken());
                break;
            default:
                syntaxError(currentToken());
        }
    }

    private void variableDeclaration(){
        if(currentTokenType() != Token.Type.IDENTIFIER){
            syntaxError(currentToken());
            return;
        }

        nextToken();

        switch(currentTokenType()){
            case DELIMITER:
                nextToken();
                
                if(!isEndOfFile()){
                    expression();
                } else {
                    syntaxError(currentToken());
                }
                
                break;
            case ASSIGN_OP:
                nextToken();
                variableInstatiation();
                break;
            default:
                syntaxError(currentToken());
        }
    }

    private void variableInstatiation(){
        if(!isData()) {
            syntaxError(currentToken());
            return;
        }

        nextToken();

        if(!isDelimiter()){
            syntaxError(currentToken());
            return;
        }

        nextToken();
        
        if(!isEndOfFile()){
            expression();
        }
    }

    private void variableReassignment(){
        if(!isAssignment()){
            syntaxError(currentToken());
            return;
        }

        nextToken();

        if(!isData()){
            syntaxError(currentToken());
            return;
        }

        nextToken();

        if(!isDelimiter()){
            syntaxError(currentToken());
            return;
        }

        nextToken();
    
        if(!isEndOfFile()){
            expression();
        }
    }

    public void parse(){
        expression();

        if(encounteredError || !isEndOfFile()){
            System.out.println("Syntax Error!");
            gui.updateStatus("Syntax Analysis Unsuccessful!");
            return;
        }
        
        System.out.println("Syntax Analysis Successful!");
        gui.updateStatus("Syntax Analysis Successful!");
    }
}