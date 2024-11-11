public class Token{
    enum Type {
        DATA_TYPES, IDENTIFIER, STRING_LIT, CONSTANT, ASSIGN_OP, DELIMITER, EOF,
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