package gui;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;

public final class RegionDesigner {
    public void setBackground(Background bg, Region... regions) {
        for (var region : regions) region.setBackground(bg);
    }

    public void setBorder(Border b, Region... regions) {
        for (var region : regions) region.setBorder(b);
    }

    public void setMaxHeight(double d, Region... regions) {
        for (var region : regions) region.setMaxHeight(d);
    }

    public void setMaxWidth(double d, Region... regions) {
        for (var region : regions) region.setMaxWidth(d);
    }

    public void setOnMouseEntered(EventHandler<MouseEvent> handler, Region... regions) {
        for (var region : regions) region.setOnMouseEntered(handler);
    }

    public void setOnMouseExited(EventHandler<MouseEvent> handler, Region... regions) {
        for (var region : regions) region.setOnMouseExited(handler);
    }

    public void setPrefHeight(double d, Region... regions) {
        for (var region : regions) region.setPrefHeight(d);
    }

    public void setPrefWidth(double d, Region... regions) {
        for (var region : regions) region.setPrefWidth(d);
    }

    public void setStyle(String style, Node... nodes) {
        for (var node : nodes) node.setStyle(style);
    }
}
