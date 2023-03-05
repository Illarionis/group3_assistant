package gui;

import design.engine.Assistant;
import design.gui.Session;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public final class UserSession<Input, Output> implements Session<Input, Output> {
    @Override
    public void start(Stage s, Assistant<Input, Output> a) {
        // Creating a bordered layout for the main scene/screen
        BorderPane pane = new BorderPane();

        // Creating the skill editor.
        var editor = new SkillEditor();

        // Setting the desired (max) width of the editor
        editor.setPrefWidth(300);

        // Setting a background to the editor.
        var fill = new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY);
        var background = new Background(fill);
        editor.setBackground(background);

        // Setting the editor on the left of the main window.
        pane.setLeft(editor);

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
