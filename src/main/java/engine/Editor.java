package engine;

import design.Displayable;
import gui.Borders;
import gui.Designer;
import gui.Factory;
import gui.Paddings;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public abstract class Editor implements Displayable {
    private final VBox panel;
    protected final Button add, save;
    protected final Label listHeader, variableHeader, valueHeader;
    protected final ObservableList<Node> items, variables, values;
    public Editor(Factory f, Designer d) {
        add   = f.createButton("ADD", 30, 30, Double.MAX_VALUE, 30);
        save  = f.createButton("SAVE", 30, 30, Double.MAX_VALUE, 30);
        d.setBorder(Borders.GRAY, add, save);
        d.setHGrow(Priority.ALWAYS, add, save);

        variableHeader = f.createLabel("VARIABLES", Pos.CENTER, 120, 30, Double.MAX_VALUE, 30);
        final VBox variableHolder = f.createVBox(5, Paddings.CONTENT, Pos.CENTER);
        final VBox variablePanel = f.createVBox(2, Paddings.DEFAULT, Pos.TOP_CENTER, variableHeader, variableHolder);
        variables = variableHolder.getChildren();
        d.setBorder(Borders.GRAY, variableHeader, variableHolder);

        valueHeader = f.createLabel("VALUES", Pos.CENTER, 300, 30, Double.MAX_VALUE, 30);
        final VBox valueHolder = f.createVBox(5, Paddings.CONTENT, Pos.TOP_CENTER);
        final ScrollPane valueViewport = f.createScrollPane(valueHolder);
        final VBox valuePanel = f.createVBox(2, Paddings.DEFAULT, Pos.TOP_CENTER, valueHeader, valueViewport, add);
        values = valueHolder.getChildren();
        d.setBorder(Borders.GRAY, valueHeader, valueViewport);
        valuePanel.setPrefWidth(500);

        listHeader = f.createLabel("LIST", Pos.CENTER, 30, 30, Double.MAX_VALUE, 30);
        final VBox itemHolder = f.createVBox(5, Paddings.CONTENT, Pos.TOP_CENTER);
        final ScrollPane itemViewport = f.createScrollPane(itemHolder);
        final VBox itemPanel = f.createVBox(2, Paddings.DEFAULT, Pos.TOP_CENTER, listHeader, itemViewport);
        items = itemHolder.getChildren();
        d.setBorder(Borders.GRAY, listHeader, itemViewport);
        d.setVGrow(Priority.ALWAYS, valuePanel, itemViewport, valueViewport);
        itemPanel.setPrefWidth(300);

        final VBox leftPanel = f.createVBox(2, Paddings.DEFAULT, Pos.TOP_CENTER, variablePanel, valuePanel);
        final HBox viewports = f.createHBox(2, Paddings.DEFAULT, Pos.CENTER, leftPanel, itemPanel);
        panel = f.createVBox(2, Paddings.DEFAULT, Pos.TOP_CENTER, viewports, save);
        d.setHGrow(Priority.ALWAYS, leftPanel, itemPanel);
        d.setVGrow(Priority.ALWAYS, viewports);
    }

    @Override
    public Region getPanel() {
        return panel;
    }
}
