package engine;

import design.Displayable;
import design.Removable;
import gui.Borders;
import gui.Factory;
import gui.Paddings;
import gui.Symbols;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public final class Item implements Displayable, Removable {
    private final Button name, delete;
    private final HBox panel;

    public Item(Factory f) {
        name = f.createButton("...", 30, 30, Double.MAX_VALUE, 30);
        name.setBorder(Borders.GRAY);
        HBox.setHgrow(name, Priority.ALWAYS);

        delete = f.createButton(Symbols.MULTIPLICATION, 30, 30, 30, 30);
        panel  = f.createHBox(2, Paddings.DEFAULT, Pos.CENTER, name, delete);
    }

    public void setOnAction(EventHandler<ActionEvent> handler) {
        name.setOnAction(handler);
    }

    public void setName(String s) {
        name.setText(s);
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
