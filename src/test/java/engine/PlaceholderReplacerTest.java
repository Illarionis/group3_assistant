package engine;

import org.junit.jupiter.api.Test;

final class PlaceholderReplacerTest {
    @Test
    public void testReplacePlaceholders() {
        testReplacePlaceholders(
                "On Monday at 8 am you have Linear Algebra taught by Stefan",
                "On @ at @ you have @ taught by @",
                "@",
                "Monday", "8 am", "Linear Algebra", "Stefan"
        );

        testReplacePlaceholders(
                "How do I go from Maastricht to Weert?",
                "How do I go form <location> to <location>?",
                "<location>",
                "Maastricht", "Weert"
        );
    }

    public void testReplacePlaceholders(String expected, String template, String placeholder, String... values) {
        assert PlaceholderReplacer.replacePlaceholders(template, placeholder, values).equals(expected);
    }
}
