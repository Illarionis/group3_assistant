package skill;

import engine.PlaceholderReplacer;

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
    private final String inputTemplate, inputPlaceholder, outputTemplate, outputPlaceholder;
    private final int inputPlaceholderCount, outputPlaceholderCount;

    /**
     * Creates an empty placeholder skill.
     *
     * @param inputTemplate          The template the skill should use to derive all its inputs from.
     * @param inputPlaceholder       The placeholder symbols used in the input template.
     * @param inputPlaceholderCount  The number of placeholders used in the input template.
     * @param outputTemplate         The template the skill should use to derive all its outputs from.
     * @param outputPlaceholder      The placeholder symbols used in the output template.
     * @param outputPlaceholderCount The number of placeholders used in the output template.
     **/
    public PlaceholderSkill(String inputTemplate, String inputPlaceholder, int inputPlaceholderCount, String outputTemplate, String outputPlaceholder, int outputPlaceholderCount) {
        this.inputTemplate = inputTemplate;
        this.inputPlaceholder = inputPlaceholder;
        this.inputPlaceholderCount = inputPlaceholderCount;
        this.outputTemplate = outputTemplate;
        this.outputPlaceholder = outputPlaceholder;
        this.outputPlaceholderCount = outputPlaceholderCount;
        map = new HashMap<>();
    }

    /**
     * Adds a (input, output) associations to the skill.
     *
     * @param inputs  The inputs that should substitute the placeholders in the input template.
     * @param outputs The outputs that should substitute the placeholders in the output template.
     * @return The output previously associated with the input, if the input was mapped. <br>
     *         Null, otherwise.
     **/
    public String map(String[] inputs, String[] outputs) {
        if (inputs.length != inputPlaceholderCount)
            throw new IllegalArgumentException("Mismatch between number of inputs and number of placeholders in input template!");
        else if (outputs.length != outputPlaceholderCount)
            throw new IllegalArgumentException("Mismatch between number of outputs and number of placeholders in output template!");
        final String input = PlaceholderReplacer.replacePlaceholders(inputTemplate, inputPlaceholder, inputs);
        final String output = PlaceholderReplacer.replacePlaceholders(outputTemplate, outputPlaceholder, outputs);
        System.out.println(output);
        return map.put(input, output);
    }

    /**
     * Removes a (input, output) association from the skill.
     *
     * @param inputs The inputs that should substitute the placeholders in the input template.
     * @return The output associated with the input, if the input was mapped. <br>
     *         Null, otherwise.
     **/
    public String remove(String[] inputs) {
        if (inputs.length != inputPlaceholderCount)
            throw new IllegalArgumentException("Mismatch between number of inputs and number of placeholders in input template!");
        final String input = PlaceholderReplacer.replacePlaceholders(inputTemplate, inputPlaceholder, inputs);
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
