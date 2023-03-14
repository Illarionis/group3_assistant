import engine.LogController;
import gui.Chat;
import gui.Overview;
import gui.SkillEditor;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        final var skillOverview = new Overview("SKILL LIST", "EDIT SKILLS");
        skillOverview.setPrefWidth(300);

        final Button addNewSkill = skillOverview.getFootnoteButton();
        addNewSkill.setText("+");
        addNewSkill.setOnAction(newTabEvent -> {
            final SkillEditor se = new SkillEditor();
            final Tab t = skillOverview.createTab();
            t.setContent(se);
            skillOverview.addTab(t);

            final Button b = skillOverview.createNode();
            b.setOnAction(event -> skillOverview.openTab(t));
            b.setText("Title is not specified");
            skillOverview.addNode(b);

            final Button deleteSkill = se.getDeleteSkillButton();
            deleteSkill.setOnAction(deleteSkillEvent -> {
                skillOverview.removeNode(b);
                skillOverview.removeTab(t);
            });

            final TextField questionField = se.getQuestionField();
            questionField.textProperty().addListener((observable, oldValue, newValue) -> b.setText(newValue));
        });

        final var chatOverview = new Overview("CONVERSATIONS", "CHAT");
        chatOverview.setPrefWidth(300);

        final Button addNewChat = chatOverview.getFootnoteButton();
        addNewChat.setText("+");
        addNewChat.setOnAction(newTabEvent -> {
            final Chat c = new Chat();

            final Tab t = chatOverview.createTab();
            t.setContent(c);
            chatOverview.addTab(t);

            final Button b = chatOverview.createNode();
            b.setOnAction(clickEvent -> chatOverview.openTab(t));
            b.setText("Title is not specified");
            chatOverview.addNode(b);

            final TextField titleField = c.getTitleField();
            titleField.textProperty().addListener((observable, oldValue, newValue) -> b.setText(newValue));

            final TextField messageField = c.getMessageField();
            messageField.setOnKeyPressed(keyPressedEvent -> {
                if (keyPressedEvent.getCode() == KeyCode.ENTER) {
                    String s = messageField.getText();
                    if (s == null || s.isBlank()) {
                        return;
                    }

                    final Node message = c.createNode(messageField.getText(), true);
                    c.addNode(message);

                    LogController l = new LogController(69, 0, s, true);
                    l.save();
                    messageField.clear();
                }
            });
        });

        HBox.setHgrow(skillOverview, Priority.ALWAYS);
        HBox.setHgrow(chatOverview, Priority.ALWAYS);
        final HBox root = new HBox();
        root.getChildren().addAll(skillOverview, chatOverview);

        final var scene = new Scene(root, 600, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
