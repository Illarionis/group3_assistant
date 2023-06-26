package utility;

import nlp.ContextFreeGrammar;

import java.util.ArrayList;
import java.util.List;

public final class TerminalDetector {
    /**
     * Detects all terminals in a string. <br>
     * WARNING: The terminals must have been sorted in the grammar before calling this method!
     *
     * @param s A string consisting of only terminals registered to a grammar besides spaces and punctuation.
     * @param g The grammar containing the terminals the string should fully consist of.
     * @return A list of strings representing the terminals the string consists from left to right.
     * @throws IllegalArgumentException Thrown whenever the string can NOT belong to the grammar as it does
     *                                  NOT purely consist of terminals registered to the grammar, besides the
     *                                  spaces and punctuation.
     *
     **/
    public List<String> detectTerminals(String s, ContextFreeGrammar g) {
        // List to contain terminals obtained from left to right that form the string s
        List<String> detected = new ArrayList<>();

        // Accessing the terminals that should ALREADY have been SORTED from longest -> smallest
        final List<String> terminals = g.getTerminals();

        // Detecting the terminals from left to right
        int i = 0;
        while (i < s.length()) {
            // Attempting to detect a space in the input
            // If there is a space, then the input is a sentence
            // Therefore it has to go through a different processing approach than the default one
            if (s.contains(" ")) {
                final String[] segments = s.split(" ");
                for (int j = 0; j < segments.length; j++) {
                    // Removing dots
                    while (segments[j].contains(".")) {
                        segments[j] = segments[j].replaceFirst("\\.", "");
                        i = i + 1;
                    }

                    // Removing commas
                    while (segments[j].contains(",")) {
                        segments[j] = segments[j].replaceFirst(",", "");
                        i = i + 1;
                    }

                    // Removing exclamation marks
                    while (segments[j].contains("!")) {
                        segments[j] = segments[j].replaceFirst("!", "");
                        i = i + 1;
                    }

                    // Removing question marks
                    while (segments[j].contains("?")) {
                        segments[j] = segments[j].replaceFirst("\\?", "");
                        i = i + 1;
                    }
                }
                i = i + segments.length - 1;

                for (String segment : segments) {
                    for (String t : terminals) {
                        if (segment.equalsIgnoreCase(t)) {
                            detected.add(t);
                            i = i + t.length();
                        }
                    }
                }
                // Confirming whether everything in the split was detected
                if (i == s.length()) continue;

                // Returning false as not every symbol was recognized.
                throw new IllegalArgumentException("String does not consist of only terminals!");
            }

            // Standard processing approach
            int _i = i;
            for (String t : terminals) {
                final int j = i + t.length();
                if (j > s.length()) continue;

                final String sub = s.substring(i, j);
                if (sub.equals(t)) {
                    detected.add(t);
                    i = i + t.length();
                }
            }
            if (i == _i) throw new IllegalArgumentException("String does not consist of only terminals!");
        }

        return detected;
    }
}
