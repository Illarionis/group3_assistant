package design;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public interface Removable {
    void setOnDelete(EventHandler<ActionEvent> handler);
}
