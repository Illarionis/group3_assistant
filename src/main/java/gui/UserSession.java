package gui;

import design.engine.Assistant;
import design.gui.Session;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public final class UserSession<Input, Output> implements Session<Input, Output> {
    @Override
    public void start(Stage s, Assistant<Input, Output> a) {
        // Creating a bordered layout for the main scene/screen
        BorderPane pane = new BorderPane();

        // Creating the side menu
        SideMenu menu = new SideMenu(100);
        pane.setLeft(menu);

        // Creating the chat window
        ChatManager<Input, Output> manager = new ChatManager<>(a);

        // Setting the chat window as the centered object of the border layout
        pane.setCenter(manager);

        // Showing the scene
        Scene conversation = new Scene(pane, 640, 640);
        s.setScene(conversation);
        if (!s.isShowing()) {
            s.show();
        }
    }
}
