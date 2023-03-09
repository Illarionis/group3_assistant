package gui;


import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.scene.paint.*;
import javafx.scene.text.*;


public class SkillEditor extends VBox {
    private final Button DELETE_SKILL = new Button("Delete");
    private final Button SAVE_SKILL = new Button("Save");
    private final TextField QUESTION_FIELD = new TextField();
    private final ObservableList<Node> SKILL_SLOTS;

    public SkillEditor() {
        //HBox for the question: includes question label and textfield
        HBox questionBox=new HBox();
        questionBox.getChildren().add(new Label("Question: "));
        questionBox.setAlignment(Pos.CENTER);
        questionBox.setSpacing(5);
        questionBox.getChildren().add(QUESTION_FIELD);

        // Creating the slot holder
        VBox slotHolder = new VBox();
        SKILL_SLOTS = slotHolder.getChildren();
        addNewSlot(SKILL_SLOTS);

        //HBox for the buttons: includes save and delete buttons
        HBox buttonsBox=new HBox();
        buttonsBox.getChildren().add(SAVE_SKILL);
        buttonsBox.getChildren().add(DELETE_SKILL);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setSpacing(5);

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

        //adding the HBoxes
        getChildren().add(questionBox);
        setAlignment(Pos.CENTER);
        setSpacing(70);

        getChildren().add(slotHolder);
        setAlignment(Pos.CENTER);
        setSpacing(70);

        getChildren().add(buttonsBox);
        setAlignment(Pos.CENTER);

        getChildren().add(textBox);
        setAlignment(Pos.CENTER);
    }

    private void addNewSlot(ObservableList<Node> slotList) {
        // Creating the slot object
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(5);

        // Creating slot components
        Label l = new Label("Slot: ");
        TextField slotField = new TextField();
        Button newSlotButton = new Button("+");
        Button removeSlotButton = new Button("-");

        // Adding all components to the slot object
        box.getChildren().addAll(l, slotField, newSlotButton, removeSlotButton);

        // Defining the functionality of the new slot button.
        newSlotButton.setOnAction(e -> addNewSlot(slotList));

        // Defining the functionality of the remove slot button.
        removeSlotButton.setOnAction(e -> {
            if (slotList.size() > 1) {
                slotList.remove(box);
            }
        });

        // Adding the slot to the slot list
        slotList.add(box);
    }

    public void clear() {
        QUESTION_FIELD.setText("");
        SKILL_SLOTS.clear();
        addNewSlot(SKILL_SLOTS);
    }

    public void display(String question, String[] slots) {
        QUESTION_FIELD.setText(question);
        int delta = SKILL_SLOTS.size() - slots.length;
        if (delta < 0) {
            for (int i = delta; i < 0; i++) {
                addNewSlot(SKILL_SLOTS);
            }
        } else if (delta > 0) {
            for (int i = delta; i > 0; i--) {
                SKILL_SLOTS.remove(SKILL_SLOTS.size() - 1);
            }
        }
        for (int i = 0; i < slots.length; i++) {
            HBox slot = (HBox) SKILL_SLOTS.get(i);
            for (Node n : slot.getChildren()) {
                if (n instanceof TextField) {
                    TextField slotInput = (TextField) n;
                    slotInput.setText(slots[i]);
                    break;
                }
            }
        }
    }

    public Button getDeleteButton() {
        return DELETE_SKILL;
    }

    public Button getSaveButton() {
        return SAVE_SKILL;
    }

    public String readQuestionField() {
        return QUESTION_FIELD.getText();
    }

    public String[] readSkillSlots() {
        String[] slotInputs = new String[SKILL_SLOTS.size()];
        for (int i = 0; i < slotInputs.length; i++) {
            HBox slot = (HBox) SKILL_SLOTS.get(i);
            for (Node n : slot.getChildren()) {
                if (n instanceof TextField) {
                    TextField slotInput = (TextField) n;
                    slotInputs[i] = slotInput.getText();
                    break;
                }
            }
        }
        return slotInputs;
    }
}