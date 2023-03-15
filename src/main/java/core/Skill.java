package core;

import design.Recoverable;
import design.SkillReader;
import design.SkillWriter;
import gui.SkillPane;

/**
 * Provides a modifiable skill that can be stored.
 **/
public final class Skill extends SkillPane implements Recoverable, SkillReader, SkillWriter {
    /**
     * Creates a new empty skill.
     **/
    public Skill() {
        /* Place Holder */
    }

    /**
     * Creates a skill from a snapshot.
     *
     * @param snapshot A string describing the input and output values the skill should have.
     **/
    public Skill(String snapshot) {
        recover(snapshot);
    }

    @Override
    public String getInput() {
        return getInputField().getText();
    }

    @Override
    public String[] getOutput() {
        final var outputFields = getOutputFields();
        final String[] outputs = new String[outputFields.size()];
        for (int i = 0; i < outputs.length; i++) {
            outputs[i] = outputFields.get(i).getText();
        }
        return outputs;
    }

    @Override
    public String snapshot() {
        final String input = getInput();
        final String[] outputs = getOutput();
        /* *
         * Task:
         * Store the input and output variables to a string in a fashion such that if the string is passed
         * to the recover method, the input and output variables are updated according to whatever the values
         * are stored in this string.
         *
         * Example:
         * Let's say we have the input variable as "What lectures do I have on Monday?" and the output variable as
         * ["At 9 am, you have nothing", "At 10 am, you have nothing as well"].
         * Let's also assume we like to store these values as a JSON string, then when this snapshot() method
         * gets called, we return the following string
         * { "input":"What lectures do I have on Monday?", "output": ["At 9 am, you have nothing", "At 10 am, you have nothing as well"] }
         * */
        return null;
    }

    @Override
    public void recover(String snapshot) {
        /* *
         * Task:
         * Use the string obtained from the snapshot method to restore the input and output values of the skill
         *
         * Example:
         * Let's consider an example where the snapshot() method returns a JSON string formatted as below.
         * { "input":"example question", "output":["slot 1 example", "Hello world!"] }
         * So, whenever this string gets passed to this recover() method, the input variable of the skill should be
         * "example question" and the output variable should be the array ["slot 1 example", "Hello world!"].
         * In other words:
         *  - if getInput() gets called, it should return the string "example question"
         *  - if getOutput() gets called, it should return the string array ["slot 1 example", "Hello world!"]
         * */
        final String input = "";
        final String[] output = new String[0];
        setInput(input);
        setOutput(output);
    }

    @Override
    public void setInput(String s) {
        getInputField().setText(s);
    }

    @Override
    public void setOutput(String[] arr) {
        if (arr.length > 0) {
            final var outputFields = getOutputFields();
            while (outputFields.size() > arr.length) {
                removeOneOutputField();
            }
            while (outputFields.size() < arr.length) {
                addOneOutputField();
            }
            for (int i = 0; i < arr.length; i++) {
                outputFields.get(i).setText(arr[i]);
            }
        }
    }
}
