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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Requirements to run the grammar machine learning model
        final File nlpModel     = new File("src/main/python/nlp.py");
        final File nlpTerminate = new File("src/main/resources/nlp/model.terminate");
        final File nlpTrain     = new File("src/main/resources/nlp/model.train");
        final File nlpPredict   = new File("src/main/resources/nlp/model.predict");
        final File nlpDataset   = new File("src/main/resources/nlp/data.csv");
        final File nlpInput     = new File("src/main/resources/nlp/input.txt");
        final File nlpOutput    = new File("src/main/resources/nlp/output.txt");

        // Requirements to run the face recognition model
        final File ivpModel     = new File("/src/main/python/ivp.py");
        final File ivpTerminate = new File("/src/main/resources/ivp/model.terminate");
        final File ivpPredict   = new File("/src/main/resources/ivp/model.predict");
        final File ivpDataset = new File("src/main/resources/ivp/database");
        final File ivpInput   = new File("/src/main/resources/ivp/input.jpg");
        final File ivpOutput = new File("/src/main/resources/ivp/output.txt");

        // ...
        final var assistant = new Assistant();
        final var jazzy     = new Jazzy();
        final var camera    = new CamController();
        final var detector  = new FaceDetector();
        final var saver     = new FrameSaver();
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
        final var profiles  = new ProfileCollection(generator, factory, designer, camera, saver, ivpDataset);
        final var window    = new Window(factory);
        final var scene =  factory.createScene(window.getPanel(), 960, 960);

        // Provides the multitasking solution
        final ExecutorService service = Executors.newFixedThreadPool(5);
        primaryStage.setOnCloseRequest(event -> service.shutdown());

        // Configuring the application
        primaryStage.setTitle("ASSISTANT");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Defining window tabs
        final var showChat = factory.createButton("CHAT", 100, 30, Double.MAX_VALUE, 30);
        final var showData = factory.createButton("DATASET", 100, 30, Double.MAX_VALUE, 30);
        final var showGrammar = factory.createButton("GRAMMAR", 100, 30, Double.MAX_VALUE, 30);
        final var showSkills = factory.createButton("SKILLS", 100, 30, Double.MAX_VALUE, 30);
        final var showDetection = factory.createButton("DETECTION", 100, 30, Double.MAX_VALUE, 30);
        final var showUsers = factory.createButton("USERS", 100, 30, Double.MAX_VALUE, 30);

        // Configuring the interactions the user has with window
        showChat.setOnAction(event -> window.show(showChat, chat.getPanel()));
        showData.setOnAction(event -> window.show(showData, dataset.getPanel()));
        showGrammar.setOnAction(event -> window.show(showGrammar, grammar.getPanel()));
        showSkills.setOnAction(event -> window.show(showSkills, skills.getPanel()));
        showDetection.setOnAction(event -> window.show(showDetection, detection.getPanel()));
        showUsers.setOnAction(event -> window.show(showUsers, profiles.getPanel()));

        // Completing the window setup
        window.add(showChat, showData, showGrammar, showSkills, showDetection, showUsers);
        window.show(showData, dataset.getPanel());
        designer.setBorder(Borders.GRAY, showChat, showData, showGrammar, showSkills, showDetection, showUsers);

        // Requirement to hold a conversation with the assistant
        final Stack<String> messages = new Stack<>();

        // Defining how the assistant converses
        final Runnable conversation = () -> {
            System.out.println("@CONVERSATION: Started to loop...");
            while (primaryStage.isShowing()) {
                if (messages.empty()) continue;
                final String in = messages.pop();
                final String out = assistant.respond(in);
                Platform.runLater(() -> chat.registerAssistantMessage(factory, out));
            }
            System.out.println("@CONVERSATION: Stopped looping...");
        };

        // Requirement to run spell checks
        final Stack<String> spellCheckRequests = new Stack<>();

        // Defining how spelling is getting checked
        final Runnable spellChecker = () -> {
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
                        for (int i = 0; i < misspelled.size(); i++) chat.registerAssistantMessage(factory,  "(" + (i + 1) + ") " + misspelled.get(i));
                    }
                });
            }
            System.out.println("@SPELL_CHECKER: Stopped looping...");
        };

        // Grammar machine learning model
        final var grammarModel  = new GrammarModel(generator, reader, writer, nlpModel, nlpTerminate, nlpTrain, nlpPredict, nlpDataset, nlpInput, nlpOutput);

        // Loading training data into the gui
        dataset.load(nlpDataset);
        dataset.setOnSaved(nlpTrain);

        // Requirement to run the grammar checks
        final Stack<String> grammarCheckRequests = new Stack<>();

        // Defining how grammar is getting checked
        final Runnable grammarChecker = () -> {
            if (nlpPredict.exists() && !nlpPredict.delete()) throw new IllegalStateException("Failed to delete nlp prediction request!");
            else if (nlpTerminate.exists() && !nlpTerminate.delete()) throw new IllegalStateException("Failed to delete nlp termination request!");

            System.out.println("@GRAMMAR_CHECKER: Training model...");
            grammarModel.train();
            final long startTime = System.currentTimeMillis();
            while (nlpTrain.exists()) {
                if (!grammarModel.isRunning()) grammarModel.start();

                final long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime > 30000) {
                    System.out.println("@GRAMMAR_CHECKER: Failed to train model within allocated time...");
                    grammarModel.terminate();
                    return;
                }
            }

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
            grammarModel.terminate();
        };

        // Face recognition model
        final FaceRecognition recognitionModel = new FaceRecognition(generator, reader, ivpModel, ivpTerminate, ivpPredict, ivpDataset, ivpInput, ivpOutput);

        // Requirement to run face recognition
        final Stack<Frame> frames = new Stack<>();

        // Requirements to run face detections
        final boolean[] manualDetectRequest = {false};
        final long delaySeconds = 31;
        final long[] delayStartTime = new long[1];
        final long[] remainingSeconds = new long[1];
        final long[] previousCheckpoint = new long[1];
        final Runnable resetDetectionDelay = () -> {
            remainingSeconds[0] = delaySeconds;
            previousCheckpoint[0] = delaySeconds;
            delayStartTime[0] = System.currentTimeMillis();
        };

        // ...
        detection.setOnClick(event -> manualDetectRequest[0] = true);

        // Defining how the face is getting detected
        final Runnable faceDetection = () -> {
            final var converter  = new JavaFXFrameConverter();
            final Image[] images = new Image[1];
            final Runnable captureNotification = () -> detection.setButtonText("Detecting...");
            final Runnable successNotification = () -> detection.setButtonText("Detected successfully!");
            final Runnable failureNotification = () -> detection.setButtonText("No one detected!");
            final Runnable delayNotification   = () -> detection.setButtonText("Detecting in " + remainingSeconds[0] + " seconds.");
            final Runnable showUsedPicture     = () -> detection.setImage(images[0]);
            System.out.println("@FACE_DETECTION: Starting to loop...");
            previousCheckpoint[0] = delaySeconds;
            delayStartTime[0] = System.currentTimeMillis();
            while(primaryStage.isShowing()) {
                remainingSeconds[0] = delaySeconds - (System.currentTimeMillis() - delayStartTime[0]) / 1000;
                if (remainingSeconds[0] <= 0 || manualDetectRequest[0]) {
                    Platform.runLater(captureNotification);
                    final var frame = camera.takePicture();
                    final boolean detected = detector.detectFace(frame);
                    if (detected) Platform.runLater(successNotification);
                    else Platform.runLater(failureNotification);
                    images[0] = converter.convert(frame);
                    Platform.runLater(showUsedPicture);
                    if (detected) frames.push(frame);
                    else Platform.runLater(() -> detection.setImageLabelText("No one detected..."));
                    manualDetectRequest[0] = false;
                    resetDetectionDelay.run();
                } else if (remainingSeconds[0] < previousCheckpoint[0]) {
                    Platform.runLater(delayNotification);
                    previousCheckpoint[0] = remainingSeconds[0];
                }
            }
            System.out.println("@FACE_DETECTION: Stopped looping...");
        };

        // Defining how the face is getting recognized.
        final Runnable faceRecognition = () -> {
            if (ivpPredict.exists() && !ivpPredict.delete()) throw new IllegalStateException("Failed to delete ivp prediction request!");
            else if (ivpTerminate.exists() && !ivpTerminate.delete()) throw new IllegalStateException("Failed to delete ivp termination request!");

            System.out.println("@FACE_RECOGNITION: Starting to loop...");
            while (primaryStage.isShowing()) {
                if (frames.empty()) continue;
                else if (!recognitionModel.isRunning()) recognitionModel.start();
                final var frame = frames.pop();
                saver.save(ivpInput, frame);
                generator.generate(ivpPredict);
                final String out = recognitionModel.predict();
                if (out.isBlank()) Platform.runLater(() -> detection.setImageLabelText("Failed to recognize..."));
                else Platform.runLater(() -> detection.setImageLabelText("Recognized: " + out));
            }

            System.out.println("@FACE_RECOGNITION: Stopped looping...");
            recognitionModel.terminate();
        };

        // Assigning tasks that can ran simultaneously
        service.submit(conversation);
        service.submit(spellChecker);
        service.submit(grammarChecker);
        service.submit(faceDetection);
        service.submit(faceRecognition);

        // Defining how the chat works
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

        // Resetting the inactivity timers whenever the uses moves the mouse or presses a key
        scene.setOnMouseMoved(event -> resetDetectionDelay.run());
        scene.setOnKeyPressed(event -> resetDetectionDelay.run());

        // Printing the python command
        System.out.println(
                "NLP Python Command:\n" +
                "python "
                        + nlpModel.getAbsolutePath() + " "
                        + nlpTerminate.getAbsolutePath() + " "
                        + nlpTrain.getAbsolutePath() + " "
                        + nlpPredict.getAbsolutePath() + " "
                        + nlpDataset.getAbsolutePath() + " "
                        + nlpInput.getAbsolutePath() + " "
                        + nlpOutput.getAbsolutePath()
        );

        // Printing the python command
        System.out.println(
                "IVP Python Command:\n" +
                        "python "
                        + ivpModel.getAbsolutePath() + " "
                        + ivpTerminate.getAbsolutePath() + " "
                        + ivpPredict.getAbsolutePath() + " "
                        + ivpDataset.getAbsolutePath() + " "
                        + ivpInput.getAbsolutePath() + " "
                        + ivpOutput.getAbsolutePath()
        );
    }
}