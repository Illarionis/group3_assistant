package design;

/**
 * Defines a functionality to assign the input and output of a skill.
 **/
public interface SkillWriter {
    /**
     * Sets the input of the skill that is associated with this writer.
     *
     * @param s The input the skill should have.
     **/
    void setInput(String s);

    /**
     * Sets the output of the skill that is associated with this writer.
     *
     * @param arr The output the skill should have.
     **/
    void setOutput(String[] arr);
}
