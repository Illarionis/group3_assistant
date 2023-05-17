package engine;

import org.junit.jupiter.api.Test;

import java.util.Objects;

final class TemplateFillerTest {
    private void testFillTemplate(String template, String placeholder, String[] values, String expected) {
        final String result = TemplateFiller.fillTemplate(template, placeholder, values);
        assert Objects.equals(result, expected);
    }

    @Test
    public void testFillTemplate() {
        String expected = "A 2x2 matrix is a square matrix.";
        String[] values = {"2", "2", "is", "a square matrix."};
        testFillTemplate("A @x@ matrix @ @", "@", values, expected);

        expected = "Welcome to programming!";
        values = new String[]{"Welcome", "programming"};
        testFillTemplate("% to %!", "%", values, expected);
    }
}
