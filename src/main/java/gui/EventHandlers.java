package gui;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

public final class EventHandlers {
    public static final EventHandler<MouseEvent> HIGHLIGHT_REGION = event -> {
        final var region = (Region) event.getSource();
        if (region.getBackground() == Backgrounds.DEFAULT) region.setBackground(Backgrounds.HIGHLIGHT);
    };

    public static final EventHandler<MouseEvent> REMOVE_HIGHLIGHT = event -> {
        final var region = (Region) event.getSource();
        if (region.getBackground() == Backgrounds.HIGHLIGHT) region.setBackground(Backgrounds.DEFAULT);
    };
}
