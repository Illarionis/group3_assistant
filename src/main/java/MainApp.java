import engine.Assistant;
import gui.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public final class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Assistant
        final var assistant = new Assistant();

        // Utility
        final var factory = new Factory();

        // Control flags
        final boolean[] enterDarkMode = new boolean[1];

        // Backgrounds
        final Background assistantMessageBackground = Background.fill(Color.rgb(150, 180, 200));
        final Background userMessageBackground      = Background.fill(Color.rgb(150, 200, 180));

        // Color schemes
        final Color brightModeColor = Color.WHITE;
        final Color darkModeColor   = Color.rgb(30, 30, 30);

        // Borders
        final Border brightModeBorder = Border.stroke(darkModeColor);
        final Border darkModeBorder = Border.stroke(brightModeColor);

        // Insets
        final Insets padding = new Insets(5, 5, 5, 5);

        // Inline CSS
        final String brightModeTextStyle = "-fx-font: 14px arial; -fx-text-fill: rgb(30, 30, 30); -fx-prompt-text-fill: rgb(120, 120, 120);";
        final String darkModeTextStyle   = "-fx-font: 14px arial; -fx-text-fill: rgb(210, 210, 210); -fx-prompt-text-fill: rgb(120, 120, 120);";

        // Symbols
        final String sunSymbol      = Character.toString(9728);
        final String moonSymbol     = Character.toString(127769);
        final String editorModeText = "ASSISTANT MODE: " + Character.toString(128221);
        final String chatModeText   = "ASSISTANT MODE: " + Character.toString(128490);

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                    Chat                                                   *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
        final var chat      = new Chat(factory);
        chat.setBackgrounds(assistantMessageBackground, userMessageBackground);
        chat.setAssistant(assistant);

        final var chatPanel = chat.getPanel();
        chatPanel.setMinWidth(500);
        HBox.setHgrow(chatPanel, Priority.ALWAYS);

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                  Detection                                                *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
        final var faceDetection             = new FaceDetection(factory);
        final var terminateDetectionProcess = faceDetection.getTerminationProcess();
        final var detectionBackgroundThread = faceDetection.getBackgroundThread();
        final var faceDetectionPanel        = faceDetection.getPanel();
        faceDetectionPanel.setMinWidth(500);
        HBox.setHgrow(faceDetectionPanel, Priority.ALWAYS);

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                  Overviews                                                *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
        final var skillOverview   = new Overview(factory);
        skillOverview.setTitle("SKILL");

        final var skillOverviewPanel = skillOverview.getPanel();
        skillOverviewPanel.setMinWidth(250);
        skillOverviewPanel.setMaxWidth(250);

        final var grammarOverview = new Overview(factory);
        grammarOverview.setTitle("GRAMMAR");

        final var grammarOverviewPanel = grammarOverview.getPanel();
        grammarOverviewPanel.setMinWidth(250);
        grammarOverviewPanel.setMaxWidth(250);

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                    Skill                                                  *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
        final var skillEditor = new SkillEditor(factory);
        skillEditor.setAssistant(assistant);
        skillEditor.setOverview(skillOverview);

        final var skillEditorPanel = skillEditor.getPanel();
        skillEditorPanel.setMinWidth(500);
        HBox.setHgrow(skillEditorPanel, Priority.ALWAYS);

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                    Grammar                                                *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
        final var grammarEditor = new GrammarEditor(factory);
        grammarEditor.setAssistant(assistant);
        grammarEditor.setOverview(grammarOverview);

        final var grammarEditorPanel = grammarEditor.getPanel();
        grammarEditorPanel.setMinWidth(500);
        HBox.setHgrow(grammarEditorPanel, Priority.ALWAYS);

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                    Title                                                  *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
        final var titleBar = new TitleBar(factory, primaryStage);
        final var titleBarPanel = titleBar.getPanel();
        HBox.setHgrow(titleBarPanel, Priority.ALWAYS);

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                    Scenes                                                 *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
        final HBox chatScenePanel = factory.createHBox(10, faceDetectionPanel, chatPanel);
        VBox.setVgrow(chatScenePanel, Priority.ALWAYS);

        final VBox chatSceneRoot = factory.createVBox(10, padding, chatScenePanel);
        chatSceneRoot.setBackground(Background.EMPTY);

        final Scene chatScene = new Scene(chatSceneRoot, 1200, 900);

        final HBox editorScenePanel = factory.createHBox(10, skillOverviewPanel, skillEditorPanel, grammarEditorPanel, grammarOverviewPanel);
        VBox.setVgrow(editorScenePanel, Priority.ALWAYS);

        final VBox editorSceneRoot = factory.createVBox(10, padding, editorScenePanel);
        editorSceneRoot.setBackground(Background.EMPTY);

        final Scene editorScene = new Scene(editorSceneRoot, 1600, 900);

        // Providing the switch to chat scene process
        final Runnable enterChatSceneProcess = () -> {
            editorSceneRoot.getChildren().remove(titleBarPanel);
            chatSceneRoot.getChildren().add(0, titleBarPanel);
            primaryStage.setScene(chatScene);
            primaryStage.centerOnScreen();
            titleBar.getSwitchSceneButton().setText(chatModeText);
        };

        // Providing the switch to editor scene process
        final Runnable enterEditorSceneProcess = () -> {
            chatSceneRoot.getChildren().remove(titleBarPanel);
            editorSceneRoot.getChildren().add(0, titleBarPanel);
            primaryStage.setScene(editorScene);
            primaryStage.centerOnScreen();
            titleBar.getSwitchSceneButton().setText(editorModeText);
        };


        // Providing the switch scene functionality
        titleBar.setSwitchSceneEvent(() -> {
            if (primaryStage.getScene() == chatScene) enterEditorSceneProcess.run();
            else if (primaryStage.getScene() == editorScene) enterChatSceneProcess.run();
        });

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                  Color Theme                                              *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
        final Runnable enterBrightModeProcess = () -> {
            // Updating borders
            chat.setBorder(brightModeBorder);
            faceDetection.setBorder(brightModeBorder);
            skillOverview.setBorder(brightModeBorder);
            grammarOverview.setBorder(brightModeBorder);
            skillEditor.setBorder(brightModeBorder);
            grammarEditor.setBorder(brightModeBorder);
            titleBar.setBorder(brightModeBorder);

            // Updating text styles
            chat.setStyle(brightModeTextStyle);
            faceDetection.setStyle(brightModeTextStyle);
            skillOverview.setStyle(brightModeTextStyle);
            grammarOverview.setStyle(brightModeTextStyle);
            skillEditor.setStyle(brightModeTextStyle);
            grammarEditor.setStyle(brightModeTextStyle);
            titleBar.setStyle(brightModeTextStyle);

            // Updating scene background colors
            chatScene.setFill(brightModeColor);
            editorScene.setFill(brightModeColor);

            // Updating color theme icon
            titleBar.getSwitchColorThemeButton().setText(sunSymbol);

            // Updating the color theme boolean flag
            enterDarkMode[0] = true;
        };

        final Runnable enterDarkModeProcess = () -> {
            // Updating borders
            chat.setBorder(darkModeBorder);
            faceDetection.setBorder(darkModeBorder);
            skillOverview.setBorder(darkModeBorder);
            grammarOverview.setBorder(darkModeBorder);
            skillEditor.setBorder(darkModeBorder);
            grammarEditor.setBorder(darkModeBorder);
            titleBar.setBorder(darkModeBorder);

            // Updating text styles
            chat.setStyle(darkModeTextStyle);
            faceDetection.setStyle(darkModeTextStyle);
            skillOverview.setStyle(darkModeTextStyle);
            grammarOverview.setStyle(darkModeTextStyle);
            skillEditor.setStyle(darkModeTextStyle);
            grammarEditor.setStyle(darkModeTextStyle);
            titleBar.setStyle(darkModeTextStyle);

            // Updating scene background colors
            chatScene.setFill(darkModeColor);
            editorScene.setFill(darkModeColor);

            // Updating color theme icon
            titleBar.getSwitchColorThemeButton().setText(moonSymbol);

            // Updating the color theme boolean flag
            enterDarkMode[0] = false;
        };

        // Selecting initial color scheme
        enterBrightModeProcess.run();

        // Assigning the switch color scheme functionality
        titleBar.setSwitchColorThemeEvent(() -> {
            if (enterDarkMode[0]) enterDarkModeProcess.run();
            else enterBrightModeProcess.run();
        });

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                   Stage                                                   *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

        // Selecting the initial scene
        enterChatSceneProcess.run();

        // Removing the original title bar
        primaryStage.initStyle(StageStyle.UNDECORATED);

        // Providing the termination condition of the background thread
        primaryStage.setOnCloseRequest(closeEvent -> {
            terminateDetectionProcess.run();
            primaryStage.close();
        });

        // Showing the stage
        primaryStage.show();

        // Starting the background thread
        detectionBackgroundThread.start();
    }
}