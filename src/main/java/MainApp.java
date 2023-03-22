import engine.Assistant;
import engine.Data;
import gui.Chat;
import gui.Overview;
import gui.SkillEditor;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.UUID;


public class MainApp extends Application {
    private static final String GUEST = "guest";

    public static void main(String[] args) {
        launch(args);
    }

    private static String randomFileId() {
        return UUID.randomUUID() + ".txt";
    }

    @Override
    public void start(Stage primary) {
        final Assistant a = new Assistant();

        final Overview chatOverview = new Overview("CONVERSATIONS", "CHAT");
        chatOverview.setPrefSize(300, 600);
        final Data chatData = new Data(new File("data/chats"));
        final File[] chatFiles = chatData.read(GUEST);
        for (File f : chatFiles) {
            final String s = chatData.read(f);
            final Chat c = new Chat(s);
            link(a, c, chatData, chatOverview, GUEST, f.getName());
        }
        chatOverview.getFootnoteButton().setOnAction(newChatEvent -> {
            final Chat c = new Chat();
            link(a, c, chatData, chatOverview, GUEST, randomFileId());
        });

        final Overview skillOverview = new Overview("SKILLS", "EDIT");
        skillOverview.setPrefSize(600, 600);
        final Data skillData = new Data(new File("data/skills"));
        final File[] skillFiles = skillData.read(GUEST);
        for (File f : skillFiles) {
            final String s = skillData.read(f);
            final SkillEditor e = new SkillEditor(s);
            link(a, e, skillData, skillOverview, GUEST, f.getName());

        }
        skillOverview.getFootnoteButton().setOnAction(newSkillEvent -> {
            final SkillEditor e = new SkillEditor();
            link(a, e, skillData, skillOverview, GUEST, randomFileId());
        });

        final HBox root = new HBox();
        root.getChildren().addAll(skillOverview, chatOverview);
        root.setAlignment(Pos.CENTER);

        final Scene s = new Scene(root, 600, 600);
        primary.setScene(s);
        primary.show();
    }

    private void link(Assistant a, Chat c, Data d, Overview o, String accountId, String fileId) {
        final TextField msgField = c.getMessageField();
        msgField.setOnKeyPressed(keyPressedEvent -> {
            if (keyPressedEvent.getCode() == KeyCode.ENTER) {
                final String msg = msgField.getText();
                if (msg != null && !msg.isBlank()) {
                    c.newMessage(msg, GUEST);

                    final String[] responses = a.respond(msg);
                    for (String s : responses) {
                        c.newMessage(s, "Assistant");
                    }
                    msgField.clear();
                }
            }
        });

        final Tab t = new Tab("", c);
        final Button b = c.getTitleTracker();
        b.setOnAction(openTabEvent -> o.openTab(t));
        o.addButton(b);
        o.addTab(t);


        c.getDeleteButton().setOnAction(deleteDataEvent -> {
            o.removeButton(b);
            o.removeTab(t);
            d.delete(accountId, fileId);
        });
        c.getSaveButton().setOnAction(saveDataEvent -> d.store(accountId, fileId, c.toString()));
    }

    private void link(Assistant a, SkillEditor e, Data d, Overview o, String accountId, String fileId) {
        final Tab t = new Tab("", e);
        final Button b = e.getTitleTracker();
        b.setOnAction(openTabEvent -> o.openTab(t));
        o.addButton(b);
        o.addTab(t);

        a.addSkill(e);

        e.getDeleteButton().setOnAction(deleteDataEvent -> {
            o.removeButton(b);
            o.removeTab(t);
            d.delete(accountId, fileId);
            a.removeSkill(e);
        });
        e.getSaveButton().setOnAction(saveDataEvent -> d.store(accountId, fileId, e.toString()));
    }
}
