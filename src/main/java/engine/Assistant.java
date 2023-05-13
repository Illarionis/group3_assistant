package engine;

import skill.Skill;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class Assistant {
    private final List<Skill> skills;

    public Assistant() {
        skills = new ArrayList<>();
    }

    public void add(Skill s) {
        if (!skills.contains(s)) skills.add(s);
    }

    public void remove(Skill s) {
        skills.remove(s);
    }

    public String respond(String s) {
        /* *
         * Attempting to find a direct match for the string.yy
         * */
        for (var skill : skills) {
            final Map<String, String> associations = skill.getAssociations();
            final String out = associations.get(s);
            if (out != null) return out;
        }

        /* *
         * In case no direct matching was found, we could search all (input, output) associations
         * to find the best matching input.
         * */
        for (var skill : skills) {
            final Map<String, String> associations = skill.getAssociations();
            for (String input : associations.keySet()) {
                /* *
                 * Attempt to find an input with at least 95% similarity
                 * This implies in a 100 character string, 5 characters are allowed to be wrongly spelled or be missing
                 * */
                int count = Math.min(input.length(), s.length());
                int matches = 0;
                for (int i = 0; i < count; i++) {
                    if (s.charAt(i) == input.charAt(i)) matches++;
                }
                double percentage = 100.0 * matches / input.length();
                if (percentage > 95.0)  return associations.get(input);
            }
        }

        /* *
         * In case nothing is found, we simply return the default message.
         * */
        return "Could not recognize your message.";
    }
}
