import engine.Assistant;
import gui.NodeFactory;
import gui.RegionDesigner;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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

        // Assistant
        final var assistant   = new Assistant();

        // Insets
        final Insets padding = new Insets(5, 5, 5, 5);

        // Constant reference to scenes and their root nodes
        final Scene[] scenes     = new Scene[3]; // scenes[0]     => start scene      | scenes[1]     => chat scene      | scenes[2]     => editor scene
        final VBox[]  sceneRoots = new VBox[3];  // sceneRoots[0] => start scene root | sceneRoots[1] => chat scene root | sceneRoots[2] => editor scene root

        // Scene sizes (width x height)
        final int[] sizeStartScene  = {360, 360};
        final int[] sizeChatScene   = {360, 720};
        final int[] sizeEditorScene = {1200, 720};

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
        final var factory  = new NodeFactory();

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                               Title Bar                                                   *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

        // Building the title bar
        final Button buttonCloseStage    = new Button(symbolMultiplication);
        final Button buttonMinimizeStage = new Button(symbolDash);
        final Button buttonSwitchScene   = new Button(symbolMemo);
        final Button buttonSwitchTheme   = new Button(symbolMoon);
        final Label  title               = factory.createLabel("DIGITAL ASSISTANT", Pos.CENTER);
        final HBox   stageControls       = factory.createHorizontalBox(0, Insets.EMPTY, Pos.CENTER, buttonMinimizeStage, buttonCloseStage);
        final HBox   appControls         = factory.createHorizontalBox(0, Insets.EMPTY, Pos.CENTER, buttonSwitchTheme, buttonSwitchScene);
        final HBox   titleBar            = factory.createHorizontalBox(0, Insets.EMPTY, Pos.CENTER, appControls, title, stageControls);

        // Completing the design
        designer.setBackground(Background.EMPTY, buttonCloseStage, buttonMinimizeStage, buttonSwitchScene, buttonSwitchTheme);
        designer.setMaxWidth(Double.MAX_VALUE, buttonCloseStage, buttonMinimizeStage, buttonSwitchScene, buttonSwitchTheme, title);
        designer.setPrefWidth(50, buttonCloseStage, buttonMinimizeStage, buttonSwitchScene, buttonSwitchTheme);
        designer.setPrefWidth(100, stageControls, appControls);
        designer.setPrefHeight(50, buttonCloseStage, buttonMinimizeStage, buttonSwitchScene, buttonSwitchTheme, title);
        HBox.setHgrow(title, Priority.ALWAYS);

        // Assigning visual effects
        designer.setOnMouseEntered(handlerButtonRedHighlight, buttonCloseStage);
        designer.setOnMouseEntered(handlerButtonDefaultHighlight, buttonMinimizeStage, buttonSwitchScene, buttonSwitchTheme);
        designer.setOnMouseExited(handlerButtonResetHighlight, buttonCloseStage, buttonMinimizeStage, buttonSwitchScene, buttonSwitchTheme);

        // Providing bright mode support
        actionsBrightModeSwitch.add(() -> {
            designer.setBorder(borderBrightMode, titleBar);
            designer.setStyle(textStyleBrightMode, buttonCloseStage, buttonMinimizeStage, buttonSwitchScene, buttonSwitchTheme, title);
        });

        // Providing dark mode support
        actionsDarkModeSwitch.add(() -> {
            designer.setBorder(borderDarkMode, titleBar);
            designer.setStyle(textStyleDarkMode, buttonCloseStage, buttonMinimizeStage, buttonSwitchScene, buttonSwitchTheme, title);
        });

        // Providing stage relocation support
        title.setOnMousePressed(handlerStartRelocation);
        title.setOnMouseDragged(handlerCompleteRelocation);

        // Assigning click event handlers
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

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                   Chat                                                    *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

        // Building the chat
        final VBox       chatHistory  = factory.createVerticalBox(5, padding, Pos.TOP_CENTER);
        final ScrollPane viewportChat = factory.createScrollPane(scrollPaneStylesheet, chatHistory);
        final TextField  chatInput    = factory.createTextField("Click to type...");

        // Completing the design
        designer.setBackground(Background.EMPTY, chatInput);
        VBox.setVgrow(viewportChat, Priority.ALWAYS);

        // Providing bright mode support
        actionsBrightModeSwitch.add(() -> {
            designer.setBorder(borderBrightMode, viewportChat, chatInput);
            designer.setStyle(textStyleBrightMode, chatInput);
        });

        // Providing dark mode support
        actionsDarkModeSwitch.add(() -> {
            designer.setBorder(borderDarkMode, viewportChat, chatInput);
            designer.setStyle(textStyleDarkMode, chatInput);
        });

        // Assigning text field functionality
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
            final HBox     inputBox       = factory.createHorizontalBox(0, Insets.EMPTY, Pos.CENTER_RIGHT, inputTextFlow);
            final HBox     outputBox      = factory.createHorizontalBox(0, Insets.EMPTY, Pos.CENTER_LEFT, outputTextFlow);

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

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                  Overview                                                 *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

        // Building labels to identify overviews
        final Label labelSkill   = factory.createLabel("Skill", Pos.CENTER);
        final Label labelGrammar = factory.createLabel("Grammar", Pos.CENTER);

        // Building content holders
        final VBox skillHolder   = factory.createVerticalBox(5, padding, Pos.TOP_CENTER);
        final VBox grammarHolder = factory.createVerticalBox(5, padding, Pos.TOP_CENTER);

        // Building scroll panes
        final ScrollPane viewportSkillList   = factory.createScrollPane(scrollPaneStylesheet, skillHolder);
        final ScrollPane viewportGrammarList = factory.createScrollPane(scrollPaneStylesheet, grammarHolder);

        // Building overviews
        final VBox skillOverview   = factory.createVerticalBox(3, padding, Pos.CENTER, labelSkill, viewportSkillList);
        final VBox grammarOverview = factory.createVerticalBox(3, padding, Pos.CENTER, labelGrammar, viewportGrammarList);

        // Completing the design
        designer.setMaxWidth(Double.MAX_VALUE, labelSkill, labelGrammar);
        designer.setPrefHeight(30, labelSkill, labelGrammar);
        VBox.setVgrow(viewportSkillList, Priority.ALWAYS);
        VBox.setVgrow(viewportGrammarList, Priority.ALWAYS);

        // Providing bright mode support
        actionsBrightModeSwitch.add(() -> {
            designer.setBorder(borderBrightMode, labelSkill, labelGrammar);
            designer.setBorder(borderBrightMode, viewportSkillList, viewportGrammarList);
            designer.setStyle(textStyleBrightMode, labelSkill, labelGrammar);
        });

        // Providing dark mode support
        actionsDarkModeSwitch.add(() -> {
            designer.setBorder(borderDarkMode, labelSkill, labelGrammar);
            designer.setBorder(borderDarkMode, viewportSkillList, viewportGrammarList);
            designer.setStyle(textStyleDarkMode, labelSkill, labelGrammar);
        });

        // Todo: Create a basic editor which directly associates (input, output)

        // Todo: Add a placeholder editor (phase 1) if time over

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                   Rules                                                   *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

        // Building the box to contain rules
        final VBox viewportRulesContent = factory.createVerticalBox(5, padding, Pos.TOP_CENTER);

        // Providing how graphically displayable rules are generated
        final RuleGenerator generator = (lhs, rhs) -> {
            // Building a displayable rule
            final Button buttonDeleteRule = new Button(symbolMultiplication);
            final Label  labelWarning     = factory.createLabel("", Pos.CENTER);
            final Label  labelImplication = factory.createLabel(symbolRightArrow, Pos.CENTER);
            final TextField textFieldLHS  = factory.createTextField("1 non-terminal");
            final TextField textFieldRHS  = factory.createTextField("1 terminal or up to 2 non-terminals");
            final HBox ruleBox  = factory.createHorizontalBox(5, Insets.EMPTY, Pos.CENTER, textFieldLHS, labelImplication, textFieldRHS, buttonDeleteRule);
            final VBox finalBox = factory.createVerticalBox(0, Insets.EMPTY, Pos.CENTER, labelWarning, ruleBox);

            // Completing the rule design
            designer.setBackground(Background.EMPTY, buttonDeleteRule, textFieldLHS, textFieldRHS);
            designer.setPrefWidth(120, textFieldLHS);
            designer.setPrefWidth(240, textFieldRHS);
            designer.setStyle(textStyleWarning, labelWarning);
            textFieldLHS.setText(lhs);
            textFieldRHS.setText(rhs);

            // Providing bright mode support
            final Action brightModeRuleAction = () -> {
                designer.setBorder(borderBrightMode, buttonDeleteRule, textFieldLHS, textFieldRHS);
                designer.setStyle(textStyleBrightMode, buttonDeleteRule, textFieldLHS, textFieldRHS, labelImplication);
            };
            actionsBrightModeSwitch.add(brightModeRuleAction);

            // Providing dark mode support
            final Action darkModeRuleAction = () -> {
                designer.setBorder(borderDarkMode, buttonDeleteRule, textFieldLHS, textFieldRHS);
                designer.setStyle(textStyleDarkMode, buttonDeleteRule, textFieldLHS, textFieldRHS, labelImplication);
            };
            actionsDarkModeSwitch.add(darkModeRuleAction);

            // Assigning visual effects
            designer.setOnMouseEntered(handlerButtonRedHighlight, buttonDeleteRule);
            designer.setOnMouseExited(handlerButtonResetHighlight, buttonDeleteRule);

            // Applying current color scheme
            if (themeEventHistory[0] == eventEnterBrightMode) brightModeRuleAction.execute();
            else darkModeRuleAction.execute();

            // Adding rule to the editor
            viewportRulesContent.getChildren().add(finalBox);

            // Creating event handler to delete rule
            final EventHandler<ActionEvent> handlerDeleteRule = event -> {
                viewportRulesContent.getChildren().remove(finalBox);
                actionsBrightModeSwitch.remove(brightModeRuleAction);
                actionsDarkModeSwitch.remove(darkModeRuleAction);
            };

            // Assigning functionality to delete rule button
            buttonDeleteRule.setOnAction(handlerDeleteRule);

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

            return handlerDeleteRule;
        };

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                 Grammar                                                   *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

        // Building buttons
        final Button buttonNewRule       = new Button("+");
        final Button buttonDeleteGrammar = new Button("DELETE");
        final Button buttonSaveGrammar   = new Button("SAVE");

        // Building labels
        final Label labelGrammarEditor = factory.createLabel("Editor: Grammar", Pos.CENTER);
        final Label labelStartSymbol   = factory.createLabel("Start-Symbol ", Pos.CENTER);
        final Label labelArrow         = factory.createLabel(symbolRightArrow, Pos.CENTER);

        // Building text fields
        final TextField textFieldStartSymbol = factory.createTextField("1 non-terminal");

        // Building horizontal boxes
        final HBox boxStartSymbol       = factory.createHorizontalBox(5, Insets.EMPTY, Pos.CENTER, labelStartSymbol, labelArrow, textFieldStartSymbol);
        final HBox boxGrammarButtons    = factory.createHorizontalBox(2, Insets.EMPTY, Pos.CENTER, buttonDeleteGrammar, buttonSaveGrammar);

        // Building the scroll pane to browse rules
        final ScrollPane viewportRules  = factory.createScrollPane(scrollPaneStylesheet, viewportRulesContent);

        // Building the editor
        final VBox grammarEditor  = factory.createVerticalBox(3, padding, Pos.CENTER, labelGrammarEditor, viewportRules, buttonNewRule, boxGrammarButtons);

        // Completing the design
        designer.setBackground(Background.EMPTY, buttonNewRule, buttonDeleteGrammar, buttonSaveGrammar, textFieldStartSymbol);
        designer.setMaxWidth(Double.MAX_VALUE, buttonNewRule, buttonDeleteGrammar, buttonSaveGrammar, labelGrammarEditor);
        designer.setPrefHeight(30, labelGrammarEditor);
        designer.setMaxWidth(120, textFieldStartSymbol);
        HBox.setHgrow(buttonDeleteGrammar, Priority.ALWAYS);
        HBox.setHgrow(buttonSaveGrammar, Priority.ALWAYS);
        VBox.setVgrow(viewportRules, Priority.ALWAYS);
        VBox.setVgrow(grammarEditor, Priority.ALWAYS);
        viewportRulesContent.getChildren().add(boxStartSymbol);
        textFieldStartSymbol.setAlignment(Pos.CENTER);

        // Providing bright mode support
        actionsBrightModeSwitch.add(() -> {
            designer.setBorder(borderBrightMode, buttonNewRule, buttonDeleteGrammar, buttonSaveGrammar);
            designer.setBorder(borderBrightMode, labelGrammarEditor, textFieldStartSymbol);
            designer.setBorder(borderBrightMode, viewportRules);
            designer.setStyle(textStyleBrightMode, buttonNewRule, buttonDeleteGrammar, buttonSaveGrammar);
            designer.setStyle(textStyleBrightMode, labelGrammarEditor, labelStartSymbol, labelArrow);
            designer.setStyle(textStyleBrightMode, textFieldStartSymbol);
        });

        // Providing dark mode support
        actionsDarkModeSwitch.add(() -> {
            designer.setBorder(borderDarkMode, buttonNewRule, buttonDeleteGrammar, buttonSaveGrammar);
            designer.setBorder(borderDarkMode, labelGrammarEditor, textFieldStartSymbol);
            designer.setBorder(borderDarkMode, viewportRules);
            designer.setStyle(textStyleDarkMode, buttonNewRule, buttonDeleteGrammar, buttonSaveGrammar);
            designer.setStyle(textStyleDarkMode, labelGrammarEditor, labelStartSymbol, labelArrow);
            designer.setStyle(textStyleDarkMode, textFieldStartSymbol);
        });

        // Assigning visual effects
        designer.setOnMouseEntered(handlerButtonGreenHighlight, buttonNewRule, buttonSaveGrammar);
        designer.setOnMouseEntered(handlerButtonRedHighlight, buttonDeleteGrammar);
        designer.setOnMouseExited(handlerButtonResetHighlight, buttonNewRule, buttonDeleteGrammar, buttonSaveGrammar);

        // Assigning functionality to the new rule button
        final List<EventHandler<ActionEvent>> deleteRuleHandlers = new ArrayList<>();
        buttonNewRule.setOnAction(newRuleEvent -> deleteRuleHandlers.add(generator.generate("", "")));

        // Todo: Complete assigning functionality to buttons
        buttonDeleteGrammar.setOnAction(event -> {
            for (var handler : deleteRuleHandlers) handler.handle(null);
            deleteRuleHandlers.clear();
        });

        buttonSaveGrammar.setOnAction(event -> {
            // Todo: Complete saving rules as grammar
        });

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                            Complete Editor                                                *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

        // Building the complete editor
        final HBox editor = factory.createHorizontalBox(0, Insets.EMPTY, Pos.CENTER, skillOverview, grammarEditor, grammarOverview);

        // Completing the design
        HBox.setHgrow(skillOverview, Priority.ALWAYS);
        HBox.setHgrow(grammarEditor, Priority.ALWAYS);
        HBox.setHgrow(grammarOverview, Priority.ALWAYS);
        VBox.setVgrow(editor, Priority.ALWAYS);

        // Providing bright mode support
        actionsBrightModeSwitch.add(() -> designer.setBorder(borderBrightMode, editor));

        // Providing dark mode support
        actionsDarkModeSwitch.add(() -> designer.setBorder(borderDarkMode, editor));

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                  Start                                                    *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

        // Todo: Create authorization panel (aka sceneRoots[0])

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                  Roots                                                    *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

        // Building the scene roots
        sceneRoots[1] = factory.createVerticalBox(3, padding, Pos.CENTER, viewportChat, chatInput);
        sceneRoots[2] = factory.createVerticalBox(3, padding, Pos.CENTER, editor);

        // Completing the design
        designer.setBackground(Background.EMPTY, sceneRoots[1], sceneRoots[2]);

        // Providing bright mode support
        actionsBrightModeSwitch.add(() -> designer.setBorder(borderBrightMode, sceneRoots[1], sceneRoots[2]));

        // Providing dark mode support
        actionsBrightModeSwitch.add(() -> designer.setBorder(borderDarkMode, sceneRoots[1], sceneRoots[2]));


        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                  Scene                                                    *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

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

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                  Stage                                                     *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

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

    // Todo: If needed, update interface to have the assistant support grammar (classes)
    private interface RuleGenerator {
        EventHandler<ActionEvent> generate(String lhs, String rhs);
    }
}