package gui;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public final class Message extends HBox {
    private static final Color[] BACKGROUND_COLORS = {
            Color.rgb(255, 255, 255),
            Color.rgb(220, 248, 198)
    };

    private static final TextAlignment[] TEXT_ALIGNMENTS = {
            TextAlignment.LEFT,
            TextAlignment.RIGHT
    };

    /**
     * Creates a new message that can be displayed in the chat.
     *
     * @param senderId 0, if the message is sent by the assistant. <br>
     *                 1, if the message is sent by the user.
     * @param content The content that should be displayed.
     **/
    public Message(int senderId, String content) {
        final var text = new Text();
        text.setTextAlignment(TextAlignment.CENTER);
        text.setText(content);

        final var textBackground = Background.fill(BACKGROUND_COLORS[senderId]);
        final var textSpacing = new Insets(5, 5, 5, 5);
        final var textFlow = new TextFlow();
        textFlow.getChildren().add(text);
        textFlow.setBackground(textBackground);
        textFlow.setPadding(textSpacing);
        textFlow.setTextAlignment(TEXT_ALIGNMENTS[senderId]);

        getChildren().add(textFlow);
    }
}
