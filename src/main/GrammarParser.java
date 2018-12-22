package main;

import exceptions.LexicalException;
import exceptions.SyntaxException;

import java.io.*;
import java.util.*;

public class GrammarParser {

    private Token currentToken;

    private Iterator<Token> tokenIterator;

    private Set<String> nonTerminals;

    private Set<String> terminals;

    private Map<String, Set<List<String>>> productions;

    private String startSymbol;

    private GrammarParser(String grammarText) throws LexicalException {
        if(grammarText == null)
            throw new NullPointerException("The grammar must not be null.");
        if(grammarText.isEmpty())
            throw new IllegalArgumentException("The grammar must not be empty.");

        tokenIterator = new Scanner(grammarText);
        nonTerminals = new HashSet<>();
        terminals = new HashSet<>();
        productions = new HashMap<>();
    }

    public static Grammar getGrammar(String fileName) throws IOException, LexicalException, SyntaxException {
        StringBuilder grammarText = new StringBuilder();

        FileReader fileReader = new FileReader(fileName);

        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line;

        while((line = bufferedReader.readLine()) != null)
            grammarText.append(line);

        bufferedReader.close();

        GrammarParser parser = new GrammarParser(grammarText.toString());

        return parser.parse();
    }

    private boolean isExpected(TokenKind kind) {
        return currentToken.getKind().equals(kind);
    }

    private Grammar parse() throws SyntaxException {
        currentToken = tokenIterator.next();
        parseStart();
        return new Grammar(terminals, nonTerminals, productions, startSymbol);
    }

    private void parseStart() throws SyntaxException {
        parseProductionRule();

        if(currentToken != null) {
            parseNewLineSign();
            parseStart();
        }
    }

    private void parseProductionRule() throws SyntaxException {
        NonTerminalToken nonTerminal = parseNonTerminal();
        parseProductionSign();
        Set<List<String>> production = parseProduction();
        if(!productions.containsKey(nonTerminal.getValue()))
            productions.put(nonTerminal.getValue(), new HashSet<>());
        productions.get(nonTerminal.getValue()).addAll(production);
    }

    private Set<List<String>> parseProduction() throws SyntaxException {
        if(currentToken == null)
            throw new SyntaxException("Expected a terminal or non terminal, but there is no more token.");

        Set<List<String>> productions = new HashSet<>();
        List<String> production = new LinkedList<>();

        try {
            NonTerminalToken nonTerminal = parseNonTerminal();
            production.add(nonTerminal.getValue());
        } catch(SyntaxException e) {
            try {
                Token terminal = parseTerminal();
                production.add(terminal.getSpelling());
            } catch(SyntaxException e2) {
                throw new SyntaxException("Expected a terminal or non terminal, but the kind of the current token is \"" + currentToken.getKind() + "\"");
            }
        }

        while(true) {
            try {
                NonTerminalToken nonTerminal = parseNonTerminal();
                production.add(nonTerminal.getValue());
            } catch(SyntaxException e) {
                try {
                    Token terminal = parseTerminal();
                    production.add(terminal.getSpelling());
                } catch(SyntaxException e2) {
                    break;
                }
            }
        }
        productions.add(production);

        try {
            parseAlternativeSign();
        } catch(SyntaxException e) {
            return productions;
        }

        productions.addAll(parseProduction());
        return productions;
    }

    private NonTerminalToken parseNonTerminal() throws SyntaxException {
        if(currentToken == null)
            throw new SyntaxException("Expected a non terminal, but there is no more token.");

        if(isExpected(TokenKind.NonTerminal)) {
            if(startSymbol == null)
                startSymbol = ((NonTerminalToken) currentToken).getValue();
            nonTerminals.add(((NonTerminalToken) currentToken).getValue());
            NonTerminalToken output = (NonTerminalToken) currentToken;
            currentToken = tokenIterator.next();
            return output;
        }

        throw new SyntaxException("Expected a non terminal, but the kind of the current token is \"" + currentToken.getKind() + "\"");
    }

    private Token parseTerminal() throws SyntaxException {
        if(currentToken == null)
            throw new SyntaxException("Expected a terminal, but there is no more token.");

        if(isExpected(TokenKind.Literal)) {
            terminals.add(currentToken.getSpelling());
            Token output = currentToken;
            currentToken = tokenIterator.next();
            return output;
        }

        throw new SyntaxException("Expected a terminal, but the kind of the current token is \"" + currentToken.getKind() + "\"");
    }

    private void parseProductionSign() throws SyntaxException {
        if(currentToken == null)
            throw new SyntaxException("Expected a production sign, but there is no more token.");

        if(!isExpected(TokenKind.ProductionSign))
            throw new SyntaxException("Expected a production sign, but the kind of the current token is \"" + currentToken.getKind() + "\"");

        currentToken = tokenIterator.next();
    }

    private void parseNewLineSign() throws SyntaxException {
        if(currentToken == null)
            throw new SyntaxException("Expected a new line sign, but there is no more token.");

        if(!isExpected(TokenKind.NewLineSign))
            throw new SyntaxException("Expected a new line sign, but the kind of the current token is \"" + currentToken.getKind() + "\"");

        currentToken = tokenIterator.next();
    }

    private void parseAlternativeSign() throws SyntaxException {
        if(currentToken == null)
            throw new SyntaxException("Expected an alternative sign, but there is no more token.");

        if(!isExpected(TokenKind.AlternativeSign))
            throw new SyntaxException("Expected an alternative sign, but the kind of the current token is \"" + currentToken.getKind() + "\"");

        currentToken = tokenIterator.next();
    }

    public static void main(String[] args) throws LexicalException, SyntaxException {
        String grammarText = "<S> ::= <T> | <S> + <T>\n<T> ::= <F> | <T> * <F>\n<F> ::= z | ( <S> )";
        System.out.println("Grammar:");
        System.out.println(grammarText);
        System.out.println();
        System.out.println();
        System.out.println();
        GrammarParser parser = new GrammarParser(grammarText);
        parser.parse();
    }
}
