package gui;

import design.engine.Assistant;
import design.gui.Session;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class ChatSession<Input, Output> implements Session<Input, Output> {
    @Override
    public void start(Stage s, Assistant<Input, Output> a) {
        ChatManager<Input, Output> manager = new ChatManager<>(a);
        Scene conversation = new Scene(manager, 640, 640);
        s.setScene(conversation);
        if (!s.isShowing()) {
            s.show();
        }
    }
}
