package gui;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.paint.*;
import javafx.scene.text.*;


public class SkillEditor {

    //buttons
    Button bsave=new Button("Save");
    Button bdelete=new Button("Delete");
    Button addslot= new Button("+");

    //labels
    Label Q1=new Label("Question: ");
    Label S2=new Label("Slots: ");


    //textfields
    TextField question=new TextField();
    TextField slots=new TextField();
    TextField newslot = new TextField();


    VBox vboxnewslot = new VBox();


    SkillEditor(Stage primaryStage)
    {

        //HBox for the question: includes question label and textfield
        HBox questionBox=new HBox();
        questionBox.getChildren().add(Q1);
        questionBox.setAlignment(Pos.CENTER);
        questionBox.setSpacing(5);
        questionBox.getChildren().add(question);


        //HBox for the slots: includes slot label and textfield
        HBox slotBox=new HBox();
        slotBox.getChildren().add(S2);
        slotBox.setAlignment(Pos.CENTER);
        slotBox.setSpacing(5);
        slotBox.getChildren().add(slots);
        slotBox.getChildren().add(addslot);

        vboxnewslot.getChildren().add(slotBox);


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
        VBox editor=new VBox();
        editor.getChildren().add(questionBox);
        editor.setAlignment(Pos.CENTER);
        editor.setSpacing(70);

        editor.getChildren().add(vboxnewslot);
        editor.setAlignment(Pos.CENTER);
        editor.setSpacing(70);

        editor.getChildren().add(buttonsBox);
        editor.setAlignment(Pos.CENTER);

        editor.getChildren().add(textBox);
        editor.setAlignment(Pos.CENTER);

        addslot.setOnAction(event -> {
            //setting a max of 10 slots per skill
            for (int i = 0; i < 10; i++) {
                newslot.setPrefWidth(5);
                newslot.setPrefHeight(5);
                vboxnewslot.getChildren().add(newslot);
            }
        });

        Scene scene = new Scene (editor, 500, 750);
        primaryStage.setTitle("Skill Editor");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}