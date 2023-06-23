package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;

public final class Overview implements Displayable {
    private final Label title;
    private final List<Node> nodes;
    private final VBox panel;

    public Overview(Factory factory) {
        title = factory.createLabel("TITLE", Pos.CENTER, 250, 30, Double.MAX_VALUE, 30);

        final VBox content = factory.createVBox(5, Paddings.DEFAULT, Pos.TOP_CENTER);
        final ScrollPane viewport = factory.createScrollPane(content);
        nodes = content.getChildren();

        panel = factory.createVBox(3, Insets.EMPTY, Pos.CENTER, title, viewport);
        panel.setMinSize(250, 720);
        VBox.setVgrow(viewport, Priority.ALWAYS);
        factory.update(Borders.GRAY_OVERLAY, title, viewport);
    }

    public void add(Node n) {
        nodes.add(n);
    }

    public void remove(Node n) {
        nodes.remove(n);
    }

    public void setTitle(String s) {
        title.setText(s);
    }

    @Override
    public Region getPanel() {
        return panel;
    }
}
