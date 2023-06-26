package gui;

import javafx.scene.layout.*;

public final class Designer {
    public void setBorder(Border b, Region... regions) {
        for (Region r : regions) r.setBorder(b);
    }

    public void setHGrow(Priority p, Region... regions) {
        for (Region r : regions) HBox.setHgrow(r, p);
    }

    public void setVGrow(Priority p, Region... regions) {
        for (Region r : regions) VBox.setVgrow(r, p);
    }
}
