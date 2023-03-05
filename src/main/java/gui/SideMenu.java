package gui;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

final class SideMenu extends VBox {
    public SideMenu() {
        BackgroundFill fill = new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY);
        Background bg = new Background(fill);
        setBackground(bg);

        //adding the button to go to the skill editor
       /*  Button skillButton = new Button("Skill Editor");
       skillButton.setOnAction(event -> {
            Stage newStage = new Stage();
            Scene newScene = new Scene(new SkillEditor());
            newStage.setScene(newScene);
            newStage.show();
        });

        SideMenu.getChildren().add(skillButton);
        Scene scene = new Scene(SideMenu, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();*/

    }
}
