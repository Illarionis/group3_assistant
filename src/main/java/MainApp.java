import engine.Assistant;
import engine.Grammar;
import engine.Jazzy;
import engine.faceDetection.CamController;
import engine.faceDetection.FaceDetector;
import gui.ChatWindow;
import gui.EditorWindow;
import gui.Factory;
import gui.LoginWindow;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.util.Stack;

public final class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        final var assistant = new Assistant();
        final var camera    = new CamController();
        final var detector  = new FaceDetector();
        final var grammar   = new Grammar();
        final var jazzy     = new Jazzy();
        final var factory   = new Factory();
        final var chat      = new ChatWindow(factory);
        final var login     = new LoginWindow(factory);
        final var editor    = new EditorWindow(factory, assistant);

        final boolean[] runBackgroundThreads = {true};
        final long detectionDelaySeconds = 30;
        final long startCheckpoint = 31;
        final long[] detectionStartTime = {System.currentTimeMillis()};
        final long[] previousCheckpoint = {startCheckpoint};
        final Stack<String> unchecked = new Stack<>();
        final Stack<String> unpredicted = new Stack<>();
        final Stack<String> unanswered = new Stack<>();

        final Stage secondaryStage = new Stage();
        secondaryStage.initStyle(StageStyle.UNDECORATED);
        secondaryStage.setTitle("ASSISTANT EDITOR");
        secondaryStage.setScene(editor.getScene());

        final double[] secondaryPreviousCoordinates = new double[2];
        final EventHandler<MouseEvent> secondaryTitlePressHandler = event -> {
            secondaryPreviousCoordinates[0] = event.getScreenX();
            secondaryPreviousCoordinates[1] = event.getScreenY();
        };
        final EventHandler<MouseEvent> secondaryTitleDragHandler = event -> {
            final double x = secondaryStage.getX() + event.getScreenX() - secondaryPreviousCoordinates[0];
            final double y = secondaryStage.getY() + event.getScreenY() - secondaryPreviousCoordinates[1];
            secondaryPreviousCoordinates[0] = event.getScreenX();
            secondaryPreviousCoordinates[1] = event.getScreenY();
            secondaryStage.setX(x);
            secondaryStage.setY(y);
        };
        editor.setOnCloseClicked(event -> {
            detectionStartTime[0] = System.currentTimeMillis();
            previousCheckpoint[0] = startCheckpoint;
            secondaryStage.close();
        });
        editor.setOnTitlePressed(secondaryTitlePressHandler);
        editor.setOnTitleDragged(secondaryTitleDragHandler);

        final EventHandler<ActionEvent> closePrimaryStageHandler = event -> {
            final var closeWindowEvent = new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST);
            primaryStage.getScene().getWindow().fireEvent(closeWindowEvent);
            secondaryStage.close();
        };

        final double[] previousPrimaryCoordinates = new double[2];
        final EventHandler<MouseEvent> primaryTitlePressHandler = event -> {
            previousPrimaryCoordinates[0] = event.getScreenX();
            previousPrimaryCoordinates[1] = event.getScreenY();
        };
        final EventHandler<MouseEvent> primaryTitleDragHandler = event -> {
            final double x = primaryStage.getX() + event.getScreenX() - previousPrimaryCoordinates[0];
            final double y = primaryStage.getY() + event.getScreenY() - previousPrimaryCoordinates[1];
            previousPrimaryCoordinates[0] = event.getScreenX();
            previousPrimaryCoordinates[1] = event.getScreenY();
            primaryStage.setX(x);
            primaryStage.setY(y);
        };

        final Runnable enterLoginScene = () -> {
            primaryStage.setScene(login.getScene());
            primaryStage.centerOnScreen();
        };
        final Runnable enterChatScene = () -> {
            primaryStage.setScene(chat.getScene());
            primaryStage.centerOnScreen();
        };

        chat.setOnCloseClicked(closePrimaryStageHandler);
        chat.setOnTitleDragged(primaryTitleDragHandler);
        chat.setOnTitlePressed(primaryTitlePressHandler);
        chat.setOnSettingsPressed(event -> {
            if (secondaryStage.isShowing()) return;
            secondaryStage.show();
        });

        login.setOnButtonClicked(event -> enterChatScene.run());
        login.setOnCloseClicked(closePrimaryStageHandler);
        login.setOnTitleDragged(primaryTitleDragHandler);
        login.setOnTitlePressed(primaryTitlePressHandler);

        primaryStage.setOnCloseRequest(event -> {
            runBackgroundThreads[0] = false;
            grammar.terminate();
        });
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("DIGITAL ASSISTANT");
        primaryStage.setScene(login.getScene());
        primaryStage.show();

        if (grammar.start()) System.out.println("Successfully started the grammar process in the background.");
        else throw new IllegalStateException("Failed to start grammar process in the background.");

        final Thread faceDetectionInvoker = new Thread(() -> {
            while(runBackgroundThreads[0]) {
                if (secondaryStage.isShowing()) continue;
                final long remainingSeconds = detectionDelaySeconds - (System.currentTimeMillis() - detectionStartTime[0]) / 1000;
                if (remainingSeconds < previousCheckpoint[0] && remainingSeconds > 0 && remainingSeconds <= 10 && primaryStage.getScene() == chat.getScene()) {
                    Platform.runLater(() -> chat.registerAssistantMessage(factory, "Taking a picture to detect your presence in " + remainingSeconds + " seconds."));
                    previousCheckpoint[0] = remainingSeconds;
                } else if (remainingSeconds < previousCheckpoint[0] && remainingSeconds > 0 && primaryStage.getScene() == login.getScene()) {
                    Platform.runLater(() -> login.setButtonText("Click to unlock or wait " + remainingSeconds + " seconds for face detection to start."));
                    previousCheckpoint[0] = remainingSeconds;
                } else if (remainingSeconds <= 0){
                    Platform.runLater(() -> {
                        if (primaryStage.getScene() == chat.getScene()) chat.registerAssistantMessage(factory, "Taking a picture!");
                        else login.setButtonText("Taking a picture!");
                    });
                    final var frame = camera.takePicture();
                    final boolean detected = detector.detectFace(frame);
                    if (detected)
                        Platform.runLater(() -> {
                            if (primaryStage.getScene() == chat.getScene()) {
                                chat.registerAssistantMessage(factory, "Detected your presence");
                                chat.registerAssistantMessage(factory, "Will check again after " + detectionDelaySeconds + " seconds.");
                            }
                            else enterChatScene.run();

                        });
                    else
                        Platform.runLater(() -> {
                            if (primaryStage.getScene() == chat.getScene()) {
                                chat.registerAssistantMessage(factory, "Failed to detect your presence. Therefore, locking the application.");
                                enterLoginScene.run();
                            }
                        });
                    detectionStartTime[0] = System.currentTimeMillis();
                    previousCheckpoint[0] = startCheckpoint;
                }
            }
        });
        detectionStartTime[0] = System.currentTimeMillis();
        faceDetectionInvoker.start();

        final Thread spellCheckInvoker = new Thread(() -> {
            while(runBackgroundThreads[0]) {
                if (unchecked.empty()) continue;
                final String input = unchecked.pop();
                final String quoted = "\"" + input + "\"";
                Platform.runLater(() -> chat.registerAssistantMessage(factory, "Checking spelling for " + quoted));
                final var misspelled = jazzy.checkSpelling(input);
                if (misspelled.size() == 0) Platform.runLater(() -> {
                    chat.registerAssistantMessage(factory,"Did not find any misspelled words in " + quoted);
                    unpredicted.push(input);
                });
                else Platform.runLater(() -> {
                    chat.registerAssistantMessage(factory, "Found misspelled words in " + quoted);
                    chat.registerAssistantMessage(factory, "Following words are misspelled:");
                    for (String s : misspelled) chat.registerAssistantMessage(factory, s);
                });
            }
        });
        spellCheckInvoker.start();

        final Thread grammarCheckInvoker = new Thread(() -> {
            while(runBackgroundThreads[0]) {
                if (unpredicted.empty()) continue;
                final String input = unpredicted.pop();
                final String quoted = "\"" + input + "\"";
                Platform.runLater(() -> chat.registerAssistantMessage(factory, "Checking grammar for " + quoted));
                if (!grammar.isRunning()) grammar.start();
                final boolean valid = grammar.recognize(input);
                if (valid)
                    Platform.runLater(() -> chat.registerAssistantMessage(factory, "No grammar mistakes found in " + quoted));
                else
                    Platform.runLater(() -> chat.registerAssistantMessage(factory, "Failed to recognize grammar of " + quoted));
                unanswered.add(input);
            }
        });
        grammarCheckInvoker.start();

        final Thread conversationInvoker = new Thread(() -> {
            while(runBackgroundThreads[0]) {
                if (unanswered.empty()) continue;
                final String input = unanswered.pop();
                final String output = assistant.respond(input);
                Platform.runLater(() -> chat.registerAssistantMessage(factory, output));
            }
        });
        conversationInvoker.start();

        chat.setInputChangeListener(((observable, oldValue, newValue) -> {
            detectionStartTime[0] = System.currentTimeMillis();
            previousCheckpoint[0] = startCheckpoint;
        }));

        final EventHandler<KeyEvent> keyPressedHandler = event -> {
            if (event.getCode() != KeyCode.ENTER) return;
            final var textField = (TextField) event.getSource();
            final String input = textField.getText();

            textField.clear();
            if (input.isBlank()) return;
            chat.registerUserMessage(factory, input);

            unchecked.add(input);;

            detectionStartTime[0] = System.currentTimeMillis();
            previousCheckpoint[0] = startCheckpoint;
        };
        chat.setOnKeyPressedHandler(keyPressedHandler);
    }
}