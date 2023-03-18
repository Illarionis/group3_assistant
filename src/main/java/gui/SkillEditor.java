package gui;


import engine.Skill;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.scene.paint.*;
import javafx.scene.text.*;

import java.util.ArrayList;
import java.util.List;

public class SkillEditor extends VBox implements Skill {
    private final Button deleteSkill, saveSkill;
    private final List<Node> slots;
    private final List<TextField> slotFields;
    private final TextField questionField;

    public SkillEditor() {
        slotFields = new ArrayList<>();

        deleteSkill = new Button("DELETE");
        deleteSkill.setPrefSize(120, 30);

        saveSkill = new Button("SAVE");
        saveSkill.setPrefSize(120, 30);

        final HBox buttonHolder = new HBox();
        buttonHolder.getChildren().add(deleteSkill);
        buttonHolder.getChildren().add(saveSkill);
        buttonHolder.setAlignment(Pos.CENTER);
        buttonHolder.setSpacing(5);

        questionField = new TextField();
        questionField.setPromptText("Click to type...");

        final Label questionLabel = new Label("Question:");

        final HBox questionHolder = new HBox();
        questionHolder.getChildren().addAll(questionLabel, questionField);
        questionHolder.setAlignment(Pos.CENTER);
        questionHolder.setSpacing(5);

        final VBox slotContent = new VBox();
        slots = slotContent.getChildren();
        addNewSlot();

        final ScrollPane slotHolder = new ScrollPane(slotContent);
        slotHolder.setBackground(Background.EMPTY);
        slotHolder.setBorder(Border.EMPTY);
        slotHolder.setFitToHeight(true);
        slotHolder.setFitToWidth(true);

        //adding the textflow for the help text
        HBox textBox=new HBox();
        TextFlow text_flow = new TextFlow();
        Text help = new Text("HELP\n");
        help.setFill(Color.RED);
        help.setFont(Font.font("Times New Roman", FontPosture.ITALIC,25));

        Text helptext = new Text("Through this Skill Editor you have the possibility to define new skills. Said skills are loaded and saved into a text file ");
        helptext.setFill(Color.BLACK);
        helptext.setFont(Font.font("Helvetica", 15));

        text_flow.getChildren().add(help);
        text_flow.getChildren().add(helptext);
        textBox.getChildren().add(text_flow);

        getChildren().addAll(questionHolder, slotHolder, buttonHolder, textBox);
        setAlignment(Pos.CENTER);
        setFocused(false);
        setFocusTraversable(false);
        setPadding(new Insets(5, 0, 0, 0));
        setStyle("-fx-focus-color : transparent; -fx-faint-focus-color : transparent;");
        setSpacing(70);
    }

    public SkillEditor(String s) {
        this();

        String[] segments = s.split("\r\n");
        questionField.setText(segments[0]);

        segments = segments[1].split("\n");
        while (slotFields.size() < segments.length) {
            addNewSlot();
        }
        for (int i = 0; i < segments.length; i++) {
            slotFields.get(i).setText(segments[i]);
        }
    }

    private void addNewSlot() {
        final HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(5);

        final Label l = new Label("Slot: ");

        final TextField slotField = new TextField();
        slotField.setPromptText("Click to type...");

        final Button newSlotButton = new Button("+");
        newSlotButton.setOnAction(e -> addNewSlot());

        final Button removeSlotButton = new Button("-");
        removeSlotButton.setOnAction(e -> {
            if (slotFields.size() > 1) {
                slotFields.remove(slotField);
                slots.remove(box);
            }
        });

        box.getChildren().addAll(l, slotField, newSlotButton, removeSlotButton);

        slotFields.add(slotField);
        slots.add(box);
    }

    public Button getDeleteButton() {
        return deleteSkill;
    }

    public Button getSaveButton() {
        return saveSkill;
    }

    public Button getTitleTracker() {
        final Button b = new Button(questionField.getText());
        questionField.textProperty().addListener((observable, oldValue, newValue) -> b.setText(newValue));
        return b;
    }

    @Override
    public String getInput() {
        return questionField.getText();
    }

    @Override
    public String[] getOutput() {
        String[] slots = new String[slotFields.size()];
        for (int i = 0; i < slotFields.size(); i++) {
            slots[i] = slotFields.get(i).getText();
        }
        return slots;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getInput()).append("\r\n");
        for (String s : getOutput()) {
            sb.append(s).append("\n");
        }
        return sb.toString();
    }
}