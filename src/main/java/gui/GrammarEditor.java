package gui;

import engine.Assistant;
import engine.ContextFreeGrammar;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;

public final class GrammarEditor extends Editor {
    public GrammarEditor(Factory factory, Assistant assistant) {
        super(factory);
        list.setTitle("GRAMMAR");
        title.setText("GRAMMAR EDITOR");
        add.setText("ADD RULE");
        save.setText("SAVE GRAMMAR");

        final var terminals = new Variable(factory, "Terminals", "1 or more words");
        final var nonTerminals = new Variable(factory, "Non-Terminals", "1 or more placeholder symbols");
        final var startSymbol = new Variable(factory, "Start-Symbol", "1 non-terminal");
        variables.getChildren().addAll(
                terminals.getPanel(), nonTerminals.getPanel(), startSymbol.getPanel()
        );

        final List<Rule> ruleList = new ArrayList<>();
        add.setOnAction(addEvent -> {
            final var rule = new Rule(factory);
            rule.setOnDeleteClicked(deleteEvent -> {
                content.getChildren().remove(rule.getPanel());
                ruleList.remove(rule);
            });

            content.getChildren().add(rule.getPanel());
            ruleList.add(rule);
        });

        save.setOnAction(saveEvent -> {
            final var grammar = new ContextFreeGrammar();
            final String[] nts = nonTerminals.getValue().split(",");
            for (int i = 0; i < nts.length; i++) nts[i] = nts[i].trim();

            final String[] ts = terminals.getValue().split(",");
            for (int i = 0; i < ts.length; i++) ts[i] = ts[i].trim();

            grammar.registerNonTerminals(nts);
            grammar.registerTerminals(ts);
            grammar.setStartSymbol(startSymbol.getValue());
            grammar.sortNonTerminals();
            grammar.sortTerminals();

            for (var rule : ruleList) {
                final String lhs = rule.getLeftHandSide();
                final String rhs = rule.getRightHandSide();
                if (rhs.contains("|")) {
                    final String[] segments = rhs.split("\\|");
                    for (String segment : segments) grammar.createRule(lhs, segment.trim().split(","));
                } else grammar.createRule(lhs, rhs.split(","));
                content.getChildren().remove(rule.getPanel());
            }
            ruleList.clear();
            terminals.clear();
            nonTerminals.clear();
            startSymbol.clear();

            final var item = new Item(factory);
            item.setTitle(grammar.getStartSymbol());
            item.setOnDeleteClicked(deleteEvent -> {
                assistant.removeGrammar(grammar);
                list.remove(item.getPanel());
            });

            assistant.registerGrammar(grammar);
            list.add(item.getPanel());
        });
    }

    private static final class Rule implements Displayable {
        private final Button delete;
        private final TextField leftSide, rightSide;
        private final HBox panel;

        public Rule(Factory factory) {
            final Label arrow = factory.createLabel(Character.toString(8594), Pos.CENTER, 30, 30, 30, 30);
            delete = factory.createButton(Character.toString(10005), 30, 30, 30, 30);
            leftSide = factory.createTextField("1 non-terminal", Pos.CENTER, 120, 30, 120, 30);
            rightSide = factory.createTextField("2 non-terminals or 1 terminal", Pos.CENTER, 240, 30, 180, 30);
            panel = factory.createHBox(3, Insets.EMPTY, Pos.CENTER, leftSide, arrow, rightSide, delete);
            factory.update(Borders.GRAY_OVERLAY, leftSide, rightSide);
        }

        public String getLeftHandSide() {
            return leftSide.getText();
        }

        public String getRightHandSide() {
            return rightSide.getText();
        }

        public void setOnDeleteClicked(EventHandler<ActionEvent> handler) {
            delete.setOnAction(handler);
        }

        @Override
        public Region getPanel() {
            return panel;
        }
    }
}
