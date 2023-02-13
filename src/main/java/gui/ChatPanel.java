package gui;

import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;

import java.util.ArrayDeque;
import java.util.Queue;

final class ChatPanel extends BorderPane {
    private final Queue<String> UNPROCESSED_MESSAGES = new ArrayDeque<>();
    private final VBox CHAT_HISTORY = new VBox();

    public ChatPanel() {
        // Provides the user input field.
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

            // Reminder; When a message has a length of 0, it means there is no input.
            if (message.length() != 0) {
                // Store the message into the memory.
                UNPROCESSED_MESSAGES.add(message);

                // Display the message.
                displayMessage(message, true);
            }
        });
    }

    private HBox createMessageBox(String s, boolean stringIsUserMessage) {
        final HBox b = new HBox();
        var children = b.getChildren();
        if (stringIsUserMessage) {
            final Pane emptySpaceHolder = new Pane();
            HBox.setHgrow(emptySpaceHolder, Priority.ALWAYS);
            children.add(emptySpaceHolder);
        }
        ImageView decoratedMessage = SpeechBubble.createSpeechBubble(s, 200, 100,true);
        children.add(decoratedMessage);
        return b;
    }

    public void displayMessage(String s, boolean messageIsFromUser) {
        // Create the message box that gets displayed on the screen.
        HBox messageBox = createMessageBox(s, messageIsFromUser);

        // Add it to the conversation history, in order for it to get displayed.
        CHAT_HISTORY.getChildren().add(messageBox);
    }

    public Queue<String> getUnprocessedMessages() {
        return UNPROCESSED_MESSAGES;
    }
}
