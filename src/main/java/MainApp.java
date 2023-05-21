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
        final String textStyleBrightMode = "-fx-font: 14 arial; -fx-text-fill: rgb(30, 30, 30); -fx-prompt-text-fill: -fx-text-fill;";
        final String textStyleDarkMode   = "-fx-font: 14 arial; -fx-text-fill: rgb(180, 180, 180); -fx-prompt-text-fill: -fx-text-fill;";
        final String textStyleWarning    = "-fx-text-fill: rgb(255, 120, 150);";

        // Back-end variables
        final var assistant = new Assistant();
        final var rules     = new ArrayList<Rule>();

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

        // Reference to keep track what the current theme is
        final ActionEvent[] themeEventHistory = new ActionEvent[1];

        // Event handler to switch color themes
        final EventHandler<ActionEvent> handlerSwitchColorTheme = event -> {
            final List<Action> actions;
            if (event == eventEnterBrightMode) actions = actionsBrightModeSwitch;
            else                               actions = actionsDarkModeSwitch;
            for (var action : actions) action.execute();
            themeEventHistory[0] = event;
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

        // Assigning stage relocation on title drag
        title.setOnMousePressed(handlerStartRelocation);
        title.setOnMouseDragged(handlerCompleteRelocation);

        // Assigning click event handlers to the title bar buttons
        buttonCloseStage.setOnAction(event -> {
            // Todo; If needed, log-out (safely) once the authorization are completed
            primaryStage.close();
        });
        buttonMinimizeStage.setOnAction(event -> primaryStage.setIconified(true));
        buttonSwitchScene.setOnAction(event -> {
            if (primaryStage.getScene() == scenes[1]) {
                sceneRoots[1].getChildren().remove(titleBar);
                sceneRoots[2].getChildren().add(0, titleBar);
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

        // Building the chat
        final VBox       chatHistory  = generator.createVerticalBox(5, padding, Pos.TOP_CENTER);
        final ScrollPane viewportChat = generator.createScrollPane(scrollPaneStylesheet, chatHistory);
        final TextField  chatInput    = generator.createTextField("Click to type...");
        sceneRoots[1]                 = generator.createVerticalBox(3, padding, Pos.CENTER, viewportChat, chatInput);

        // Completing the design of the chat
        designer.setBackground(Background.EMPTY, chatInput);
        VBox.setVgrow(viewportChat, Priority.ALWAYS);

        // Providing support for bright mode chat
        actionsBrightModeSwitch.add(() -> {
            designer.setBorder(borderBrightMode, viewportChat, chatInput, sceneRoots[1]);
            designer.setStyle(textStyleBrightMode, chatInput);
        });

        // Providing support for dark mode chat
        actionsDarkModeSwitch.add(() -> {
            designer.setBorder(borderDarkMode, viewportChat, chatInput, sceneRoots[1]);
            designer.setStyle(textStyleDarkMode, chatInput);
        });

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

        // Todo: Create editor panel (aka sceneRoots[2])
        // Building the rules panel of the editor
        final Button     buttonNewRule = new Button("+");
        final Label      ruleLabel     = generator.createLabel("Rules in Chomsky-Normal Form", Pos.CENTER);
        final VBox       ruleHolder    = generator.createVerticalBox(5, padding, Pos.TOP_CENTER);
        final ScrollPane viewportRules = generator.createScrollPane(scrollPaneStylesheet, ruleHolder);
        final VBox       rulePanel     = generator.createVerticalBox(3, padding, Pos.CENTER, ruleLabel, viewportRules, buttonNewRule);

        // Completing the design of the rule panel
        designer.setBackground(Background.EMPTY, buttonNewRule);
        designer.setMaxWidth(Double.MAX_VALUE, buttonNewRule, ruleLabel);
        designer.setPrefHeight(30, ruleLabel);
        VBox.setVgrow(viewportRules, Priority.ALWAYS);

        // Providing support for bright mode editing
        actionsBrightModeSwitch.add(() -> {
            designer.setBorder(borderBrightMode, buttonNewRule, viewportRules, ruleLabel, rulePanel);
            designer.setStyle(textStyleBrightMode, buttonNewRule, ruleLabel);
        });

        // Providing support for dark mode editing
        actionsDarkModeSwitch.add(() -> {
            designer.setBorder(borderDarkMode, buttonNewRule, viewportRules, ruleLabel, rulePanel);
            designer.setStyle(textStyleDarkMode, buttonNewRule, ruleLabel);
        });

        // Assigning visual effects to the new rule button
        designer.setOnMouseEntered(handlerButtonGreenHighlight, buttonNewRule);
        designer.setOnMouseExited(handlerButtonResetHighlight, buttonNewRule);

        // Defining how new rules are generated
        final RuleGenerator ruleGenerator = (lhs, rhs) -> {
            // Building a new front-end rule
            final Button buttonDeleteRule = new Button(symbolMultiplication);
            final Label  labelWarning     = generator.createLabel("", Pos.CENTER);
            final Label  labelImplication = generator.createLabel(symbolRightArrow, Pos.CENTER);
            final TextField textFieldLHS  = generator.createTextField("1 non-terminal");
            final TextField textFieldRHS  = generator.createTextField("1 terminal or 2 non-terminals");
            final HBox ruleBox = generator.createHorizontalBox(5, Insets.EMPTY, Pos.CENTER, textFieldLHS, labelImplication, textFieldRHS, buttonDeleteRule);
            final VBox finalBox = generator.createVerticalBox(0, Insets.EMPTY, Pos.CENTER, labelWarning, ruleBox);

            // Building the respective back-end rule
            final var rule = new Rule() {
                @Override
                public String getLeftSide() {
                    return textFieldLHS.getText();
                }

                @Override
                public String getRightSide() {
                    return textFieldRHS.getText();
                }
            };

            // Completing the rule design
            designer.setBackground(Background.EMPTY, buttonDeleteRule, textFieldLHS, textFieldRHS);
            designer.setPrefWidth(120, textFieldLHS);
            designer.setPrefWidth(240, textFieldRHS);
            designer.setStyle(textStyleWarning, labelWarning);
            textFieldLHS.setText(lhs);
            textFieldRHS.setText(rhs);

            // Adding both the back-end and front-end rule to the list
            rules.add(rule);
            ruleHolder.getChildren().add(finalBox);

            // Providing support for rules in bright mode
            final Action brightModeRuleAction = () -> {
                designer.setBorder(borderBrightMode, buttonDeleteRule, textFieldLHS, textFieldRHS);
                designer.setStyle(textStyleBrightMode, buttonDeleteRule, textFieldLHS, textFieldRHS, labelImplication);
            };
            actionsBrightModeSwitch.add(brightModeRuleAction);

            // Providing support for rules in dark mode
            final Action darkModeRuleAction = () -> {
                designer.setBorder(borderDarkMode, buttonDeleteRule, textFieldLHS, textFieldRHS);
                designer.setStyle(textStyleDarkMode, buttonDeleteRule, textFieldLHS, textFieldRHS, labelImplication);
            };
            actionsDarkModeSwitch.add(darkModeRuleAction);

            // Assigning visual effects to the delete rule button
            designer.setOnMouseEntered(handlerButtonRedHighlight, buttonDeleteRule);
            designer.setOnMouseExited(handlerButtonResetHighlight, buttonDeleteRule);

            // Applying current color scheme to rule design
            if (themeEventHistory[0] == eventEnterBrightMode) brightModeRuleAction.execute();
            else darkModeRuleAction.execute();

            // Assigning functionality to delete rule button
            buttonDeleteRule.setOnAction(deleteRuleEvent -> {
                rules.remove(rule);
                ruleHolder.getChildren().remove(finalBox);
                actionsBrightModeSwitch.remove(brightModeRuleAction);
                actionsDarkModeSwitch.remove(darkModeRuleAction);
            });

            // Assigning pre-processing functionality to lhs text field
            textFieldLHS.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.contains(" ")) labelWarning.setText("Warning: Left-hand sided text field should NOT contain spaces!");
                else labelWarning.setText("");
            });

            // Assigning pre-processing functionality to rhs text field
            textFieldRHS.textProperty().addListener((observable, oldValue, newValue) -> {
                final String[] segments = newValue.split(" ");
                if (segments.length > 1) labelWarning.setText("Warning: Right-handed sided text field should NOT contain MORE THAN 1 space");
                else labelWarning.setText("");
            });
        };

        // Assigning functionality to the new rule button
        buttonNewRule.setOnAction(newRuleEvent -> ruleGenerator.generate("", ""));

        // Todo: Add content to the editor panel (aka sceneRoots[2])
        // Building the editor panel
        sceneRoots[2] = generator.createVerticalBox(3, padding, Pos.CENTER, rulePanel);

        // Completing the design of the editor panel
        VBox.setVgrow(rulePanel, Priority.ALWAYS);

        // Providing support for the editor panel in bright mode
        actionsBrightModeSwitch.add(() -> designer.setBorder(borderBrightMode, sceneRoots[2]));

        // Providing support for the editor panel in dark mode
        actionsBrightModeSwitch.add(() -> designer.setBorder(borderDarkMode, sceneRoots[2]));

        // Todo: Create authorization panel (aka sceneRoots[0])

        // Completing design of scene roots
        // Todo: Complete design of every scene
        designer.setBackground(Background.EMPTY, sceneRoots[1], sceneRoots[2]);

        // Creating scenes
        // Todo: Create every scene
        scenes[1] = new Scene(sceneRoots[1], sizeChatScene[0], sizeChatScene[1]);
        scenes[2] = new Scene(sceneRoots[2], sizeEditorScene[0], sizeEditorScene[1]);

        // Providing support for bright mode scene
        actionsBrightModeSwitch.add(() -> {
            // Todo: Add support for every scene
            scenes[1].setFill(colorBrightMode);
            scenes[2].setFill(colorBrightMode);
        });

        // Providing support for dark mode scene
        actionsDarkModeSwitch.add(() -> {
            // Todo: Add support for every scene
            scenes[1].setFill(colorDarkMode);
            scenes[2].setFill(colorDarkMode);
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

    private interface Rule {
        String getLeftSide();
        String getRightSide();
    }

    private interface RuleGenerator {
        void generate(String lhs, String rhs);
    }
}