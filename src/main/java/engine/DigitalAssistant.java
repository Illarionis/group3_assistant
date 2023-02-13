package engine;

import design.engine.Assistant;
import design.engine.Function;

public final class DigitalAssistant implements Assistant<String, String> {
    private final Function<String, String> PRE_PROCESSOR = s -> s;
    private final Function<String, String> POST_PROCESSOR = s -> s;

    @Override
    public Function<String, String> getPreProcessor() {
        return PRE_PROCESSOR;
    }

    @Override
    public Function<String, String> getPostProcessor() {
        return POST_PROCESSOR;
    }

    @Override
    public String process(String s) {
        return "System: I received the message \"" + s + "\", however I am currently incapable of processing this.";
    }
}
