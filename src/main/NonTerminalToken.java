package main;

public class NonTerminalToken extends Token {

    private String value;

    public NonTerminalToken(String value) {
        super(TokenKind.NonTerminal, "<" + value + ">");

        if(value == null)
            throw new NullPointerException("The value must not be null.");
        if(value.isEmpty())
            throw new IllegalArgumentException("The value must not be empty.");

        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
