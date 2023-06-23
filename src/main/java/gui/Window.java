package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public abstract class Window {
    private final Button close;
    private final Scene scene;
    protected final Button optional;
    protected final Label title;
    protected final HBox controls;
    protected final VBox panel;

    public Window(Factory factory) {
        optional = factory.createButton("", 50, 50, 50, 50);
        close = factory.createButton(Character.toString(10005), 50, 50, 50, 50);
        title = factory.createLabel("DIGITAL ASSISTANT", Pos.CENTER, 120, 50, Double.MAX_VALUE, 50);
        controls = factory.createHBox(0, new Insets(1, 1, 1, 1), Pos.CENTER, optional, title, close);
        HBox.setHgrow(title, Priority.ALWAYS);

        panel = factory.createVBox(3, Paddings.DEFAULT, Pos.CENTER, controls);
        scene = factory.createScene(panel);
        factory.update(Borders.GRAY_OVERLAY, controls);
    }

    public void setOnCloseClicked(EventHandler<ActionEvent> handler) {
        close.setOnAction(handler);
    }

    public void setOnTitleDragged(EventHandler<MouseEvent> handler) {
        title.setOnMouseDragged(handler);
    }

    public void setOnTitlePressed(EventHandler<MouseEvent> handler) {
        title.setOnMousePressed(handler);
    }

    public Scene getScene() {
        return scene;
    }
}
