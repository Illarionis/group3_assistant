package gui;

import engine.Assistant;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public final class ChatWindow extends VBox {
    /**
     * Creates a new chat window for a given assistant and chat history.
     *
     * @param a The assistant that should be interacting with the user.
     * @param c The chat history the window should be displaying.
     **/
    public ChatWindow(Assistant a, Chat c) {
        final TextField chatInputField = new TextField();
        chatInputField.setPromptText("Click to type...");
        chatInputField.setMaxWidth(Double.MAX_VALUE);
        chatInputField.setMinHeight(30);

        chatInputField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                final String input = chatInputField.getText();
                if (input.isEmpty()) return;

                // Create and display user message
                Message m = new Message(1, input);
                c.add(m);

                // Get and display response from assistant
                String output = a.respond(input);
                m = new Message(0, output);
                c.add(m);

                chatInputField.clear();
            }
        });

        VBox.setVgrow(c, Priority.ALWAYS);
        VBox.setVgrow(chatInputField, Priority.NEVER);

        getChildren().addAll(c, chatInputField);
        setAlignment(Pos.CENTER);
        setMinSize(360, 720);
    }
}
