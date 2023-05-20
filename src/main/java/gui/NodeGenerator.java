package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public final class NodeGenerator {
    public HBox createHorizontalBox(double spacing, Insets padding, Pos alignment, Node... children) {
        final var hbox = new HBox(children);
        hbox.setAlignment(alignment);
        hbox.setPadding(padding);
        hbox.setSpacing(spacing);
        return hbox;
    }

    public Label createLabel(String text, Pos alignment) {
        final var label = new Label(text);
        label.setAlignment(alignment);
        return label;
    }

    public TextField createTextField(String promptText) {
        final var textField = new TextField();
        textField.setPromptText(promptText);
        return textField;
    }

    public VBox createVerticalBox(double spacing, Insets padding, Pos alignment, Node... children) {
        final var vbox = new VBox(children);
        vbox.setAlignment(alignment);
        vbox.setPadding(padding);
        vbox.setSpacing(spacing);
        return vbox;
    }
}
