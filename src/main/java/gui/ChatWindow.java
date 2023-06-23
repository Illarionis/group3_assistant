package gui;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

public final class ChatWindow extends Window {
    private final List<Node> messages;
    private final TextField input;

    public ChatWindow(Factory factory) {
        super(factory);

        optional.setText(Character.toString(9881));

        final VBox content = factory.createVBox(5, Paddings.DEFAULT, Pos.TOP_CENTER);
        final ScrollPane viewport = factory.createScrollPane(content);
        messages = content.getChildren();
        input = factory.createTextField("Click to type...", Pos.CENTER_LEFT, 360, 30, Double.MAX_VALUE, 30);

        panel.getChildren().addAll(viewport, input);
        panel.setMinSize(360, 720);
        VBox.setVgrow(viewport, Priority.ALWAYS);

        factory.update(Borders.GRAY_OVERLAY, viewport, input);
    }

    public void registerAssistantMessage(Factory factory, String content) {
        final var text = factory.createText(content);
        final var textFlow = factory.createTextFlow(Backgrounds.ASSISTANT_MESSAGE, Paddings.DEFAULT, text);
        final var hbox = factory.createHBox(0, Paddings.RIGHT, Pos.CENTER_LEFT, textFlow);
        messages.add(hbox);
    }

    public void registerUserMessage(Factory factory, String content) {
        final var text = factory.createText(content);
        final var textFlow = factory.createTextFlow(Backgrounds.USER_MESSAGE, Paddings.DEFAULT, text);
        final var hbox = factory.createHBox(0, Paddings.LEFT, Pos.CENTER_RIGHT, textFlow);
        messages.add(hbox);
    }

    public void setInputChangeListener(ChangeListener<? super String> listener) {
        input.textProperty().addListener(listener);
    }

    public void setOnKeyPressedHandler(EventHandler<KeyEvent> handler) {
        input.setOnKeyPressed(handler);
    }

    public void setOnSettingsPressed(EventHandler<ActionEvent> handler) {
        optional.setOnAction(handler);
    }
}
