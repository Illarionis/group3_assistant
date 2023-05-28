package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public final class Factory {
    private final String scrollPaneCSS;

    public Factory() {
        final var css = getClass().getResource("/transparent-scroll-pane.css");
        if (css == null) throw new IllegalStateException("Failed to fetch scroll pane css file!");
        scrollPaneCSS = css.toExternalForm();
    }

    public Label createLabel(String text) {
        return createLabel(text, Pos.CENTER);
    }

    public Label createLabel(String text, Pos alignment) {
        final var label = new Label(text);
        label.setAlignment(alignment);
        label.setMinHeight(30);
        return label;
    }

    public HBox createHBox(Node... children) {
        return createHBox(0, Insets.EMPTY, Pos.CENTER, children);
    }

    public HBox createHBox(Pos alignment, Node... children) {
        return createHBox(0, Insets.EMPTY, alignment, children);
    }

    public HBox createHBox(double spacing, Node... children) {
        return createHBox(spacing, Insets.EMPTY, Pos.CENTER, children);
    }

    public HBox createHBox (double spacing, Insets padding, Pos alignment, Node... children) {
        final var box = new HBox(children);
        box.setAlignment(alignment);
        box.setPadding(padding);
        box.setSpacing(spacing);
        box.setFillHeight(true);
        return box;
    }

    public VBox createVBox(double spacing, Node... children) {
        return createVBox(spacing, Insets.EMPTY, Pos.TOP_CENTER, children);
    }

    public VBox createVBox (double spacing, Insets padding, Node... children) {
        return createVBox(spacing, padding, Pos.TOP_CENTER, children);
    }

    public VBox createVBox (double spacing, Pos alignment, Node... children) {
        return createVBox(spacing, Insets.EMPTY, alignment, children);
    }

    public VBox createVBox (double spacing, Insets padding, Pos alignment, Node... children) {
        final var box = new VBox(children);
        box.setAlignment(alignment);
        box.setPadding(padding);
        box.setSpacing(spacing);
        box.setFillWidth(true);
        return box;
    }

    public ScrollPane createScrollPane(Node content) {
        final var scrollPane = new ScrollPane(content);
        scrollPane.getStylesheets().add(scrollPaneCSS);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    public TextField createTextField(String promptText) {
        return createTextField(promptText, Pos.CENTER_LEFT);
    }

    public TextField createTextField(String promptText, Pos alignment) {
        final var textField = new TextField();
        textField.setAlignment(alignment);
        textField.setPromptText(promptText);
        textField.setBackground(Background.EMPTY);
        return textField;
    }
}
