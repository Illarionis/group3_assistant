package engine;

import org.junit.jupiter.api.Test;

import java.util.List;

final class TerminalDetectorTest {
    private void testDetectTerminals(String s, ContextFreeGrammar g, List<String> expected) {
        final var detector = new TerminalDetector();
        try {
            final List<String> result = detector.detectTerminals(s, g);
            assert result.size() == expected.size();
            assert result.containsAll(expected);
        } catch (IllegalArgumentException ignored) {
            assert false;
        }
    }
    @Test
    public void testDetectTerminals() {
        // Simple case
        var grammar = new ContextFreeGrammar();
        grammar.registerTerminals("I", "am", "happy");
        grammar.sortTerminals();

        var expected = List.of("I", "am", "happy");
        testDetectTerminals("I am happy.", grammar, expected);

        // Making it a bit more complicated
        grammar.registerTerminals("because", "she", "eats", "fish", "with", "a", "fork", "every", "evening");
        grammar.sortNonTerminals();

        expected = List.of("I", "am", "happy", "because", "she", "eats", "a", "fish", "with", "a", "fork", "every", "evening");
        testDetectTerminals("I am happy, Because she eats a fish with a fork every evening?", grammar, expected);

        expected = List.of("every", "evening", "I", "am", "happy", "because", "she", "eats", "a", "fish", "with", "a", "fork");
        testDetectTerminals("Every evening, I am happy, because she eats a fish with a fork!", grammar, expected);
    }
}
