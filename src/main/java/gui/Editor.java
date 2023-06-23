package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public abstract class Editor implements Displayable {
    protected final Button add, save;
    protected final Label title;
    protected final Overview list;
    protected final VBox content, variables, panel;

    public Editor(Factory factory) {
        list = new Overview(factory);

        title = factory.createLabel("EDITOR", Pos.CENTER, 360, 30, Double.MAX_VALUE, 30);

        variables = factory.createVBox(5, Paddings.DEFAULT, Pos.TOP_CENTER);
        variables.setMinHeight(250);

        content = factory.createVBox(5, Paddings.DEFAULT, Pos.TOP_CENTER);
        final ScrollPane viewport = factory.createScrollPane(content);

        add = factory.createButton("ADD", 180, 30, Double.MAX_VALUE, 30);
        save = factory.createButton("SAVE", 180, 30, Double.MAX_VALUE, 30);
        final HBox buttons = factory.createHBox(5, Insets.EMPTY, Pos.CENTER, add, save);
        HBox.setHgrow(add, Priority.ALWAYS);
        HBox.setHgrow(save, Priority.ALWAYS);

        panel = factory.createVBox(3, Insets.EMPTY, Pos.CENTER, title, variables, viewport, buttons);
        panel.setMinSize(450, 720);

        VBox.setVgrow(viewport, Priority.ALWAYS);
        factory.update(Borders.GRAY_OVERLAY, title, variables, viewport, add, save);
    }

    public Overview getList() {
        return list;
    }

    @Override
    public Region getPanel() {
        return panel;
    }

    protected static final class Variable implements Displayable {
        private final Label warning;
        private final TextField value;
        private final VBox panel;

        public Variable(Factory factory, String name, String description) {
            final var arrow = factory.createLabel(Character.toString(8594), Pos.CENTER, 30, 30, 30, 30);
            final var label = factory.createLabel(name, Pos.CENTER_RIGHT, 120, 30, 240, 30);
            value = factory.createTextField(description, Pos.CENTER_LEFT, 240, 30, 240, 30);
            warning = factory.createLabel("", Pos.CENTER, 360, 20, 390, 20);
            final var hbox = factory.createHBox(3, Insets.EMPTY, Pos.CENTER, label, arrow, value);
            panel = factory.createVBox(0, Insets.EMPTY, Pos.CENTER, warning, hbox);

            warning.setStyle("-fx-text-fill: rgb(210, 120, 120);");
            value.textProperty().addListener(((observable, oldValue, newValue) -> {
                if (warning.getText().length() > 0) warning.setText("");
            }));
            factory.update(Borders.GRAY_OVERLAY, value);
        }

        public void clear() {
            value.clear();
        }

        public void setWarning(String s) {
            warning.setText(s);
        }

        public String getValue() {
            return value.getText();
        }

        @Override
        public Region getPanel() {
            return panel;
        }
    }

    protected static final class Item implements Displayable {
        private final Button delete, title;
        private final HBox panel;

        public Item(Factory factory) {
            delete = factory.createButton(Character.toString(10005), 30, 30, 30, 30);
            title = factory.createButton("PLACEHOLDER", 30, 30, Double.MAX_VALUE, 30);
            panel = factory.createHBox(3, Insets.EMPTY, Pos.CENTER, title, delete);
            HBox.setHgrow(title, Priority.ALWAYS);
            factory.update(Borders.GRAY_OVERLAY, title);
        }

        public void setOnDeleteClicked(EventHandler<ActionEvent> handler) {
            delete.setOnAction(handler);
        }

        public void setOnTitleClicked(EventHandler<ActionEvent> handler) {
            title.setOnAction(handler);
        }

        public void setTitle(String s) {
            title.setText(s);
        }

        @Override
        public Region getPanel() {
            return panel;
        }
    }
}
