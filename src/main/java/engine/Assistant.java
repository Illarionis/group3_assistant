package engine;

import nlp.CYKAlgorithm;
import nlp.ContextFreeGrammar;
import utility.TerminalDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Assistant {
    /**
     * Provides a map to store user defined (input, output)-associations.
     **/
    private final Map<String, String> associations = new HashMap<>();

    /**
     * Provides a list to store user defined context free grammar.
     **/
    private final List<ContextFreeGrammar> grammarList = new ArrayList<>();

    /**
     * Provides the CYK algorithm to determine whether an input belongs to a CFG.
     **/
    private final CYKAlgorithm cyk = new CYKAlgorithm();

    /**
     * Provides a tool to format an input string such that it can be used by the CYK algorithm.
     **/
    private final TerminalDetector detector = new TerminalDetector();

    /**
     * Associates an input with an output.
     *
     * @param in  The input that should be associated with an output.
     * @param out The output that should be associated with the input.
     * @return The output that was previously associated with the input. <br>
     *         Null, if no previous association was found.
     **/
    public String associate(String in, String out) {
        if (in == null) throw new IllegalArgumentException("Null is not to be used allowed as input!");
        else if (out == null) throw new IllegalArgumentException("Null is not allowed to be used as an output!");
        return associations.put(in, out);
    }

    /**
     * Gets the output associated with an input.
     *
     * @param in The input for which an association should be found.
     * @return The output associated with the input. <br>
     *         Null, if no association was found.
     **/
    public String getAssociation(String in) {
        return associations.get(in);
    }

    /**
     * Gets all context-free grammars that have been registered to the assistant.
     *
     * @return A context-free grammar list with a size >= 0
     **/
    public List<ContextFreeGrammar> getGrammarList() {
        return grammarList;
    }

    /**
     * Registers a context-free grammar to the assistant.
     *
     * @param g The context-free grammar that should be registered.
     **/
    public void registerGrammar(ContextFreeGrammar g) {
        if (!grammarList.contains(g)) grammarList.add(g);
    }

    /**
     * Removes an (input, output)-association from the assistant.
     *
     * @param in The input of (input, output)-association that should be removed.
     * @return The output that was associated with the input. <br>
     *         Null, if no associations was found.
     **/
    public String removeAssociation(String in) {
        return associations.remove(in);
    }

    /**
     * Removes a context-free grammar from the assistant.
     *
     * @param g The context-free grammar that should be removed from the assistant.
     **/
    public void removeGrammar(ContextFreeGrammar g) {
        grammarList.remove(g);
    }

    /**
     * Requests the assistant to respond to an input from the user.
     *
     * @param in The input to which the assistant should respond to.
     * @return The response the assistant has to the input.
     **/
    public String respond(String in) {
        // Attempting to find a direct association
        final String out = getAssociation(in);
        if (out != null) return out;
        // In case of failure, determining whether input belongs to grammar
        if (validate(in)) return "Well, according to the CYK algorithm, the phrase " + "\"" + in + "\"" +
                " belongs to one of your grammars.";
        return "Failed to find a response associated with \"" + in + "\"";
    }

    /**
     * Validates an input by confirming whether the input belongs to any registered grammar in the assistant.
     *
     * @param in The input the assistant should validate.
     * @return True, if the input belongs to a grammar that has been registered to the assistant. <br>
     *         False, otherwise.
     **/
    public boolean validate(String in) {
        for (var grammar : grammarList) {
            try {
                final List<String> terminals = detector.detectTerminals(in, grammar);
                if (cyk.solve(terminals, grammar)) return true;
            } catch (IllegalArgumentException ignored) {}
        }
        return false;
    }
}
