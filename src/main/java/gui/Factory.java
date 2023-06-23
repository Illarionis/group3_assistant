package gui;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public final class Factory {
    public final EventHandler<MouseEvent> highlightOnMouseEnter;
    public final EventHandler<MouseEvent> removeHighlightOnMouseExit;
    public final String scrollPaneStylesheet;
    public final String textStyle =
            "-fx-text-fill: rgb(210, 210, 210);"+
            "-fx-prompt-text-fill: -fx-text-fill;";

    public Factory() {
        final var resource = getClass().getResource("/css/transparent-scroll-pane.css");
        scrollPaneStylesheet = resource == null ? "" : resource.toExternalForm();

        highlightOnMouseEnter = event -> {
            final var region = (Region) event.getSource();
            if (region.getBackground() == Background.EMPTY) region.setBackground(Backgrounds.HIGHLIGHT);
        };

        removeHighlightOnMouseExit = event -> {
            final var region = (Region) event.getSource();
            if (region.getBackground() == Backgrounds.HIGHLIGHT) region.setBackground(Background.EMPTY);
        };
    }

    public Button createButton(String text, double minWidth, double minHeight, double maxWidth, double maxHeight) {
        final var button = new Button(text);
        button.setMinSize(minWidth, minHeight);
        button.setMaxSize(maxWidth, maxHeight);
        button.setBackground(Background.EMPTY);
        button.setOnMouseEntered(highlightOnMouseEnter);
        button.setOnMouseExited(removeHighlightOnMouseExit);
        button.setStyle(textStyle);
        return button;
    }

    public HBox createHBox(double spacing, Insets padding, Pos alignment, Node... children) {
        final var hbox = new HBox(children);
        hbox.setAlignment(alignment);
        hbox.setPadding(padding);
        hbox.setSpacing(spacing);
        hbox.setBackground(Background.EMPTY);
        return hbox;
    }

    public Label createLabel(String text, Pos alignment, double minWidth, double minHeight, double maxWidth, double maxHeight) {
        final var label = new Label(text);
        label.setAlignment(alignment);
        label.setMinSize(minWidth, minHeight);
        label.setMaxSize(maxWidth, maxHeight);
        label.setBackground(Background.EMPTY);
        label.setStyle(textStyle);
        return label;
    }

    public Scene createScene(Region root) {
        final var scene = new Scene(root);
        scene.setFill(Colors.SCENE);
        return scene;
    }

    public ScrollPane createScrollPane(Region content) {
        final var scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setBackground(Background.EMPTY);
        scrollPane.getStylesheets().add(scrollPaneStylesheet);
        return scrollPane;
    }

    public Text createText(String s) {
        final var text = new Text(s);
        text.setFill(Colors.TEXT);
        return text;
    }

    public TextField createTextField(String promptText, Pos alignment, double minWidth, double minHeight, double maxWidth, double maxHeight) {
        final var textField = new TextField();
        textField.setAlignment(alignment);
        textField.setPromptText(promptText);
        textField.setMinSize(minWidth, minHeight);
        textField.setMaxSize(maxWidth, maxHeight);
        textField.setBackground(Background.EMPTY);
        textField.setStyle(textStyle);
        return textField;
    }

    public TextFlow createTextFlow(Background bg, Insets padding, Node... children) {
        final var textFlow = new TextFlow(children);
        textFlow.setPadding(padding);
        textFlow.setBackground(bg);
        return textFlow;
    }

    public VBox createVBox(double spacing, Insets padding, Pos alignment, Node... children) {
        final var vbox = new VBox(children);
        vbox.setAlignment(alignment);
        vbox.setPadding(padding);
        vbox.setSpacing(spacing);
        vbox.setBackground(Background.EMPTY);
        return vbox;
    }

    public void update(Border b, Region... regions) {
        for (var region : regions) region.setBorder(b);
    }
}
