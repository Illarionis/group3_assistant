package skill;

/**
 * <p>
 *     <b>Definition</b><br>
 *     Defines a skill as a mapping between two set of strings, where
 *     one set is the input set and the other set is the output set.
 * </p>
 * <br>
 * <p>
 *     <b>Example</b><br>
 *     Let the input string "Hello world" be mapped to the output string "Hello computer".<br>
 *     Then, if and only if the string "Hello world" is used as the skill input, will the skill get activated
 *     and return the string "Hello computer".
 * </p>
 **/
public interface Skill {
    /**
     * Activates the skill.
     *
     * @param s A string that should have been mapped.
     * @return The output string associated with the input string.
     * @throws IllegalArgumentException Thrown when the input string does not activate the skill.
     **/
    String activate(String s);
}
