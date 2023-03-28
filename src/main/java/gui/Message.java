package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public final class Message extends HBox {
    private final Text contentHolder;
    private final TextFlow contentFlow;

    private String content, senderId;
    private Message() {
        contentHolder = new Text();
        contentHolder.setTextAlignment(TextAlignment.CENTER);

        contentFlow = new TextFlow();
        contentFlow.getChildren().add(contentHolder);
        contentFlow.setPadding(new Insets(5, 5, 5, 5));

        getChildren().add(contentFlow);
    }

    public Message(String content, String senderId) {
        this();
        this.content = content;
        this.senderId = senderId;
        onConstructionFinished();
    }

    public Message(String s) {
        this();
        final String[] segments = s.split("\t\r\n");
        content = segments[0];
        senderId = segments[1];
        onConstructionFinished();
    }

    private void onConstructionFinished() {
        final boolean fromUser = !senderId.equals("Assistant");
        final Color c;
        if (fromUser) {
            c = Color.rgb(220, 248, 198);
            contentFlow.setTextAlignment(TextAlignment.RIGHT);
            setAlignment(Pos.CENTER_RIGHT);
        } else {
            c = Color.rgb(255, 255, 255);
            contentFlow.setTextAlignment(TextAlignment.LEFT);
            setAlignment(Pos.CENTER_LEFT);
        }
        contentFlow.setBackground(Background.fill(c));
        contentHolder.setText(content);
    }

    @Override
    public String toString() {
        return content + "\t\r\n" + senderId;
    }
}
