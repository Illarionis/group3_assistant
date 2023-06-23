package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public final class LoginWindow extends Window {
    private final Button unlock;
    public LoginWindow(Factory factory) {
        super(factory);
        unlock = factory.createButton("Click to unlock or wait for face detection to start.", 360, 360, 360, 360);
        panel.getChildren().add(unlock);
        factory.update(Borders.GRAY_OVERLAY, unlock);
    }

    public void setButtonText(String s) {
        unlock.setText(s);
    }

    public void setOnButtonClicked(EventHandler<ActionEvent> handler) {
        unlock.setOnAction(handler);
    }
}
