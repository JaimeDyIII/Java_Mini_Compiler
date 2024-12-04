import java.util.List;

public class Parser{

    private int position;
    private List<Token> tokens;
    private GUI gui;
    private boolean encounteredSyntaxError;
    SemanticAnalyzer semanticAnalyzer;
    String currentVariableName;
    String currentVariableValue;
    Token.Type currentVariableDataType;
    Token.Type currentVariableDeclaredType;


    public Parser(List<Token> tokens, GUI gui){
        semanticAnalyzer = new SemanticAnalyzer(gui);
        this.gui = gui;
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

    private boolean isData(){
        return  currentTokenType() == Token.Type.INT_LIT  ||
                currentTokenType() == Token.Type.INT      ||
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
            case "byte":
                return Token.Type.BYTE;
            case "short":
                return Token.Type.SHORT;
            case "int":
                return Token.Type.INT;
            case "long":
                return Token.Type.LONG;
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
                semanticAnalyzer.checkForReservedKeywords(currentVariableName);

                if(!semanticAnalyzer.isVariableDeclared(currentVariableName)){
                    semanticAnalyzer.setEncounteredSemanticError(true);
                    semanticAnalyzer.addError("Error: Variable " + currentVariableName + " is not declared!");
                }

                currentVariableDeclaredType = semanticAnalyzer.getVariableType(currentVariableName);

                nextToken();
                variableReassignment();
                break;
            case EOF:
                gui.update("Reached end of file while parsing!");
                break;
            default:
                gui.update("Unexpected " + currentToken() + "\nExpected Token Type: DATA_TYPES or IDENTIFIER");
        }
    }

    private void variableDeclaration(){
        if(currentTokenType() != Token.Type.IDENTIFIER){
            gui.update("Unexpected " + currentToken() + "\nExpected Token Type: IDENTIFIER");
            return;
        }
        
        currentVariableName = currentToken().getLexeme();
        semanticAnalyzer.checkForReservedKeywords(currentVariableName);
        nextToken();

        switch(currentTokenType()){
            case DELIMITER:
                semanticAnalyzer.addVariable(currentVariableName, currentVariableDeclaredType);
                nextToken();
                if(!isEndOfFile()){
                    expression();
                }
                break;
            case ASSIGN_OP:
                semanticAnalyzer.addVariable(currentVariableName, currentVariableDeclaredType);
                nextToken();
                variableInstatiation();
                break;
            default:
                gui.update("Unexpected " + currentToken() + "\nExpected Token Type: DELIMITER or ASSIGN_OP");

        }
    }

    private void variableInstatiation(){
        if(!isData()) {
            gui.update("Unexpected " + currentToken() + "\nExpected Token Type: Literals");
            return;
        }

        currentVariableValue = currentToken().getLexeme();
        currentVariableDataType = currentToken().getTokenType();

        if(currentVariableDataType == Token.Type.INT_LIT){
            semanticAnalyzer.checkNumber(currentVariableName, currentVariableValue);
            nextToken();
        } else {
            semanticAnalyzer.typeCheck(currentVariableName, currentVariableDataType);
            nextToken();
        }

        if(!isDelimiter()){
            gui.update("Unexpected " + currentToken() + "\nExpected Token Type: DELIMITER");
            return;
        }

        nextToken();
        
        if(!isEndOfFile()){
            expression();
        }
    }

    private void variableReassignment(){
        if(!isAssignment()){
            gui.update("Unexpected " + currentToken() + "\nExpected Token Type: ASSIGN_OP");
            return;
        }

        nextToken();

        if(!isData()){
            gui.update("Unexpected " + currentToken() + "\nExpected Token Type: Literals");
            return;
        }

        currentVariableValue = currentToken().getLexeme();
        currentVariableDataType = currentToken().getTokenType();

        if(currentVariableDataType == Token.Type.INT_LIT){
            semanticAnalyzer.checkNumber(currentVariableName, currentVariableValue);
            nextToken();
        } else {
            semanticAnalyzer.typeCheck(currentVariableName, currentVariableDataType);
            nextToken();
        }

        if(!isDelimiter()){
            gui.update("Unexpected " + currentToken() + "\nExpected Token Type: DELIMITER");
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
            gui.update("Syntax Analysis Unsuccessful!\n");
            return;
        }

        gui.update("Syntax Analysis Successful!\n");
    }

    public void runSemanticAnalysis(){
        if(semanticAnalyzer.hasEncounteredSemanticError() || !isEndOfFile() || !semanticAnalyzer.getErrors().isEmpty()){
            semanticAnalyzer.logErorrs();
            gui.update("Semantic Analysis Unsuccessful!\n");
            return;
        }

        gui.update("Semantic Analysis Successful!\n");
    }
}