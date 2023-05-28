import engine.Assistant;
import engine.ContextFreeGrammar;
import engine.PlaceholderCounter;
import engine.PlaceholderReplacer;
import engine.faceDetection.CamController;
import engine.faceDetection.FaceDetect;
import gui.NodeFactory;
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

import java.util.*;

public final class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Resource loading
        final var scrollPaneCSS = getClass().getResource("/transparent-scroll-pane.css");

        // Resource validation
        if (scrollPaneCSS == null) throw new IllegalStateException("Failed to fetch css file for scroll panes!");

        // Loading stylesheets
        final String scrollPaneStylesheet = scrollPaneCSS.toExternalForm();

        // Inline stylesheets
        final String brightModeTextStyle = "-fx-font: 14 arial; -fx-text-fill: rgb(30, 30, 30); -fx-prompt-text-fill: -fx-text-fill;";
        final String darkModeTextStyle   = "-fx-font: 14 arial; -fx-text-fill: rgb(180, 180, 180); -fx-prompt-text-fill: -fx-text-fill;";
        final String warningTextStyle    = "-fx-text-fill: rgb(255, 120, 150);";

        // Engine variables
        final var assistant     = new Assistant();
        final var camController = new CamController();
        final var faceDetector  = new FaceDetect();

        // Insets
        final Insets defaultPadding = new Insets(5, 5, 5, 5);
        final Insets contentPadding = new Insets(10, 5, 10, 5);

        // Constant reference to scenes and their root nodes
        final Scene[] scenes     = new Scene[3]; // scenes[0]     => start scene      | scenes[1]     => chat scene      | scenes[2]     => editor scene
        final VBox[]  sceneRoots = new VBox[3];  // sceneRoots[0] => start scene root | sceneRoots[1] => chat scene root | sceneRoots[2] => editor scene root

        // Scene sizes (width x height)
        final int[] startSceneSize  = {360, 360};
        final int[] chatSceneSize   = {360, 720};
        final int[] editorSceneSize = {1600, 720};

        // Symbols
        final String multiplicationSymbol = Character.toString(10005);
        final String dashSymbol           = Character.toString(8212);
        final String sunSymbol            = Character.toString(9728);
        final String moonSymbol           = Character.toString(127769);
        final String memoSymbol           = Character.toString(128221);
        final String speechBubbleSymbol   = Character.toString(128490);
        final String rightArrowSymbol     = Character.toString(8594);

        // Color preferences
        final Color brightMode   = Color.rgb(180, 180, 180);
        final Color darkMode     = Color.rgb(30, 30, 30);

        // Backgrounds
        final Background assistantMessageBackground = Background.fill(Color.rgb(150, 180, 200));
        final Background userMessageBackground      = Background.fill(Color.rgb(150, 200, 180));
        final Background defaultHighlightBackground = Background.fill(Color.rgb(120, 120, 120, 0.5));
        final Background greenHighlightBackground   = Background.fill(Color.rgb(120, 255, 150));
        final Background redHighlightBackground     = Background.fill(Color.rgb(255, 120, 150));

        // Borders
        final Border brightModeBorder = Border.stroke(darkMode);
        final Border darkModeBorder   = Border.stroke(brightMode);

        // Color theme switchers
        final List<Action> brightModeSwitchActions = new ArrayList<>();
        final List<Action> darkModeSwitchActions    = new ArrayList<>();

        // Color theme events
        final ActionEvent enterBrightModeEvent = new ActionEvent();
        final ActionEvent enterDarkModeEvent   = new ActionEvent();

        // Reference to keep track what the current theme is
        final ActionEvent[] colorThemeHistory = new ActionEvent[1];

        // Event handler to switch color themes
        final EventHandler<ActionEvent> switchColorThemeHandler = event -> {
            final List<Action> actions;
            if (event == enterBrightModeEvent) actions = brightModeSwitchActions;
            else                               actions = darkModeSwitchActions;
            for (var action : actions) action.execute();
            colorThemeHistory[0] = event;
        };

        // Event handlers to highlight buttons
        final EventHandler<MouseEvent> defaultHighlightHandler = event -> {
            final var region = (Region) event.getSource();
            region.setBackground(defaultHighlightBackground);
        };
        final EventHandler<MouseEvent> greenHighlightHandler = event -> {
            final var region = (Region) event.getSource();
            region.setBackground(greenHighlightBackground);
        };
        final EventHandler<MouseEvent> redHighlightHandler = event -> {
            final var region = (Region) event.getSource();
            region.setBackground(redHighlightBackground);
        };
        final EventHandler<MouseEvent> removeHighlightHandler = event -> {
            final var region = (Region) event.getSource();
            region.setBackground(Background.EMPTY);
        };

        // Event handlers to relocate the primary stage through dragging
        final double[] previousLocationCoordinates = new double[2];
        final EventHandler<MouseEvent> startRelocationHandler = event -> {
            previousLocationCoordinates[0] = event.getScreenX();
            previousLocationCoordinates[1] = event.getScreenY();
        };
        final EventHandler<MouseEvent> relocationHandler = event -> {
            final double x = primaryStage.getX() + event.getScreenX() - previousLocationCoordinates[0];
            final double y = primaryStage.getY() + event.getScreenY() - previousLocationCoordinates[1];
            previousLocationCoordinates[0] = event.getScreenX();
            previousLocationCoordinates[1] = event.getScreenY();
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
        final Button closeStageButton    = new Button(multiplicationSymbol);
        final Button minimizeStageButton = new Button(dashSymbol);
        final Button switchSceneButton   = new Button(memoSymbol);
        final Button switchThemeButton   = new Button(moonSymbol);
        final Label  titleLabel          = factory.createLabel("DIGITAL ASSISTANT", Pos.CENTER);
        final HBox   stageControls       = factory.createHorizontalBox(0, Insets.EMPTY, Pos.CENTER, minimizeStageButton, closeStageButton);
        final HBox   appControls         = factory.createHorizontalBox(0, Insets.EMPTY, Pos.CENTER, switchThemeButton, switchSceneButton);
        final HBox   titleBar            = factory.createHorizontalBox(0, Insets.EMPTY, Pos.CENTER, appControls, titleLabel, stageControls);

        // Completing the design
        designer.setBackground(Background.EMPTY, closeStageButton, minimizeStageButton, switchSceneButton, switchThemeButton);
        designer.setMaxWidth(Double.MAX_VALUE, closeStageButton, minimizeStageButton, switchSceneButton, switchThemeButton, titleLabel);
        designer.setMinHeight(50, titleBar);
        designer.setPrefWidth(50, closeStageButton, minimizeStageButton, switchSceneButton, switchThemeButton);
        designer.setPrefWidth(100, stageControls, appControls);
        designer.setPrefHeight(50, closeStageButton, minimizeStageButton, switchSceneButton, switchThemeButton, titleLabel);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);

        // Assigning visual effects
        designer.setOnMouseEntered(redHighlightHandler, closeStageButton);
        designer.setOnMouseEntered(defaultHighlightHandler, minimizeStageButton, switchSceneButton, switchThemeButton);
        designer.setOnMouseExited(removeHighlightHandler, closeStageButton, minimizeStageButton, switchSceneButton, switchThemeButton);

        // Providing bright mode support
        brightModeSwitchActions.add(() -> {
            designer.setBorder(brightModeBorder, titleBar);
            designer.setStyle(brightModeTextStyle, titleLabel);
            designer.setStyle(brightModeTextStyle, closeStageButton, minimizeStageButton);
            designer.setStyle(brightModeTextStyle, switchSceneButton, switchThemeButton);
        });

        // Providing dark mode support
        darkModeSwitchActions.add(() -> {
            designer.setBorder(darkModeBorder, titleBar);
            designer.setStyle(darkModeTextStyle, titleLabel);
            designer.setStyle(darkModeTextStyle, closeStageButton, minimizeStageButton);
            designer.setStyle(darkModeTextStyle, switchSceneButton, switchThemeButton);
        });

        // Providing stage relocation support
        titleLabel.setOnMousePressed(startRelocationHandler);
        titleLabel.setOnMouseDragged(relocationHandler);

        // Assigning click event handlers
        closeStageButton.setOnAction(event -> {
            // Todo; If needed, log-out (safely) once the authorization are completed
            primaryStage.close();
        });
        minimizeStageButton.setOnAction(event -> primaryStage.setIconified(true));
        switchSceneButton.setOnAction(event -> {
            if (primaryStage.getScene() == scenes[1]) {
                sceneRoots[1].getChildren().remove(titleBar);
                sceneRoots[2].getChildren().add(0, titleBar);
                primaryStage.setScene(scenes[2]);
                switchSceneButton.setText(speechBubbleSymbol);
            } else if (primaryStage.getScene() == scenes[2]) {
                sceneRoots[2].getChildren().remove(titleBar);
                sceneRoots[1].getChildren().add(0, titleBar);
                primaryStage.setScene(scenes[1]);
                switchSceneButton.setText(memoSymbol);
            }
            primaryStage.centerOnScreen();
        });
        switchThemeButton.setOnAction(event -> {
            final String s = switchThemeButton.getText();
            if (s.equals(sunSymbol)) {
                switchThemeButton.setText(moonSymbol);
                switchColorThemeHandler.handle(enterDarkModeEvent);
            } else if (s.equals(moonSymbol)) {
                switchThemeButton.setText(sunSymbol);
                switchColorThemeHandler.handle(enterBrightModeEvent);
            }
        });

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                   Chat                                                    *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

        // Building the chat
        final VBox       chatHistory  = factory.createVerticalBox(5, defaultPadding, Pos.TOP_CENTER);
        final ScrollPane chatViewport = factory.createScrollPane(scrollPaneStylesheet, chatHistory);
        final TextField  chatInput    = factory.createTextField("Click to type...", Pos.CENTER_LEFT);

        // Completing the design
        designer.setBackground(Background.EMPTY, chatInput);
        VBox.setVgrow(chatViewport, Priority.ALWAYS);

        // Providing bright mode support
        brightModeSwitchActions.add(() -> {
            designer.setBorder(brightModeBorder, chatViewport, chatInput);
            designer.setStyle(brightModeTextStyle, chatInput);
        });

        // Providing dark mode support
        darkModeSwitchActions.add(() -> {
            designer.setBorder(darkModeBorder, chatViewport, chatInput);
            designer.setStyle(darkModeTextStyle, chatInput);
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
            designer.setBackground(assistantMessageBackground,  outputTextFlow);
            designer.setBackground(userMessageBackground, inputTextFlow);
            designer.setMaxWidth(240, inputTextFlow, outputTextFlow);
            inputTextFlow.setPadding(defaultPadding);
            outputTextFlow.setPadding(defaultPadding);

            // Adding messages
            chatHistory.getChildren().addAll(inputBox, outputBox);

            // Todo?: Provide support for bright and dark themed messages
        });

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                  Overview                                                 *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

        // Building labels to identify overviews
        final Label skillLabel   = factory.createLabel("SKILL", Pos.CENTER);
        final Label grammarLabel = factory.createLabel("GRAMMAR", Pos.CENTER);

        // Building content holders
        final VBox skillHolder   = factory.createVerticalBox(5, defaultPadding, Pos.TOP_CENTER);
        final VBox grammarHolder = factory.createVerticalBox(5, defaultPadding, Pos.TOP_CENTER);

        // Building scroll panes
        final ScrollPane skillViewport   = factory.createScrollPane(scrollPaneStylesheet, skillHolder);
        final ScrollPane grammarViewport = factory.createScrollPane(scrollPaneStylesheet, grammarHolder);

        // Building overviews
        final VBox skillOverview   = factory.createVerticalBox(3, defaultPadding, Pos.CENTER, skillLabel, skillViewport);
        final VBox grammarOverview = factory.createVerticalBox(3, defaultPadding, Pos.CENTER, grammarLabel, grammarViewport);

        // Completing the design
        designer.setMaxWidth(Double.MAX_VALUE, skillLabel, grammarLabel);
        designer.setPrefHeight(30, skillLabel, grammarLabel);
        VBox.setVgrow(skillViewport, Priority.ALWAYS);
        VBox.setVgrow(grammarViewport, Priority.ALWAYS);

        // Providing bright mode support
        brightModeSwitchActions.add(() -> {
            designer.setBorder(brightModeBorder, skillLabel, grammarLabel);
            designer.setBorder(brightModeBorder, skillViewport, grammarViewport);
            designer.setStyle(brightModeTextStyle, skillLabel, grammarLabel);
        });

        // Providing dark mode support
        darkModeSwitchActions.add(() -> {
            designer.setBorder(darkModeBorder, skillLabel, grammarLabel);
            designer.setBorder(darkModeBorder, skillViewport, grammarViewport);
            designer.setStyle(darkModeTextStyle, skillLabel, grammarLabel);
        });

        // Todo: Create a basic editor which directly associates (input, output)

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                   Skill                                                   *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

        // Providing utility lists
        final List<TextField> inputFields           = new ArrayList<>();
        final List<TextField> outputFields          = new ArrayList<>();
        final List<Label>     skillErrorLabels      = new ArrayList<>();
        final List<Action>    deleteVariableActions = new ArrayList<>();

        // Building the buttons
        final Button addVariablesButton = new Button("ADD VARIABLES");
        final Button saveSkillButton    = new Button("SAVE SKILL");

        // Building the labels
        final Label skillEditorLabel         = factory.createLabel("SKILL EDITOR", Pos.CENTER);
        final Label placeholderLabel         = factory.createLabel("Placeholder", Pos.CENTER_RIGHT);
        final Label placeholderArrowLabel    = factory.createLabel(rightArrowSymbol, Pos.CENTER);
        final Label inputTemplateLabel       = factory.createLabel("Input Template", Pos.CENTER_RIGHT);
        final Label inputTemplateArrowLabel  = factory.createLabel(rightArrowSymbol, Pos.CENTER);
        final Label outputTemplateLabel      = factory.createLabel("Output Template", Pos.CENTER_RIGHT);
        final Label outputTemplateArrowLabel = factory.createLabel(rightArrowSymbol, Pos.CENTER);

        // Building the text fields
        final TextField placeholderTextField    = factory.createTextField("One or more characters", Pos.CENTER);
        final TextField inputTemplateTextField  = factory.createTextField("At least one or more placeholders", Pos.CENTER);
        final TextField outputTemplateTextField = factory.createTextField("At least one or more placeholders", Pos.CENTER);

        // Building the horizontal boxes
        final HBox placeholderBox    = factory.createHorizontalBox(5, Insets.EMPTY, Pos.CENTER, placeholderLabel, placeholderArrowLabel, placeholderTextField);
        final HBox inputTemplateBox  = factory.createHorizontalBox(5, Insets.EMPTY, Pos.CENTER, inputTemplateLabel, inputTemplateArrowLabel, inputTemplateTextField);
        final HBox outputTemplateBox = factory.createHorizontalBox(5, Insets.EMPTY, Pos.CENTER, outputTemplateLabel, outputTemplateArrowLabel, outputTemplateTextField);

        // Building the editor content holder
        final VBox templateHolder = factory.createVerticalBox(5, contentPadding, Pos.CENTER, placeholderBox, inputTemplateBox, outputTemplateBox);
        final VBox variableHolder = factory.createVerticalBox(5, contentPadding, Pos.TOP_CENTER);

        // Building the editor viewport
        final ScrollPane skillEditorViewport = factory.createScrollPane(scrollPaneStylesheet, variableHolder);

        // Building the editor
        final VBox skillEditor = factory.createVerticalBox(3, defaultPadding, Pos.CENTER, skillEditorLabel, templateHolder, skillEditorViewport, addVariablesButton, saveSkillButton);

        // Completing the design
        designer.setBackground(Background.EMPTY, addVariablesButton, saveSkillButton);
        designer.setBackground(Background.EMPTY, placeholderTextField, inputTemplateTextField, outputTemplateTextField);
        designer.setMaxWidth(Double.MAX_VALUE, addVariablesButton, saveSkillButton, skillEditorLabel);
        designer.setMaxWidth(Double.MAX_VALUE, placeholderTextField, inputTemplateTextField, outputTemplateTextField);
        designer.setMinHeight(30, skillEditorLabel);
        designer.setMinWidth(120, placeholderLabel, inputTemplateLabel, outputTemplateLabel);
        designer.setMinWidth(40, placeholderArrowLabel, inputTemplateArrowLabel, outputTemplateArrowLabel);
        designer.setMinWidth(240, placeholderTextField, inputTemplateTextField, outputTemplateTextField);
        VBox.setVgrow(skillEditorViewport, Priority.ALWAYS);

        // Assigning visual effects
        designer.setOnMouseEntered(greenHighlightHandler, addVariablesButton, saveSkillButton);
        designer.setOnMouseExited(removeHighlightHandler, addVariablesButton, saveSkillButton);

        // Providing bright mode support
        brightModeSwitchActions.add(() -> {
            designer.setBorder(brightModeBorder, skillEditorLabel, templateHolder, variableHolder);
            designer.setBorder(brightModeBorder, placeholderTextField, inputTemplateTextField, outputTemplateTextField);
            designer.setBorder(brightModeBorder, addVariablesButton, saveSkillButton);
            designer.setStyle(brightModeTextStyle, skillEditorLabel, placeholderLabel, inputTemplateLabel, outputTemplateLabel);
            designer.setStyle(brightModeTextStyle, placeholderArrowLabel, inputTemplateArrowLabel, outputTemplateArrowLabel);
            designer.setStyle(brightModeTextStyle, placeholderTextField, inputTemplateTextField, outputTemplateTextField);
            designer.setStyle(brightModeTextStyle, addVariablesButton, saveSkillButton);
        });

        // Providing dark mode support
        darkModeSwitchActions.add(() -> {
            designer.setBorder(darkModeBorder, skillEditorLabel, templateHolder, variableHolder);
            designer.setBorder(darkModeBorder, placeholderTextField, inputTemplateTextField, outputTemplateTextField);
            designer.setBorder(darkModeBorder, addVariablesButton, saveSkillButton);
            designer.setStyle(darkModeTextStyle, skillEditorLabel, placeholderLabel, inputTemplateLabel, outputTemplateLabel);
            designer.setStyle(darkModeTextStyle, placeholderArrowLabel, inputTemplateArrowLabel, outputTemplateArrowLabel);
            designer.setStyle(darkModeTextStyle, placeholderTextField, inputTemplateTextField, outputTemplateTextField);
            designer.setStyle(darkModeTextStyle, addVariablesButton, saveSkillButton);
        });

        // Defining the variable generator the save skill button will be using
        final VariableGenerator variableGenerator = (in, out) -> {
            // Providing the delete button
            final Button deleteButton = new Button(multiplicationSymbol);

            // Providing error label
            final Label errorLabel = factory.createLabel("", Pos.CENTER);

            // Providing the text fields
            final TextField inputField  = factory.createTextField("1st input variable, 2nd input variable, ...", Pos.CENTER);
            final TextField outputField = factory.createTextField("1st output variable, 2nd output variable, ...", Pos.CENTER);

            // Building the text field holder
            final VBox fieldBox  = factory.createVerticalBox(3, Insets.EMPTY, Pos.CENTER, inputField, outputField);

            // Building the box that includes the delete button and the text field holder
            final HBox deleteBox = factory.createHorizontalBox(3, Insets.EMPTY, Pos.CENTER, deleteButton, fieldBox);

            // Building the last box that includes the delete box and the error label
            final VBox finalBox = factory.createVerticalBox(0, Insets.EMPTY, Pos.CENTER_RIGHT, errorLabel, deleteBox);

            // Clearing the default backgrounds
            designer.setBackground(Background.EMPTY, deleteButton, inputField, outputField);

            // Removing maximum size constraints
            designer.setMaxHeight(Double.MAX_VALUE, deleteButton);
            designer.setMaxWidth(Double.MAX_VALUE, deleteButton, errorLabel);

            // Assigning preferred sizes
            designer.setPrefWidth(360, errorLabel, inputField, outputField);

            // Setting the text CSS of the error label
            designer.setStyle(warningTextStyle, errorLabel);

            // Assigning button visual effects
            designer.setOnMouseEntered(redHighlightHandler, deleteButton);
            designer.setOnMouseExited(removeHighlightHandler, deleteButton);

            // Adding the final box to the variable holder
            variableHolder.getChildren().add(finalBox);

            // Adding items to their respective list
            inputFields.add(inputField);
            outputFields.add(outputField);
            skillErrorLabels.add(errorLabel);

            // Providing bright mode support
            final Action brightModeSwitchAction = () -> {
                designer.setBorder(brightModeBorder, deleteButton, inputField, outputField);
                designer.setStyle(brightModeTextStyle, deleteButton, inputField, outputField);
            };
            brightModeSwitchActions.add(brightModeSwitchAction);

            // Providing dark mode support
            final Action darkModeSwitchAction = () -> {
                designer.setBorder(darkModeBorder, deleteButton, inputField, outputField);
                designer.setStyle(darkModeTextStyle, deleteButton, inputField, outputField);
            };
            darkModeSwitchActions.add(darkModeSwitchAction);

            // Applying current color scheme
            if (colorThemeHistory[0] == enterBrightModeEvent) brightModeSwitchAction.execute();
            else darkModeSwitchAction.execute();

            // Defining the delete event
            final Action deleteVariableAction = () -> {
                // Removing front-end entries
                variableHolder.getChildren().remove(finalBox);
                brightModeSwitchActions.remove(brightModeSwitchAction);
                darkModeSwitchActions.remove(darkModeSwitchAction);

                // Removing back-end entries
                inputFields.remove(inputField);
                outputFields.remove(outputField);
                skillErrorLabels.remove(errorLabel);
            };

            // Assigning delete event
            deleteButton.setOnAction(deleteEvent -> {
                deleteVariableActions.remove(deleteVariableAction);
                deleteVariableAction.execute();
            });

            // Setting the text values of the fields
            inputField.setText(in);
            outputField.setText(out);

            return deleteVariableAction;
        };

        // Assigning add variables button functionality
        addVariablesButton.setOnAction(addEvent -> {
            final Action deleteVariableAction = variableGenerator.generate("", "");
            deleteVariableActions.add(deleteVariableAction);
        });

        // Assigning save button functionality
        saveSkillButton.setOnAction(saveSkillEvent -> {
            // Getting the template related variables
            final String placeholder    = placeholderTextField.getText();
            final String inputTemplate  = inputTemplateTextField.getText();
            final String outputTemplate = outputTemplateTextField.getText();

            // Validating the template related variables
            // Todo: Check for blanks etc and warn the user of the error

            // Counting the placeholders in both the input and output
            final int inputPlaceholderCount  = PlaceholderCounter.countPlaceholders(inputTemplate, placeholder);
            final int outputPlaceholderCount = PlaceholderCounter.countPlaceholders(outputTemplate, placeholder);
            final int n = inputFields.size();

            // Validating the placeholder counts
            // Todo: Check > 0 and warn the user if that is not the case

            // Lists to store variables
            final String[][] inputVariables  = new String[n][];
            final String[][] outputVariables = new String[n][];

            // Reading all variables
            for (int i = 0; i < n; i++) {
                // Accessing the text fields
                final TextField inputField  = inputFields.get(i);
                final TextField outputField = outputFields.get(i);

                // Accessing the error label
                final Label errorLabel = skillErrorLabels.get(i);

                // Accessing the variables
                final String[] inputs  = inputField.getText().split(",");
                final String[] outputs = outputField.getText().split(",");

                // Validating the variable count
                // Todo: Confirm whether the number of variables equals the number of placeholders located
                //      in its respective template

                // Trimming the variables (so the variable doesn't have any leading and/or trailing spaces)
                for (int j = 0; j < inputs.length; j++) {
                    inputs[j]  = inputs[j].trim();
                    outputs[j] = outputs[j].trim();
                }

                // Validating the variable value
                // Todo: Confirm whether each string is at least not blank

                // Storing the variables
                inputVariables[i] = inputs;
                outputVariables[i] = outputs;
            }

            // Arrays to store (input, output)-associations
            final String[] inputs = new String[n];
            final String[] outputs = new String[n];

            // Processing variables and adding the (input, output) association the assistant
            for (int i = 0; i < n; i++) {
                // Getting the (input, output)-association
                final String input  = PlaceholderReplacer.replacePlaceholders(inputTemplate, placeholder, inputVariables[i]);
                final String output = PlaceholderReplacer.replacePlaceholders(outputTemplate, placeholder, outputVariables[i]);

                // Todo: Check whether the input already exists, if so prevent saving and warn the user
                // Todo: Check whether the input belongs to a grammar, if there is grammar and the input does not belong to it,
                //       then prevent saving and warn the user

                // Storing the values
                inputs[i]  = input;
                outputs[i] = output;
            }

            // Creating an association in the assistant
            for (int i = 0; i < n; i++) assistant.associate(inputs[i], outputs[i]);

            // Providing buttons for a skill overview entry
            final Button deleteSkillButton = new Button(multiplicationSymbol);
            final Button openSkillButton   = new Button(inputTemplate);

            // Building the skill overview entry
            final HBox skillBox = factory.createHorizontalBox(3, Insets.EMPTY, Pos.CENTER, deleteSkillButton, openSkillButton);

            // Completing the graphical design of the skill box
            designer.setBackground(Background.EMPTY, deleteSkillButton, openSkillButton);
            designer.setMaxWidth(Double.MAX_VALUE, openSkillButton);
            HBox.setHgrow(openSkillButton, Priority.ALWAYS);

            // Assigning button visual effects
            designer.setOnMouseEntered(redHighlightHandler, deleteSkillButton);
            designer.setOnMouseEntered(greenHighlightHandler, openSkillButton);
            designer.setOnMouseExited(removeHighlightHandler, deleteSkillButton, openSkillButton);

            // Providing bright mode support
            final Action brightModeSwitchAction = () -> {
                designer.setBorder(brightModeBorder, deleteSkillButton, openSkillButton);
                designer.setStyle(brightModeTextStyle, deleteSkillButton, openSkillButton);
            };
            brightModeSwitchActions.add(brightModeSwitchAction);

            // Providing dark mode support
            final Action darkModeSwitchAction = () -> {
                designer.setBorder(darkModeBorder, deleteSkillButton, openSkillButton);
                designer.setStyle(darkModeTextStyle, deleteSkillButton, openSkillButton);
            };
            darkModeSwitchActions.add(darkModeSwitchAction);

            // Applying color scheme on the skill box
            if (colorThemeHistory[0] == enterBrightModeEvent) brightModeSwitchAction.execute();
            else darkModeSwitchAction.execute();

            // Adding the skill box to the overview
            skillHolder.getChildren().add(skillBox);

            // Assigning the delete event
            deleteSkillButton.setOnAction(deleteSkillEvent -> {
                // Removing skill box from the overview
                skillHolder.getChildren().remove(skillBox);

                // Removing color scheme support
                brightModeSwitchActions.remove(brightModeSwitchAction);
                darkModeSwitchActions.remove(darkModeSwitchAction);

                // Removing associations from the assistant
                for (String input : inputs) assistant.removeAssociation(input);
            });

            // Assigning the open skill event
            openSkillButton.setOnAction(openSkillEvent -> {
                // Clearing saved variables
                for (Action a : deleteVariableActions) a.execute();
                deleteVariableActions.clear();

                // Updating the default fields
                placeholderTextField.setText(placeholder);
                inputTemplateTextField.setText(inputTemplate);
                outputTemplateTextField.setText(outputTemplate);

                // Generating the variables
                for (int i = 0; i < n; i++) {
                    final StringBuilder inputSB  = new StringBuilder(inputVariables[i][0]);
                    final StringBuilder outputSB = new StringBuilder(outputVariables[i][0]);
                    for (int j = 1; j < inputVariables[i].length; j++) inputSB.append(',').append(inputVariables[i][j]);
                    for (int j = 1; j < outputVariables[i].length; j++) outputSB.append(',').append(outputVariables[i][j]);
                    variableGenerator.generate(inputSB.toString(), outputSB.toString());
                }
            });

            // Clearing template fields
            placeholderTextField.clear();
            inputTemplateTextField.clear();
            outputTemplateTextField.clear();

            // Clearing saved variables
            for (Action a : deleteVariableActions) a.execute();
            deleteVariableActions.clear();
        });

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                 Grammar                                                   *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

        // Providing utility lists
        final List<TextField> leftSideRuleTextFields  = new ArrayList<>();
        final List<TextField> rightSideRuleTextFields = new ArrayList<>();
        final List<Label>     ruleErrorLabels         = new ArrayList<>();
        final List<Action>    deleteRuleActions       = new ArrayList<>();

        // Building the buttons
        final Button addRuleButton     = new Button("ADD RULE");
        final Button saveGrammarButton = new Button("SAVE GRAMMAR");

        // Building the labels
        final Label grammarEditorLabel     = factory.createLabel("GRAMMAR EDITOR", Pos.CENTER);
        final Label nonTerminalsLabel      = factory.createLabel("Non-Terminals", Pos.CENTER_RIGHT);
        final Label nonTerminalsArrowLabel = factory.createLabel(rightArrowSymbol, Pos.CENTER);
        final Label terminalsLabel         = factory.createLabel("Terminals", Pos.CENTER_RIGHT);
        final Label terminalsArrowLabel    = factory.createLabel(rightArrowSymbol, Pos.CENTER);
        final Label startSymbolLabel       = factory.createLabel("Start Symbol", Pos.CENTER_RIGHT);
        final Label startSymbolArrowLabel  = factory.createLabel(rightArrowSymbol, Pos.CENTER);

        // Building the text fields
        final TextField nonTerminalsTextField = factory.createTextField("1st placeholder 2nd placeholder, ...", Pos.CENTER);
        final TextField terminalsTextField    = factory.createTextField("1st character/word 2nd character/word, ...", Pos.CENTER);
        final TextField startSymbolTextField = factory.createTextField("1 non-terminal", Pos.CENTER);

        // Building the horizontal boxes
        final HBox nonTerminalsBox = factory.createHorizontalBox(5, Insets.EMPTY, Pos.CENTER, nonTerminalsLabel, nonTerminalsArrowLabel, nonTerminalsTextField);
        final HBox terminalsBox    = factory.createHorizontalBox(5, Insets.EMPTY, Pos.CENTER, terminalsLabel, terminalsArrowLabel, terminalsTextField);
        final HBox startSymbolBox  = factory.createHorizontalBox(5, Insets.EMPTY, Pos.CENTER, startSymbolLabel, startSymbolArrowLabel, startSymbolTextField);

        // Building the vertical boxes
        final VBox definitionHolder = factory.createVerticalBox(5, contentPadding, Pos.CENTER, nonTerminalsBox, terminalsBox, startSymbolBox);
        final VBox ruleHolder       = factory.createVerticalBox(5, contentPadding, Pos.TOP_CENTER);

        // Building the rule viewport
        final ScrollPane ruleViewport = factory.createScrollPane(scrollPaneStylesheet, ruleHolder);

        // Building the grammar editor
        final VBox grammarEditor = factory.createVerticalBox(3, defaultPadding, Pos.TOP_CENTER, grammarEditorLabel, definitionHolder, ruleViewport, addRuleButton, saveGrammarButton);

        // Completing the graphical design
        designer.setBackground(Background.EMPTY, addRuleButton, saveGrammarButton);
        designer.setBackground(Background.EMPTY, nonTerminalsTextField, terminalsTextField, startSymbolTextField);
        designer.setMaxWidth(Double.MAX_VALUE, addRuleButton, saveGrammarButton, grammarEditorLabel);
        designer.setMaxWidth(Double.MAX_VALUE, nonTerminalsTextField, terminalsTextField, startSymbolTextField);
        designer.setMinWidth(40, nonTerminalsArrowLabel, terminalsArrowLabel, startSymbolArrowLabel);
        designer.setMinWidth(120, nonTerminalsLabel, terminalsLabel, startSymbolLabel);
        designer.setMinWidth(240, nonTerminalsTextField, terminalsTextField, startSymbolTextField);
        designer.setMinHeight(30, grammarEditorLabel);
        VBox.setVgrow(ruleViewport, Priority.ALWAYS);

        // Assigning visual effects
        designer.setOnMouseEntered(greenHighlightHandler, addRuleButton, saveGrammarButton);
        designer.setOnMouseExited(removeHighlightHandler, addRuleButton, saveGrammarButton);

        // Providing bright mode support
        brightModeSwitchActions.add(() -> {
            designer.setBorder(brightModeBorder, grammarEditorLabel, definitionHolder, ruleViewport, addRuleButton, saveGrammarButton);
            designer.setBorder(brightModeBorder, nonTerminalsTextField, terminalsTextField, startSymbolTextField);
            designer.setStyle(brightModeTextStyle, grammarEditorLabel, addRuleButton, saveGrammarButton);
            designer.setStyle(brightModeTextStyle, nonTerminalsLabel, nonTerminalsArrowLabel, nonTerminalsTextField);
            designer.setStyle(brightModeTextStyle, terminalsLabel, terminalsArrowLabel, terminalsTextField);
            designer.setStyle(brightModeTextStyle, startSymbolLabel, startSymbolArrowLabel, startSymbolTextField);
        });

        // Providing dark mode support
        darkModeSwitchActions.add(() -> {
            designer.setBorder(darkModeBorder, grammarEditorLabel, definitionHolder, ruleViewport, addRuleButton, saveGrammarButton);
            designer.setBorder(darkModeBorder, nonTerminalsTextField, terminalsTextField, startSymbolTextField);
            designer.setStyle(darkModeTextStyle, grammarEditorLabel, addRuleButton, saveGrammarButton);
            designer.setStyle(darkModeTextStyle, nonTerminalsLabel, nonTerminalsArrowLabel, nonTerminalsTextField);
            designer.setStyle(darkModeTextStyle, terminalsLabel, terminalsArrowLabel, terminalsTextField);
            designer.setStyle(darkModeTextStyle, startSymbolLabel, startSymbolArrowLabel, startSymbolTextField);
        });

        // Providing the rule generator
        final RuleGenerator ruleGenerator = (lhs, rhs) -> {
            // Building the delete button
            final Button deleteRuleButton = new Button(multiplicationSymbol);

            // Building the labels
            final Label arrowLabel = factory.createLabel(rightArrowSymbol, Pos.CENTER);
            final Label errorLabel = factory.createLabel("", Pos.CENTER);

            // Building the text fields
            final TextField leftHandSideTextField = factory.createTextField("1 non-terminal", Pos.CENTER);
            final TextField rightHandSideTextField = factory.createTextField("1 non-terminal or up to 2 terminals", Pos.CENTER);

            // Building text field box with the delete button
            final HBox textFieldBox = factory.createHorizontalBox(5, Insets.EMPTY, Pos.CENTER, leftHandSideTextField, arrowLabel, rightHandSideTextField, deleteRuleButton);

            // Building the rule box
            final VBox ruleBox = factory.createVerticalBox(5, Insets.EMPTY, Pos.CENTER_LEFT, errorLabel, textFieldBox);

            // Completing the graphical design
            designer.setBackground(Background.EMPTY, deleteRuleButton, leftHandSideTextField, rightHandSideTextField);
            designer.setMaxWidth(Double.MAX_VALUE, leftHandSideTextField, rightHandSideTextField);
            designer.setMinWidth(40, arrowLabel);
            designer.setMinWidth(120, leftHandSideTextField);
            designer.setMinWidth(240, rightHandSideTextField);
            designer.setStyle(warningTextStyle, errorLabel);

            // Assigning visual effects
            designer.setOnMouseEntered(redHighlightHandler, deleteRuleButton);
            designer.setOnMouseExited(removeHighlightHandler, deleteRuleButton);

            // Adding rule to the rule holder
            ruleHolder.getChildren().add(ruleBox);

            // Adding items to their respective list
            leftSideRuleTextFields.add(leftHandSideTextField);
            rightSideRuleTextFields.add(rightHandSideTextField);
            ruleErrorLabels.add(errorLabel);

            // Providing bright mode support
            final Action brightModeSwitchAction = () -> {
                designer.setBorder(brightModeBorder, leftHandSideTextField, rightHandSideTextField);
                designer.setStyle(brightModeTextStyle, leftHandSideTextField, arrowLabel, rightHandSideTextField);
            };
            brightModeSwitchActions.add(brightModeSwitchAction);

            // Providing dark mode support
            final Action darkModeSwitchAction = () -> {
                designer.setBorder(darkModeBorder, leftHandSideTextField, rightHandSideTextField);
                designer.setStyle(darkModeTextStyle, leftHandSideTextField, arrowLabel, rightHandSideTextField);
            };
            darkModeSwitchActions.add(darkModeSwitchAction);

            // Applying current color scheme
            if (colorThemeHistory[0] == enterBrightModeEvent) brightModeSwitchAction.execute();
            else darkModeSwitchAction.execute();

            // Defining the delete rule event
            final Action deleteRuleAction = () -> {
                // Removing front-end entries
                ruleHolder.getChildren().remove(ruleBox);
                brightModeSwitchActions.remove(brightModeSwitchAction);
                darkModeSwitchActions.remove(darkModeSwitchAction);

                // Removing back-end entries
                leftSideRuleTextFields.remove(leftHandSideTextField);
                rightSideRuleTextFields.remove(rightHandSideTextField);
                ruleErrorLabels.remove(errorLabel);
            };

            // Assigning the delete event
            deleteRuleButton.setOnAction(deleteEvent -> {
                deleteRuleActions.remove(deleteRuleAction);
                deleteRuleAction.execute();
            });

            // Applying values
            leftHandSideTextField.setText(lhs);
            rightHandSideTextField.setText(rhs);

            // Returning the delete action in order to access it externally
            return deleteRuleAction;
        };

        // Assigning add rule functionality
        addRuleButton.setOnAction(newRuleEvent -> {
            final Action deleteRuleAction = ruleGenerator.generate("", "");
            deleteRuleActions.add(deleteRuleAction);
        });

        // Assigning save grammar functionality
        saveGrammarButton.setOnAction(saveGrammarEvent -> {
            // Accessing text field data
            final String nonTerminals = nonTerminalsTextField.getText();
            final String terminals    = terminalsTextField.getText();

            // Accessing base variables
            final String[] nt = nonTerminals.split(",");
            final String[] t    = terminals.split(",");
            final String   startSymbol  = startSymbolTextField.getText();

            // Removing leading and trailing spaces
            for (int i = 0; i < nt.length; i++) nt[i] = nt[i].trim();
            for (int i = 0; i < t.length; i++) t[i] = t[i].trim();

            // Utility
            final int n = leftSideRuleTextFields.size();
            final String[] leftHandSides  = new String[n];
            final String[] rightHandSides = new String[n];

            for (int i = 0; i < n; i++) {
                final TextField leftHandSideTextField  = leftSideRuleTextFields.get(i);
                final TextField rightHandSideTextField = rightSideRuleTextFields.get(i);
                final String leftHandSide  = leftHandSideTextField.getText();
                final String rightHandSide = rightHandSideTextField.getText();
                // Todo: Validate rules, if not valid, notify the user
                leftHandSides[i]  = leftHandSide;
                rightHandSides[i] = rightHandSide;
            }

            // Creating the grammar
            final var grammar = new ContextFreeGrammar();
            grammar.registerNonTerminals(nt);
            grammar.registerTerminals(t);
            grammar.setStartSymbol(startSymbol);

            // Todo: Add rules
            // Todo: Add grammar to assistant

            // Building buttons
            final Button deleteGrammarButton = new Button(multiplicationSymbol);
            final Button openGrammarButton   = new Button("CFG");

            // Building the grammar box
            final HBox grammarBox = factory.createHorizontalBox(5, Insets.EMPTY, Pos.CENTER, openGrammarButton, deleteGrammarButton);

            // Completing the graphical design
            designer.setMaxWidth(Double.MAX_VALUE, openGrammarButton);
            HBox.setHgrow(openGrammarButton, Priority.ALWAYS);

            // Assigning visual effects
            designer.setBackground(Background.EMPTY, deleteGrammarButton, openGrammarButton);
            designer.setOnMouseEntered(redHighlightHandler, deleteGrammarButton);
            designer.setOnMouseEntered(greenHighlightHandler, openGrammarButton);
            designer.setOnMouseExited(removeHighlightHandler, deleteGrammarButton, openGrammarButton);

            // Assigning the grammar box to the grammar overview
            grammarHolder.getChildren().add(grammarBox);

            // Providing bright mode support
            final Action brightModeSwitchAction = () -> {
                designer.setBorder(brightModeBorder, deleteGrammarButton, openGrammarButton);
                designer.setStyle(brightModeTextStyle, deleteGrammarButton, openGrammarButton);
            };
            brightModeSwitchActions.add(brightModeSwitchAction);

            // Providing dark mode support
            final Action darkModeSwitchAction = () -> {
                designer.setBorder(darkModeBorder, deleteGrammarButton, openGrammarButton);
                designer.setStyle(darkModeTextStyle, deleteGrammarButton, openGrammarButton);
            };
            darkModeSwitchActions.add(darkModeSwitchAction);

            // Applying current color scheme
            if (colorThemeHistory[0] == enterBrightModeEvent) brightModeSwitchAction.execute();
            else darkModeSwitchAction.execute();

            // Assigning the delete grammar event handler
            deleteGrammarButton.setOnAction(deleteGrammarEvent -> {
                grammarHolder.getChildren().remove(grammarBox);
                brightModeSwitchActions.remove(brightModeSwitchAction);
                darkModeSwitchActions.remove(darkModeSwitchAction);
                // Todo: Remove grammar from assistant
            });

            // Assigning the open grammar event handler
            openGrammarButton.setOnAction(openGrammarEvent -> {
                // Clearing existing rules
                for (Action a : deleteRuleActions) a.execute();
                deleteRuleActions.clear();

                // Updating the base text fields
                nonTerminalsTextField.setText(nonTerminals);
                terminalsTextField.setText(terminals);
                startSymbolTextField.setText(startSymbol);

                // Recreating the rules
                for (int i = 0; i < n; i++) {
                    final Action deleteRuleAction = ruleGenerator.generate(leftHandSides[i], rightHandSides[i]);
                    deleteRuleActions.add(deleteRuleAction);
                }
            });

            // Clearing base variables
            nonTerminalsTextField.clear();
            terminalsTextField.clear();
            startSymbolTextField.clear();

            // Clearing saved rules
            for (Action a : deleteRuleActions) a.execute();
            deleteRuleActions.clear();
        });

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                 Editor                                                    *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

        // Building the complete editor
        final HBox editor = factory.createHorizontalBox(0, Insets.EMPTY, Pos.CENTER, skillOverview, skillEditor, grammarEditor, grammarOverview);

        // Completing the design
        designer.setMinWidth(200, skillOverview, grammarOverview);
        designer.setMinWidth(500, skillEditor, grammarEditor);
        HBox.setHgrow(skillOverview, Priority.ALWAYS);
        HBox.setHgrow(skillEditor, Priority.ALWAYS);
        HBox.setHgrow(grammarEditor, Priority.ALWAYS);
        HBox.setHgrow(grammarOverview, Priority.ALWAYS);
        VBox.setVgrow(editor, Priority.ALWAYS);

        // Providing bright mode support
        brightModeSwitchActions.add(() -> designer.setBorder(brightModeBorder, editor));

        // Providing dark mode support
        darkModeSwitchActions.add(() -> designer.setBorder(darkModeBorder, editor));

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                  Start                                                    *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

        // Todo: Link face detection technology with the start screen buttons, so it can register a new face or confirm the old face

        // Building the buttons
        final Button signInButton = new Button("SIGN-IN");
        final Button signUpButton = new Button("SIGN-UP");

        // Creating the start panel
        final VBox startPanel = factory.createVerticalBox(5, defaultPadding, Pos.CENTER, signInButton, signUpButton);

        // Completing the design
        designer.setBackground(Background.EMPTY, signInButton, signUpButton);
        designer.setMaxWidth(180, signInButton, signUpButton);
        designer.setMinWidth(180, signInButton, signUpButton);

        // Assigning visual effects
        designer.setOnMouseEntered(defaultHighlightHandler, signInButton, signUpButton);
        designer.setOnMouseExited(removeHighlightHandler, signInButton, signUpButton);
        VBox.setVgrow(startPanel, Priority.ALWAYS);

        // Providing bright mode support
        brightModeSwitchActions.add(() -> {
            designer.setBorder(brightModeBorder, signInButton, signUpButton, startPanel);
            designer.setStyle(brightModeTextStyle, signInButton, signUpButton);
        });

        // Providing dark mode support
        darkModeSwitchActions.add(() -> {
            designer.setBorder(darkModeBorder, signInButton, signUpButton, startPanel);
            designer.setStyle(darkModeTextStyle, signInButton, signUpButton);
        });

        // Assigning functionality to buttons
        final EventHandler<ActionEvent> enterChatSceneHandler = event -> {
            // Capturing a picture
            camController.takePicture("test");

            // Detecting face
            System.out.println(faceDetector.faceDetected("test"));

            // Removing the title bar from the start scene
            sceneRoots[0].getChildren().remove(titleBar);

            // Adding the title bar to the chat scene
            sceneRoots[1].getChildren().add(0, titleBar);

            // Enabling the chat - editor switch button
            switchSceneButton.setDisable(false);

            // Loading the chat scene centered on the screen
            primaryStage.setScene(scenes[1]);
            primaryStage.centerOnScreen();
        };
        signInButton.setOnAction(enterChatSceneHandler);
        signUpButton.setOnAction(enterChatSceneHandler);

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                  Roots                                                    *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

        // Building the scene roots
        sceneRoots[0] = factory.createVerticalBox(3, defaultPadding, Pos.CENTER, titleBar, startPanel);
        sceneRoots[1] = factory.createVerticalBox(3, defaultPadding, Pos.CENTER, chatViewport, chatInput);
        sceneRoots[2] = factory.createVerticalBox(3, defaultPadding, Pos.CENTER, editor);

        // Completing the design
        designer.setBackground(Background.EMPTY, sceneRoots);

        // Providing bright mode support
        brightModeSwitchActions.add(() -> designer.setBorder(brightModeBorder, sceneRoots));

        // Providing dark mode support
        darkModeSwitchActions.add(() -> designer.setBorder(darkModeBorder, sceneRoots));


        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                  Scene                                                    *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

        // Creating scenes
        scenes[0] = new Scene(sceneRoots[0], startSceneSize[0], startSceneSize[1]);
        scenes[1] = new Scene(sceneRoots[1], chatSceneSize[0], chatSceneSize[1]);
        scenes[2] = new Scene(sceneRoots[2], editorSceneSize[0], editorSceneSize[1]);

        // Providing support for bright mode scene
        brightModeSwitchActions.add(() -> {
            for (Scene s : scenes) s.setFill(brightMode);
        });

        // Providing support for dark mode scene
        darkModeSwitchActions.add(() -> {
            for (Scene s : scenes) s.setFill(darkMode);
        });

        // Selecting initial color scheme
        switchColorThemeHandler.handle(enterDarkModeEvent);

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                  Stage                                                     *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

        // Preparing the stage
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Digital Assistant");

        // Selecting initial scene
        // Warning; If choosing another scene as the starting scene except the start scene,
        //          make sure the title bar is assigned to that every scene
        primaryStage.setScene(scenes[0]);

        // Disabling the scene switch button in the title bar since we choose to start with the start scene
        switchSceneButton.setDisable(true);

        // Showing the application
        primaryStage.show();
    }

    private interface Action {
        void execute();
    }

    private interface RuleGenerator {
        Action generate(String lhs, String rhs);
    }

    private interface VariableGenerator {
        Action generate(String in, String out);
    }
}