package engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Grammar {
    private final List<String> nonTerminals = new ArrayList<>();
    private final List<String> terminals = new ArrayList<>();
    private final Map<String, List<String[]>> rules = new HashMap<>();
    private String startSymbol;

    public void addRule(String lhs, String rhs) {
        final String[] arr = rhs.split(" ");
        if (lhs.contains(" ")) throw new IllegalArgumentException("Left-hand side of rule is NOT allowed to contain spaces!");
        else if (arr.length > 2) throw new IllegalArgumentException("Right-hand side of rule is NOT allowed to contain more than 1 space!");

        if (!nonTerminals.contains(lhs)) {
            nonTerminals.add(lhs);
            rules.put(lhs, new ArrayList<>());
        }

        if (arr.length == 1 && !terminals.contains(arr[0])) {
            terminals.add(arr[0]);
        }

        List<String[]> options = rules.get(lhs);
        options.add(arr);
    }

    public Map<String, List<String[]>> getRules() {
        return rules;
    }

    public List<String> getNonTerminals() {
        return nonTerminals;
    }

    public List<String> getTerminals() {
        return terminals;
    }

    public String getStartSymbol() {
        return startSymbol;
    }

    public void setStartSymbol(String s) {
        startSymbol = s;
    }
}
