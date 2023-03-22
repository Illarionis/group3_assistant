package gui;

import engine.AuthorizationHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public final class LogIn extends VBox {
    public LogIn(AuthorizationHandler registrationHandler, AuthorizationHandler loginHandler) {
        // Username related operations
        final Label usernameLabel = getLabel("Username:");
        final TextField usernameField = getUsernameField();
        final HBox usernameBox = encapsulateHorizontally(usernameLabel, usernameField);

        // Password related operations
        final Label passwordLabel = getLabel("Password:");
        final TextField passwordField = getPasswordField();
        final HBox passwordBox = encapsulateHorizontally(passwordLabel, passwordField);

        // Buttons to register and login
        final Button register = getButton("Sign-Up");
        final Button login = getButton("Sign-In");
        final HBox buttonBox = encapsulateHorizontally(register, login);
        buttonBox.setPrefHeight(60);
        buttonBox.setSpacing(30);

        // Configuring this object
        getChildren().addAll(usernameBox, passwordBox, buttonBox);
        setAlignment(Pos.CENTER);
        setSpacing(5.0);

        // Defining alerts
        final Alert usernameIsEmptyAlert = getErrorAlert("Username Error", "You did not provide a username.", "Please, type a username!");
        final Alert passwordIsEmptyAlert = getErrorAlert("Password Error", "You did not provide a password.", "Please, type a password");

        // Defining the registration event
        register.setOnAction(event -> {
            if (usernameField.getText().isBlank()) {
                usernameIsEmptyAlert.showAndWait();
            } else if (passwordField.getText().isBlank()) {
                passwordIsEmptyAlert.showAndWait();
            } else if (registrationHandler != null && registrationHandler.handle(usernameField.getText(), passwordField.getText())) {
                usernameField.clear();
                passwordField.clear();
            }
        });

        // Defining the login event
        login.setOnAction(event -> {
            if (usernameField.getText().isBlank()) {
                usernameIsEmptyAlert.showAndWait();
            } else if (passwordField.getText().isBlank()) {
                passwordIsEmptyAlert.showAndWait();
            } else if (loginHandler != null && loginHandler.handle(usernameField.getText(), passwordField.getText())) {
                usernameField.clear();
                passwordField.clear();
            }
        });
    }

    private HBox encapsulateHorizontally(Node... nodes) {
        final HBox box = new HBox();
        box.getChildren().addAll(nodes);
        box.setAlignment(Pos.CENTER);
        box.setPrefSize(300, 30);
        box.setSpacing(1.0);
        return box;
    }

    private Alert getErrorAlert(String title, String header, String content) {
        final Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(header);
        a.setContentText(content);
        return a;
    }

    private Button getButton(String s) {
        final Button b = new Button(s);
        b.setPrefSize(90, 30);
        return b;
    }

    private Label getLabel(String s) {
        final Label l = new Label(s);
        l.setAlignment(Pos.CENTER_RIGHT);
        l.setPrefSize(90, 30);
        return l;
    }

    private PasswordField getPasswordField() {
        final PasswordField f = new PasswordField();
        f.setPrefSize(210, 30);
        return f;
    }

    private TextField getUsernameField() {
        final TextField f = new TextField();
        f.setPrefSize(210, 30);
        return f;
    }
}
