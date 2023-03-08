package gui;

import design.engine.Assistant;
import design.gui.Session;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public final class UserSession<Input, Output> implements Session<Input, Output> {
    @Override
    public void start(Stage s, Assistant<Input, Output> a) {
        ChatManager<Input, Output> manager = new ChatManager<>(a);
        var overview = new SkillOverview(300);
        var pane = new BorderPane();
        pane.setLeft(overview);
        pane.setCenter(manager);

        Scene conversation = new Scene(pane, 640, 640);
        s.setScene(conversation);
        if (!s.isShowing()) {
            s.show();
        }
    }
}
