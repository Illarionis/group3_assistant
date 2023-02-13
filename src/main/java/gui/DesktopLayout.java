package gui;

import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * Provides the layout of the application running on a desktop.
 **/
public class DesktopLayout extends AnchorPane {
    private final TabPane CHATS = new TabPane();
    private int activeTabs = 0;

    public DesktopLayout() {
        final SideMenu MENU = new SideMenu();
        final HBox CONTROLS = new HBox();

        // Setting the preferred width of the side menu in a 1920x1080 setting
        MENU.setPrefWidth(180);

        // Setting the anchor locations of the side menu
        AnchorPane.setTopAnchor(MENU, 0.0);
        AnchorPane.setLeftAnchor(MENU, 0.0);
        AnchorPane.setBottomAnchor(MENU, 0.0);

        // Setting the anchor locations of the chat tabs
        AnchorPane.setTopAnchor(CHATS,0.0);
        AnchorPane.setLeftAnchor(CHATS, 180.0);
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
        getChildren().addAll(MENU, CHATS, CONTROLS);
    }
}
