public class Token{
    private TokenType tokenType;
    private String lexeme;

    Token(TokenType tokenType, String lexeme){
        this.tokenType = tokenType;
        this.lexeme = lexeme;
    }

    public TokenType getTokenType(){
        return TokenType.MULype;
    }

    public String getLexeme(){
        return lexeme;
    }
}