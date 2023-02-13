package design.engine;

/**
 * Defines an abstract function.
 **/
public interface Function<Input, Output> {
    /**
     * Evaluates an input to obtain an output.
     *
     * @param input The input the function should evaluate.
     * @return The output that is mapped to the input.
     **/
    Output evaluate(Input input);
}
