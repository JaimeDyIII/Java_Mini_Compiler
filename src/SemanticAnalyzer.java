import java.util.HashMap;
import java.util.Map;

public class SemanticAnalyzer {
    private Map<String, String> variables;

    public SemanticAnalyzer() {
        this.variables = new HashMap<>();
    }
    
    private void addVariable(String variableName, String variableType) {
        if(variables.containsKey(variableName)){
            System.out.println("Error: Variable already declared");
        } else {
            variables.put(variableName, variableType);
        }
    }
    
    private boolean isVariableDeclared(String variableName) {
        return variables.containsKey(variableName);
    }

    private String getVariableType(String variableName) {
        return variables.get(variableName);
    }

    private boolean typeCheck(String inputVariableType, String variableType) {
        return inputVariableType.equals(variableType);
    }

    public void SemanticAnalaysis(){
        
    }
}
