package engine;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

final class CYKAlgorithmTest {
    @Test
    public void testSolve1() {
        // Grammar example taken from : https://www.geeksforgeeks.org/cyk-algorithm-for-context-free-grammar/
        final ContextFreeGrammar g = new ContextFreeGrammar();
        g.registerNonTerminals("S", "A", "B", "C");
        g.registerTerminals("a", "b");
        g.setStartSymbol("S");

        // Registering the rules
        g.createRule("S", "AB", "BC");
        g.createRule("A", "BA", "a");
        g.createRule("B", "CC", "b");
        g.createRule("C", "AB", "a");

        // Sorting the non-terminals and terminals
        g.sortNonTerminals();
        g.sortTerminals();

        // Segmented sentence to see the table result
        final List<String> sentence = new ArrayList<>(List.of("b", "a", "a", "b", "a"));

        final var algorithm = new CYKAlgorithm();
        final String[][] table = algorithm.buildTable(sentence, g);
        for (String[] strings : table) {
            for (String string : strings) {
                System.out.print(string + "|");
            }
            System.out.print("\n");
        }
        assert algorithm.solve(sentence, g);
    }

    @Test
    public void testSolve2() {
        // Example taken from : https://en.wikipedia.org/wiki/CYK_algorithm
        final ContextFreeGrammar g = new ContextFreeGrammar();
        g.registerNonTerminals("S", "N", "P", "V", "PP", "Det", "NP", "VP");
        g.registerTerminals("she", "eats", "fish", "with", "a", "fork");
        g.setStartSymbol("S");

        // Registering the rules
        g.createRule("S","NP VP");
        g.createRule("VP", "VP PP");
        g.createRule("VP", "V NP");
        g.createRule("VP", "eats");
        g.createRule("PP", "P NP");
        g.createRule("NP", "Det N");
        g.createRule("NP", "she");
        g.createRule("V", "eats");
        g.createRule("P", "with");
        g.createRule("N", "fish");
        g.createRule("N", "fork");
        g.createRule("Det", "a");

        // Sorting the non-terminals and terminals
        g.sortNonTerminals();
        g.sortTerminals();

        // Preparing to run the  test
        final var detector = new TerminalDetector();
        final var algorithm = new CYKAlgorithm();
        final List<String> l1 = detector.detectTerminals("she eats a fish with a fork", g);
        final List<String> l2 = detector.detectTerminals("She eats a fish with a fork.", g);
        assert algorithm.solve(l1, g);
        assert algorithm.solve(l2, g);
    }
}