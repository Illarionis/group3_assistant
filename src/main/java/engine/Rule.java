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

public final class Rule implements Displayable, Removable {
    private final Button delete;
    private final HBox panel;
    private final TextField leftHandSide, rightHandSide;

    public Rule(Factory f, Designer d) {
        final Label arrow = f.createLabel(Symbols.RIGHTWARDS_ARROW, Pos.CENTER, 30, 30, 30, 30);
        delete        = f.createButton(Symbols.MULTIPLICATION, 30, 30, 30, 30);
        leftHandSide  = f.createTextField("...", Pos.CENTER, 120, 30, Double.MAX_VALUE, 30);
        rightHandSide = f.createTextField("...", Pos.CENTER, 240, 30, Double.MAX_VALUE, 30);
        panel         = f.createHBox(2, Paddings.DEFAULT, Pos.CENTER, leftHandSide, arrow, rightHandSide, delete);
        d.setBorder(Borders.GRAY, leftHandSide, rightHandSide);
    }

    public String getLeftHandSide() {
        return leftHandSide.getText();
    }

    public String getRightHandSide() {
        return rightHandSide.getText();
    }

    public void setLeftHandSide(String s) {
        leftHandSide.setText(s);
    }

    public void setLeftHandSideDescription(String s) {
        leftHandSide.setPromptText(s);
    }

    public void setRightHandSide(String s) {
        rightHandSide.setText(s);
    }

    public void setRightHandSideDescription(String s) {
        rightHandSide.setPromptText(s);
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
