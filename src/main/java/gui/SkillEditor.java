package gui;


import engine.Skill;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.scene.paint.*;
import javafx.scene.text.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class SkillEditor extends VBox implements Skill {
    private final Button deleteSkill, saveSkill;
    private final List<Node> slots;
    private final TextField questionField;
    private final TextField answerField;
    private  int questionslots;
    private  int answerslots;



    public SkillEditor() {

        questionslots = 1;
        answerslots = 1;


        deleteSkill = new Button("DELETE");
        deleteSkill.setPrefSize(120, 30);

        saveSkill = new Button("SAVE");
        saveSkill.setPrefSize(120, 30);

        final HBox buttonHolder = new HBox();
        buttonHolder.getChildren().add(deleteSkill);
        buttonHolder.getChildren().add(saveSkill);
        buttonHolder.setAlignment(Pos.CENTER);
        buttonHolder.setSpacing(5);

        final Label nquestionLabel = new Label("n slots question: " + questionslots);
        final Button newQSlotButton = new Button("+");
        newQSlotButton.setOnAction(e -> questionslots++);

        final Button removeQSlotButton = new Button("-");
        removeQSlotButton.setOnAction(e -> {
            if (questionslots > 1) {
                questionslots-=1;
            }
        });


        final Label nanswerLabel = new Label("n slots answer: " + answerslots);
        final Button newASlotButton = new Button("+");
        newASlotButton.setOnAction(e -> answerslots++);

        final Button removeASlotButton = new Button("-");
        removeASlotButton.setOnAction(e -> {
            if (answerslots > 1) {
                answerslots -= 1;
            }
        });
        final HBox SlotbuttonHolder = new HBox();
        SlotbuttonHolder.getChildren().add(nquestionLabel);
        SlotbuttonHolder.getChildren().add(newQSlotButton);
        SlotbuttonHolder.getChildren().add(removeQSlotButton);
        SlotbuttonHolder.getChildren().add(nanswerLabel);
        SlotbuttonHolder.getChildren().add(newASlotButton);
        SlotbuttonHolder.getChildren().add(removeASlotButton);
        SlotbuttonHolder.setAlignment(Pos.CENTER);
        SlotbuttonHolder.setSpacing(5);

        questionField = new TextField();
        questionField.setPromptText("Click to type...");

        final Label questionLabel = new Label("Question:");
        final HBox questionHolder = new HBox();
        questionHolder.getChildren().addAll(questionLabel, questionField);
        questionHolder.setAlignment(Pos.CENTER);
        questionHolder.setSpacing(5);
        final VBox slotContent = new VBox();
        slots =  slotContent.getChildren();
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

        getChildren().addAll(questionHolder, SlotbuttonHolder, slotHolder,answerHolder, buttonHolder, textBox);
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
        questionslots = segments[0].split(",").length;
        answerslots = segments[1].split(",").length;
        slots.remove(0);
        while (slots.size() < segments.length/2) {
            addNewSlot();
        }
        System.out.println("segments size" + segments.length);
        System.out.println("slots size" + slots.size());
        for (int i = 0; i < segments.length; i+=2) {
            String[] QSegments = segments[i].split(",");
                for (int j = 0; j < questionslots; j++) {
                    HBox Qbox = (HBox) slots.get(i/2);
                    Qbox = (HBox) Qbox.getChildren().get(1);
                    TextField Qfield = (TextField) Qbox.getChildren().get(j);
                    System.out.println(Qfield.getText());
                    Qfield.setText(QSegments[j]);
                }
            String[] ASegments = segments[i+1].split(",");
            for (int j = 0; j < answerslots; j++) {
                HBox Abox = (HBox) slots.get(i/2);
                Abox = (HBox) Abox.getChildren().get(2);
                TextField Afield = (TextField) Abox.getChildren().get(j);
                Afield.setText(ASegments[j]);
            }
        }
    }

    private void addNewSlot() {

        System.out.println(questionslots);
        System.out.println(answerslots);

        final HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(5);

        final Label l = new Label("Slot: ");

        final HBox slotFieldsPrompt = new HBox();
        for(int i =0; i<questionslots; i++) {
            final TextField slotPrompt = new TextField();
            slotPrompt.setPromptText("Prompt variable");
            slotFieldsPrompt.getChildren().add(slotPrompt);
        }

        final HBox slotFieldsAnswer = new HBox();
        for(int i =0; i<answerslots; i++) {
            final TextField slotAnswer = new TextField();
            slotAnswer.setPromptText("corresponding answer");
            slotFieldsAnswer.getChildren().add(slotAnswer);
        }

        final Button newSlotButton = new Button("+");
        newSlotButton.setOnAction(e -> addNewSlot());

        final Button removeSlotButton = new Button("-");
        removeSlotButton.setOnAction(e -> {
            if (slots.size() > 1) {
                slots.remove(box);
            }
        });

        box.getChildren().addAll(l, slotFieldsPrompt, slotFieldsAnswer, newSlotButton, removeSlotButton);

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
        b.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        questionField.textProperty().addListener((observable, oldValue, newValue) -> b.setText(newValue));
        return b;
    }

    @Override
    public String getInput() {

        String target = "$";
        String replacement = "([a-zA-Z0-9]*)"; //Regex for a word, which can contains lower or upper case or numbers
        String inputProcessed = questionField.getText().replace(target, replacement);
        questionField.setText(inputProcessed);



        return inputProcessed;
    }

    @Override
    public String[] getOutput() {
        String target = "$";
        String outputProcessed = answerField.getText().replace(target, "%s");
        String[] slots = new String[1];
        slots[0] = outputProcessed;
        answerField.setText(outputProcessed);
        return slots;
    }

    @Override
    public String[] getOutput(String[] inputParam) {
        System.out.println("-----------------------------GETOUTPUT----------------------------------------------------");
        String[] outputParam= new String[answerslots];
        Boolean found = false;
        Boolean corr = true;
        for (int i =0; i< slots.size();i++) {
            corr = true;
            if(!found) {
                int j = 0;
                for (; j < questionslots; j++) {
                    HBox Qbox = (HBox) slots.get(i);
                    Qbox = (HBox) Qbox.getChildren().get(1);
                    TextField Qfield = (TextField) Qbox.getChildren().get(j);
                    System.out.println(Qfield.getText());
                    if (!inputParam[j].equals(Qfield.getText())) {
                        corr = false;
                        System.out.println("-----------------------------Not all params correspond----------------------------------------------------");
                        break;
                    }

                }

                if (corr) {
                    found = true;
                    for (int k = 0; k < answerslots; k++) {
                        HBox Abox = (HBox) slots.get(i);
                        Abox = (HBox) Abox.getChildren().get(2);
                        TextField Afield = (TextField) Abox.getChildren().get(k);
                        outputParam[k] = Afield.getText();
                        System.out.println(Afield.getText());

                    }

                } else if(i == (slots.size() - 1)) {
                    String[] rslots = new String[1];
                    rslots[0] = "not recognising input, perhaps a misspell?";
                    return rslots;
                }
            }
        }
        String target = "$";
        String outputProcessed = answerField.getText().replace(target, "%s");
        String[] rslots = new String[1];
        rslots[0] = String.format(outputProcessed, (Object[]) outputParam);
        System.out.println("-----------------------------INSERT----------------------------------------------------");
        System.out.println(outputParam[0]);
        System.out.println(rslots[0]);
        return rslots;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getInput()).append("\r\n");
        for (int i =0; i< slots.size();i++) {
            for(int j =0; j< questionslots;j++) {
                HBox Qbox = (HBox)slots.get(i);
                Qbox = (HBox) Qbox.getChildren().get(1);
                TextField Qfield = (TextField) Qbox.getChildren().get(j);
                sb.append(Qfield.getText()).append(",");
            }
            sb.delete(sb.lastIndexOf(","), sb.length());
            sb.append("\n");
            for(int j =0; j< answerslots;j++) {
                HBox Abox = (HBox)slots.get(i);
                Abox = (HBox) Abox.getChildren().get(2);
                TextField Afield = (TextField) Abox.getChildren().get(j);
                sb.append(Afield.getText()).append(",");
            }
            sb.delete(sb.lastIndexOf(","), sb.length());
            sb.append("\n");
        }
        sb.delete(sb.lastIndexOf("\n"), sb.length());
        sb.append("\r\n");
        sb.append(answerField.getText());
        System.out.println("-----------------------------STRING--------------------------------------------------");
        System.out.println(sb);
        return sb.toString();
    }
}