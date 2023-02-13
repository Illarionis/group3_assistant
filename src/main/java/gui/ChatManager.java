package gui;

import design.engine.Assistant;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

final class ChatManager<Input, Output> extends AnchorPane {
    private int activeTabs = 0;

    public ChatManager(Assistant<Input, Output> a) {
        // Definition of the components used to make the chat window.
        final TabPane CHATS = new TabPane();
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
            // Making the new tab.
            Tab t = new Tab("Tab " + ++activeTabs);

            // Making a new chat panel.
            ChatPanel<Input, Output> chat = new ChatPanel<>(a);
            t.setContent(chat);

            // Adding the new conversation tab.
            CHATS.getTabs().add(t);
        });

        // Adding all components to the layout
        getChildren().addAll(CHATS, CONTROLS);
    }
}
