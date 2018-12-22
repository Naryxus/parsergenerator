package main;

import exceptions.LexicalException;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Iterator;

public class Scanner implements Iterator<Token>, Iterable<Token> {

    private char currentChar;

    private StringBuilder currentSpelling;

    private CharacterIterator iterator;
    
    private Token currentToken;

    public Scanner(String grammarText) throws LexicalException {
        if(grammarText == null)
            throw new NullPointerException("The grammar must not be null.");
        if(grammarText.isEmpty())
            throw new IllegalArgumentException("The grammar must not be empty.");

        iterator = new StringCharacterIterator(grammarText);
        currentChar = iterator.current();
        currentToken = scan();
    }

    private void take(char expectedChar) throws LexicalException {
        if(currentChar == expectedChar) {
            currentSpelling.append(currentChar);
            currentChar = iterator.next();
        } else
            throw new LexicalException("The expected char was '" + expectedChar + "', but the current char is '" + currentChar + "'");
    }

    private void takeIt() {
        currentSpelling.append(currentChar);
        currentChar = iterator.next();
    }

    private Token scan() throws LexicalException {
        currentSpelling = new StringBuilder();

        while(currentChar == ' ')
            takeIt();

        if(currentChar == CharacterIterator.DONE)
            return null;

        currentSpelling = new StringBuilder();
        if(currentChar == '\n') {
            takeIt();

            return new Token(TokenKind.NewLineSign, currentSpelling.toString());
        } else if(currentChar == ':') {
            takeIt();
            take(':');
            take('=');

            if(currentChar != ' ')
                throw new LexicalException("There must not be any other character than a blank after the production sign, but the current char is '" + currentChar + "'");

            return new Token(TokenKind.ProductionSign, currentSpelling.toString());
        } else if(currentChar == '|') {
            takeIt();

            if(currentChar != ' ')
                throw new LexicalException("There must not be any other character than a blank after the alternative sign, but the current char is '" + currentChar + "'");

            return new Token(TokenKind.AlternativeSign, currentSpelling.toString());
        } else if(currentChar == '<') {
            takeIt();

            if(!Character.isLetterOrDigit(currentChar))
                throw new LexicalException("There must be at least one letter or digit at the beginning of a non terminal. But the current char is '" + currentChar + "'");

            while(Character.isLetterOrDigit(currentChar))
                takeIt();

            if(currentChar != '>')
                throw new LexicalException("A non terminal must end with a '>'-sign and must not contain any other character than letters or digits. But the current char is '" + currentChar + "'");

            takeIt();

            return new NonTerminalToken(currentSpelling.substring(1, currentSpelling.length() - 1));
        } else {
            takeIt();
            while(currentChar != CharacterIterator.DONE && currentChar != '\n' && currentChar != ' ')
                takeIt();

            return new Token(TokenKind.Literal, currentSpelling.toString());
        }
    }

    public static void main(String[] args) throws LexicalException {
        String grammar = "<Fuuu> ::= z\n<T> | ::= <F>";

        Scanner sc = new Scanner(grammar);

        sc.forEach(System.out::println);

        /*Token token;
        while((token = sc.scan()) != null)
            System.out.println(token);*/
    }

    @Override
    public boolean hasNext() {
        return currentToken != null;
    }

    @Override
    public Token next() {
        Token output = currentToken;
        try {
            currentToken = scan();
        } catch(LexicalException e) {
            e.printStackTrace();
        }
        return output;
    }

    @Override
    public Iterator<Token> iterator() {
        return this;
    }
}
