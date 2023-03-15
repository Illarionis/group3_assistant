package gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Provides the chat pane, which is the graphical component
 * to display a conversation between the assistant and the user.
 **/
public abstract class ChatPane extends AnchorPane {
    private static final Border SOLID_BOX = Border.stroke(Color.DARKGRAY);

    public ChatPane() {
        final VBox listContent = new VBox();
        listContent.setSpacing(1.0);

        final Button deleteChat = new Button("x");
        deleteChat.setBackground(Background.EMPTY);
        deleteChat.setBorder(SOLID_BOX);
        deleteChat.setMaxSize(30, 30);
        deleteChat.setPrefSize(30, 30);
        setTopAnchor(deleteChat, 5.0);
        setRightAnchor(deleteChat, 5.0);

        final Button saveChat = new Button(Character.toString(8595));
        saveChat.setBackground(Background.EMPTY);
        saveChat.setStyle("-fx-underline: true;");
        saveChat.setBorder(SOLID_BOX);
        saveChat.setMaxSize(30, 30);
        saveChat.setPrefSize(30, 30);
        setTopAnchor(saveChat, 5.0);
        setLeftAnchor(saveChat, 5.0);

        final TextField titleField = createTextField(30);
        titleField.setAlignment(Pos.CENTER);
        titleField.setPromptText("Click here to type a title");
        setTopAnchor(titleField, 5.0);
        setLeftAnchor(titleField, 35.0);
        setRightAnchor(titleField, 35.0);

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

        getChildren().addAll(titleField, viewPort, messageField, deleteChat, saveChat);
    }

    private TextField createTextField(double h) {
        final TextField tf = new TextField();
        tf.setBorder(SOLID_BOX);
        tf.setPrefHeight(h);
        return tf;
    }

    /**
     * Obtains the delete button of the chat pane.
     *
     * @return The button the user should click in order to delete the chat.
     **/
    public Button getDeleteButton() {
        return (Button) getChildren().get(3);
    }

    /**
     * Obtains the save button of the chat pane.
     *
     * @return The button the user should click in order to save the chat.
     **/
    public Button getSaveButton() {
        return (Button) getChildren().get(4);
    }

    /**
     * Obtains the message input field of the chat pane.
     *
     * @return The text field the user uses to type messages.
     **/
    public TextField getMessageField() {
        return (TextField) getChildren().get(2);
    }

    /**
     * Obtains the title input field of the chat pane.
     *
     * @return The text field the user uses to type the title of the chat pane.
     **/
    public TextField getTitleField() {
        return (TextField) getChildren().get(0);
    }
}
