import engine.Assistant;
import gui.NodeGenerator;
import gui.RegionDesigner;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;

public final class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        /* *
         * Acknowledgements (or say warnings):
         * 1. Most naming conventions are somewhat reverted (e.g., 'buttonClose' instead of 'closeButton'),
         *    which is mainly to make searching more convenient due to large number declared variables
         * 2. Mainly of use functional programming instead of OOP
         * */

        // Resource loading
        final var scrollPaneCSS = getClass().getResource("/transparent-scroll-pane.css");

        // Resource validation
        if (scrollPaneCSS == null) throw new IllegalStateException("Failed to fetch css file for scroll panes!");

        // Loading stylesheets
        final String scrollPaneStylesheet = scrollPaneCSS.toExternalForm();

        // Inline stylesheets
        final String textStyleBrightMode = "-fx-text-fill: rgb(30, 30, 30); -fx-prompt-text-fill: -fx-text-fill;";
        final String textStyleDarkMode   = "-fx-text-fill: rgb(180, 180, 180); -fx-prompt-text-fill: -fx-text-fill;";
        final String textStyleWarning    = "-fx-text-fill: rgb(255, 120, 150);";

        // Assistant
        final var assistant = new Assistant();

        // Insets
        final Insets padding = new Insets(5, 5, 5, 5);

        // Constant reference to scenes and their root nodes
        final Scene[] scenes     = new Scene[3]; // scenes[0]     => start scene      | scenes[1]     => chat scene      | scenes[2]     => editor scene
        final VBox[]  sceneRoots = new VBox[3];  // sceneRoots[0] => start scene root | sceneRoots[1] => chat scene root | sceneRoots[2] => editor scene root

        // Scene sizes (width x height)
        final int[] sizeStartScene  = {360, 360};
        final int[] sizeChatScene   = {360, 720};
        final int[] sizeEditorScene = {720, 720};

        // Symbols
        final String symbolMultiplication = Character.toString(10005);
        final String symbolDash           = Character.toString(8212);
        final String symbolSun            = Character.toString(9728);
        final String symbolMoon           = Character.toString(127769);
        final String symbolMemo           = Character.toString(128221);
        final String symbolSpeechBubble   = Character.toString(128490);
        final String symbolRightArrow     = Character.toString(8594);

        // Color preferences
        final Color colorBrightMode   = Color.rgb(180, 180, 180);
        final Color colorDarkMode     = Color.rgb(30, 30, 30);

        // Backgrounds
        final Background backgroundAssistantMessage       = Background.fill(Color.rgb(150, 180, 200));
        final Background backgroundUserMessage            = Background.fill(Color.rgb(150, 200, 180));
        final Background backgroundButtonDefaultHighlight = Background.fill(Color.rgb(120, 120, 120, 0.5));
        final Background backgroundButtonGreenHighlight   = Background.fill(Color.rgb(120, 255, 150));
        final Background backgroundButtonRedHighlight     = Background.fill(Color.rgb(255, 120, 150));

        // Borders
        final Border borderBrightMode = Border.stroke(colorDarkMode);
        final Border borderDarkMode   = Border.stroke(colorBrightMode);

        // Color theme switchers
        final List<Action> actionsBrightModeSwitch = new ArrayList<>();
        final List<Action> actionsDarkModeSwitch    = new ArrayList<>();

        // Color theme events
        final ActionEvent eventEnterBrightMode = new ActionEvent();
        final ActionEvent eventEnterDarkMode   = new ActionEvent();

        // Event handler to switch color themes
        final EventHandler<ActionEvent> handlerSwitchColorTheme = event -> {
            final List<Action> actions;
            if (event == eventEnterBrightMode) actions = actionsBrightModeSwitch;
            else                               actions = actionsDarkModeSwitch;
            for (var action : actions) action.execute();
        };

        // Event handlers to highlight buttons
        final EventHandler<MouseEvent> handlerButtonDefaultHighlight = event -> {
            final var region = (Region) event.getSource();
            region.setBackground(backgroundButtonDefaultHighlight);
        };
        final EventHandler<MouseEvent> handlerButtonGreenHighlight = event -> {
            final var region = (Region) event.getSource();
            region.setBackground(backgroundButtonGreenHighlight);
        };
        final EventHandler<MouseEvent> handlerButtonRedHighlight = event -> {
            final var region = (Region) event.getSource();
            region.setBackground(backgroundButtonRedHighlight);
        };
        final EventHandler<MouseEvent> handlerButtonResetHighlight = event -> {
            final var region = (Region) event.getSource();
            region.setBackground(Background.EMPTY);
        };

        // Event handlers to relocate the primary stage through dragging
        final double[] coordinatesPreviousLocation = new double[2];
        final EventHandler<MouseEvent> handlerStartRelocation = event -> {
            coordinatesPreviousLocation[0] = event.getScreenX();
            coordinatesPreviousLocation[1] = event.getScreenY();
        };
        final EventHandler<MouseEvent> handlerCompleteRelocation = event -> {
            final double x = primaryStage.getX() + event.getScreenX() - coordinatesPreviousLocation[0];
            final double y = primaryStage.getY() + event.getScreenY() - coordinatesPreviousLocation[1];
            coordinatesPreviousLocation[0] = event.getScreenX();
            coordinatesPreviousLocation[1] = event.getScreenY();
            primaryStage.setX(x);
            primaryStage.setY(y);
        };

        // Utilities to reduce the number of duplicated lines when designing the GUI
        final var designer = new RegionDesigner();
        final var generator = new NodeGenerator();

        // Building the modifiable title bar
        final Button buttonCloseStage    = new Button(symbolMultiplication);
        final Button buttonMinimizeStage = new Button(symbolDash);
        final Button buttonSwitchScene   = new Button(symbolMemo);
        final Button buttonSwitchTheme   = new Button(symbolMoon);
        final Label  title               = generator.createLabel("DIGITAL ASSISTANT", Pos.CENTER);
        final HBox   stageControls       = generator.createHorizontalBox(0, Insets.EMPTY, Pos.CENTER, buttonMinimizeStage, buttonCloseStage);
        final HBox   appControls         = generator.createHorizontalBox(0, Insets.EMPTY, Pos.CENTER, buttonSwitchTheme, buttonSwitchScene);
        final HBox   titleBar            = generator.createHorizontalBox(0, Insets.EMPTY, Pos.CENTER, appControls, title, stageControls);

        // Completing the design of the title bar
        designer.setBackground(Background.EMPTY, buttonCloseStage, buttonMinimizeStage, buttonSwitchScene, buttonSwitchTheme);
        designer.setMaxWidth(Double.MAX_VALUE, buttonCloseStage, buttonMinimizeStage, buttonSwitchScene, buttonSwitchTheme, title);
        designer.setPrefWidth(50, buttonCloseStage, buttonMinimizeStage, buttonSwitchScene, buttonSwitchTheme);
        designer.setPrefWidth(100, stageControls, appControls);
        designer.setPrefHeight(50, buttonCloseStage, buttonMinimizeStage, buttonSwitchScene, buttonSwitchTheme, title);
        HBox.setHgrow(title, Priority.ALWAYS);

        // Assigning visual effects to the title bar buttons
        designer.setOnMouseEntered(handlerButtonRedHighlight, buttonCloseStage);
        designer.setOnMouseEntered(handlerButtonDefaultHighlight, buttonMinimizeStage, buttonSwitchScene, buttonSwitchTheme);
        designer.setOnMouseExited(handlerButtonResetHighlight, buttonCloseStage, buttonMinimizeStage, buttonSwitchScene, buttonSwitchTheme);

        // Assigning click event handlers to the title bar buttons
        buttonCloseStage.setOnAction(event -> {
            // Todo; If needed, log-out (safely) once the authorization are completed
            primaryStage.close();
        });
        buttonMinimizeStage.setOnAction(event -> primaryStage.setIconified(true));
        buttonSwitchScene.setOnAction(event -> {
            if (primaryStage.getScene() == scenes[1]) {
                sceneRoots[1].getChildren().remove(titleBar);
                sceneRoots[2].getChildren().add(0, title);
                primaryStage.setScene(scenes[2]);
                buttonSwitchScene.setText(symbolSpeechBubble);
            } else if (primaryStage.getScene() == scenes[2]) {
                sceneRoots[2].getChildren().remove(titleBar);
                sceneRoots[1].getChildren().add(0, titleBar);
                primaryStage.setScene(scenes[1]);
                buttonSwitchScene.setText(symbolMemo);
            }
            primaryStage.centerOnScreen();
        });
        buttonSwitchTheme.setOnAction(event -> {
            final String s = buttonSwitchTheme.getText();
            if (s.equals(symbolSun)) {
                buttonSwitchTheme.setText(symbolMoon);
                handlerSwitchColorTheme.handle(eventEnterDarkMode);
            } else if (s.equals(symbolMoon)) {
                buttonSwitchTheme.setText(symbolSun);
                handlerSwitchColorTheme.handle(eventEnterBrightMode);
            }
        });

        // Assigning stage relocation on title drag
        title.setOnMousePressed(handlerStartRelocation);
        title.setOnMouseDragged(handlerCompleteRelocation);

        // Providing support for a bright mode title bar
        actionsBrightModeSwitch.add(() -> {
            designer.setBorder(borderBrightMode, titleBar);
            designer.setStyle(textStyleBrightMode, buttonCloseStage, buttonMinimizeStage, buttonSwitchScene, buttonSwitchTheme, title);
        });

        // Providing support for a dark mode title bar
        actionsDarkModeSwitch.add(() -> {
            designer.setBorder(borderDarkMode, titleBar);
            designer.setStyle(textStyleDarkMode, buttonCloseStage, buttonMinimizeStage, buttonSwitchScene, buttonSwitchTheme, title);
        });

        // Building the chat
        final VBox       chatHistory  = generator.createVerticalBox(5, padding, Pos.TOP_CENTER);
        final ScrollPane viewportChat = generator.createScrollPane(scrollPaneStylesheet, chatHistory);
        final TextField  chatInput    = generator.createTextField("Click to type...");
        sceneRoots[1]                 = generator.createVerticalBox(3, Insets.EMPTY, Pos.CENTER, viewportChat, chatInput);

        // Completing the design of the chat
        designer.setBackground(Background.EMPTY, chatInput);
        VBox.setVgrow(viewportChat, Priority.ALWAYS);

        // Assigning functionality to the chat input text field
        chatInput.setOnKeyPressed(event -> {
            if (event.getCode() != KeyCode.ENTER) return;

            // Completing user-assistant interaction
            final String   input  = chatInput.getText();
            final String   output = assistant.respond(input);

            // Building messages
            final Text     inputText      = new Text(input);
            final Text     outputText     = new Text(output);
            final TextFlow inputTextFlow  = new TextFlow(inputText);
            final TextFlow outputTextFlow = new TextFlow(outputText);
            final HBox     inputBox       = generator.createHorizontalBox(0, Insets.EMPTY, Pos.CENTER_RIGHT, inputTextFlow);
            final HBox     outputBox      = generator.createHorizontalBox(0, Insets.EMPTY, Pos.CENTER_LEFT, outputTextFlow);

            // Completing message designs
            designer.setBackground(backgroundAssistantMessage,  outputTextFlow);
            designer.setBackground(backgroundUserMessage, inputTextFlow);
            designer.setMaxWidth(240, inputTextFlow, outputTextFlow);
            inputTextFlow.setPadding(padding);
            outputTextFlow.setPadding(padding);

            // Adding messages
            chatHistory.getChildren().addAll(inputBox, outputBox);

            // Todo?: Provide support for bright and dark themed messages
        });

        // Providing support for bright mode chat
        actionsBrightModeSwitch.add(() -> {
            designer.setBorder(borderBrightMode, viewportChat, chatInput);
            designer.setStyle(textStyleBrightMode, chatInput);
        });

        // Providing support for dark mode chat
        actionsDarkModeSwitch.add(() -> {
            designer.setBorder(borderDarkMode, viewportChat, chatInput);
            designer.setStyle(textStyleDarkMode, chatInput);
        });

        // Todo: Create editor panel (aka sceneRoots[2])

        // Todo: Create authorization panel (aka sceneRoots[0])

        // Completing design of scene roots
        // Todo: Complete design of every scene
        designer.setBackground(Background.EMPTY, sceneRoots[1]);

        // Creating scenes
        // Todo: Create every scene
        scenes[1] = new Scene(sceneRoots[1], sizeChatScene[0], sizeChatScene[1]);

        // Providing support for bright mode scene
        actionsBrightModeSwitch.add(() -> {
            // Todo: Add support for every scene
            scenes[1].setFill(colorBrightMode);
        });

        // Providing support for dark mode scene
        actionsDarkModeSwitch.add(() -> {
            // Todo: Add support for every scene
            scenes[1].setFill(colorDarkMode);
        });

        // Selecting initial color scheme
        handlerSwitchColorTheme.handle(eventEnterDarkMode);

        // Todo: Remove statement below if authorization panel will contain the title bar
        sceneRoots[1].getChildren().add(0, titleBar);

        // Preparing the stage
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Digital Assistant");

        // Selecting initial scene
        // Todo: Switch to scenes[0] once every scene and their functionality is completed
        primaryStage.setScene(scenes[1]);

        // Showing the application
        primaryStage.show();
    }

    private interface Action {
        void execute();
    }
}