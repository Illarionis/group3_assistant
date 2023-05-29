package gui;

import engine.PlaceholderCounter;
import engine.PlaceholderReplacer;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

public final class SkillEditor extends Editor implements Displayable, Styleable {
    private final List<ValuePair> valuePairs;

    public SkillEditor(Factory f) {
        // Building the default editor look
        super(f);

        // Adjusting the value of the template labels
        titleHolder.setText("SKILL EDITOR");
        topLabel.setText("Placeholder " + arrowSymbol + " ");
        middleLabel.setText("Input Template " + arrowSymbol + " ");
        bottomLabel.setText("Output Template " + arrowSymbol + " ");

        // Adjusting the prompt text of the template fields
        topTextField.setPromptText("At least one symbol");
        middleTextField.setPromptText("At least one placeholder");
        bottomTextField.setPromptText("At least one placeholder");

        // Adjusting the value of the buttons
        addButton.setText("ADD VALUES");
        saveButton.setText("SAVE SKILL");

        // Providing a list to store the (input, output)-values
        valuePairs = new ArrayList<>();

        // Providing the add value functionality
        addButton.setOnAction(newValueEvent -> {
            // Providing a (input, output) value pair
            final var value = new ValuePair(f);

            // Storing the (input, output) value pair
            valuePairs.add(value);

            // Displaying the (input, output) value pair through the value holder
            valueHolder.getChildren().add(value.getPanel());

            // Defining what to do when the delete button of the value pair has been clicked
            value.setDeleteEvent(() -> {
                // Removing the value pair from the back-end list
                valuePairs.remove(value);

                // Removing the value pair from the front-end list
                valueHolder.getChildren().remove(value.getPanel());
            });

            // Applying the current color scheme to the value pair
            value.setBorder(titleHolder.getBorder());
            value.setStyle(titleHolder.getStyle());
        });

        // Providing the save skill functionality
        saveButton.setOnAction(newSkillEvent -> {
            // Accessing the (input, output) structure
            final String placeholder    = topTextField.getText();
            final String inputTemplate  = middleTextField.getText();
            final String outputTemplate = bottomTextField.getText();

            // Counting the number of placeholders in the template
            final int inputPlaceholderCount  = PlaceholderCounter.countPlaceholders(inputTemplate, placeholder);
            final int outputPlaceholderCount = PlaceholderCounter.countPlaceholders(outputTemplate, placeholder);

            // Storing the number of (input, output)-values
            final int n = valuePairs.size();

            // Providing arrays to store the (input, output)-values
            final String[]   rawInputValues  = new String[n];
            final String[]   rawOutputValues = new String[n];
            final String[][] inputValues     = new String[n][];
            final String[][] outputValues    = new String[n][];

            // Storing all (input, output)-values
            for (int i = 0; i < n; i++) {
                // Accessing the (input, output) value pair
                final var value = valuePairs.get(i);

                // Storing the values raw
                rawInputValues[i]  = value.getInput();
                rawOutputValues[i] = value.getOutput();

                // Processing the raw values
                inputValues[i]  = rawInputValues[i].split(",");
                outputValues[i] = rawOutputValues[i].split(",");

                // Validating the value lengths
                if (inputValues[i].length != inputPlaceholderCount) {
                    // Providing a warning of the mismatched length
                    value.setWarning("Input Warning: #values != #placeholders");

                    // Exiting the new skill event functionality prematurely
                    return;
                } else if (outputValues[i].length != outputPlaceholderCount) {
                    // Providing a warning of the mismatched length
                    value.setWarning("Output Warning: #values != #placeholders");

                    // Exiting the new skill event functionality prematurely
                    return;
                }

                // Trimming the input values to remove unnecessary leading and trailing spaces
                for (int j = 0; j < inputPlaceholderCount; j++) inputValues[i][j] = inputValues[i][j].trim();

                // Trimming the output values to remove unnecessary leading and trailing spaces
                for (int j = 0; j < outputPlaceholderCount; j++) outputValues[i][j] = outputValues[i][j].trim();
            }

            // Providing arrays to store the actual (input, output) values
            final String[] inputs  = new String[n];
            final String[] outputs = new String[n];

            // Substituting placeholders in templates to obtain the actual input and output values
            for (int i = 0; i < n; i++) {
                inputs[i]  = PlaceholderReplacer.replacePlaceholders(inputTemplate, placeholder, inputValues[i]);
                outputs[i] = PlaceholderReplacer.replacePlaceholders(outputTemplate, placeholder, outputValues[i]);
                System.out.println(inputs[i]);
            }

            // Updating the assistant
            if (a != null) {
                if (a.getGrammarList().size() > 0) {
                    // Providing an input validation progress
                    for (int i = 0; i < n; i++) {
                        // Validating the input through the assistant
                        if (a.validate(inputs[i])) continue;

                        // Accessing the value pair of the invalid input
                        final var value = valuePairs.get(i);

                        // Providing an invalid input warning
                        value.setWarning("Warning: Input does not belong to any grammar!");

                        // Exiting the new skill event prematurely
                        return;
                    }
                }
                // Associating all (input, output) values
                for (int i = 0; i < n; i++) a.associate(inputs[i], outputs[i]);
            }

            // Clearing the editor since all values have been stored (and/or processed)
            topTextField.clear();
            middleTextField.clear();
            bottomTextField.clear();
            valueHolder.getChildren().clear();
            valuePairs.clear();

            // Providing a skill button to control the state of the skill in the overview
            final var skillButton = new OverviewButton(f);
            skillButton.setText(inputTemplate);

            // Storing the skill (button)
            overviewButtons.add(skillButton);

            // Adding the skill (button) to the skill overview
            if (o != null) o.add(skillButton.getPanel());

            // Applying the color scheme to the button
            skillButton.setBorder(titleHolder.getBorder());
            skillButton.setStyle(titleHolder.getStyle());

            // Providing a process to remove the skill from local list, skill overview and assistant
            skillButton.setDeleteEvent(() -> {
                if (o != null) o.remove(skillButton.getPanel());
                if (a != null) for (int i = 0; i < n; i++) a.removeAssociation(inputs[i]);
                overviewButtons.remove(skillButton);
            });

            // Providing a process to display the skill in the skill editor
            skillButton.setDisplayEvent(() -> {
                // Clearing existing values from the editor
                topTextField.clear();
                middleTextField.clear();
                bottomTextField.clear();
                valueHolder.getChildren().clear();
                valuePairs.clear();

                // Updating the structural text fields
                topTextField.setText(placeholder);
                middleTextField.setText(inputTemplate);
                bottomTextField.setText(outputTemplate);

                // Generating new value pairs
                for (int i = 0; i < n; i++) {
                    // Generating a new value pair
                    final var value = new ValuePair(f);

                    // Storing the raw input and output value
                    value.setInput(rawInputValues[i]);
                    value.setOutput(rawOutputValues[i]);

                    // Storing the (input, output) value pair
                    valuePairs.add(value);

                    // Displaying the (input, output) value pair through the value holder
                    valueHolder.getChildren().add(value.getPanel());

                    // Defining what to do when the delete button of the value pair has been clicked
                    value.setDeleteEvent(() -> {
                        // Removing the value pair from the back-end list
                        valuePairs.remove(value);

                        // Removing the value pair from the front-end list
                        valueHolder.getChildren().remove(value.getPanel());
                    });

                    // Applying the current color scheme to the value pair
                    value.setBorder(titleHolder.getBorder());
                    value.setStyle(titleHolder.getStyle());
                }
            });
        });
    }

    @Override
    public void setBorder(Border b) {
        super.setBorder(b);
        for (var value  : valuePairs) value.setBorder(b);
    }

    @Override
    public void setStyle(String s) {
        super.setStyle(s);
        for (var value  : valuePairs) value.setStyle(s);
    }

    private static final class ValuePair implements Displayable, Styleable {
        private final Button delete;
        private final Label warningHolder, inputLabel, outputLabel;
        private final TextField inputField, outputField;
        private final VBox panel;

        public ValuePair(Factory f) {
            final String arrowSymbol = Character.toString(8594);

            // Providing a button to delete the variables from the skill editor
            delete = new Button(Character.toString(10005));
            delete.setBackground(Background.EMPTY);
            delete.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            // Providing a label to display warnings concerning the input and output values
            warningHolder = f.createLabel("");
            warningHolder.setStyle(WARNING_TEXT_STYLE);

            // Providing a label to recognize the input text field
            inputLabel = f.createLabel("Input Values " + arrowSymbol + " ", Pos.CENTER_RIGHT);
            inputLabel.setMinWidth(150);

            // Providing a text field to get the input values
            inputField = f.createTextField("value 1, value 2, ...", Pos.CENTER);
            inputField.setBackground(Background.EMPTY);
            inputField.setMinWidth(240);
            inputField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!warningHolder.getText().isBlank()) warningHolder.setText("");
            });

            // Providing a box to display the input components horizontally
            final HBox inputBox = f.createHBox(inputLabel, inputField);

            // Providing a label to recognize the output text field
            outputLabel = f.createLabel("Output Values " + arrowSymbol + " ", Pos.CENTER_RIGHT);
            outputLabel.setMinWidth(150);

            // Providing a text field to get the output values
            outputField = f.createTextField("value 1, value 2, ...", Pos.CENTER);
            outputField.setBackground(Background.EMPTY);
            outputField.setMinWidth(240);
            outputField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!warningHolder.getText().isBlank()) warningHolder.setText("");
            });

            // Providing a box to display the output components horizontally
            final HBox outputBox = f.createHBox(outputLabel, outputField);

            // Providing a box to display the input and output box vertically
            final VBox ioBox = f.createVBox(3, Pos.CENTER, inputBox, outputBox);

            // Providing a box to include the delete button with the IO box
            final HBox deleteBox = f.createHBox(3, ioBox, delete);
            VBox.setVgrow(deleteBox, Priority.ALWAYS);

            // Providing the final displayable panel
            panel = f.createVBox(3, Pos.CENTER, warningHolder, deleteBox);

            // Assigning visual effects to the delete button
            delete.setOnMouseEntered(enterEvent -> delete.setBackground(Effects.RED_OVERLAY_BACKGROUND));
            delete.setOnMouseExited(exitEvent -> delete.setBackground(Background.EMPTY));
        }

        public String getInput() {
            return inputField.getText();
        }

        public String getOutput() {
            return outputField.getText();
        }

        public void setInput(String s) {
            inputField.setText(s);
        }

        public void setOutput(String s) {
            outputField.setText(s);
        }

        public void setWarning(String s) {
            warningHolder.setText(s);
        }

        public void setDeleteEvent(Runnable process) {
            delete.setOnAction(event -> process.run());
        }

        @Override
        public Region getPanel() {
            return panel;
        }

        @Override
        public void setBorder(Border b) {
            inputField.setBorder(b);
            outputField.setBorder(b);
        }

        @Override
        public void setStyle(String s) {
            delete.setStyle(s);
            inputLabel.setStyle(s);
            inputField.setStyle(s);
            outputLabel.setStyle(s);
            outputField.setStyle(s);
        }
    }
}
