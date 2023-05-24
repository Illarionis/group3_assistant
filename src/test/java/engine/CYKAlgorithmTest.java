package engine;

import org.junit.jupiter.api.Test;

final class CYKAlgorithmTest {
    @Test
    public void testSolve() {
        // Grammar example taken from : https://www.geeksforgeeks.org/cyk-algorithm-for-context-free-grammar/
        ContextFreeGrammar g = new ContextFreeGrammar();
        g.setStartSymbol("S");
        g.registerNonTerminals("S", "A", "B", "C");
        g.registerTerminals("a", "b");
        g.createRule("S", "A", "B");
        g.createRule("S", "B", "C");
        g.createRule("A", "B", "A");
        g.createRule("A", "a");
        g.createRule("B", "C", "C");
        g.createRule("B", "b");
        g.createRule("C", "A", "B");
        g.createRule("C", "a");

        final String[] in = {"baaba"};
        for (String s : in) assert CYKAlgorithm.solve(s, g);
    }
}