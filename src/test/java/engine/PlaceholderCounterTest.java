package engine;

import org.junit.jupiter.api.Test;

final class PlaceholderCounterTest {
    @Test
    public void testCountPlaceholders() {
        testCountPlaceholders("What $ do I have on $ at $ in $?", "$", 4);
        testCountPlaceholders("On @ at @ you have @ in @ taught by @", "@", 5);
        testCountPlaceholders("How to go from <location> to <location>", "<location>", 2);
        testCountPlaceholders("Sentence without placeholders", "&", 0);
    }

    public void testCountPlaceholders(String template, String placeholder, int expectedCount) {
        final var counter = new PlaceholderCounter();
        assert counter.countPlaceholders(template, placeholder) == expectedCount;
    }
}
