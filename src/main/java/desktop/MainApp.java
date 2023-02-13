package desktop;

import engine.DigitalAssistant;
import gui.ChatSession;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        var assistant = new DigitalAssistant();
        var session = new ChatSession<String, String>();
        session.start(stage, assistant);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
