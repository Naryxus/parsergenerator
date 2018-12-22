package main;

import exceptions.LexicalException;
import exceptions.SyntaxException;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class Parser {

    private Grammar grammar;

    private int currentPosition;

    private String text;

    public Parser(Grammar grammar) {
        if(grammar == null)
            throw new NullPointerException("The grammar must not be null.");

        this.grammar = grammar;
    }

    public void parse(String text) {
        this.text = text;
        currentPosition = 0;

        parseProductionRule(grammar.getStartSymbol());
    }

    private void parseProductionRule(String nonTerminal) {
        Set<List<String>> rules = grammar.getProductions().get(nonTerminal);
        for(List<String> rule : rules) {
            parseProduction(rule);
            if(currentPosition == text.length())
                System.out.println("Parsing successful");
        }
    }

    private void parseProduction(List<String> production) {
        for(String token : production) {
            if(grammar.getTerminals().contains(token)) {
                if(text.regionMatches(currentPosition, token, 0, token.length()))
                    currentPosition += token.length();
            }
        }
    }

    public static void main(String[] args) throws IOException, LexicalException, SyntaxException {
        Grammar grammar = GrammarParser.getGrammar("/home/stefan/Dokumente/java_projects/grammar/out/production/grammar/simple_grammar.gr");

        Parser parser = new Parser(grammar);
        parser.parse("Hallo");
    }
}
