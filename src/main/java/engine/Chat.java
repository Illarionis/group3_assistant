package engine;

import design.Displayable;
import gui.*;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;

public final class Chat implements Displayable {
    private final List<Node> timeline;
    private final TextField input;
    private final VBox panel;

    public Chat(Factory f, Designer d) {
        final VBox content = f.createVBox(5, Paddings.CONTENT, Pos.TOP_CENTER);
        timeline = content.getChildren();

        final ScrollPane viewport = f.createScrollPane(content);
        VBox.setVgrow(viewport, Priority.ALWAYS);

        input = f.createTextField("Click to type...", Pos.CENTER_LEFT, 30, 30, Double.MAX_VALUE, 30);
        panel = f.createVBox(2, Paddings.DEFAULT, Pos.CENTER, viewport, input);

        d.setBorder(Borders.GRAY, content, input);
    }

    public void clearTimeline() {
        timeline.clear();
    }

    public void registerAssistantMessage(Factory f, String s) {
        final var text = f.createText(s);
        final var textFlow = f.createTextFlow(Paddings.CONTENT, text);
        final var hbox = f.createHBox(0, Paddings.RIGHT, Pos.CENTER_LEFT, textFlow);
        textFlow.setBackground(Backgrounds.ASSISTANT_MESSAGE);
        textFlow.setBorder(Borders.GRAY);
        timeline.add(hbox);
    }

    public void registerUserMessage(Factory f, String s) {
        final var text = f.createText(s);
        final var textFlow = f.createTextFlow(Paddings.CONTENT, text);
        final var hbox = f.createHBox(0, Paddings.LEFT, Pos.CENTER_RIGHT, textFlow);
        textFlow.setBackground(Backgrounds.USER_MESSAGE);
        textFlow.setBorder(Borders.GRAY);
        timeline.add(hbox);
    }

    public void setChangeListener(ChangeListener<? super String> listener) {
        input.textProperty().addListener(listener);
    }

    public void setOnKeyPressed(EventHandler<KeyEvent> handler) {
        input.setOnKeyPressed(handler);
    }

    @Override
    public Region getPanel() {
        return panel;
    }
}
