package main;

public class Token {

    private TokenKind kind;

    private String spelling;

    public Token(TokenKind kind, String spelling) {
        if(kind == null)
            throw new NullPointerException("The token kind must not be null.");
        if(spelling == null)
            throw new NullPointerException("The spelling must not be null.");
        if(spelling.isEmpty())
            throw new IllegalArgumentException("The spelling must not be empty.");

        this.kind = kind;
        this.spelling = spelling.replace("\n", "\\n");
    }

    public TokenKind getKind() {
        return kind;
    }

    public String getSpelling() {
        return spelling;
    }

    @Override
    public String toString() {
        return kind.name() + "(" + spelling + ")";
    }
}
