package skill;

import java.util.HashMap;

/**
 * <p>
 *     <b>Definition</b><br>
 *     Defines a skill, where the mapping between the input and output set is predefined.
 * </p>
 * <br>
 * <p>
 *     <b>Example</b><br>
 *     Assume, "Hello world" is mapped to "Hey computer" and  "Hello class" is mapped to "Hi teacher" by the user.<br>
 *     So, the set {"Hello world", "Hello class"} would be the input set and the set {"Hey computer", "Hi teacher"}
 *     would be the output set. <br>
 *     Then, (1) whenever "Hello world" is used as the skill input, the output would be "Hey computer" and
 *     (2) whenever "Hello class" is used as the skill input, the output would be "Hi teacher".
 * </p>
 **/
public final class BasicSkill implements Skill {
    private final HashMap<String, String> map;

    /**
     * Creates an empty basic skill.
     **/
    public BasicSkill() {
        map = new HashMap<>();
    }

    /**
     * Adds a (input, output) mapping to the skill.
     *
     * @param input  The input that should be mapped an output.
     * @param output The output that should be associated with the input. <br>
     *               (Warning: Passing null as an output, will make the input associated with null)
     * @return The output previously associated with the input, if the input was already mapped. <br>
     *         Null, otherwise.
     **/
    public String map(String input, String output) {
        return map.put(input, output);
    }

    /**
     * Removes a (input, output) mapping from the skill.
     *
     * @param input The input string for which the mapping should be removed.
     * @return The output associated with the input, if the input was mapped.
     **/
    public String remove(String input) {
        return map.remove(input);
    }

    @Override
    public String activate(String s) {
        final String out = map.get(s);
        if (out != null) return out;
        throw new IllegalArgumentException("String " + s + " is not mapped to an output!");
    }
}
