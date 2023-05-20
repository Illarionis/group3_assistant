package engine;

import java.util.HashMap;
import java.util.Map;

public final class Assistant {
    /**
     * Provides a map to store user defined (input, output)-associations.
     **/
    private final Map<String, String> associations = new HashMap<>();

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
     * Requests the assistant to respond to an input from the user.
     *
     * @param in The input to which the assistant should respond to.
     * @return The response the assistant has to the input.
     **/
    public String respond(String in) {
        final String out = getAssociation(in);
        if (out != null) return out;
        // Todo: Check for spelling mistakes
        // Todo: Check whether input belongs to a grammar
        return "Apologies, could not recognize your message.";
    }
}
