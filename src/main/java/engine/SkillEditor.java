package engine;

import design.Displayable;
import design.Removable;
import gui.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import utility.PlaceholderCounter;
import utility.PlaceholderReplacer;

import java.util.ArrayList;
import java.util.List;

public final class SkillEditor extends Editor {
    public SkillEditor(Factory f, Designer d, Assistant a) {
        super(f, d);
        final Variable inputPlaceholder = new Variable(f);
        inputPlaceholder.setName("Input Placeholder");
        inputPlaceholder.setDescription("1 or more symbols");

        final Variable inputTemplate = new Variable(f);
        inputTemplate.setName("Input Template");
        inputTemplate.setDescription("Phrase with 1 or more placeholders");

        final Variable outputPlaceholder = new Variable(f);
        outputPlaceholder.setName("Output Placeholder");
        outputPlaceholder.setDescription("1 or more symbols");

        final Variable outputTemplate = new Variable(f);
        outputTemplate.setName("Output Template");
        outputTemplate.setDescription("Phrase with 1 or more placeholders");

        variables.addAll(inputPlaceholder.getPanel(), inputTemplate.getPanel(), outputPlaceholder.getPanel(), outputTemplate.getPanel());

        final var grammar  = a.getGrammarList();
        final var counter  = new PlaceholderCounter();
        final var replacer = new PlaceholderReplacer();
        final List<Data> entries = new ArrayList<>();
        add.setOnAction(addEvent -> {
            final var data = new Data(f, d);
            data.setDescriptionX("x1, x2, x3, ...");
            data.setDescriptionY("y1, y2, y3, ...");
            data.setOnDelete(deleteEvent -> {
                values.remove(data.getPanel());
                entries.add(data);
            });
            values.add(data.getPanel());
            entries.add(data);
        });

        save.setOnAction(saveEvent -> {
            final String[] placeholders = new String[2];
            placeholders[0] = inputPlaceholder.getValue();
            placeholders[1] = outputPlaceholder.getValue();
            if (placeholders[0].isBlank() || placeholders[1].isBlank()) {
                System.out.println("@SKILL_EDITOR: Blank placeholder detected!");
                return;
            }

            final String[] templates = new String[2];
            templates[0] = inputTemplate.getValue();
            templates[1] = outputTemplate.getValue();
            if (templates[0].isBlank() || templates[1].isBlank()) {
                System.out.println("@SKILL_EDITOR: Blank template detected!");
                return;
            }

            final int[] counts = new int[2];
            counts[0] = counter.countPlaceholders(templates[0], placeholders[0]);
            counts[1] = counter.countPlaceholders(templates[1], placeholders[1]);
            if (counts[0] == 0 || counts[1] == 0) {
                System.out.println("@SKILL_EDITOR: No placeholders in template!");
                return;
            }

            final int n = entries.size();
            final String[][] xs = new String[n][];
            final String[][] ys = new String[n][];
            for (int i = 0; i < n; i++) {
                final var data = entries.get(i);
                xs[i] = data.getX().split(",");
                ys[i] = data.getY().split(",");
                if (xs[i].length != counts[0] || ys[i].length != counts[1]) {
                    System.out.println("@SKILL_EDITOR: #placeholders in template != #values");
                    return;
                }
            }

            final String[] in  = new String[n];
            final String[] out = new String[n];
            for (int i = 0; i < n; i++) {
                in[i]  = replacer.replacePlaceholders(templates[0], placeholders[0], xs[i]);
                out[i] = replacer.replacePlaceholders(templates[1], placeholders[1], ys[i]);
                if (grammar.isEmpty()) continue;
                else if (a.validate(in[i])) continue;
                System.out.println("@SKILL_EDITOR: Input does not belong to a CFG!");
                return;
            }

            for (int i = 0; i < n; i++) a.associate(in[i], out[i]);

            final var copy = new ArrayList<>(entries);
            final var item = new Item(f);
            item.setName(templates[0] + ' ' + Symbols.RIGHTWARDS_ARROW + ' ' + templates[1]);
            item.setOnDelete(deleteEvent -> {
                for (int i = 0; i < n; i++) a.removeAssociation(in[i]);
                items.remove(item.getPanel());
            });
            item.setOnAction(showEvent -> {
                values.clear();
                entries.clear();
                entries.addAll(copy);
                for (var data : entries) values.add(data.getPanel());
                inputPlaceholder.setValue(placeholders[0]);
                inputTemplate.setValue(templates[0]);
                outputPlaceholder.setValue(placeholders[1]);
                outputTemplate.setValue(templates[1]);
            });

            items.add(item.getPanel());
            entries.clear();
            values.clear();
            inputPlaceholder.setValue("");
            inputTemplate.setValue("");
            outputPlaceholder.setValue("");
            outputTemplate.setValue("");
        });
    }

    private static final class Data implements Displayable, Removable {
        private final Button delete;
        private final HBox panel;
        private final TextField leftHandSide, rightHandSide;

        public Data(Factory f, Designer d) {
            final Label arrow = f.createLabel(Symbols.RIGHTWARDS_ARROW, Pos.CENTER, 30, 30, 30, 30);
            delete = f.createButton(Symbols.MULTIPLICATION, 30, 30, 30, 30);
            leftHandSide = f.createTextField("...", Pos.CENTER, 120, 30, Double.MAX_VALUE, 30);
            rightHandSide = f.createTextField("...", Pos.CENTER, 120, 30, Double.MAX_VALUE, 30);
            panel = f.createHBox(2, Paddings.DEFAULT, Pos.CENTER, leftHandSide, arrow, rightHandSide, delete);
            d.setBorder(Borders.GRAY, leftHandSide, rightHandSide);
        }

        public String getX() {
            return leftHandSide.getText();
        }

        public String getY() {
            return rightHandSide.getText();
        }

        public void setDescriptionX(String s) {
            leftHandSide.setPromptText(s);
        }

        public void setDescriptionY(String s) {
            rightHandSide.setPromptText(s);
        }

        public void setX(String s) {
            leftHandSide.setText(s);
        }

        public void setY(String s) {
            rightHandSide.setText(s);
        }

        @Override
        public Region getPanel() {
            return panel;
        }

        @Override
        public void setOnDelete(EventHandler<ActionEvent> handler) {
            delete.setOnAction(handler);
        }
    }
}
