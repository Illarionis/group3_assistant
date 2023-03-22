package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;

public final class Chat extends AnchorPane {
    private static final Border SOLID_BOX = Border.stroke(Color.DARKGRAY);
    private final Button deleteChatButton, saveChatButton;
    private final List<Node> messages;

    public Chat() {
        deleteChatButton = new Button("x");
        deleteChatButton.setBorder(SOLID_BOX);
        deleteChatButton.setMaxSize(30, 30);
        deleteChatButton.setPrefSize(30, 30);
        setRightAnchor(deleteChatButton, 5.0);
        setTopAnchor(deleteChatButton, 5.0);

        saveChatButton = new Button(Character.toString(8595));
        saveChatButton.setBorder(SOLID_BOX);
        saveChatButton.setMaxSize(30, 30);
        saveChatButton.setPrefSize(30, 30);
        saveChatButton.setStyle("-fx-underline : true");
        setLeftAnchor(saveChatButton, 5.0);
        setTopAnchor(saveChatButton, 5.0);

        final VBox listContent = new VBox();
        listContent.setSpacing(2.0);
        listContent.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        messages = listContent.getChildren();

        final TextField titleField = createTextField(30);
        titleField.setAlignment(Pos.CENTER);
        titleField.setPromptText("Type a title");
        setTopAnchor(titleField, 5.0);
        setLeftAnchor(titleField, 35.0);
        setRightAnchor(titleField, 34.0);

        final TextField messageField = createTextField(20);
        messageField.setPromptText("Type a message...");
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

        getChildren().addAll(titleField, viewPort, messageField, deleteChatButton, saveChatButton);
    }

    public Chat(String s) {
        this();

        final String[] segments = s.split("\n");
        getTitleField().setText(segments[0]);

        for (int i = 1; i < segments.length; i++) {
            final Message m = new Message(segments[i]);
            messages.add(m);
        }
    }

    private TextField createTextField(double h) {
        final TextField tf = new TextField();
        tf.setBorder(SOLID_BOX);
        tf.setPrefHeight(h);
        return tf;
    }

    private TextField getTitleField() {
        return (TextField) getChildren().get(0);
    }

    public void newMessage(String content, String senderId) {
        final Message m = new Message(content, senderId);
        messages.add(m);
    }

    public Button getDeleteButton() {
        return deleteChatButton;
    }


    public Button getSaveButton() {
        return saveChatButton;
    }

    public Button getTitleTracker() {
        final Button b = new Button(getTitleField().getText());
        b.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        getTitleField().textProperty().addListener((observable, oldValue, newValue) -> b.setText(newValue));
        return b;
    }

    public TextField getMessageField() {
        return (TextField) getChildren().get(2);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getTitleField().getText()).append("\n");
        for (Node n : messages) {
            sb.append(n).append("\n");
        }
        return sb.toString();
    }
}
