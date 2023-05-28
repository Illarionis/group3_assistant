package gui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public final class Overview {
    private final Label titleHolder;
    private final ScrollPane viewport;
    private final VBox content;
    private final VBox panel;

    public Overview(Factory f) {
        titleHolder = f.createLabel("");
        titleHolder.setMaxWidth(Double.MAX_VALUE);
        titleHolder.setPrefHeight(30);

        content = f.createVBox(5, new Insets(5, 5, 5, 5));
        viewport   = f.createScrollPane(content);
        VBox.setVgrow(viewport, Priority.ALWAYS);

        panel = f.createVBox(3, titleHolder, viewport);
    }

    public Region getPanel() {
        return panel;
    }

    public void add(Node... elements) {
        content.getChildren().addAll(elements);
    }

    public void remove(Node... elements) {
        content.getChildren().removeAll(elements);
    }

    public void setBorder(Border b) {
        titleHolder.setBorder(b);
        viewport.setBorder(b);
    }

    public void setStyle(String s) {
        titleHolder.setStyle(s);
    }

    public void setTitle(String s) {
        titleHolder.setText(s);
    }
}
