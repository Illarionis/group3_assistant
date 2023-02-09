package desktop;

import gui.DesktopLayout;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        Scene s = new Scene(new DesktopLayout(), 1280, 960);
        stage.setScene(s);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
