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

    public void addRule(String lhs, String[] rhs) {
        if (!nonTerminals.contains(lhs)) {
            nonTerminals.add(lhs);
            rules.put(lhs, new ArrayList<>());
        }

        if (rhs.length == 1 && !terminals.contains(rhs[0])) {
            terminals.add(rhs[0]);
        }

        List<String[]> options = rules.get(lhs);
        options.add(rhs);
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
