package gui;

import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

final class ChatManager extends AnchorPane {
    private final TabPane CHATS = new TabPane();
    private int activeTabs = 0;

    public ChatManager() {
        final HBox CONTROLS = new HBox();

        // Setting the anchor locations of the chat tabs
        AnchorPane.setTopAnchor(CHATS,1.0);
        AnchorPane.setLeftAnchor(CHATS, 1.0);
        AnchorPane.setRightAnchor(CHATS, 1.0);
        AnchorPane.setBottomAnchor(CHATS, 1.0);

        // Setting the anchor locations of the control buttons
        AnchorPane.setTopAnchor(CONTROLS, 3.0);
        AnchorPane.setRightAnchor(CONTROLS, 5.0);

        // Adding a new tab button to the control buttons
        Button newTab = new Button("+");
        CONTROLS.getChildren().add(newTab);

        // Defining the functionality of the new tab button
        newTab.setOnMouseClicked(event -> {
            Tab t = new Tab("Tab " + ++activeTabs);
            ChatPanel chat = new ChatPanel();
            t.setContent(chat);
            CHATS.getTabs().add(t);
        });

        // Adding all components to the layout
        getChildren().addAll(CHATS, CONTROLS);
    }
}
