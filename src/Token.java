public class Token{
    enum Type {
        // Literals
        IDENTIFIER, STRING_LIT, CHAR_LIT, CONSTANT, 
        
        // Data Type
        DATA_TYPES, 
        
        // Data Types
        BYTE, SHORT, INT, CHAR, FLOAT, DOUBLE, BOOLEAN,

        // Single Character
        ASSIGN_OP, DELIMITER, 
        
        // End of File
        EOF,
    }

    private Type type;
    private String lexeme;

    Token(Type type, String lexeme){
        this.type = type;
        this.lexeme = lexeme;
    }

    public Type getTokenType(){
        return type;
    }

    public String getLexeme(){
        return lexeme;
    }

    @Override
    public String toString() {
        return "Token{" + "type=" + type + ", value='" + lexeme + '\'' + '}';
    }
}