package engine;

import design.Displayable;
import design.Removable;
import gui.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public final class Data implements Displayable, Removable {
    private static final String INVALID = "0", VALID = "1";
    private final Button delete, y;
    private final HBox panel;
    private final TextField x;

    public Data(Factory f, Designer d) {
        delete = f.createButton(Symbols.MULTIPLICATION, 30, 30, 30, 30);
        y      = f.createButton("?", 60, 30, 60, 30);
        x      = f.createTextField("...", Pos.CENTER_RIGHT, 270, 30, Double.MAX_VALUE, 30);
        panel  = f.createHBox(2, Paddings.DEFAULT, Pos.CENTER, x, y, delete);
        d.setBorder(Borders.GRAY, x, y);

        y.setOnAction(event -> {
            if (y.getText().equals(Symbols.WRONG)) {
                y.setBackground(Backgrounds.GREEN);
                y.setText(Symbols.CORRECT);
            } else {
                y.setBackground(Backgrounds.RED);
                y.setText(Symbols.WRONG);
            }
        });
    }

    public String getX() {
        return x.getText();
    }

    public String getY() {
        return y.getText().equals(Symbols.CORRECT) ? VALID : INVALID;
    }

    public void setX(String s) {
        x.setText(s);
    }

    public void setY(String s) {
        if (s.equals(VALID)) {
            y.setBackground(Backgrounds.GREEN);
            y.setText(Symbols.CORRECT);
        } else {
            y.setBackground(Backgrounds.RED);
            y.setText(Symbols.WRONG);
        }
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
