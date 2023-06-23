package engine;

public final class PlaceholderCounter {
    /**
     * Counts the number of placeholders present in a template string.
     *
     * @param template The string containing placeholders for which the placeholders should be counted.
     * @param placeholder The placeholder character sequence used in the template string.
     * @return An integer >= 0
     **/
    public int countPlaceholders(String template, String placeholder) {
        int count = 0;
        int startIndex = 0;
        while (startIndex < template.length()) {
            final int endIndex = startIndex + placeholder.length();
            final String s = template.substring(startIndex, endIndex);
            if (s.equals(placeholder)) {
                count++;
                startIndex = endIndex;
            } else {
                startIndex++;
            }
        }
        return count;
    }
}
