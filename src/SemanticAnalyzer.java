import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SemanticAnalyzer {
    private Map<String, Token.Type> variables;
    private List<String> errors;
    private boolean encounteredSemanticError = false;

    public SemanticAnalyzer() {
        this.variables = new HashMap<>();
        this.encounteredSemanticError = false;
        errors = new ArrayList<>();
    }
    
    public boolean hasEncounteredSemanticError() {
        return encounteredSemanticError;
    }

    public void setEncounteredSemanticError(boolean encounteredSemanticError) {
        this.encounteredSemanticError = encounteredSemanticError;
    }

    public void addError(String error) {
        errors.add(error);
    }


    public void addVariable(String variableName, Token.Type variableType) {
        if(!isVariableDeclared(variableName)){
            variables.put(variableName, variableType);
        } else {
            encounteredSemanticError = true;
            errors.add("Error: " + variableName + " is already declared");
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
            errors.add("Error: Variable " + variableName + " is not declared.");
            return;
        }
        if(!declaredType.equals(variableType)) {
            encounteredSemanticError = true;
            errors.add("Error: Invalid Variable Assignment {" + variableType + "} assigned to {" + declaredType + "}");
            return;
        }
    }

    public void printErrors(){
        if(errors.isEmpty()){
            System.out.println("No errors found");
            return;
        }

        for(String error : errors){
            System.out.println(error);
        }
    }
}
