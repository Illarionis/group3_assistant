package ivp;

import design.Displayable;
import gui.Borders;
import gui.Designer;
import gui.Factory;
import gui.Paddings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public final class DetectionStatus implements Displayable {
    private final Button detect;
    private final ImageView image;
    private final VBox panel;

    public DetectionStatus(Factory f, Designer d) {
        image = new ImageView();
        final VBox content = f.createVBox(5, Paddings.CONTENT, Pos.CENTER, image);
        final ScrollPane viewport = f.createScrollPane(content);
        VBox.setVgrow(viewport, Priority.ALWAYS);

        detect = f.createButton("Click to detect...", 30, 30, Double.MAX_VALUE, 30);
        panel  = f.createVBox(2, Paddings.DEFAULT, Pos.TOP_CENTER, detect, viewport);
        d.setBorder(Borders.GRAY, detect, viewport);
    }

    public void setDetection(EventHandler<ActionEvent> handler) {
        detect.setOnAction(handler);
    }

    public void setImage(Image i) {
        image.setImage(i);
        image.setFitWidth(panel.getWidth() - 50);
        image.setFitHeight(panel.getHeight() - 50);
    }

    public void setText(String s) {
        detect.setText(s);
    }

    @Override
    public Region getPanel() {
        return panel;
    }
}
