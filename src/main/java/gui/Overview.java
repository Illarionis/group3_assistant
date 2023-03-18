package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;

public final class Overview extends AnchorPane {
    private static final Border SOLID_BOX = Border.stroke(Color.DARKGRAY);
    private final ActionEvent listViewRequested, tabViewRequested;
    private final EventHandler<ActionEvent> viewModeEventHandler;
    private final List<Node> buttons;
    private final List<Tab> tabs;
    private final SelectionModel<Tab> cursor;

    public Overview(String listViewTitle, String tabViewTitle) {
        final Button manageContent = getExpandableButton(20);
        manageContent.setText("+");
        setLeftAnchor(manageContent, 5.0);
        setRightAnchor(manageContent, 5.0);
        setBottomAnchor(manageContent, 5.0);

        final Button titleHolder = getExpandableButton(30);
        setTopAnchor(titleHolder, 5.0);
        setLeftAnchor(titleHolder, 5.0);
        setRightAnchor(titleHolder, 5.0);

        final VBox listContent = new VBox();
        listContent.setSpacing(1.0);
        buttons = listContent.getChildren();

        final TabPane tabContent = new TabPane();
        tabContent.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs = tabContent.getTabs();
        cursor = tabContent.getSelectionModel();

        final ScrollPane viewPort = new ScrollPane();
        viewPort.setBorder(SOLID_BOX);
        viewPort.setFitToHeight(true);
        viewPort.setFitToWidth(true);
        setTopAnchor(viewPort, 34.0);
        setLeftAnchor(viewPort, 5.0);
        setRightAnchor(viewPort, 5.0);
        setBottomAnchor(viewPort, 30.0);

        listViewRequested = new ActionEvent();
        tabViewRequested = new ActionEvent();

        viewModeEventHandler = event -> {
            if (event == listViewRequested && viewPort.getContent() != listContent) {
                titleHolder.setOnAction(clickEvent -> showTabs());
                titleHolder.setText(listViewTitle);
                viewPort.setContent(listContent);
            } else if (event == tabViewRequested && viewPort.getContent() != tabContent) {
                titleHolder.setOnAction(clickEvent -> showList());
                titleHolder.setText(tabViewTitle);
                viewPort.setContent(tabContent);
            }
        };

        getChildren().addAll(titleHolder, viewPort, manageContent);
        setFocused(false);
        setFocusTraversable(false);
        setStyle("-fx-focus-color : transparent; -fx-faint-focus-color : transparent;");
        showList();
    }

    private Button getExpandableButton(double h) {
        final var button = new Button();
        button.setBorder(SOLID_BOX);
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button.setPrefHeight(h);
        return button;
    }

    public void addTab(Tab t) {
        if (!tabs.contains(t)) {
            tabs.add(t);
        }
    }

    public void addButton(Button b) {
        if (!buttons.contains(b)) {
            buttons.add(b);
            b.setBorder(SOLID_BOX);
            b.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            b.setPrefHeight(20);
        }
    }

    public Button getFootnoteButton() {
        return (Button) getChildren().get(2);
    }

    public void openTab(Tab t) {
        cursor.select(t);
        showTabs();
    }

    public void removeButton(Button b) {
        buttons.remove(b);
    }

    public void removeTab(Tab t) {
        tabs.remove(t);
        if (tabs.size() == 0) {
            showList();
        }
    }

    public void showList() {
        viewModeEventHandler.handle(listViewRequested);
    }

    public void showTabs() {
        viewModeEventHandler.handle(tabViewRequested);
    }
}
