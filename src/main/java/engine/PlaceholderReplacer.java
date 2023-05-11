package engine;

public final class PlaceholderReplacer {
    /**
     * Replaces placeholders in a template by their respective values.
     *
     * @param template    The template for which the placeholders should be substituted.
     * @param placeholder The placeholder character sequence used in the template.
     * @param values      The array of values that should be substituted into the template.
     **/
    public static String replacePlaceholders(String template, String placeholder, String... values) {
        String result = template;
        for (String s : values)
            result = result.replaceFirst(placeholder, s);
        return result;
    }
}
