package core;

import design.Assistant;
import design.DataFile;
import engine.DigitalAssistant;
import gui.Overview;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;

import java.time.LocalDateTime;

public final class Session extends HBox {
    private static final Database CHATS = new Database("/.data/chats/");
    private static final Database SKILLS = new Database("/.data/skills/");

    public Session(String accountId) {
        final DigitalAssistant a = new DigitalAssistant();

        final Overview chats = new Overview("CONVERSATIONS", "CHAT");
        chats.setPrefSize(300, 600);

        final Button newChatButton = chats.getFootnoteButton();
        newChatButton.setText("+");
        newChatButton.setOnAction(newTabEvent -> {
            final DataFile f = CHATS.createNewFile(accountId);
            final Chat c = new Chat();
            addChatToOverview(f, c, chats);
            connectChatWithAssistant(accountId, c, a);
        });

        final Overview skills = new Overview("SKILLS", "EDIT");
        skills.setPrefSize(300, 600);

        final Button newSkillButton = skills.getFootnoteButton();
        newSkillButton.setText("+");
        newSkillButton.setOnAction(newTabEvent -> {
            final DataFile f = SKILLS.createNewFile(accountId);
            final Skill s = new Skill();
            addSkillToOverview(f, s, skills, a);
            a.addSkill(s);
        });

        final DataFile[] savedChats = CHATS.getFiles(accountId);
        for (DataFile file : savedChats) {
            final String snapshot = file.getData();
            final Chat c = new Chat(snapshot);
            addChatToOverview(file, c, chats);
            connectChatWithAssistant(accountId, c, a);
        }

        final DataFile[] savedSkills = SKILLS.getFiles(accountId);
        for (DataFile file : savedSkills) {
            final String snapshot = file.getData();
            final Skill s = new Skill(snapshot);
            addSkillToOverview(file, s, skills, a);
            a.addSkill(s);
        }

        getChildren().addAll(skills, chats);
        setAlignment(Pos.CENTER);
        setPrefSize(600, 600);
    }

    private void addChatToOverview(DataFile f, Chat c, Overview o) {
        final Tab t = o.createTab();
        t.setContent(c);

        final Button b = o.createButton();
        b.setOnAction(clickEvent -> o.openTab(t));

        c.getTitleField().textProperty().addListener((observable, oldValue, newValue) -> b.setText(newValue));

        c.getDeleteButton().setOnAction(deleteEvent -> {
            o.removeButton(b);
            o.removeTab(t);
            f.deleteData();
        });

        c.getSaveButton().setOnAction(saveEvent -> {
            final String snapshot = c.snapshot();
            f.storeData(snapshot);
        });

        o.addButton(b);
        o.addTab(t);
    }

    private void addSkillToOverview(DataFile f, Skill s, Overview o, Assistant a) {
        final Tab t = o.createTab();
        t.setContent(s);

        final Button b = o.createButton();
        b.setOnAction(clickEvent -> o.openTab(t));

        s.getInputField().textProperty().addListener((observable, oldValue, newValue) -> b.setText(newValue));

        s.getDeleteButton().setOnAction(deleteEvent -> {
            o.removeButton(b);
            o.removeTab(t);
            a.removeSkill(s);
            f.deleteData();
        });

        s.getSaveButton().setOnAction(saveEvent -> {
            final String snapshot = s.snapshot();
            f.storeData(snapshot);
        });
    }

    private void connectChatWithAssistant(String accountId, Chat c, DigitalAssistant a) {
        c.getMessageField().setOnKeyPressed(keyPressedEvent -> {
            if (keyPressedEvent.getCode() == KeyCode.ENTER) {
                final String s = c.getMessageField().getText();
                if (s == null || s.isBlank()) {
                    return;
                }
                LocalDateTime date = LocalDateTime.now();
                final ChatMessage userMsg = new ChatMessage(s, date.toString(), accountId);
                c.addMessage(userMsg);

                date = LocalDateTime.now();
                final String response = a.getResponse(s);
                final ChatMessage systemMsg = new ChatMessage(response, date.toString(), "SYSTEM");
                c.addMessage(systemMsg);
            }
        });
    }
}
