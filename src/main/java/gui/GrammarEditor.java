package gui;

import engine.ContextFreeGrammar;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

public final class GrammarEditor extends Editor implements Displayable, Styleable {
    private final List<Rule> rules;

    public GrammarEditor(Factory f) {
        // Building the default editor look
        super(f);

        // Adjusting the value of the template labels
        titleHolder.setText("GRAMMAR EDITOR");
        topLabel.setText("Non-Terminals " + arrowSymbol + " ");
        middleLabel.setText("Terminals " + arrowSymbol + " ");
        bottomLabel.setText("Start Symbol " + arrowSymbol + " ");

        // Adjusting the prompt text of the template fields
        topTextField.setPromptText("placeholder 1, placeholder 2, ...");
        middleTextField.setPromptText("value 1, value 2, ...");
        bottomTextField.setPromptText("1 non-terminal");

        // Adjusting the value of the buttons
        addButton.setText("ADD RULE");
        saveButton.setText("SAVE GRAMMAR");

        // Providing a list to store all rules
        rules = new ArrayList<>();

        // Providing the add rule functionality
        addButton.setOnAction(newRuleEvent -> {
            // Providing a new rule
            final var rule = new Rule(f);

            // Storing the rule
            rules.add(rule);

            // Displaying the rule through the rule holder
            valueHolder.getChildren().add(rule.getPanel());

            // Defining what to do when the delete button of the rule pair has been clicked
            rule.setDeleteEvent(() -> {
                // Removing the rule from the back-end list
                rules.remove(rule);

                // Removing the rule pair from the front-end list
                valueHolder.getChildren().remove(rule.getPanel());
            });

            // Applying the current color scheme to the rule pair
            rule.setBorder(titleHolder.getBorder());
            rule.setStyle(titleHolder.getStyle());
        });

        // Providing the save grammar functionality
        saveButton.setOnAction(newGrammarEvent -> {
            // Accessing the grammar structure
            final String rawNonTerminals = topTextField.getText();
            final String rawTerminals    = middleTextField.getText();
            final String startSymbol     = bottomTextField.getText();

            // Processing the raw values
            final String[] nonTerminals = rawNonTerminals.split(",");
            final String[] terminals    = rawTerminals.split(",");

            // Trimming non-terminals to remove leading and trailing spaces
            for (int i = 0; i < nonTerminals.length; i++) nonTerminals[i] = nonTerminals[i].trim();

            // Trimming terminals to remove leading and trailing spaces
            for (int i = 0; i < terminals.length; i++) terminals[i] = terminals[i].trim();

            // Providing grammar to store the values
            final var grammar = new ContextFreeGrammar();
            grammar.registerNonTerminals(nonTerminals);
            grammar.registerTerminals(terminals);
            grammar.setStartSymbol(startSymbol);

            // Utility
            final List<String> nonTerminalList = grammar.getNonTerminals();

            // Counting the number of rules
            final int n = rules.size();

            // Providing arrays to store the rule values
            final String[]   leftHandSides  = new String[n];
            final String[]   rightHandSides = new String[n];

            // Storing all rule data
            for (int i = 0; i < n; i++) {
                // Accessing the rule
                final var rule = rules.get(i);

                // Accessing the data on the left-hand side
                leftHandSides[i]     = rule.getLeftHandSide();

                // Validating the left-hand side
                if (!nonTerminalList.contains(leftHandSides[i])) {
                    // Providing the warning of the invalid left hand side
                    rule.setWarning("Warning: Left-hand side does not contain a non-terminal!");

                    // Exiting the new rule event prematurely
                    return;
                }

                // Accessing the data on the right-hand side
                rightHandSides[i] = rule.getRightHandSide();

                // Attempting to add the rule to the grammar
                try {
                    // Confirming whether the '|' (OR-symbol) has been used
                    if (rightHandSides[i].contains("|")) {
                        final String[] segments = rightHandSides[i].split("\\|");
                        for (int j = 0; j < segments.length; j++) segments[j] = segments[j].trim();
                        grammar.createRule(leftHandSides[i], segments);
                    }
                    else grammar.createRule(leftHandSides[i], rightHandSides[i]);
                } catch (IllegalArgumentException e) {
                    rule.setWarning("Warning: " + e.getMessage());
                    return;
                }
            }

            // Updating the assistant
            if (a != null) a.registerGrammar(grammar);

            // Clearing the editor
            topTextField.clear();
            middleTextField.clear();
            bottomTextField.clear();
            valueHolder.getChildren().clear();
            rules.clear();

            // Providing a grammar button to display the grammar in the grammar overview
            final var grammarButton = new OverviewButton(f);
            grammarButton.setText(rawNonTerminals);

            // Storing the grammar (button)
            overviewButtons.add(grammarButton);

            // Adding the grammar (button) to the overview
            if (o != null) o.add(grammarButton.getPanel());

            // Applying the current color scheme
            grammarButton.setBorder(titleHolder.getBorder());
            grammarButton.setStyle(titleHolder.getStyle());

            // Providing the delete grammar event
            grammarButton.setDeleteEvent(() -> {
                if (o != null) o.remove(grammarButton.getPanel());
                if (a != null) a.removeGrammar(grammar);
                overviewButtons.remove(grammarButton);
            });

            // Providing the display grammar event
            grammarButton.setDisplayEvent(() -> {
                // Clearing the editor
                topTextField.clear();
                middleTextField.clear();
                bottomTextField.clear();
                valueHolder.getChildren().clear();
                rules.clear();

                // Update the grammar structure text fields
                topTextField.setText(rawNonTerminals);
                middleTextField.setText(rawTerminals);
                bottomTextField.setText(startSymbol);

                // Updating the rule section
                for (int i = 0; i < n; i++) {
                    // Providing a new rule
                    final var rule = new Rule(f);
                    rule.setLeftHandSide(leftHandSides[i]);
                    rule.setRightHandSide(rightHandSides[i]);

                    // Storing the rule to the back-end list
                    rules.add(rule);

                    // Storing the rule to the front-end list
                    valueHolder.getChildren().add(rule.getPanel());

                    // Providing the delete event
                    rule.setDeleteEvent(() -> {
                        // Removing the rule from the back-end list
                        rules.remove(rule);

                        // Removing the rule from the front-end list
                        valueHolder.getChildren().remove(rule.getPanel());
                    });

                    // Applying the current color scheme
                    rule.setBorder(titleHolder.getBorder());
                    rule.setStyle(titleHolder.getStyle());
                }
            });
        });
    }

    @Override
    public void setBorder(Border b) {
        super.setBorder(b);
        for (var rule : rules) rule.setBorder(b);
    }

    @Override
    public void setStyle(String s) {
        super.setStyle(s);
        for (var rule : rules) rule.setStyle(s);
    }

    private static final class Rule implements Displayable, Styleable {
        private final Button delete;
        private final TextField leftHandSide, rightHandSide;
        private final Label arrowLabel, warningHolder;
        private final VBox panel;

        public Rule(Factory f) {
            final String arrowSymbol = Character.toString(8594);

            // Providing a button to delete the rule from the grammar editor
            delete = new Button(Character.toString(10005));
            delete.setBackground(Background.EMPTY);
            delete.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            // Providing a label to display warnings concerning the rule
            warningHolder = f.createLabel("");
            warningHolder.setStyle(WARNING_TEXT_STYLE);

            // Providing a label to display that the left hande side implies the right hand side
            arrowLabel = f.createLabel(arrowSymbol, Pos.CENTER);
            arrowLabel.setMinWidth(20);

            // Providing a text field to get the left hand side of the rule
            leftHandSide = f.createTextField("1 non-terminal", Pos.CENTER);
            leftHandSide.setBackground(Background.EMPTY);
            leftHandSide.setMinWidth(100);
            leftHandSide.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!warningHolder.getText().isBlank()) warningHolder.setText("");
            });

            // Providing a text field to get the right hand side of the rule
            rightHandSide = f.createTextField("1 terminal or 2 non-terminals", Pos.CENTER);
            rightHandSide.setBackground(Background.EMPTY);
            rightHandSide.setMinWidth(240);
            rightHandSide.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!warningHolder.getText().isBlank()) warningHolder.setText("");
            });

            // Providing a box to display the rule components horizontally
            final HBox ruleBox = f.createHBox(3, leftHandSide, arrowLabel, rightHandSide, delete);

            // Providing the final displayable panel
            panel = f.createVBox(3, Pos.CENTER, warningHolder, ruleBox);

            // Assigning visual effects to the delete button
            delete.setOnMouseEntered(enterEvent -> delete.setBackground(Effects.RED_OVERLAY_BACKGROUND));
            delete.setOnMouseExited(exitEvent -> delete.setBackground(Background.EMPTY));
        }

        public String getLeftHandSide() {
            return leftHandSide.getText();
        }

        public String getRightHandSide() {
            return rightHandSide.getText();
        }

        public void setLeftHandSide(String s) {
            leftHandSide.setText(s);
        }

        public void setRightHandSide(String s) {
            rightHandSide.setText(s);
        }

        public void setWarning(String s) {
            warningHolder.setText(s);
        }

        public void setDeleteEvent(Runnable process) {
            delete.setOnAction(deleteEvent -> process.run());
        }

        @Override
        public Region getPanel() {
            return panel;
        }

        @Override
        public void setBorder(Border b) {
            leftHandSide.setBorder(b);
            rightHandSide.setBorder(b);
        }

        @Override
        public void setStyle(String s) {
            leftHandSide.setStyle(s);
            rightHandSide.setStyle(s);
            arrowLabel.setStyle(s);
            delete.setStyle(s);
        }
    }
}
