import engine.Assistant;
import engine.Chat;
import engine.GrammarEditor;
import engine.SkillEditor;
import gui.Borders;
import gui.Designer;
import gui.Factory;
import gui.Window;
import io.Generator;
import io.Reader;
import io.Writer;
import ivp.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import nlp.Dataset;
import nlp.GrammarModel;
import nlp.Jazzy;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.JavaFXFrameConverter;

import java.io.File;
import java.util.List;
import java.util.Stack;

public final class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        final var assistant = new Assistant();
        final var jazzy     = new Jazzy();
        final var camera    = new CamController();
        final var detector  = new FaceDetector();
        final var generator = new Generator();
        final var reader    = new Reader();
        final var writer    = new Writer();
        final var designer  = new Designer();
        final var factory   = new Factory();
        final var chat      = new Chat(factory, designer);
        final var dataset   = new Dataset(factory, designer, generator, reader, writer);
        final var grammar   = new GrammarEditor(factory, designer, assistant);
        final var skills    = new SkillEditor(factory, designer, assistant);
        final var detection = new DetectionStatus(factory, designer);
        final var window    = new Window(factory);
        final var scene =  factory.createScene(window.getPanel(), 960, 960);
        primaryStage.setTitle("ASSISTANT");
        primaryStage.setScene(scene);
        primaryStage.show();

        final var showChat = factory.createButton("CHAT", 100, 30, Double.MAX_VALUE, 30);
        showChat.setOnAction(event -> window.show(showChat, chat.getPanel()));

        final var showData = factory.createButton("DATASET", 100, 30, Double.MAX_VALUE, 30);
        showData.setOnAction(event -> window.show(showData, dataset.getPanel()));

        final var showGrammar = factory.createButton("GRAMMAR", 100, 30, Double.MAX_VALUE, 30);
        showGrammar.setOnAction(event -> window.show(showGrammar, grammar.getPanel()));

        final var showSkills = factory.createButton("SKILLS", 100, 30, Double.MAX_VALUE, 30);
        showSkills.setOnAction(event -> window.show(showSkills, skills.getPanel()));

        final var showDetection = factory.createButton("DETECTION", 100, 30, Double.MAX_VALUE, 30);
        showDetection.setOnAction(event -> window.show(showDetection, detection.getPanel()));

        window.add(showChat, showData, showGrammar, showSkills, showDetection);
        window.show(showData, dataset.getPanel());
        designer.setBorder(Borders.GRAY, showChat, showData, showGrammar, showSkills, showDetection);

        final Stack<String> messages = new Stack<>();
        final Thread conversation = new Thread(() -> {
            System.out.println("@CONVERSATION: Started to loop...");
            while (primaryStage.isShowing()) {
                if (messages.empty()) continue;
                final String in = messages.pop();
                final String out = assistant.respond(in);
                Platform.runLater(() -> chat.registerAssistantMessage(factory, out));
            }
            System.out.println("@CONVERSATION: Stopped looping...");
        });
        conversation.start();

        final Stack<String> spellCheckRequests = new Stack<>();
        final Thread spellChecker = new Thread(() -> {
            System.out.println("@SPELL_CHECKER: Starting to loop...");
            while (primaryStage.isShowing()) {
                if (spellCheckRequests.empty()) continue;
                final String in = spellCheckRequests.pop();
                final List<String>  misspelled = jazzy.checkSpelling(in);
                Platform.runLater(() -> {
                    if (misspelled.isEmpty()) {
                        chat.registerAssistantMessage(factory, "No words were misspelled in " + "\"" + in + "\"");
                    } else {
                        chat.registerAssistantMessage(factory, "Following words were misspelled in " + "\"" + in + "\"");
                        for (int i = 0; i < misspelled.size(); i++) chat.registerAssistantMessage(factory,  "(" + (i + 1) + ")" + misspelled.get(i));
                    }
                });
            }
            System.out.println("@SPELL_CHECKER: Stopped looping...");
        });
        spellChecker.start();

        final File nlpModel     = new File("src/main/python/nlp.py");
        final File nlpTerminate = new File("src/main/resources/nlp/model.terminate");
        final File nlpTrain     = new File("src/main/resources/nlp/model.train");
        final File nlpPredict   = new File("src/main/resources/nlp/model.predict");
        final File nlpDataset   = new File("src/main/resources/nlp/data.csv");
        final File nlpInput     = new File("src/main/resources/nlp/input.txt");
        final File nlpOutput    = new File("src/main/resources/nlp/output.txt");
        final var grammarModel  = new GrammarModel(generator, reader, writer, nlpModel, nlpTerminate, nlpTrain, nlpPredict, nlpDataset, nlpInput, nlpOutput);
        if (grammarModel.start()) grammarModel.train();
        dataset.load(nlpDataset);
        dataset.setOnSaved(nlpTrain);

        final Stack<String> grammarCheckRequests = new Stack<>();
        final Thread grammarChecker = new Thread(() -> {
            System.out.println("@GRAMMAR_CHECKER: Starting to loop...");
            while (primaryStage.isShowing()) {
                if (grammarCheckRequests.empty()) continue;
                else if (!grammarModel.isRunning()) grammarModel.start();
                final String in = grammarCheckRequests.pop();
                final String quoted = "\"" + in + "\"";
                final int out = grammarModel.predict(in);
                switch (out) {
                    case -1:
                        Platform.runLater(() -> chat.registerAssistantMessage(factory, "Failed to predict whether " + quoted + " has correct grammar within allocated time."));
                        break;
                    case 0:
                        Platform.runLater(() -> chat.registerAssistantMessage(factory, quoted + " is not phrased correctly."));
                        break;
                    case 1:
                        Platform.runLater(() -> chat.registerAssistantMessage(factory, quoted + " is phrased correctly."));
                        break;
                    default:
                        Platform.runLater(() -> chat.registerAssistantMessage(factory, "Contact tech support as grammar module did not yield a valid response."));
                        break;
                }
            }
            System.out.println("@GRAMMAR_CHECKER: Stopped looping...");
        });
        grammarChecker.start();

        final long delaySeconds = 31;
        final long[] delayStartTime = new long[1];
        final long[] remainingSeconds = new long[1];
        final long[] previousCheckpoint = new long[1];
        final Stack<Frame>  frames   = new Stack<>();
        final Runnable resetDetectionDelay = () -> {
            remainingSeconds[0] = delaySeconds;
            previousCheckpoint[0] = delaySeconds;
            delayStartTime[0] = System.currentTimeMillis();
        };
        final Thread faceDetection = new Thread(() -> {
            final var converter  = new JavaFXFrameConverter();
            final Image[] images = new Image[1];
            final Runnable captureNotification = () -> detection.setText("Detecting...");
            final Runnable successNotification = () -> detection.setText("Detected successfully!");
            final Runnable failureNotification = () -> detection.setText("No one detected!");
            final Runnable delayNotification   = () -> detection.setText("Detecting in " + remainingSeconds[0] + " seconds.");
            final Runnable showUsedPicture     = () -> detection.setImage(images[0]);
            System.out.println("@FACE_DETECTION: Starting to loop...");
            previousCheckpoint[0] = delaySeconds;
            delayStartTime[0] = System.currentTimeMillis();
            while(primaryStage.isShowing()) {
                remainingSeconds[0] = delaySeconds - (System.currentTimeMillis() - delayStartTime[0]) / 1000;
                if (remainingSeconds[0] <= 0) {
                    Platform.runLater(captureNotification);
                    final var frame = camera.takePicture();
                    final boolean detected = detector.detectFace(frame);
                    if (detected) Platform.runLater(successNotification);
                    else Platform.runLater(failureNotification);
                    images[0] = converter.convert(frame);
                    Platform.runLater(showUsedPicture);
                    resetDetectionDelay.run();
                    if (detected) frames.push(frame);
                } else if (remainingSeconds[0] < previousCheckpoint[0]) {
                    Platform.runLater(delayNotification);
                    previousCheckpoint[0] = remainingSeconds[0];
                }
            }
            System.out.println("@FACE_DETECTION: Stopped looping...");
        });
        faceDetection.start();

        final File ivpModel     = new File("src/main/python/ivp.py");
        final File ivpTerminate = new File("src/main/resources/ivp/model.terminate");
        final File ivpPredict   = new File("src/main/resources/ivp/model.predict");
        final File ivpDataset   = new File("src/main/resources/ivp/database");
        final File ivpInput     = new File("src/main/resources/ivp/input.jpg");
        final File ivpOutput    = new File("src/main/resources/ivp/output.txt");
        final var faceRecognition = new FaceRecognition(generator, reader, ivpModel, ivpTerminate, ivpPredict, ivpDataset, ivpInput, ivpOutput);
        faceRecognition.start();

        // Todo: Check what happens in the python process when
        //      a) the image database is empty
        //      b) no image is provided
        //      c) an unknown face is provided
        //       Then the question on how to include multi-user
        //       using face recognition can be answered.
        final Thread faceChecker = new Thread(() -> {
            final var saver = new FrameSaver();
            System.out.println("@FACE_CHECKER: Starting to loop...");
            while (primaryStage.isShowing()) {
                if (frames.empty()) continue;
                final var frame = frames.pop();
                saver.save(ivpInput, frame);
                if (!faceRecognition.isRunning()) faceRecognition.start();
                final String out = faceRecognition.predict();
                Platform.runLater(() -> chat.registerAssistantMessage(factory, "Recognized " + out));
            }
            System.out.println("@FACE_CHECKER: Stopped looping...");
        });
        faceChecker.start();

        chat.setOnKeyPressed(event -> {
            if (event.getCode() != KeyCode.ENTER) return;
            final var textField = (TextField) event.getSource();
            final String in = textField.getText();
            textField.clear();

            if (in.isBlank()) return;
            chat.registerUserMessage(factory, in);
            messages.push(in);
            spellCheckRequests.push(in);
            grammarCheckRequests.push(in);
        });

        chat.setChangeListener(((observable, oldValue, newValue) -> resetDetectionDelay.run()));
        scene.setOnMouseMoved(event -> resetDetectionDelay.run());
        scene.setOnKeyPressed(event -> resetDetectionDelay.run());
    }
}