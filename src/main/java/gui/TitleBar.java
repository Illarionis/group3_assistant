package gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public final class TitleBar implements Displayable, Styleable {
    private final Label titleHolder;
    private final Button closeStage, maximizeStage, minimizeStage, switchColorTheme, switchScene;
    private final HBox panel;

    public TitleBar(Factory f, Stage s) {
        final String maximizeSymbol = Character.toString(128470);
        final String overlapSymbol = Character.toString(128471);

        // Providing a label to display the tile
        titleHolder = f.createLabel("DIGITAL ASSISTANT");
        titleHolder.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(titleHolder, Priority.ALWAYS);

        // Providing a button to close the stage
        closeStage = new Button(Character.toString(10005));
        closeStage.setBackground(Background.EMPTY);
        closeStage.setMinSize(50, 50);

        // Providing a button to maximize the stage
        maximizeStage = new Button();
        maximizeStage.setBackground(Background.EMPTY);
        maximizeStage.setMinSize(50, 50);
        if (s.isMaximized()) maximizeStage.setText(overlapSymbol);
        else maximizeStage.setText(maximizeSymbol);

        // Providing a button to minimize the stage
        minimizeStage = new Button(Character.toString(8212));
        minimizeStage.setBackground(Background.EMPTY);
        minimizeStage.setMinSize(50, 50);

        // Providing a box to display the stage controls horizontally
        final HBox stageControls = f.createHBox(minimizeStage, maximizeStage, closeStage);

        // Providing a button to switch color themes
        switchColorTheme = new Button();
        switchColorTheme.setBackground(Background.EMPTY);
        switchColorTheme.setMinSize(50, 50);

        // Providing a button to switch scenes
        switchScene = new Button();
        switchScene.setBackground(Background.EMPTY);
        switchScene.setMinSize(100, 50);

        // Providing a box to display the app controls horizontally
        final HBox appControls = f.createHBox(switchColorTheme, switchScene);

        // Providing the final displayable panel
        panel = f.createHBox(appControls, titleHolder, stageControls);

        // Providing relocation functionality on a mouse drag of the title
        final double[] coordinates = new double[2];
        titleHolder.setOnMousePressed(pressEvent -> {
            coordinates[0] = pressEvent.getScreenX();
            coordinates[1] = pressEvent.getScreenY();
        });
        titleHolder.setOnMouseDragged(dragEvent -> {
            double x = s.getX() + dragEvent.getScreenX() - coordinates[0];
            double y = s.getY() + dragEvent.getScreenY() - coordinates[1];
            coordinates[0] = dragEvent.getScreenX();
            coordinates[1] = dragEvent.getScreenY();
            s.setX(x);
            s.setY(y);
        });

        // Providing the close functionality
        closeStage.setOnAction(clickEvent -> {
            final var closeWindowEvent = new WindowEvent(s, WindowEvent.WINDOW_CLOSE_REQUEST);
            s.getScene().getWindow().fireEvent(closeWindowEvent);
        });

        // Providing the maximize functionality
        final Runnable maximizeProcess = () -> {
            if (s.isMaximized()) {
                s.setMaximized(false);
                maximizeStage.setText(maximizeSymbol);
            } else {
                s.setMaximized(true);
                maximizeStage.setText(overlapSymbol);
            }
        };
        maximizeStage.setOnAction(clickEvent -> maximizeProcess.run());

        // Providing the minimize functionality
        minimizeStage.setOnAction(clickEvent -> s.setIconified(true));

        // Assigning visual effects to the close stage button
        closeStage.setOnMouseEntered(enterEvent -> closeStage.setBackground(Effects.RED_OVERLAY_BACKGROUND));
        closeStage.setOnMouseExited(exitEvent -> closeStage.setBackground(Background.EMPTY));

        // Assigning visual effects to the maximize stage button
        maximizeStage.setOnMouseEntered(enterEvent -> maximizeStage.setBackground(Effects.GRAY_OVERLAY_BACKGROUND));
        maximizeStage.setOnMouseExited(exitEvent -> maximizeStage.setBackground(Background.EMPTY));

        // Assigning visual effects to the minimize stage button
        minimizeStage.setOnMouseEntered(enterEvent -> minimizeStage.setBackground(Effects.GRAY_OVERLAY_BACKGROUND));
        minimizeStage.setOnMouseExited(exitEvent -> minimizeStage.setBackground(Background.EMPTY));

        // Assigning visual effects to the switch color theme button
        switchColorTheme.setOnMouseEntered(enterEvent -> switchColorTheme.setBackground(Effects.GRAY_OVERLAY_BACKGROUND));
        switchColorTheme.setOnMouseExited(exitEvent -> switchColorTheme.setBackground(Background.EMPTY));

        // Assigning visual effects to the switch scene button
        switchScene.setOnMouseEntered(enterEvent -> switchScene.setBackground(Effects.GRAY_OVERLAY_BACKGROUND));
        switchScene.setOnMouseExited(exitEvent -> switchScene.setBackground(Background.EMPTY));
    }

    public Button getSwitchColorThemeButton() {
        return switchColorTheme;
    }

    public Button getSwitchSceneButton() {
        return switchScene;
    }

    public void setSwitchColorThemeEvent(Runnable process) {
        switchColorTheme.setOnAction(clickEvent -> process.run());
    }

    public void setSwitchSceneEvent(Runnable process) {
        switchScene.setOnAction(clickEvent -> process.run());
    }

    @Override
    public Region getPanel() {
        return panel;
    }

    @Override
    public void setBorder(Border b) {
        panel.setBorder(b);
    }

    @Override
    public void setStyle(String s) {
        closeStage.setStyle(s);
        maximizeStage.setStyle(s);
        minimizeStage.setStyle(s);
        switchColorTheme.setStyle(s);
        switchScene.setStyle(s);
        titleHolder.setStyle(s);
    }
}
