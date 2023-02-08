package gui;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.ArrayDeque;
import java.util.Queue;

final class ChatPanel extends BorderPane {
    private final Queue<String> UNPROCESSED_MESSAGES = new ArrayDeque<>();
    private final VBox CHAT_HISTORY = new VBox();

    public ChatPanel() {

        final TextField INPUT_FIELD = new TextField();

        // Sets how much empty space there should be between each message.
        CHAT_HISTORY.setSpacing(5.0);

        // Placing the components on the panel
        setCenter(CHAT_HISTORY);
        setBottom(INPUT_FIELD);

        // Tracking the keys in the console for now
        INPUT_FIELD.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Detected a change in input!");
            System.out.println(">  Went from " + oldValue + " to " + newValue);
        });

        // Confirming whether the user pressed 'enter'
        // In such a case, the following happens:
        //  1. The message typed by the user gets extracted from the input field.
        //  2. The input field gets cleared.
        //  3. The extracted message gets displayed.
        //  4. The extracted message gets stored in a queue.
        INPUT_FIELD.setOnKeyPressed(e -> {
            if (e.getCode() != KeyCode.ENTER) {
                return;
            }

            String message = INPUT_FIELD.getText();
            INPUT_FIELD.clear();

            if (message.length() != 0) {
                Text messageHolder = new Text("User: " + message);
                CHAT_HISTORY.getChildren().add(messageHolder);
                UNPROCESSED_MESSAGES.add(message);
            }
        });
    }

    public void displayMessage(String s) {
        Text messageHolder = new Text(s);
        CHAT_HISTORY.getChildren().add(messageHolder);
    }

    public Queue<String> getUnprocessedMessages() {
        return UNPROCESSED_MESSAGES;
    }
}
