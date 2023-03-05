package gui;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

final class SideMenu extends VBox {
    public SideMenu(double width) {
        BackgroundFill fill = new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY);
        Background bg = new Background(fill);
        setBackground(bg);

        // Creating and adding a button to open the skill editor
        Button openEditorButton = new Button("Add a skill");
        openEditorButton.setPrefWidth(width);
        openEditorButton.setOnAction(event -> {
            Stage newStage = new Stage();
            Scene newScene = new Scene(new SkillEditor(), 300, 600);
            newStage.setScene(newScene);
            newStage.show();
        });
        getChildren().add(openEditorButton);
    }
}
