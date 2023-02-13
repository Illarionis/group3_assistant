package design.gui;

import design.engine.Assistant;
import javafx.stage.Stage;

/**
 * Defined a conversation session between a digital assistant and a user.
 **/
public interface Session<Input, Output> {
    /**
     * Starts a conversation session between a digital assistant and a user.
     *
     * @param s The stage that should show the conversation session.
     * @param a The assistant that should be involved in the conversation session.
     **/
    void start(Stage s, Assistant<Input, Output> a);
}
