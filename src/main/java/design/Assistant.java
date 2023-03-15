package design;

/**
 * Defines functionalities the digital assistant should have.
 **/
public interface Assistant {
    /**
     * Gets the response of the assistant to a user message.
     *
     * @param msg The message the assistant should respond to.
     * @return A string representing the response the assistant has to the message.
     **/
    String getResponse(String msg);

    /**
     * Adds a skill to the assistant.
     *
     * @param s The skill the assistant should have.
     **/
    void addSkill(SkillReader s);

    /**
     * Removes a skill from the assistant.
     *
     * @param s The skill the assistant should no longer support.
     **/
    void removeSkill(SkillReader s);
}
