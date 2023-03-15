import core.SecuritySystem;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class MainApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        final var security = new SecuritySystem(primaryStage);
        final var startScene = new Scene(security, 300, 600);
        primaryStage.setScene(startScene);
        primaryStage.show();
    }
}
