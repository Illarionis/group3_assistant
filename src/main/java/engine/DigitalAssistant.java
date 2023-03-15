package engine;

import design.Assistant;
import design.SkillReader;

import java.util.ArrayList;
import java.util.List;

public final class DigitalAssistant implements Assistant {
    private final List<SkillReader> skills;

    public DigitalAssistant() {
        skills = new ArrayList<>();
    }

    @Override
    public String getResponse(String msg) {
        /* *
         * Task:
         * Interpret the message and find the skills that are getting activated/triggered
         * through matching the segments of the message string (or the entire message)
         * with the skill inputs.
         * Once all triggered skills are found, write their outputs in an appropriate string
         * and return this string
         * */
        return null;
    }

    @Override
    public void addSkill(SkillReader s) {
        if (!skills.contains((s))) {
            /* *
             * Task:
             * Determine whether there exists a skill with the same input.
             * If so, then this skill should not be added.
             *
             * Or
             *
             * Conclude that the skill input processing should happen in the skill window of the GUI.
             * If so, let me know.
             * */
            skills.add(s);
        }
    }

    @Override
    public void removeSkill(SkillReader s) {
        skills.remove(s);
    }
}
