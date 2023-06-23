package gui;

import engine.Assistant;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;


public final class EditorWindow extends Window {
    public EditorWindow(Factory factory, Assistant assistant) {
        super(factory);
        final Editor skill = new SkillEditor(factory, assistant);
        final Editor grammar = new GrammarEditor(factory, assistant);
        final HBox editors = factory.createHBox(
                3, Insets.EMPTY, Pos.CENTER,
                skill.getList().getPanel(), skill.getPanel(),
                grammar.getPanel(), grammar.getList().getPanel()
        );
        panel.getChildren().add(editors);
        title.setText("ASSISTANT EDITOR");
    }
}
