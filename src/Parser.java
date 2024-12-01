import java.util.List;

public class Parser{
    private int position;
    private List<Token> tokens;
    private GUI gui;
    private boolean encounteredSyntaxError;
    SemanticAnalyzer semanticAnalyzer;
    String currentVariableName;
    Token.Type currentVariableDataType;
    Token.Type currentVariableDeclaredType;


    public Parser(List<Token> tokens, GUI gui){
        this.gui = gui;
        semanticAnalyzer = new SemanticAnalyzer();
        this.tokens = tokens;
        this.encounteredSyntaxError = false;
        this.position = 0;
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
        encounteredSyntaxError = true;
        if(isEndOfFile()){
            System.out.println("Reached end of file while parsing!");
        } else {
            System.out.println("Syntax Error: " + token);
        }
    }

    private boolean isData(){
        return  currentTokenType() == Token.Type.INT      ||
                currentTokenType() == Token.Type.FLOAT    ||
                currentTokenType() == Token.Type.BYTE     ||
                currentTokenType() == Token.Type.SHORT    ||
                currentTokenType() == Token.Type.LONG     ||
                currentTokenType() == Token.Type.DOUBLE   ||
                currentTokenType() == Token.Type.BOOLEAN  || 
                currentTokenType() == Token.Type.CHAR_LIT || 
                currentTokenType() == Token.Type.STRING_LIT;
    }

    private Token.Type determineVariableType(String lexeme) {
        switch(lexeme){
            case "int":
                return Token.Type.INT;
            case "float":
                return Token.Type.FLOAT;
            case "double":
                return Token.Type.DOUBLE;
            case "boolean":
                return Token.Type.BOOLEAN;
            case "char":
                return Token.Type.CHAR_LIT;
            case "String":
                return Token.Type.STRING_LIT;
            default:
                semanticAnalyzer.addError("Invalid data type: " + lexeme);
                return null;
        }
    }    

    private void expression(){
        currentVariableName = null;
        currentVariableDeclaredType = null;
        currentVariableDataType = null;

        switch(currentTokenType()){
            case DATA_TYPES:
                currentVariableDeclaredType = determineVariableType(currentToken().getLexeme());
                nextToken();
                variableDeclaration();
                break;
            case IDENTIFIER:
                currentVariableName = currentToken().getLexeme();

                if(!semanticAnalyzer.isVariableDeclared(currentVariableName)){
                    semanticAnalyzer.setEncounteredSemanticError(true);
                    semanticAnalyzer.addError("Variable is " + currentVariableName + "is not declared!");
                }

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
        
        currentVariableName = currentToken().getLexeme();
        nextToken();

        switch(currentTokenType()){
            case DELIMITER:
                semanticAnalyzer.addVariable(currentVariableName, currentVariableDeclaredType);
                nextToken();
                if(!isEndOfFile()){
                    expression();
                } else {
                    syntaxError(currentToken());
                }
                break;
            case ASSIGN_OP:
                semanticAnalyzer.addVariable(currentVariableName, currentVariableDeclaredType);
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

        currentVariableDataType = currentToken().getTokenType();
        semanticAnalyzer.typeCheck(currentVariableName, currentVariableDataType);
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

        currentVariableDataType = currentToken().getTokenType();
        semanticAnalyzer.typeCheck(currentVariableName, currentVariableDataType);
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
    }

    public void runSyntaxAnalysis(){
        if(encounteredSyntaxError || !isEndOfFile()){
            System.out.println("Syntax Error!");
            gui.updateStatus("Syntax Analysis Unsuccessful!");
            return;
        }

        System.out.println("Syntax Analysis Successful!");
        gui.updateStatus("Syntax Analysis Successful!");
    }

    public void runSemanticAnalysis(){
        if(semanticAnalyzer.hasEncounteredSemanticError()){
            System.out.println("Semantic Error!");
            gui.updateStatus("Semantic Analysis Unsuccessful!");
            semanticAnalyzer.printErrors();
            return;
        }

        System.out.println("Semantic Analysis Successful!");
        gui.updateStatus("Semantic Analysis Successful!");
    }
}