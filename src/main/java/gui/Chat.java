package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.util.List;

public final class Chat extends ScrollPane {
    private final List<Node> messages;

    /**
     * Creates an empty chat.
     **/
    public Chat() {
        final Insets padding = new Insets(5, 5, 5, 5);
        final VBox scrollContent = new VBox();
        scrollContent.setAlignment(Pos.TOP_CENTER);
        messages = scrollContent.getChildren();

        setContent(scrollContent);
        setPadding(padding);
        setMinSize(300, 600);
        setFitToHeight(true);
        setFitToWidth(true);
    }

    /**
     * Adds a new message to the chat.
     *
     * @param m The message that should be added to the chat.
     **/
    public void add(Message m) {
        messages.add(m);
    }
}
