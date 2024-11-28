import java.util.List;

public class Parser{
    // redo parser
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
        position++;
    }

    private boolean isEndOfFile(){
        return currentTokenType() == Token.Type.EOF;

    }

    private void reachedEOF(){
        if(isEndOfFile()){
            System.out.println("Reached End of file while parsing!");
        }
    }

    private boolean isDelimiter(){
        return currentTokenType() == Token.Type.DELIMITER;
    }

    private boolean isAssignment(){
        return currentTokenType() == Token.Type.ASSIGN_OP;
    }


    private void syntaxError(Token token){
        encounteredError = true;
        System.out.println("Syntax Error: " + token);
    }

    private boolean isData(){
        return  currentTokenType() == Token.Type.BYTE       || 
                currentTokenType() == Token.Type.SHORT      || 
                currentTokenType() == Token.Type.INT        || 
                currentTokenType() == Token.Type.LONG       || 
                currentTokenType() == Token.Type.FLOAT      || 
                currentTokenType() == Token.Type.DOUBLE     || 
                currentTokenType() == Token.Type.CHAR_LIT   || 
                currentTokenType() == Token.Type.STRING_LIT;
    }

    private void expression(){
        switch(currentTokenType()){
            case DATA_TYPES:
                nextToken();
                DataType();
                break;
            case IDENTIFIER:
                nextToken();
                variableReassignment();
                break;
            case EOF:
                encounteredError = true;
                System.out.println("Reached End of file while parsing!");
                break;
            default:
                syntaxError(currentToken());
        }
    }

    private void DataType(){
        switch(currentTokenType()){
            case IDENTIFIER:
                nextToken();
                identifier();
                break;
            case EOF:
                encounteredError = true;
                System.out.println("Reached End of file while parsing!");
                break;
            default:
                syntaxError(currentToken());
        }
    }

    private void identifier(){
        switch(currentTokenType()){
            case ASSIGN_OP:
                nextToken();
                variableInstatiation();
                break;
            case DELIMITER:
                nextToken();
                variableDeclaration();
                break;
            case EOF:
                encounteredError = true;
                System.out.println("Reached End of file while parsing!");
                break;
            default:
                syntaxError(currentToken());
        }

    }

    private void variableDeclaration(){
        if(!isEndOfFile()){
            expression();

        }
    }

    private void variableInstatiation(){
        if(!isData()) {
            syntaxError(currentToken());
            reachedEOF();
            return;
        }
        nextToken();

        if(!isDelimiter()){
            syntaxError(currentToken());
            reachedEOF();
            return;
        }
        nextToken();
        
        if(currentTokenType() != Token.Type.EOF){
            expression();
        }
    }

    private void variableReassignment(){
        if(!isAssignment()){
            syntaxError(currentToken());
            reachedEOF();
            return;
        }
        nextToken();

        if(!isData()){
            syntaxError(currentToken());
            reachedEOF();
            return;
        }
        nextToken();

        if(!isDelimiter()){
            syntaxError(currentToken());
            reachedEOF();
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