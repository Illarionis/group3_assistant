import engine.Assistant;
import gui.Chat;
import gui.ChatWindow;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        final var assistant = new Assistant();
        final var chat = new Chat();
        final var chatWindow = new ChatWindow(assistant, chat);

        final var scene = new Scene(chatWindow);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Digital Assistant");
        primaryStage.show();
    }
}