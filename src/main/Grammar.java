package main;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Grammar {

    private Set<String> terminals;

    private Set<String> nonTerminals;

    private Map<String, Set<List<String>>> productions;

    private String startSymbol;

    public Grammar(Set<String> terminals, Set<String> nonTerminals, Map<String, Set<List<String>>> productions, String startSymbol) {
        if(terminals == null)
            throw new NullPointerException("The terminals must not be null.");
        if(terminals.isEmpty())
            throw new IllegalArgumentException("There must be at least one terminal symbol.");
        if(nonTerminals == null)
            throw new NullPointerException("The non terminals must not be null.");
        if(nonTerminals.isEmpty())
            throw new IllegalArgumentException("There must be at least one non terminal symbol.");
        if(productions == null)
            throw new NullPointerException("The productions must not be null.");
        if(productions.isEmpty())
            throw new IllegalArgumentException("There must be at least one production.");
        if(startSymbol == null)
            throw new NullPointerException("The start symbol must not be null.");
        if(startSymbol.isEmpty())
            throw new IllegalArgumentException("The start symbol must not be empty.");

        this.terminals = terminals;
        this.nonTerminals = nonTerminals;
        this.productions = productions;
        this.startSymbol = startSymbol;
    }

    public Set<String> getTerminals() {
        return terminals;
    }

    public Set<String> getNonTerminals() {
        return nonTerminals;
    }

    public Map<String, Set<List<String>>> getProductions() {
        return productions;
    }

    public String getStartSymbol() {
        return startSymbol;
    }
}
