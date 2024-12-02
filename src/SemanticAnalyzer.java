import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SemanticAnalyzer {
    private Map<String, Token.Type> variables;
    private boolean encounteredSemanticError = false;
    private List<String> semanticErrors;
    private GUI gui;

    public SemanticAnalyzer(GUI gui) {
        semanticErrors = new ArrayList<>();
        variables = new HashMap<>();
        this.encounteredSemanticError = false;
        this.gui = gui;
    }
    
    public List<String> getErrors(){
        return semanticErrors;
    }

    public boolean hasEncounteredSemanticError() {
        return encounteredSemanticError;
    }

    public void setEncounteredSemanticError(boolean encounteredSemanticError) {
        this.encounteredSemanticError = encounteredSemanticError;
    }

    public void addError(String error) {
        semanticErrors.add(error);
    }


    public void addVariable(String variableName, Token.Type variableType) {
        if(!isVariableDeclared(variableName)){
            variables.put(variableName, variableType);
        } else {
            encounteredSemanticError = true;
            addError("Error: " + variableName + " is already declared");
        }
    }
    
    public boolean isVariableDeclared(String variableName) {
        return variables.containsKey(variableName);
    }

    public Token.Type getVariableType(String variableName) {
        return variables.get(variableName);
    }

    public void typeCheck(String variableName, Token.Type variableType) {
        Token.Type declaredType = getVariableType(variableName);
        if(declaredType == null) {
            encounteredSemanticError = true;
            addError("Error: Variable " + variableName + " is not declared.");
            return;
        }

        if(!declaredType.equals(variableType)) {
            encounteredSemanticError = true;
            addError("Error: Invalid Variable Assignment {" + variableType + "} assigned to {" + declaredType + "}");
            return;
        }
    }

    public void checkNumber(String variableName, String variableValue){
        String valueWithoutLetters = variableValue.replaceAll("[a-zA-Z]", "");
        long value = Long.parseLong(valueWithoutLetters);
        Token.Type declaredType = getVariableType(variableName);

        if(declaredType == Token.Type.BYTE && value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE){
           return;    
        }

        if(declaredType == Token.Type.SHORT && value >= Short.MIN_VALUE && value <= Short.MAX_VALUE){
            return;
        }

        if(declaredType == Token.Type.INT && value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE){
            return;
        }

        if(declaredType == Token.Type.LONG && value >= Long.MIN_VALUE && value <= Long.MAX_VALUE){
            return;
        }

        encounteredSemanticError = true;
        gui.update("Error: Invalid Variable Assignment {" + variableValue + "} assigned to {" + declaredType + "}\n");
    }

    public void logErorrs(){
        for(String s : semanticErrors){
            gui.update(s + "\n");
        }
    }
}
