package gui;

import engine.Assistant;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public final class Chat implements Displayable, Styleable {
    private final Insets padding;
    private final Label titleHolder;
    private final ScrollPane viewport;
    private final TextField inputField;
    private final VBox chatPanel, content;
    private Background assistantMessageBackground, userMessageBackground;
    private Assistant a;

    public Chat(Factory f) {
        padding = new Insets(5, 5, 5, 5);

        inputField = f.createTextField("Click to type...");
        inputField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() != KeyCode.ENTER) return;

            final String input = inputField.getText();
            registerUserMessage(input);
            inputField.clear();

            final String output;
            if (a != null) output = a.respond(input);
            else output = "Apologies, currently not capable of responding to any input.";
            registerAssistantMessage(output);
        });

        titleHolder = f.createLabel("CHAT");
        titleHolder.setMaxWidth(Double.MAX_VALUE);

        content = f.createVBox(5, padding);
        viewport = f.createScrollPane(content);
        chatPanel = f.createVBox(3, titleHolder, viewport, inputField);

        VBox.setVgrow(viewport, Priority.ALWAYS);
    }

    private HBox createHBox(Node... children) {
        final HBox b = new HBox(children);
        content.getChildren().add(b);
        return b;
    }

    private TextFlow createTextFlow(String text) {
        final Text t = new Text(text);
        final TextFlow f = new TextFlow(t);
        f.setPadding(padding);
        f.setMaxWidth(240);
        return f;
    }

    public void registerAssistantMessage(String s) {
        final TextFlow f = createTextFlow(s);
        f.setBackground(assistantMessageBackground);

        final HBox b = createHBox(f);
        b.setAlignment(Pos.CENTER_LEFT);
    }

    public void registerUserMessage(String s) {
        final TextFlow f = createTextFlow(s);
        f.setBackground(userMessageBackground);

        final HBox b = createHBox(f);
        b.setAlignment(Pos.CENTER_RIGHT);
    }

    public void setAssistant(Assistant a) {
        this.a = a;
    }

    public void setBackgrounds(Background assistantMessageBackground, Background userMessageBackground) {
        this.assistantMessageBackground = assistantMessageBackground;
        this.userMessageBackground = userMessageBackground;
    }

    @Override
    public Region getPanel() {
        return chatPanel;
    }

    @Override
    public void setBorder(Border b) {
        titleHolder.setBorder(b);
        viewport.setBorder(b);
        inputField.setBorder(b);
    }

    @Override
    public void setStyle(String s) {
        titleHolder.setStyle(s);
        inputField.setStyle(s);
    }
}
