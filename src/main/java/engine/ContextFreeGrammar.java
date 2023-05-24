package engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ContextFreeGrammar {
    private final List<String> nonTerminals, terminals;
    private final Map<String, List<String[]>> rules;
    private String startSymbol;

    /**
     * Creates an empty grammar with no rules, non-terminals, terminals and start symbol.
     **/
    public ContextFreeGrammar() {
        nonTerminals = new ArrayList<>();
        terminals = new ArrayList<>();
        rules = new HashMap<>();
    }

    /**
     * Creates a rule for a non-terminal and its substitution values.
     *
     * @param nonTerminal   The non-terminal for which the rule should be created.
     * @param substitutions The non-terminals or terminal that will substitute the non-terminal.
     * @throws IllegalArgumentException Thrown when the non-terminal has NOT been added to the grammar.
     *                                  -or- at least one of the substitution values is neither a non-terminal nor a terminal.
     **/
    public void createRule(String nonTerminal, String... substitutions) {
        // Confirming whether the non-terminal has been registered
        if (!nonTerminals.contains(nonTerminal)) throw new IllegalArgumentException("Not allowed to create rules for unregistered non-terminals!");

        // Confirming whether the substitution values have been registered as either a non-terminal or a terminal
        for (String s : substitutions) {
            if (nonTerminals.contains(s) || terminals.contains(s)) continue;
            throw new IllegalArgumentException("Not allowed to create rules for unregistered substitution values!");
        }

        // Obtaining the mapped substitution options of the non-terminal
        final List<String[]> rhsRules = rules.computeIfAbsent(nonTerminal, k -> new ArrayList<>());

        // Adding the substitution option to the options list
        rhsRules.add(substitutions);
    }

    /**
     * Obtains the production rules of the grammar.
     *
     * @return A (String, String[] List)-map, where each string key represent a non-terminal and each string array
     *         provides a substitution option for the non-terminal.
     **/
    public Map<String, List<String[]>> getRules() {
        return rules;
    }

    /**
     * Obtains the non-terminals of the grammar.
     *
     * @return A list of string, where each string represent a symbol that can be substituted
     *         by a terminal or a non-terminal.
     **/
    public List<String> getNonTerminals() {
        return nonTerminals;
    }

    /**
     * Obtains the terminals of the grammar.
     *
     * @return A list of string, where each string represent a character or a word that is used to formulate a phrase.
     **/
    public List<String> getTerminals() {
        return terminals;
    }

    /**
     * Obtains the start symbol of the grammar.
     *
     * @return The symbol where the grammar starts substituting symbols from.
     **/
    public String getStartSymbol() {
        return startSymbol;
    }

    /**
     * Adds non-terminals to the grammar.
     *
     * @param values The non-terminals that should be added to the grammar.
     **/
    public void registerNonTerminals(String... values) {
        for (String s : values) {
            if (this.nonTerminals.contains(s)) continue;
            this.nonTerminals.add(s);
        }
    }

    /**
     * Adds terminals to the grammar.
     *
     * @param values The terminals that should be added to the grammar.
     **/
    public void registerTerminals(String... values) {
        for (String s : values) {
            if (this.terminals.contains(s)) continue;
            this.terminals.add(s);
        }
    }

    /**
     * Removes a specific rule of a non-terminal from the grammar.
     *
     * @param nonTerminal   The non-terminal for which the rule should be removed.
     * @param substitutions The substitutions involved in the rule that should be removed.
     **/
    public void removeRule(String nonTerminal, String... substitutions) {
        final List<String[]> options = rules.get(nonTerminal);
        if (options == null) return;
        options.remove(substitutions);
    }

    /**
     * Removes all rules of a non-terminal from the grammar.
     *
     * @param nonTerminal The non-terminal for which all its rules should be removed.
     **/
    public void removeRules(String nonTerminal) {
        rules.remove(nonTerminal);
    }

    /**
     * Removes non-terminals from the grammar.
     *
     * @param values The non-terminals that should be removed from the grammar.
     **/
    public void removeNonTerminals(String... values) {
        for (String s : values) nonTerminals.remove(s);
    }

    /**
     * Removes terminals from the grammar.
     *
     * @param values The terminals that should be removed from the grammar.
     **/
    public void removeTerminals(String... values) {
        for (String s : values) terminals.remove(s);
    }

    /**
     * Sets the start symbol of the grammar.
     *
     * @param s The start symbol the grammar should have.
     **/
    public void setStartSymbol(String s) {
        startSymbol = s;
    }
}
