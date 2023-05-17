package engine;

public class TemplateFiller {
    /**
     * Substitutes all placeholders in a template string by their respective values.
     *
     * @param template    The template for which the placeholders should be substituted.
     * @param placeholder The placeholder used in the template.
     * @param values      The values that should be substituted into the template.
     * @return The string obtained from substituting placeholders in the template by their respective values.
     * @throws IllegalArgumentException Thrown when the number of values do not match the number of placeholders
     *                                  inside the template.
     **/
    public static String fillTemplate(String template, String placeholder, String[] values) {
        final int placeholderCount = PlaceholderCounter.countPlaceholders(template, placeholder);
        if (values.length != placeholderCount) throw new IllegalArgumentException("Less values than placeholders!");
        return PlaceholderReplacer.replacePlaceholders(template, placeholder, values);
    }
}
