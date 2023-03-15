package old.gui;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;

public final class Chat extends AnchorPane {
    private static final Border SOLID_BOX = Border.stroke(Color.DARKGRAY);
    private final List<Node> nodes;

    public Chat() {
        final VBox listContent = new VBox();
        listContent.setSpacing(1.0);
        nodes = listContent.getChildren();

        final TextField titleField = createTextField(30);
        titleField.setAlignment(Pos.CENTER);
        titleField.setPromptText("Click here to type a title");
        setTopAnchor(titleField, 5.0);
        setLeftAnchor(titleField, 5.0);
        setRightAnchor(titleField, 5.0);

        final TextField messageField = createTextField(20);
        messageField.setPromptText("Click here to type a message...");
        setLeftAnchor(messageField, 5.0);
        setRightAnchor(messageField, 5.0);
        setBottomAnchor(messageField, 5.0);

        final ScrollPane viewPort = new ScrollPane(listContent);
        viewPort.setBorder(SOLID_BOX);
        viewPort.setFitToHeight(true);
        viewPort.setFitToWidth(true);
        setTopAnchor(viewPort, 34.0);
        setLeftAnchor(viewPort, 5.0);
        setRightAnchor(viewPort, 5.0);
        setBottomAnchor(viewPort, 30.0);

        getChildren().addAll(titleField, viewPort, messageField);
    }

    private TextField createTextField(double h) {
        final TextField tf = new TextField();
        tf.setBorder(SOLID_BOX);
        tf.setPrefHeight(h);
        return tf;
    }

    public void addNode(Node n) {
        if (!nodes.contains(n)) {
            nodes.add(n);
        }
    }

    public Node createNode(String s, boolean isSendByUser) {
        final var message = SpeechBubble.createSpeechBubble(s, isSendByUser);
        final HBox messageContainer = new HBox();
        if (isSendByUser) {
            messageContainer.setAlignment(Pos.CENTER_RIGHT);
        } else {
            messageContainer.setAlignment(Pos.CENTER_LEFT);
        }
        messageContainer.getChildren().add(message);
        return messageContainer;
    }

    public TextField getMessageField() {
        return (TextField) getChildren().get(2);
    }

    public TextField getTitleField() {
        return (TextField) getChildren().get(0);
    }
}
