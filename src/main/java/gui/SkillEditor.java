package gui;


import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.scene.paint.*;
import javafx.scene.text.*;


public class SkillEditor extends VBox {

    //buttons
    Button bsave=new Button("Save");
    Button bdelete=new Button("Delete");

    //labels
    Label Q1=new Label("Question: ");

    //textfields
    TextField question=new TextField();

    public SkillEditor() {
        //HBox for the question: includes question label and textfield
        HBox questionBox=new HBox();
        questionBox.getChildren().add(Q1);
        questionBox.setAlignment(Pos.CENTER);
        questionBox.setSpacing(5);
        questionBox.getChildren().add(question);

        // Creating the slot holder
        VBox slotHolder = new VBox();
        ObservableList<Node> slots = slotHolder.getChildren();
        addNewSlot(slots);

        //HBox for the buttons: includes save and delete buttons
        HBox buttonsBox=new HBox();
        buttonsBox.getChildren().add(bsave);
        buttonsBox.getChildren().add(bdelete);
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
}