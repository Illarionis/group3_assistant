package engine;

import org.junit.jupiter.api.Test;

import java.util.*;

final class ContextFreeGrammarTest {
    private void testGetNonTerminals(ContextFreeGrammar g, String... expectedValues) {
        final List<String> nonTerminals = g.getNonTerminals();
        for (String expected : expectedValues) assert nonTerminals.contains(expected);
    }

    private void testGetTerminals(ContextFreeGrammar g, String... expectedValues) {
        final List<String> terminals = g.getTerminals();
        for (String expected : expectedValues) assert terminals.contains(expected);
    }

    private void testGetStartSymbol(ContextFreeGrammar g, String expectedValue) {
        assert Objects.equals(g.getStartSymbol(), expectedValue);
    }

    private void testRegisterNonTerminals(ContextFreeGrammar g, List<String> nonTerminals, List<String> expectedList) {
        g.registerNonTerminals(nonTerminals.toArray(String[]::new));
        final List<String> l = g.getNonTerminals();
        for (String s : expectedList) assert l.contains(s);
    }

    private void testRegisterTerminals(ContextFreeGrammar g, List<String> terminals, List<String> expectedList) {
        g.registerTerminals(terminals.toArray(String[]::new));
        final List<String> l = g.getTerminals();
        for (String s : expectedList) assert l.contains(s);
    }

    private void testRemoveNonTerminals(ContextFreeGrammar g, List<String> nonTerminals, List<String> expectedList) {
        g.removeNonTerminals(nonTerminals.toArray(String[]::new));
        final List<String> l = g.getNonTerminals();
        for (String s : nonTerminals) assert !l.contains(s);
        for (String s : expectedList) assert l.contains(s);
    }

    private void testRemoveTerminals(ContextFreeGrammar g, List<String> terminals, List<String> expectedList) {
        g.removeTerminals(terminals.toArray(String[]::new));
        final List<String> l = g.getTerminals();
        for (String s : terminals) assert !l.contains(s);
        for (String s : expectedList) assert l.contains(s);
    }

    private void testGetRules(ContextFreeGrammar g, Map<String, List<String[]>> expectedMap) {
        final Map<String, List<String[]>> rules = g.getRules();
        for (String key : expectedMap.keySet()) {
            final List<String[]> expectedList = expectedMap.get(key);
            final List<String[]> actualList = rules.get(key);
            for (String[] expectedArray : expectedList) assert actualList.contains(expectedArray);
        }
    }

    private void testRemoveRule(ContextFreeGrammar g, String nonTerminal, String[] substitutions) {
        g.removeRule(nonTerminal, substitutions);
        assert !g.getRules().get(nonTerminal).contains(substitutions);
    }

    private void testRemoveRules(ContextFreeGrammar g, String nonTerminal) {
        g.removeRules(nonTerminal);
        assert g.getRules().get(nonTerminal) == null;
    }

    @Test
    public void testGetNonTerminals() {
        final String[] nonTerminals = {"x", "y"};
        final var grammar = new ContextFreeGrammar();
        grammar.registerNonTerminals(nonTerminals);
        testGetNonTerminals(grammar, nonTerminals);
    }

    @Test
    public void testGetTerminals() {
        final String[] terminals = {"x", "y"};
        final var grammar = new ContextFreeGrammar();
        grammar.registerTerminals(terminals);
        testGetTerminals(grammar, terminals);
    }

    @Test
    public void testGetStartSymbol() {
        final String[] symbols = {"x", "y"};
        final var grammar = new ContextFreeGrammar();
        for (String s : symbols) {
            grammar.setStartSymbol(s);
            testGetStartSymbol(grammar, s);
        }
    }

    @Test
    public void testRegisterNonTerminals() {
        final List<String> expectedList = List.of("x", "y");
        final List<String> nonTerminals = List.of("x", "y");
        final var grammar = new ContextFreeGrammar();
        testRegisterNonTerminals(grammar, nonTerminals, expectedList);
    }

    @Test
    public void testRegisterTerminals() {
        final List<String> expectedList = List.of("x", "y");
        final List<String> terminals = List.of("x", "y");
        final var grammar = new ContextFreeGrammar();
        testRegisterTerminals(grammar, terminals, expectedList);
    }

    @Test
    public void testRemoveNonTerminals() {
        final List<String> expectedList = List.of("a");
        final List<String> nonTerminals = List.of("x", "y");
        final var grammar = new ContextFreeGrammar();
        grammar.registerNonTerminals("a");
        testRemoveNonTerminals(grammar, nonTerminals, expectedList);
    }

    @Test
    public void testRemoveTerminals() {
        final List<String> expectedList = List.of("a");
        final List<String> terminals = List.of("x", "y");
        final var grammar = new ContextFreeGrammar();
        grammar.registerTerminals("a");
        testRemoveTerminals(grammar, terminals, expectedList);
    }

    @Test
    public void testGetRules() {
        final String key = "a";
        final String[] values = {"x", "y"};
        final List<String[]> l = new ArrayList<>();
        l.add(values);

        final Map<String, List<String[]>> expectedMap = new HashMap<>();
        expectedMap.put(key, l);

        final var grammar = new ContextFreeGrammar();
        grammar.registerNonTerminals(key);
        grammar.registerTerminals(values);
        grammar.createRule(key, values);

        testGetRules(grammar, expectedMap);
    }

    @Test
    public void testRemoveRule() {
        final String[] key = {"a", "b"};
        final String[][] values = {{"x", "y"}, {"1", "2"}};

        final var grammar = new ContextFreeGrammar();
        grammar.registerNonTerminals(key);
        grammar.registerTerminals(values[0]);
        grammar.registerTerminals(values[1]);

        for (String k : key) {
            for (String[] v : values) {
                grammar.createRule(k, v);
                testRemoveRule(grammar, k, v);
            }
        }
    }

    @Test
    public void testRemoveRules() {
        final String[] key = {"a", "b"};
        final String[][] values = {{"x", "y"}, {"1", "2"}};

        final var grammar = new ContextFreeGrammar();
        grammar.registerNonTerminals(key);
        grammar.registerTerminals(values[0]);
        grammar.registerTerminals(values[1]);

        for (String k : key) {
            for (String[] v : values) grammar.createRule(k, v);
            testRemoveRules(grammar, k);
        }
    }
}

