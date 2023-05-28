package gui;

import engine.Assistant;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

public abstract class Editor implements Displayable, Styleable {
    // INLINE CSS
    protected static final String WARNING_TEXT_STYLE = "-fx-text-fill:rgb(180, 0, 0);";

    // Utility
    protected final List<OverviewButton> overviewButtons;
    protected final String arrowSymbol = Character.toString(8594);

    // Graphical Design
    protected final Button addButton, saveButton;
    protected final Label titleHolder, topLabel, middleLabel, bottomLabel;
    protected final ScrollPane viewport;
    protected final TextField topTextField, middleTextField, bottomTextField;
    protected final VBox templateHolder, valueHolder, editorPanel;

    // Functionality
    protected Assistant a;
    protected Overview o;

    public Editor(Factory f) {
        final Insets padding = new Insets(5, 5, 5, 5);

        // Providing a label to recognize the editor
        titleHolder = f.createLabel("EDITOR TITLE");
        titleHolder.setMaxWidth(Double.MAX_VALUE);

        // Providing a box to hold all template boxes
        templateHolder = f.createVBox(5, padding, Pos.CENTER);

        // Providing a box to hold all value boxes
        valueHolder = f.createVBox(5, padding);

        // Providing a scroll pane to make the value holder scrollable
        viewport = f.createScrollPane(valueHolder);
        VBox.setVgrow(viewport, Priority.ALWAYS);

        // Providing a button to add a value box to the value holder
        addButton = new Button("ADD");
        addButton.setBackground(Background.EMPTY);
        addButton.setMaxWidth(Double.MAX_VALUE);

        // Providing a button to save the templates and values
        saveButton = new Button("SAVE");
        saveButton.setBackground(Background.EMPTY);
        saveButton.setMaxWidth(Double.MAX_VALUE);

        // Providing a panel to represent the editor
        editorPanel = f.createVBox(3, titleHolder, templateHolder, viewport, addButton, saveButton);

        // Providing a label to recognize the top template
        topLabel = f.createLabel("TOP LABEL", Pos.CENTER_RIGHT);
        topLabel.setMinWidth(150);

        // Providing a text field for the top template
        topTextField = f.createTextField("TOP TEXT FIELD", Pos.CENTER);
        topTextField.setBackground(Background.EMPTY);
        topTextField.setMinWidth(240);

        // Providing a box to display the top template components horizontally
        final HBox topBox = f.createHBox(Pos.CENTER, topLabel, topTextField);

        // Providing a label to recognize the middle template
        middleLabel = f.createLabel("MID LABEL", Pos.CENTER_RIGHT);
        middleLabel.setMinWidth(150);

        // Providing a text field for the middle template
        middleTextField = f.createTextField("MIDDLE TEXT FIELD", Pos.CENTER);
        middleTextField.setBackground(Background.EMPTY);
        middleTextField.setMinWidth(240);

        // Providing a box to display the middle template components horizontally
        final HBox middleBox = f.createHBox(Pos.CENTER, middleLabel, middleTextField);

        // Providing a label to recognize the bottom template
        bottomLabel = f.createLabel("BOTTOM LABEL", Pos.CENTER_RIGHT);
        bottomLabel.setMinWidth(150);

        // Providing a text field for the bottom template
        bottomTextField = f.createTextField("BOTTOM TEXT FIELD", Pos.CENTER);
        bottomTextField.setBackground(Background.EMPTY);
        bottomTextField.setMinWidth(240);

        // Providing a box to display the bottom template components horizontally
        final HBox bottomBox = f.createHBox(Pos.CENTER, bottomLabel, bottomTextField);

        // Adding the template boxes to the template holder
        templateHolder.getChildren().addAll(topBox, middleBox, bottomBox);

        // Providing a list to store all overview buttons
        overviewButtons = new ArrayList<>();

        // Assigning visual effects to the add button
        addButton.setOnMouseEntered(enterEvent -> addButton.setBackground(Effects.GREEN_OVERLAY_BACKGROUND));
        addButton.setOnMouseExited(exitEvent -> addButton.setBackground(Background.EMPTY));

        // Assigning visual effects to the save button
        saveButton.setOnMouseEntered(enterEvent -> saveButton.setBackground(Effects.GREEN_OVERLAY_BACKGROUND));
        saveButton.setOnMouseExited(exitEvent -> saveButton.setBackground(Background.EMPTY));
    }

    public void setAssistant(Assistant a) {
        this.a = a;
    }

    public void setOverview(Overview o) {
        this.o = o;
    }

    @Override
    public Region getPanel() {
        return editorPanel;
    }

    @Override
    public void setBorder(Border b) {
        titleHolder.setBorder(b);
        templateHolder.setBorder(b);
        viewport.setBorder(b);

        addButton.setBorder(b);
        saveButton.setBorder(b);

        topTextField.setBorder(b);
        middleTextField.setBorder(b);
        bottomTextField.setBorder(b);

        for (var button : overviewButtons) button.setBorder(b);
    }

    @Override
    public void setStyle(String s) {
        titleHolder.setStyle(s);

        addButton.setStyle(s);
        saveButton.setStyle(s);

        topLabel.setStyle(s);
        middleLabel.setStyle(s);
        bottomLabel.setStyle(s);

        topTextField.setStyle(s);
        middleTextField.setStyle(s);
        bottomTextField.setStyle(s);

        for (var button : overviewButtons) button.setStyle(s);
    }

    protected static final class OverviewButton implements Displayable, Styleable {
        private final Button delete, display;
        private final HBox panel;

        public OverviewButton(Factory f) {
            // Providing a button to delete the skill button from the skill overview
            delete = new Button(Character.toString(10005));
            delete.setBackground(Background.EMPTY);
            delete.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            // Providing a button to display the skill in the skill editor
            display = new Button();
            display.setBackground(Background.EMPTY);
            display.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            HBox.setHgrow(display, Priority.ALWAYS);

            // Providing a box to display the buttons horizontally
            panel = f.createHBox(3, display, delete);

            // Assigning visual effects for the delete button
            delete.setOnMouseEntered(enterEvent -> delete.setBackground(Effects.RED_OVERLAY_BACKGROUND));
            delete.setOnMouseExited(exitEvent -> delete.setBackground(Background.EMPTY));

            // Assigning visual effects for the display button
            display.setOnMouseEntered(enterEvent -> display.setBackground(Effects.GREEN_OVERLAY_BACKGROUND));
            display.setOnMouseExited(exitEvent -> display.setBackground(Background.EMPTY));
        }

        public void setDeleteEvent(Runnable process) {
            delete.setOnAction(deleteEvent -> process.run());
        }

        public void setDisplayEvent(Runnable process) {
            display.setOnAction(displayEvent -> process.run());
        }

        public void setText(String s) {
            display.setText(s);
        }

        @Override
        public Region getPanel() {
            return panel;
        }

        @Override
        public void setBorder(Border b) {
            display.setBorder(b);
        }

        @Override
        public void setStyle(String s) {
            delete.setStyle(s);
            display.setStyle(s);
        }
    }
}
