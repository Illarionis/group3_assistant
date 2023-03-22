/**

package gui;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class LogIn extends Application{

    TextField password=new TextField();
    PasswordField username=new PasswordField();
    Label passw=new Label("Password: ");
    Label usern=new Label("Username: ");
    Button login=new Button("Log In");
    VBox frame = new VBox();

    public String[] getContent(){
        String name = password.getText();
        String password = username.getText();
        String[] infos=new String[]{};
        infos[0]=name;
        infos[1]=password;
        return infos;
    }

    //Compare password entered and infos we have stored
    public String Compare(){
        //find user
        //unsalt
        //compare
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        HBox passwordbox=new HBox();
        passwordbox.getChildren().add(passw);
        passwordbox.setAlignment(Pos.CENTER);
        passwordbox.setSpacing(5);
        passwordbox.getChildren().add(password);


        HBox usernamebox=new HBox();
        usernamebox.getChildren().add(usern);
        usernamebox.setAlignment(Pos.CENTER);
        usernamebox.setSpacing(5);
        usernamebox.getChildren().add(username);

        frame.getChildren().add(usernamebox);
        frame.setAlignment(Pos.CENTER);
        frame.setSpacing(50);
        frame.getChildren().add(passwordbox);
        frame.setAlignment(Pos.CENTER);
        frame.setSpacing(50);
        frame.getChildren().add(login);

        login.setOnAction(event -> {
            if (password.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No text entered");
                alert.setContentText("Please enter some text.");
                alert.showAndWait();
            } else {
                try (CSVWriter writer = new CSVWriter(new FileWriter("data.csv", true))) {
                    String[] data = {text};
                    writer.writeNext(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                password.clear();
            }
        });

        login.setOnAction(event -> {
            if (username.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No text entered");
                alert.setContentText("Please enter some text.");
                alert.showAndWait();
            } else {
                String text = username.getText();
                try (CSVWriter writer = new CSVWriter(new FileWriter("data.csv", true))) {
                    String[] data = {text};
                    writer.writeNext(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                username.clear();
            }
        });

        Scene scene = new Scene (frame, 300, 300);
        primaryStage.setTitle("LogInPrompt");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }

}
 **/