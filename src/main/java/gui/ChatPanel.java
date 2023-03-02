package gui;

import design.engine.Assistant;
import design.engine.Function;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;

final class ChatPanel<Input, Output> extends BorderPane {
    private final VBox CHAT_HISTORY = new VBox();

    public ChatPanel(Assistant<Input, Output> a) {
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
                // Display the message.
                displayMessage(message, true);


                // Obtains the functions to pre-process the input and post-process the output.
                Function<String, Input> f1  = a.getPreProcessor();
                Function<Output, String> f2 = a.getPostProcessor();

                // Pre-processing the input.
                Input x = f1.evaluate(message);

                // Obtaining the output.
                Output y = a.process(x);

                // Post-processing the output.
                String response = f2.evaluate(y);

                // Displaying the received response.
                displayMessage(response, false);
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
        ImageView decoratedMessage = SpeechBubble.createSpeechBubble(s,stringIsUserMessage);
        children.add(decoratedMessage);
        return b;
    }

    private void displayMessage(String s, boolean messageIsFromUser) {
        // Create the message box that gets displayed on the screen.
        HBox messageBox = createMessageBox(s, messageIsFromUser);

        // Add it to the conversation history, in order for it to get displayed.
        CHAT_HISTORY.getChildren().add(messageBox);
    }
}
