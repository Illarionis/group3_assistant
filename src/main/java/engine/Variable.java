package engine;

import design.Displayable;
import gui.*;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public final class Variable implements Displayable {
    private final HBox panel;
    private final Label name;
    private final TextField value;

    public Variable(Factory f) {
        final Label arrow = f.createLabel(Symbols.RIGHTWARDS_ARROW, Pos.CENTER, 30, 30, 30, 30);
        name  = f.createLabel("...", Pos.CENTER_RIGHT, 120, 30, Double.MAX_VALUE, 30);
        value = f.createTextField("...", Pos.CENTER_LEFT, 240, 30, Double.MAX_VALUE, 30);
        panel = f.createHBox(2, Paddings.DEFAULT, Pos.CENTER, name, arrow, value);
        value.setBorder(Borders.GRAY);
    }

    public String getValue() {
        return value.getText();
    }

    public void setDescription(String s) {
        value.setPromptText(s);
    }

    public void setName(String s) {
        name.setText(s);
    }

    public void setValue(String s) {
        value.setText(s);
    }

    @Override
    public Region getPanel() {
        return panel;
    }
}
