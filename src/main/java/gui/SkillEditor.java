package gui;

import engine.Assistant;
import engine.PlaceholderCounter;
import engine.PlaceholderReplacer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public final class SkillEditor extends Editor {
    public SkillEditor(Factory factory, Assistant assistant) {
        super(factory);
        list.setTitle("SKILLS");
        title.setText("SKILL EDITOR");
        add.setText("ADD VALUES");
        save.setText("SAVE SKILL");

        final var inputPlaceholder  = new Variable(factory, "Input Placeholder", "1 or more characters");
        final var inputTemplate     = new Variable(factory, "Input Template", "1 or more placeholders");
        final var outputPlaceholder = new Variable(factory, "Output Placeholder", "1 or more characters");
        final var outputTemplate    = new Variable(factory, "Output Template", "1 or more placeholders");
        variables.getChildren().addAll(
                inputPlaceholder.getPanel(), inputTemplate.getPanel(),
                outputPlaceholder.getPanel(), outputTemplate.getPanel()
        );

        final var counter  = new PlaceholderCounter();
        final var replacer = new PlaceholderReplacer();
        final List<Data> dataList = new ArrayList<>();

        add.setOnAction(addEvent -> {
            final var data = new Data(factory);
            data.setOnDeleteClicked(deleteEvent -> {
                this.content.getChildren().remove(data.getPanel());
                dataList.remove(data);
            });

            this.content.getChildren().add(data.getPanel());
            dataList.add(data);
        });

        save.setOnAction(saveEvent -> {
            final String[] placeholders = {
                    inputPlaceholder.getValue(),
                    outputPlaceholder.getValue()
            };
            final String[] templates = {
                    inputTemplate.getValue(),
                    outputTemplate.getValue()
            };
            final int[] placeholderCounts = {
                    counter.countPlaceholders(templates[0], placeholders[0]),
                    counter.countPlaceholders(templates[1], placeholders[1])
            };

            if (placeholders[0].isBlank()) {
                inputPlaceholder.setWarning("Error: Empty input placeholder!");
                return;
            } else if (placeholders[1].isBlank()) {
                outputPlaceholder.setWarning("Error: Empty output placeholder!");
            } else if (placeholderCounts[0] == 0) {
                inputTemplate.setWarning("Error: Input template does not contain a single placeholder!");
                return;
            } else if (placeholderCounts[1] == 0) {
                outputTemplate.setWarning("Error: Output template does not contain a single placeholder!");
                return;
            }

            final String[][] xs = new String[dataList.size()][];
            final String[][] ys = new String[dataList.size()][];

            for (int i = 0; i < dataList.size(); i++) {
                final var data = dataList.get(i);
                xs[i] = data.getInput().split(",");
                ys[i] = data.getOutput().split(",");
                if (xs[i].length != placeholderCounts[0]) {
                    data.setInputWarning("Error: Input template does not as many values as placeholders!");
                    return;
                } else if (ys[i].length != placeholderCounts[1]) {
                    data.setOutputWarning("Error: Output template does not contain as many values as placeholders!");
                    return;
                }
            }

            final String[] inputs = new String[dataList.size()];
            final String[] outputs = new String[dataList.size()];
            for (int i = 0; i < dataList.size(); i++) {
                inputs[i]  = replacer.replacePlaceholders(templates[0], placeholders[0], xs[i]);
                outputs[i] = replacer.replacePlaceholders(templates[1], placeholders[1], ys[i]);

                if (assistant.validate(inputs[i]) || assistant.getGrammarList().size() == 0) continue;
                final var data = dataList.get(i);
                data.setInputWarning("Error: Values do not yield an input that belongs to a grammar.");
                return;
            }

            for (int i = 0; i < dataList.size(); i++) {
                final String input  = inputs[i];
                final String output = outputs[i];

                final var item = new Item(factory);
                item.setTitle(input);
                item.setOnDeleteClicked(deleteEvent -> {
                    assistant.removeAssociation(input);
                    list.remove(item.getPanel());
                });

                assistant.associate(input, output);
                list.add(item.getPanel());
            }

            if (dataList.size() > 0) {
                inputPlaceholder.clear();
                inputTemplate.clear();
                outputPlaceholder.clear();
                outputTemplate.clear();
            }

            for (var data : dataList) content.getChildren().remove(data.getPanel());
            dataList.clear();
        });
    }

    private static final class Data implements Displayable {
        private final Button delete;
        private final HBox panel;
        private final Variable input, output;

        public Data(Factory factory) {
            delete = factory.createButton(Character.toString(10005), 30, 30, 30, 30);
            input = new Variable(factory, "Input Values", "x1, x2, x3, ...");
            output = new Variable(factory, "Output Values", "y1, y2, y3, ...");


            final var vbox = factory.createVBox(3, Insets.EMPTY, Pos.CENTER, input.getPanel(), output.getPanel());
            panel = factory.createHBox(3, Insets.EMPTY, Pos.CENTER, vbox, delete);
        }

        public String getInput() {
            return input.getValue();
        }

        public String getOutput() {
            return output.getValue();
        }

        public void setOnDeleteClicked(EventHandler<ActionEvent> handler) {
            delete.setOnAction(handler);
        }

        public void setInputWarning(String s) {
            input.setWarning(s);
        }

        public void setOutputWarning(String s) {
            output.setWarning(s);
        }

        @Override
        public Region getPanel() {
            return panel;
        }
    }
}
