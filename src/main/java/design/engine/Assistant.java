package design.engine;

/**
 * Defines the structure of a digital assistant.
 **/
public interface Assistant<Input, Output> {
    /**
     * Obtains the function responsible for pre-processing the user input,
     * before it can be fed to the process function.
     *
     * @return The pre-processed user input.
     **/
    Function<String, Input> getPreProcessor();

    /**
     * Obtains the function responsible for post-processing the assistant output,
     * before it can be used by a UI to displayed to the user.
     *
     * @return The post-processed assistant output.
     **/
    Function<Output, String> getPostProcessor();

    /**
     * Lets the assistant determine the output associated with a given input.
     *
     * @return The output associated with the input.
     **/
    Output process(Input input);
}
