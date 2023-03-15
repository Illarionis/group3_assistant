package design;

/**
 * Defines a functionality to read the input and output of a skill.
 **/
public interface SkillReader {
    /**
     * Obtains the input value of the skill that is associated with this reader.
     *
     * @return A string representing the input of the skill.
     **/
    String getInput();

    /**
     * Obtains the output value of the skill that is associated with this reader.
     *
     * @return A string array representing the output of the skill.
     **/
    String[] getOutput();
}
