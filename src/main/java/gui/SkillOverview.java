package gui;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public final class SkillOverview extends VBox {
    private final double OVERVIEW_WIDTH;
    private final SkillEditor EDITOR;
    private final Stage EDITOR_STAGE;
    private final ObservableList<Node> SKILLS;

    public SkillOverview(double width) {
        OVERVIEW_WIDTH = width;

        EDITOR = new SkillEditor();
        Button saveSkill = EDITOR.getSaveButton();
        saveSkill.setOnAction(e -> onSaveButtonClicked());

        EDITOR_STAGE = new Stage();
        EDITOR_STAGE.setTitle("SKILL EDITOR");
        EDITOR_STAGE.setScene(new Scene(EDITOR, 300, 600));

        Label title = new Label("SKILLS");
        HBox titleBar = new HBox();
        titleBar.setBackground(null);
        titleBar.setBorder(Border.stroke(Color.LIGHTGRAY));
        titleBar.setPrefSize(width, 30);
        titleBar.setAlignment(Pos.CENTER);
        titleBar.getChildren().add(title);

        VBox scrollContent = new VBox();
        SKILLS = scrollContent.getChildren();

        ScrollPane sp = new ScrollPane(scrollContent);
        sp.setBackground(null);
        sp.setBorder(Border.stroke(Color.LIGHTGRAY));
        VBox.setVgrow(sp, Priority.ALWAYS);

        Button openEditor = new Button("+");
        openEditor.setBackground(null);
        openEditor.setBorder(Border.stroke(Color.LIGHTGRAY));
        openEditor.setPrefSize(width, 20);
        openEditor.setOnAction(e -> {
            EDITOR.clear();

            Button deleteSkill = EDITOR.getDeleteButton();
            deleteSkill.setText("CLEAR");
            deleteSkill.setOnAction(deleteEvent -> EDITOR.clear());
            showEditor();
        });

        getChildren().addAll(titleBar, sp, openEditor);
    }

    private void assignSkillClickEvent(Button skill, String question, String[] slots) {
        skill.setOnAction(e -> {
            Button deleteSkill = EDITOR.getDeleteButton();
            deleteSkill.setText("DELETE");
            deleteSkill.setOnAction(deleteEvent -> onDeleteButtonClicked(skill));

            Button saveSkill = EDITOR.getSaveButton();
            saveSkill.setOnAction(saveEvent -> onSkillEdited(skill));

            EDITOR.display(question, slots);

            showEditor();
        });
    }

    private Button createSkill(String question) {
        Button b = new Button("Skill: " + question);
        b.setBorder(Border.stroke(Color.LIGHTGRAY));
        b.setBackground(null);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setPrefWidth(OVERVIEW_WIDTH - 4);
        return b;
    }

    private void onDeleteButtonClicked(Node skill) {
        SKILLS.remove(skill);
        EDITOR_STAGE.close();
    }

    private void onSaveButtonClicked() {
        String question = EDITOR.readQuestionField();
        String[] slots = EDITOR.readSkillSlots();
        addSkill(question, slots);
        EDITOR_STAGE.close();
    }

    private void onSkillEdited(Button skill) {
        String question = EDITOR.readQuestionField();
        String[] slots = EDITOR.readSkillSlots();
        assignSkillClickEvent(skill, question, slots);
        EDITOR_STAGE.close();
    }

    private void showEditor() {
        if (!EDITOR_STAGE.isShowing()) {
            EDITOR_STAGE.show();
        }
    }

    public void addSkill(String question, String[] slots) {
        Button skill = createSkill(question);
        assignSkillClickEvent(skill, question, slots);
        SKILLS.add(skill);
    }
}
