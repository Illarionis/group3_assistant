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
    private final List<TextField> slotFieldsPrompt;
    private final List<TextField> slotFieldsAnswer;
    private final TextField questionField;
    private final TextField answerField;


    public SkillEditor() {
        slotFieldsPrompt = new ArrayList<>();
        slotFieldsAnswer = new ArrayList<>();

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


        answerField = new TextField();
        final Label answerLabel = new Label("Answer:");
        final HBox answerHolder = new HBox();
        answerHolder.getChildren().addAll(answerLabel, answerField);
        answerHolder.setAlignment(Pos.CENTER);
        answerHolder.setSpacing(5);




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

        getChildren().addAll(questionHolder, slotHolder,answerHolder, buttonHolder, textBox);
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


        answerField.setText(segments[2]);

        segments = segments[1].split("\n");
        while (slotFieldsPrompt.size() < segments.length/2) {
            addNewSlot();
        }
        System.out.println("-----------------------------ERRORR---------------------------------------------------");

        for (int i = 0; i < segments.length; i+=2) {
            slotFieldsPrompt.get(i/2).setText(segments[i]);
            slotFieldsAnswer.get(i/2).setText(segments[i+1]);
        }
    }

    private void addNewSlot() {
        final HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(5);

        final Label l = new Label("Slot: ");

        final TextField slotFieldPrompt = new TextField();
        slotFieldPrompt.setPromptText("Prompt variable");

        final TextField slotFieldAnswer = new TextField();
        slotFieldAnswer.setPromptText("corresponding answer");

        final Button newSlotButton = new Button("+");
        newSlotButton.setOnAction(e -> addNewSlot());

        final Button removeSlotButton = new Button("-");
        removeSlotButton.setOnAction(e -> {
            if (slotFieldsPrompt.size() > 1) {
                slotFieldsPrompt.remove(slotFieldPrompt);
                slotFieldsAnswer.remove(slotFieldAnswer);
                slots.remove(box);
            }
        });

        box.getChildren().addAll(l, slotFieldPrompt, slotFieldAnswer, newSlotButton, removeSlotButton);

        slotFieldsPrompt.add(slotFieldPrompt);
        slotFieldsAnswer.add(slotFieldAnswer);
        slots.add(box);

        System.out.println("-----------------------------LENGTH--------------------------------------------------");
        System.out.println(slotFieldsPrompt.size());
        System.out.println(slotFieldsAnswer.size());
        System.out.println(slots.size());

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

        String target = "*";
        String replacement = "([a-zA-Z0-9]*)"; //Regex for a word, which can contains lower or upper case or numbers
        String inputProcessed = questionField.getText().replace(target, replacement);
        questionField.setText(inputProcessed);



        return inputProcessed;
    }

    @Override
    public String[] getOutput() {
        String target = "*";
        String outputProcessed = answerField.getText().replace(target, "%s");
        String[] slots = new String[1];
        slots[0] = outputProcessed;
        answerField.setText(outputProcessed);
        return slots;
    }

    @Override
    public String[] getOutput(String inputParam) {
        String outputParam= slotFieldsAnswer.get(0).getText();
        for (int i = 0; i < slotFieldsPrompt.size(); i++) {
            if (inputParam.equals(slotFieldsPrompt.get(i).getText())){
                outputParam = slotFieldsAnswer.get(i).getText();
                break;

            }
        }
        String target = "*";
        String outputProcessed = answerField.getText().replace(target, "%s");
        String[] slots = new String[1];
        slots[0] = String.format(outputProcessed, outputParam);
        System.out.println("-----------------------------INSERT----------------------------------------------------");
        System.out.println(outputParam);
        System.out.println(slots[0]);
        return slots;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getInput()).append("\r\n");
        for (int i =0; i< slotFieldsPrompt.size();i++) {
            sb.append(slotFieldsPrompt.get(i).getText()).append("\n");
            sb.append(slotFieldsAnswer.get(i).getText()).append("\n");
        }
        sb.delete(sb.lastIndexOf("\n"), sb.length());
        sb.append("\r\n");
        sb.append(answerField.getText());
        System.out.println("-----------------------------STRING--------------------------------------------------");
        System.out.println(sb.toString());
        return sb.toString();
    }
}