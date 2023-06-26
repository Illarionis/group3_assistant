package nlp;

import utility.StringSizeComparator;

import java.util.*;

public final class ContextFreeGrammar {
    private final Comparator<String> sortCriteria;
    private final List<String> nonTerminals, terminals;
    private final Map<String, List<List<String>>> rules;
    private String startSymbol;

    /**
     * Creates an empty grammar with no rules, non-terminals, terminals and start symbol.
     **/
    public ContextFreeGrammar() {
        nonTerminals = new ArrayList<>();
        terminals = new ArrayList<>();
        rules = new HashMap<>();
        sortCriteria = new StringSizeComparator();
    }

    /**
     * Creates a rule for a non-terminal and its substitution values. <br>
     * WARNING: You must sort both the non-terminals and terminals before calling this method as each substitution
     *          string will be interpreted and not directly added.
     *
     * @param nonTerminal   The non-terminal for which the rule should be created.
     * @param substitutions The non-terminals or terminal that will substitute the non-terminal.
     * @throws IllegalArgumentException Thrown when the non-terminal has NOT been added to the grammar.
     *                                  -or- at least one of the substitution values is neither a non-terminal nor a terminal.
     **/
    public void createRule(String nonTerminal, String... substitutions) {
        // Confirming whether the non-terminal has been registered
        if (!nonTerminals.contains(nonTerminal)) throw new IllegalArgumentException("Not allowed to create rules for unregistered non-terminals!");

        // Obtaining the mapped substitution options of the non-terminal
        final List<List<String>> options = rules.computeIfAbsent(nonTerminal, k -> new ArrayList<>());

        // Interpreting every rule (or say substitution option)
        for (String s : substitutions) {
            // Entry
            final List<String> l = new ArrayList<>();

            int i = 0;
            while (i < s.length()) {
                final int i_ = i;

                // Attempting to detect a space at the current index
                if (s.contains(" ")) {
                    final String[] segments = s.split(" ");
                    i = i + 1;

                    for (String segment : segments) {
                        if (nonTerminals.contains(segment)) {
                            l.add(segment);
                            i = i + segment.length();
                        }
                    }
                    // Confirming whether everything in the split was detected
                    if (i == s.length()) continue;

                    // Throwing an exception as not every symbol was recognized.
                    throw new IllegalArgumentException("Failed to detect all non-terminals in the rule split!");
                }

                // Attempting to detect a non-terminal
                for (String nt : nonTerminals) {
                    final int j = i + nt.length();
                    if (j > s.length()) continue;

                    final String sub = s.substring(i, j);
                    if (sub.equals(nt)) {
                        l.add(nt);
                        i = i + nt.length();
                    }
                }

                // Confirming whether a non-terminal was detected
                if (i_ != i) continue;

                // Attempting to detect a terminal
                for (String t : terminals) {
                    final int j = i + t.length();
                    if (j > s.length()) continue;

                    final String sub = s.substring(i, j);
                    if (sub.equals(t)) {
                        l.add(t);
                        i = i + t.length();
                    }
                }

                // Confirming whether a terminal was detected
                if (i_ != i) continue;

                // Failed to detect a non-terminal, terminal and space
                // Therefore, throwing an exception
                throw new IllegalArgumentException("Failed to detect a non-terminal, terminal and space!");
            }

            // Creating the rule entry
            options.add(l);
        }
    }

    /**
     * Obtains the production rules of the grammar.
     *
     * @return A map containing all rules registered to the grammar, where each key in the map entry
     *         represents a non-terminal and each value represents all substitution options for that non-terminal.
     *         Each string list represent a production rule for that non-terminal and each string in the string
     *         list represent at most one symbol that will substitute the non-terminal.
     **/
    public Map<String, List<List<String>>> getRules() {
        return rules;
    }

    /**
     * Obtains the non-terminals of the grammar.
     *
     * @return A list of string, where each string represent a symbol that could be substituted
     *         by one or more non-terminals and/or one or more terminals.
     **/
    public List<String> getNonTerminals() {
        return nonTerminals;
    }

    /**
     * Obtains the terminals of the grammar.
     *
     * @return A list of strings, where each string represent a character or a word
     *         that is part of the grammar's alphabet
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
     * Registers new non-terminals to the grammar.
     *
     * @param values The non-terminals that should be added to the grammar.
     * @throws IllegalArgumentException Thrown when one or more values already have been registered as a terminal.
     **/
    public void registerNonTerminals(String... values) {
        for (String s : values) {
            if (terminals.contains(s)) throw new IllegalArgumentException("Provided a non-terminal value that already has been registered as a terminal!");
            else if (nonTerminals.contains(s)) continue;
            nonTerminals.add(s);
        }
    }

    /**
     * Registers new terminals to the grammar.
     *
     * @param values The terminals that should be added to the grammar.
     * @throws IllegalArgumentException Thrown when one or more values already have been registered as a non-terminal.
     **/
    public void registerTerminals(String... values) {
        for (String s : values) {
            if (nonTerminals.contains(s)) throw new IllegalArgumentException("Provided a terminal value that already has been registered as a non-terminal!");
            else if (terminals.contains(s)) continue;
            terminals.add(s);
        }
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
     * @param s A registered non-terminal the grammar should use as its start symbol.
     * @throws IllegalArgumentException Thrown when the s is not a registered non-terminal.
     **/
    public void setStartSymbol(String s) {
        if (nonTerminals.contains(s)) startSymbol = s;
        else throw new IllegalArgumentException("Provided start symbol is not a non-terminal!");
    }

    /**
     * Sorts the non-terminals of the grammar according to their length from the largest to the smallest.
     **/
    public void sortNonTerminals() {
        nonTerminals.sort(sortCriteria);
    }

    /**
     * Sorts the terminals of the grammar according to their length from the largest to the smallest.
     **/
    public void sortTerminals() {
        terminals.sort(sortCriteria);
    }
}
