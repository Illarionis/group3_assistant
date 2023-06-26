package engine;

import gui.Designer;
import gui.Factory;
import nlp.ContextFreeGrammar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class GrammarEditor extends Editor {
    public GrammarEditor(Factory f, Designer d, Assistant a) {
        super(f, d);
        valueHeader.setText("RULES");

        final Variable terminals = new Variable(f);
        terminals.setName("Terminals");
        terminals.setDescription("1 or more words");

        final Variable nonTerminals = new Variable(f);
        nonTerminals.setName("Non-Terminals");
        nonTerminals.setDescription("1 or more placeholders");

        final Variable startSymbol = new Variable(f);
        startSymbol.setName("Start-Symbol");
        startSymbol.setDescription("1 non-terminal");

        variables.addAll(terminals.getPanel(), nonTerminals.getPanel(), startSymbol.getPanel());

        final List<Rule> rules = new ArrayList<>();

        add.setOnAction(addEvent -> {
            final var rule = new Rule(f, d);
            rule.setLeftHandSideDescription("1 non-terminal");
            rule.setRightHandSideDescription("2 non-terminals or 1 terminal");
            rule.setOnDelete(deleteEvent -> {
                values.remove(rule.getPanel());
                rules.add(rule);
            });
            values.add(rule.getPanel());
            rules.add(rule);
        });


        save.setOnAction(saveEvent -> {
            final String[] arr = new String[3];
            arr[0] = terminals.getValue();
            arr[1] = nonTerminals.getValue();
            arr[2] = startSymbol.getValue();

            final String[] ts = terminals.getValue().split(",");
            for (int i = 0; i < ts.length; i++) ts[i] = ts[i].trim();

            final String[] nts = nonTerminals.getValue().split(",");
            for (int i = 0; i < nts.length; i++) nts[i] = nts[i].trim();
            System.out.println(Arrays.toString(nts));

            final String ss = startSymbol.getValue().trim();

            final var grammar = new ContextFreeGrammar();
            grammar.registerNonTerminals(nts);
            grammar.registerTerminals(ts);
            grammar.setStartSymbol(ss);

            for (final var rule : rules) {
                final String lhs = rule.getLeftHandSide();
                final String rhs = rule.getRightHandSide();
                final String[] substitutions = rhs.split("\\|");
                for (int i = 0; i < substitutions.length; i++) substitutions[i] = substitutions[i].trim();
                grammar.createRule(lhs, substitutions);
            }

            final var copy = new ArrayList<>(rules);
            final var item = new Item(f);
            item.setName(arr[1]);
            item.setOnDelete(deleteEvent -> {
                a.removeGrammar(grammar);
                items.remove(item.getPanel());
            });
            item.setOnAction(showEvent -> {
                values.clear();
                rules.clear();
                rules.addAll(copy);
                for (var rule : rules) values.add(rule.getPanel());
                terminals.setValue(arr[0]);
                nonTerminals.setValue(arr[1]);
                startSymbol.setValue(arr[2]);
            });

            a.registerGrammar(grammar);
            items.add(item.getPanel());
            rules.clear();
            values.clear();
            terminals.setValue("");
            nonTerminals.setValue("");
            startSymbol.setValue("");
        });
    }
}
