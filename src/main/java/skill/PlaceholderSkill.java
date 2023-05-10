package skill;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     <b>Definition</b><br>
 *     Defines a skill, where the strings in both the input and output set are derived from a template.
 * </p>
 * <br>
 * <p>
 *     <b>Example</b><br>
 *     Assume the user is interested to have a skill to know which lectures at what time he or she has. <br>
 *     Let (1) the input template be "What lecture do I have $ at $?", (2) the output template be "On @ at @ you have @"
 *     and (3) the lecture on Monday 8 am be Linear Programming. <br>
 *     Then, whenever the string "What lecture do I have Monday at 8 am?" is used as the skill input, the skill
 *     will return the output string "On Monday at 8 am you have Linear Programming:.
 * </p>
 **/
public final class PlaceholderSkill implements Skill {
    private final HashMap<String, String> map;
    private final int inputPlaceholderCount, outputPlaceholderCount;
    private final String inputPlaceholder, outputPlaceholder;
    private final String inputTemplate, outputTemplate;

    public PlaceholderSkill(int inputPlaceholderCount, String inputPlaceholder, String inputTemplate, int outputPlaceholderCount, String outputPlaceholder, String outputTemplate) {
        this.inputPlaceholderCount = inputPlaceholderCount;
        this.inputPlaceholder = inputPlaceholder;
        this.inputTemplate = inputTemplate;
        this.outputPlaceholderCount = outputPlaceholderCount;
        this.outputPlaceholder = outputPlaceholder;
        this.outputTemplate = outputTemplate;
        map = new HashMap<>();
    }

    private String buildInput(String[] inputVariables) {
        String input = inputTemplate;
        for (String s : inputVariables)
            input = input.replaceFirst(inputPlaceholder, s);
        return input;
    }

    private String buildOutput(String[] outputVariables) {
        String output = outputTemplate;
        for (String s : outputVariables)
            output = output.replaceFirst(outputPlaceholder, s);
        return output;
    }

    private void validateInputVariables(String[] inputVariables) {
        if (inputVariables.length > inputPlaceholderCount)
            throw new IllegalArgumentException("Detected more input variables than placeholders in input template!");
        else if (inputVariables.length < inputPlaceholderCount)
            throw new IllegalArgumentException("Detected less input variables than placeholders in input template!");
    }

    private void validateOutputVariables(String[] outputVariables) {
        if (outputVariables.length > outputPlaceholderCount)
            throw new IllegalArgumentException("Detected more output variables than placeholders in output template!");
        else if (outputVariables.length < outputPlaceholderCount)
            throw new IllegalArgumentException("Detected less output variables than placeholders in output template!");
    }

    /**
     * Adds a (input, output) association to the skill.
     *
     * @param inputVariables  The input variables leading to the input that should be mapped.
     * @param outputVariables The output variables leading to the output that should be associated with the input.
     * @return The output previously associated with the input, if the input was mapped.<br>
     *         Null, otherwise.
     * @throws IllegalArgumentException Thrown whenever the number of input variables is not equal to the number of
     *                                  placeholders in the input template.<br>
     *                                  -or- the number of output variables is not equal to the number of placeholders
     *                                  in the output template.
     **/
    public String map(String[] inputVariables, String[] outputVariables) {
        validateInputVariables(inputVariables);
        validateOutputVariables(outputVariables);
        final String input = buildInput(inputVariables);
        final String output = buildOutput(outputVariables);
        return map.put(input, output);
    }

    /**
     * Removes a (input, output) association from the skill using input variables.
     *
     * @param inputVariables The input variables leading to an input for which the mapping should be removed.
     * @return The output associated with the input, if the input was mapped.
     * @throws IllegalArgumentException Thrown whenever the number of input variables is not equal to the number of
     *                                  placeholders in the input template.<br>
     **/
    public String remove(String[] inputVariables) {
        validateInputVariables(inputVariables);
        final String input = buildInput(inputVariables);
        return map.remove(input);
    }

    @Override
    public String activate(String s) {
        final String out = map.get(s);
        if (out != null) return out;
        throw new IllegalArgumentException("String " + s + " is not mapped to an output!");
    }

    @Override
    public Map<String, String> getAssociations() {
        return map;
    }
}
