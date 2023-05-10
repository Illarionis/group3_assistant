package skill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *     <b>Definition</b><br>
 *     Defines a skill, where the input set is derived from context-free grammar in CNF.
 * </p>
 * <br>
 * <p>
 *     <b>Example</b><br>
 *     Let's consider the phrase "It is raining outside". <br>
 *     The terminals (aka vocabulary) of a grammar supporting this phrase is a set that contains at bare minimum the
 *     words "it", "is", "raining", "outside".
 *     <br>
 *     <br>
 *     For the grammar to be in CNF, the right-hand side of each rule can <b>not</b> contain more than 2 non-terminals
 *     or more than 1 terminal. This implies to build the sentence "It is raining outside" at least 4 rules are required,
 *     where each rule denotes a substitution of a non-terminal by a terminal. <br>
 *     Since, a non-terminal can be substituted by at most 2 non-terminals, we require at least 2 more rules in order to
 *     to construct a sentence that consists of 4 words.
 *     <br>
 *     <br>
 *     For this example, let the set of non-terminals (aka placeholders) be {Noun, Verb, Adverb, Location} and S denote
 *     the start symbol, then a grammar supporting the sentence "It is raining outside" would be: <br>
 *     Rule 1: S -> Noun Verb <br>
 *     Rule 2: Verb -> Verb Adverb <br>
 *     Rule 3: Adverb -> Adverb Location <br>
 *     Rule 4: Noun -> it <br>
 *     Rule 5: Verb -> is <br>
 *     Rule 6: Adverb -> raining <br>
 *     Rule 7: Location -> outside
 *     <br>
 *     <br>
 *     Reconstructing sentence starting with start symbol would be accomplished in the following order:<br>
 *     (1) Applying rule 1 on "S" leads to "Noun Verb" as S -> Noun Verb <br>
 *     (2) Applying rule 2 on "Noun Verb" leads to "Noun Verb Adverb" as Verb -> Verb Adverb <br>
 *     (3) Applying rule 3 on "Noun Verb Adverb" leads to "Noun Verb Adverb Location" as Adverb -> Adverb Location <br>
 *     (4) Applying rule 4 on "Noun Verb Adverb Location" leads to "it Verb Adverb Location" as Noun -> it <br>
 *     (5) Applying rule 5 on "it Verb Adverb Location" leads to "it is Adverb Location" as Verb -> is <br>
 *     (6) Applying rule 6 on "it is Adverb Location" leads to "it is raining Location" as Adverb -> raining <br>
 *     (7) Applying rule 7 on "it is raining Location" leads to "It is raining outside" as Location -> outside<br>
 *     <br>
 *     <br>
 *     Last but not least, a logical response to the sentence "It is raining outside", would be "Take an umbrella".
 *     So, the input sentence "It is raining outside" belonging to the grammar can be mapped to the (user defined)
 *     output sentence "Take an umbrella". Then, this mapping would be part of the grammar skill.
 * </p>
 **/
public final class GrammarSkill implements Skill {
    private final HashMap<String, String> map; // (input, output) mappings where the input string belongs to the grammar
    private final HashMap<String, List<String[]>> rules; // production rules in CNF
    private final List<String> nonTerminals, terminals; // terminals (aka vocab) and non-terminals (aka placeholders)
    private final String startSymbol; // symbol to start building the sentence/phrase from.

    public GrammarSkill(List<String> nonTerminals, List<String> terminals, String startSymbol) {
        this.nonTerminals = nonTerminals;
        this.terminals = terminals;
        this.startSymbol = startSymbol;
        rules = new HashMap<>();
        map = new HashMap<>();
    }

    /* *
     * Checks whether the string belongs to the grammar.
     * This check is done by performing the CYK-algorithm,
     *
     * */
    private void validateInput(String input) {
        /* *
         * Todo: @Natalia implement the CYK algorithm here.
         *       You may check the algorithm by completing the main function at the bottom of this file.
         * */
        throw new IllegalArgumentException("Input " + input + " does not belong to the grammar.");
    }

    public void addRule(String leftSide, String... rightSide) {
        if (!nonTerminals.contains(leftSide) && !leftSide.equals(startSymbol))
            throw new IllegalArgumentException("Left side of rule is neither the start symbol nor a known non-terminal");
        else if (rightSide.length == 0)
            throw new IllegalArgumentException("Right side of the rule is empty!");
        else if (rightSide.length > 2)
            throw new IllegalArgumentException("Right side of the rule can NOT have more 2 symbols.");
        else if (rightSide.length == 2 && !nonTerminals.contains(rightSide[0]) && !nonTerminals.contains(rightSide[1]))
            throw new IllegalArgumentException("Right side of the rule does NOT contain 2 non-terminals");
        else if (rightSide.length == 1 && !nonTerminals.contains(rightSide[0]) && !terminals.contains(rightSide[0]))
            throw new IllegalArgumentException("Right side of the rule contains an unrecognized symbol!");

        List<String[]> l = rules.get(leftSide);
        if (l == null) {
            l = new ArrayList<>();
            l.add(rightSide);
            rules.put(leftSide, l);
        } else {
            l.add(rightSide);
        }
    }

    /**
     * Adds a (input, output) mapping to the skill if and only if the input belongs to the grammar.
     *
     * @param input  The input that should be mapped.
     * @param output The output that should be associated with the input.
     * @return The output previously associated with the input, if the input was mapped. <br>
     *         Null, otherwise.
     **/
    public String map(String input, String output) {
        validateInput(input);
        return map.put(input, output);
    }

    /**
     * Removes a (input, output) association from the skill.
     *
     * @param input The input for which the mapping should be removed.
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

    public static void main(String[] args) {
        /* *
         * Sentence "It is raining outside"
         * Non-Terminals = {Noun, Verb, Adverb, Location}
         * Terminals = {it, is, raining, outside}
         * Rules:
         *        S -> Noun Verb
         *     Verb -> Verb Adverb.
         *   Adverb -> Adverb Location
         *     Noun -> it
         *     Verb -> is
         *   Adverb -> raining
         * Location -> outside
         * */

        String[] nonTerminals = {"Noun", "Verb", "Adverb", "Location"};
        String[] terminals = {"it", "is", "raining", "outside"};
        String startSymbol = "S";

        var skill = new GrammarSkill(Arrays.asList(nonTerminals), Arrays.asList(terminals), startSymbol);
        skill.addRule("S", "Noun", "Verb");
        skill.addRule("Verb", "Verb", "Adverb");
        skill.addRule("Adverb", "Adverb", "Location");
        skill.addRule("Noun", "it");
        skill.addRule("Verb", "is");
        skill.addRule("Adverb", "raining");
        skill.addRule("Location", "outside");

        skill.map("It is raining outside", "Take a umbrella");
        System.out.println(skill.activate("It is raining outside"));
    }
}
