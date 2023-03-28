package engine;

/**
 * Defines a skill as an input associated with some output. <br>
 * Example: The question "What lectures do I have on Monday?" would be the input,
 *          whereas the answer "At 9 am, you have math." would be one output.
 **/
public interface Skill {
    /**
     * Obtains the input that defines the skill.
     **/
    String getInput();

    /**
     * Obtains the output associated with the skill.
     **/

    String[] getOutput();
    String[] getOutput(String[] inputParam);
}
