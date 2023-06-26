package gui;

import design.Displayable;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public final class Window implements Displayable {
    private final ObservableList<Node> buttons;
    private final ScrollPane viewport;
    private final VBox panel;
    private Button previous;

    public Window(Factory f) {
        final var hbox = f.createHBox(2, Paddings.DEFAULT, Pos.CENTER_LEFT);
        buttons  = hbox.getChildren();
        viewport = f.createScrollPane(null);
        panel    = f.createVBox(2, Paddings.DEFAULT, Pos.TOP_CENTER, hbox, viewport);
        VBox.setVgrow(viewport, Priority.ALWAYS);
    }

    public void add(Button... b) {
        buttons.addAll(b);
    }

    public void show(Button b, Region r) {
        if (b.getBackground() != Backgrounds.SELECTED_TAB) b.setBackground(Backgrounds.SELECTED_TAB);
        if (viewport.getContent() != r) viewport.setContent(r);
        if (previous == b) return;
        else if (previous != null) previous.setBackground(Backgrounds.DEFAULT);
        previous = b;
    }

    @Override
    public Region getPanel() {
        return panel;
    }
}
