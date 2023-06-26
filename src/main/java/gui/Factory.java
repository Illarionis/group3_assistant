package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public final class Factory {
    public final String scrollPaneStylesheet;

    public Factory() {
        final var resource = getClass().getResource("/css/scroll-pane.css");
        scrollPaneStylesheet = resource == null ? "" : resource.toExternalForm();
    }

    public Button createButton(String text, double minWidth, double minHeight, double maxWidth, double maxHeight) {
        final var button = new Button(text);
        button.setMinSize(minWidth, minHeight);
        button.setMaxSize(maxWidth, maxHeight);
        button.setBackground(Backgrounds.DEFAULT);
        button.setStyle(TextStyles.DEFAULT);
        button.setOnMouseEntered(EventHandlers.HIGHLIGHT_REGION);
        button.setOnMouseExited(EventHandlers.REMOVE_HIGHLIGHT);
        return button;
    }

    public HBox createHBox(double spacing, Insets padding, Pos alignment, Node... children) {
        final var hbox = new HBox(children);
        hbox.setAlignment(alignment);
        hbox.setPadding(padding);
        hbox.setSpacing(spacing);
        hbox.setFillHeight(true);
        hbox.setBackground(Backgrounds.DEFAULT);
        return hbox;
    }

    public Label createLabel(String text, Pos alignment, double minWidth, double minHeight, double maxWidth, double maxHeight) {
        final var label = new Label(text);
        label.setAlignment(alignment);
        label.setMinSize(minWidth, minHeight);
        label.setMaxSize(maxWidth, maxHeight);
        label.setBackground(Backgrounds.DEFAULT);
        label.setStyle(TextStyles.DEFAULT);
        return label;
    }

    public Scene createScene(Region content, double width, double height) {
        final var scene = new Scene(content, width, height);
        scene.setFill(Colors.SCENE);
        return scene;
    }

    public ScrollPane createScrollPane(Region content) {
        final var scrollPane = new ScrollPane(content);
        scrollPane.setBackground(Backgrounds.DEFAULT);
        scrollPane.getStylesheets().add(scrollPaneStylesheet);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        return scrollPane;
    }

    public Text createText(String s) {
        final var text = new Text(s);
        text.setFill(Colors.TEXT);
        return text;
    }

    public TextField createTextField(String promptText, Pos alignment, double minWidth, double minHeight, double maxWidth, double maxHeight) {
        final var textField = new TextField();
        textField.setPromptText(promptText);
        textField.setAlignment(alignment);
        textField.setMinSize(minWidth, minHeight);
        textField.setMaxSize(maxWidth, maxHeight);
        textField.setBackground(Backgrounds.DEFAULT);
        textField.setStyle(TextStyles.DEFAULT);
        return textField;
    }

    public TextFlow createTextFlow(Insets padding, Node... children) {
        final var textFlow = new TextFlow(children);
        textFlow.setPadding(padding);
        textFlow.setBackground(Backgrounds.DEFAULT);
        return textFlow;
    }

    public VBox createVBox(double spacing, Insets padding, Pos alignment, Node... children) {
        final var vbox = new VBox(children);
        vbox.setAlignment(alignment);
        vbox.setPadding(padding);
        vbox.setSpacing(spacing);
        vbox.setFillWidth(true);
        vbox.setBackground(Backgrounds.DEFAULT);
        return vbox;
    }
}
