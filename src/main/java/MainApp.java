import engine.Assistant;
import engine.PlaceholderCounter;
import engine.PlaceholderReplacer;
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

        // Assistant
        final var assistant   = new Assistant();

        // Insets
        final Insets padding = new Insets(5, 5, 5, 5);
        final Insets viewportContentPadding = new Insets(10, 5, 10, 5);

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
        final VBox       chatHistory  = factory.createVerticalBox(5, padding, Pos.TOP_CENTER);
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
        final Label skillLabel   = factory.createLabel("Skill", Pos.CENTER);
        final Label grammarLabel = factory.createLabel("Grammar", Pos.CENTER);

        // Building content holders
        final VBox skillHolder   = factory.createVerticalBox(5, padding, Pos.TOP_CENTER);
        final VBox grammarHolder = factory.createVerticalBox(5, padding, Pos.TOP_CENTER);

        // Building scroll panes
        final ScrollPane skillViewport   = factory.createScrollPane(scrollPaneStylesheet, skillHolder);
        final ScrollPane grammarViewport = factory.createScrollPane(scrollPaneStylesheet, grammarHolder);

        // Building overviews
        final VBox skillOverview   = factory.createVerticalBox(3, padding, Pos.CENTER, skillLabel, skillViewport);
        final VBox grammarOverview = factory.createVerticalBox(3, padding, Pos.CENTER, grammarLabel, grammarViewport);

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
        final List<TextField> inputFields      = new ArrayList<>();
        final List<TextField> outputFields     = new ArrayList<>();
        final List<Label>     skillErrorLabels = new ArrayList<>();

        // Building the button
        final Button addVariablesButton = new Button("ADD VARIABLES");
        final Button saveSkillButton    = new Button("SAVE SKILL");

        // Building the labels
        final Label skillEditorLabel         = factory.createLabel("Skill Editor", Pos.CENTER);
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
        final VBox templateHolder = factory.createVerticalBox(5, viewportContentPadding, Pos.CENTER, placeholderBox, inputTemplateBox, outputTemplateBox);
        final VBox variableHolder = factory.createVerticalBox(5, padding, Pos.TOP_CENTER);

        // Building the editor viewport
        final ScrollPane skillEditorViewport = factory.createScrollPane(scrollPaneStylesheet, variableHolder);

        // Building the editor
        final VBox skillEditor = factory.createVerticalBox(3, padding, Pos.CENTER, skillEditorLabel, templateHolder, skillEditorViewport, addVariablesButton, saveSkillButton);

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

            // Assigning delete event
            deleteButton.setOnAction(deleteEvent -> {
                variableHolder.getChildren().remove(finalBox);
                inputFields.remove(inputField);
                outputFields.remove(outputField);
                skillErrorLabels.remove(errorLabel);
            });

            // Setting the text values of the fields
            inputField.setText(in);
            outputField.setText(out);
        };

        // Assigning add variables button functionality
        addVariablesButton.setOnAction(addEvent -> variableGenerator.generate("", ""));

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

            // Defining the delete event
            deleteSkillButton.setOnAction(deleteSkillEvent -> {
                // Removing skill box from the overview
                skillHolder.getChildren().remove(skillBox);

                // Removing color scheme support
                brightModeSwitchActions.remove(brightModeSwitchAction);
                darkModeSwitchActions.remove(darkModeSwitchAction);

                // Removing associations from the assistant
                for (String input : inputs) assistant.removeAssociation(input);
            });

            // Defining the open skill event
            openSkillButton.setOnAction(openSkillEvent -> {
                // Clearing any existing entries in the variable holder
                variableHolder.getChildren().clear();

                // Updating the default fields
                placeholderTextField.setText(placeholder);
                inputTemplateTextField.setText(inputTemplate);
                outputTemplateTextField.setText(outputTemplate);

                // Generating the variables
                for (int i = 0; i < n; i++) variableGenerator.generate(inputs[i], outputs[i]);
            });

            // Clearing template fields
            placeholderTextField.clear();
            inputTemplateTextField.clear();
            outputTemplateTextField.clear();

            // Clearing saved variables
            variableHolder.getChildren().clear();
        });

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                   Rules                                                   *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

        // Defining a utility list
        final List<Rule> rules = new ArrayList<>();

        // Building the box to contain rules
        final VBox ruleHolder = factory.createVerticalBox(5, padding, Pos.TOP_CENTER);

        // Providing how graphically displayable rules are generated
        final RuleGenerator ruleGenerator = (lhs, rhs) -> {
            // Providing a constant memory reference (required for deleting the rule)
            final Rule[] arr = new Rule[1];

            // Building a displayable rule
            final Button deleteRuleButton = new Button(multiplicationSymbol);
            final Label  ruleWarningLabel = factory.createLabel("", Pos.CENTER);
            final Label  arrowLabel       = factory.createLabel(rightArrowSymbol, Pos.CENTER);
            final TextField lhsTextField  = factory.createTextField("1 non-terminal", Pos.CENTER);
            final TextField rhsTextField  = factory.createTextField("1 terminal or up to 2 non-terminals", Pos.CENTER);
            final HBox ruleBox  = factory.createHorizontalBox(5, Insets.EMPTY, Pos.CENTER, lhsTextField, arrowLabel, rhsTextField, deleteRuleButton);
            final VBox finalBox = factory.createVerticalBox(0, Insets.EMPTY, Pos.CENTER, ruleWarningLabel, ruleBox);

            // Completing the rule design
            designer.setBackground(Background.EMPTY, deleteRuleButton, lhsTextField, rhsTextField);
            designer.setPrefWidth(120, lhsTextField);
            designer.setPrefWidth(240, rhsTextField);
            designer.setStyle(warningTextStyle, ruleWarningLabel);
            lhsTextField.setAlignment(Pos.CENTER);
            lhsTextField.setText(lhs);
            rhsTextField.setAlignment(Pos.CENTER);
            rhsTextField.setText(rhs);

            // Providing bright mode support
            final Action brightModeSwitchAction = () -> {
                designer.setBorder(brightModeBorder, deleteRuleButton, lhsTextField, rhsTextField);
                designer.setStyle(brightModeTextStyle, deleteRuleButton, lhsTextField, rhsTextField, arrowLabel);
            };
            brightModeSwitchActions.add(brightModeSwitchAction);

            // Providing dark mode support
            final Action darkModeSwitchAction = () -> {
                designer.setBorder(darkModeBorder, deleteRuleButton, lhsTextField, rhsTextField);
                designer.setStyle(darkModeTextStyle, deleteRuleButton, lhsTextField, rhsTextField, arrowLabel);
            };
            darkModeSwitchActions.add(darkModeSwitchAction);

            // Assigning visual effects
            designer.setOnMouseEntered(redHighlightHandler, deleteRuleButton);
            designer.setOnMouseExited(removeHighlightHandler, deleteRuleButton);

            // Applying current color scheme
            if (colorThemeHistory[0] == enterBrightModeEvent) brightModeSwitchAction.execute();
            else darkModeSwitchAction.execute();

            // Adding rule to the editor
            ruleHolder.getChildren().add(finalBox);

            // Defining the rule delete event handler
            EventHandler<ActionEvent> deleteRuleHandler = deleteRuleEvent -> {
                rules.remove(arr[0]);
                ruleHolder.getChildren().remove(finalBox);
                brightModeSwitchActions.remove(brightModeSwitchAction);
                darkModeSwitchActions.remove(darkModeSwitchAction);
            };

            // Assigning functionality to delete rule button
            deleteRuleButton.setOnAction(deleteRuleHandler);

            // Assigning pre-processing functionality to lhs text field
            lhsTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.contains(" ")) ruleWarningLabel.setText("Warning: Left-hand sided text field should NOT contain spaces!");
                else ruleWarningLabel.setText("");
            });

            // Assigning pre-processing functionality to rhs text field
            rhsTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                final String[] segments = newValue.split(" ");
                if (segments.length > 2) ruleWarningLabel.setText("Warning: Right-handed sided text field should NOT contain MORE THAN 1 space");
                else ruleWarningLabel.setText("");
            });


            // Defining the back-end access point of data located in the front-end
            arr[0] = new Rule() {
                @Override
                public void delete() {
                    deleteRuleHandler.handle(null);
                }

                @Override
                public String getLeftHandSide() {
                    return lhsTextField.getText();
                }

                @Override
                public String getRightHandSide() {
                    return rhsTextField.getText();
                }

                @Override
                public void setWarning(String s) {
                    ruleWarningLabel.setText(s);
                }
            };

            // Updating the utility list
            rules.add(arr[0]);
        };

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                                 Grammar                                                   *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

        // Building buttons
        final Button newRuleButton       = new Button("ADD RULE");
        final Button saveGrammarButton   = new Button("SAVE RULES");

        // Building labels
        final Label grammarEditorLabel = factory.createLabel("Grammar Editor", Pos.CENTER);

        // Building the scroll pane to browse rules
        final ScrollPane ruleViewport  = factory.createScrollPane(scrollPaneStylesheet, ruleHolder);

        // Building the editor
        final VBox grammarEditor  = factory.createVerticalBox(3, padding, Pos.CENTER, grammarEditorLabel, ruleViewport, newRuleButton, saveGrammarButton);

        // Completing the design
        designer.setBackground(Background.EMPTY, newRuleButton, saveGrammarButton);
        designer.setMaxWidth(Double.MAX_VALUE, newRuleButton, saveGrammarButton, grammarEditorLabel);
        designer.setMinHeight(30, grammarEditorLabel);
        HBox.setHgrow(saveGrammarButton, Priority.ALWAYS);
        VBox.setVgrow(ruleViewport, Priority.ALWAYS);
        VBox.setVgrow(grammarEditor, Priority.ALWAYS);

        // Providing bright mode support
        brightModeSwitchActions.add(() -> {
            designer.setBorder(brightModeBorder, newRuleButton, saveGrammarButton);
            designer.setBorder(brightModeBorder, grammarEditorLabel);
            designer.setBorder(brightModeBorder, ruleViewport);
            designer.setStyle(brightModeTextStyle, newRuleButton, saveGrammarButton);
            designer.setStyle(brightModeTextStyle, grammarEditorLabel);
        });

        // Providing dark mode support
        darkModeSwitchActions.add(() -> {
            designer.setBorder(darkModeBorder, newRuleButton, saveGrammarButton);
            designer.setBorder(darkModeBorder, grammarEditorLabel);
            designer.setBorder(darkModeBorder, ruleViewport);
            designer.setStyle(darkModeTextStyle, newRuleButton, saveGrammarButton);
            designer.setStyle(darkModeTextStyle, grammarEditorLabel);
        });

        // Assigning visual effects
        designer.setOnMouseEntered(greenHighlightHandler, newRuleButton, saveGrammarButton);
        designer.setOnMouseExited(removeHighlightHandler, newRuleButton, saveGrammarButton);

        // Assigning functionality to the new rule button
        newRuleButton.setOnAction(newRuleEvent -> ruleGenerator.generate("", ""));

        // Assigning functionality to the save grammar button
        saveGrammarButton.setOnAction(event -> {
            // Validating every rule
            for (var rule : rules) {
                // Left hand side validation
                final String lhs = rule.getLeftHandSide();
                if (lhs.isBlank()) {
                    rule.setWarning("Error: Left-hand sided text field is blank!");
                    return;
                } else if (lhs.contains(" ")) {
                    rule.setWarning("Error: Left-hand sided text field contains one or more spaces!");
                    return;
                }

                // Right hand side validation
                final String rhs = rule.getRightHandSide();
                if (rhs.isBlank()) {
                    rule.setWarning("Error: Right-hand sided text field is blank!");
                    return;
                }
                final String[] segments = rhs.split(" ");
                if (segments.length > 2) {
                    rule.setWarning("Error: Right-hand sided text field contains more than two or more spaces!");
                    return;
                }
            }

            // Creating a map to store all rules (aka grammar)
            final Map<String, List<String[]>> ruleMap = new HashMap<>();

            // Browsing through every rule again...
            for (var rule : rules) {
                // Accessing both sides of the rule
                final String lhs = rule.getLeftHandSide();
                final String[] rhs = rule.getRightHandSide().split(" ");

                // Accessing map entry, if there is no map entry yet, create a new one
                final List<String[]> options = ruleMap.computeIfAbsent(lhs, k -> new ArrayList<>());

                // Adding the substitution options (aka right hand side values) to the entry
                options.add(rhs);
            }

            // Clearing all rules registered to the editor
            for (int i = 0; i < rules.size(); i++) {
                rules.get(0).delete();
            }


            // Building buttons
            final Button openGrammarButton   = new Button("GRAMMAR");
            final Button deleteGrammarButton = new Button(multiplicationSymbol);

            // Building the grammar representation
            final HBox grammarBox = factory.createHorizontalBox(3, Insets.EMPTY, Pos.CENTER, openGrammarButton, deleteGrammarButton);

            // Completing the design
            designer.setBackground(Background.EMPTY, openGrammarButton, deleteGrammarButton);
            designer.setMaxWidth(Double.MAX_VALUE, openGrammarButton);
            HBox.setHgrow(openGrammarButton, Priority.ALWAYS);

            // Adding the grammar box
            grammarHolder.getChildren().add(grammarBox);

            // Assigning visual effects
            designer.setOnMouseEntered(redHighlightHandler, deleteGrammarButton);
            designer.setOnMouseEntered(greenHighlightHandler, openGrammarButton);
            designer.setOnMouseExited(removeHighlightHandler, deleteGrammarButton, openGrammarButton);

            // Providing bright mode support
            final Action brightModeSwitchAction = () -> {
                designer.setBorder(brightModeBorder, openGrammarButton, deleteGrammarButton);
                designer.setStyle(brightModeTextStyle, openGrammarButton, deleteGrammarButton);
            };
            brightModeSwitchActions.add(brightModeSwitchAction);

            // Providing dark mode support
            final Action darkModeSwitchAction = () -> {
                designer.setBorder(darkModeBorder, openGrammarButton, deleteGrammarButton);
                designer.setStyle(darkModeTextStyle, openGrammarButton, deleteGrammarButton);
            };
            darkModeSwitchActions.add(darkModeSwitchAction);

            // Assigning functionality
            deleteGrammarButton.setOnAction(deleteSkillEvent -> {
                grammarHolder.getChildren().remove(grammarBox);
                brightModeSwitchActions.remove(brightModeSwitchAction);
                darkModeSwitchActions.remove(darkModeSwitchAction);
            });
            openGrammarButton.setOnAction(openSkillEvent -> {
                // Clearing all rules registered to the editor
                for (int i = 0; i < rules.size(); i++) {
                    rules.get(0).delete();
                }

                // Reading every entry in the rule map to re-generate the rules on the editor
                for (var mapEntry : ruleMap.entrySet()) {
                    final String lhs = mapEntry.getKey();
                    for (String[] rhs : mapEntry.getValue()) {
                        final StringBuilder sb = new StringBuilder(rhs[0]);
                        for (int i = 1; i < rhs.length; i++) sb.append(' ').append(rhs[i]);
                        ruleGenerator.generate(lhs, sb.toString());
                    }
                }
            });

            // Applying current color theme
            if (colorThemeHistory[0] == enterBrightModeEvent) brightModeSwitchAction.execute();
            else darkModeSwitchAction.execute();
        });

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *                                            Complete Editor                                                *
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
        final VBox startPanel = factory.createVerticalBox(5, padding, Pos.CENTER, signInButton, signUpButton);

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
        sceneRoots[0] = factory.createVerticalBox(3, padding, Pos.CENTER, titleBar, startPanel);
        sceneRoots[1] = factory.createVerticalBox(3, padding, Pos.CENTER, chatViewport, chatInput);
        sceneRoots[2] = factory.createVerticalBox(3, padding, Pos.CENTER, editor);

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

    private interface Rule {
        void delete();
        String getLeftHandSide();
        String getRightHandSide();
        void setWarning(String s);
    }

    private interface RuleGenerator {
        void generate(String lhs, String rhs);
    }

    private interface VariableGenerator {
        void generate(String in, String out);
    }
}