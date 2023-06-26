package ivp;

import design.Displayable;
import engine.Item;
import engine.Variable;
import gui.Borders;
import gui.Designer;
import gui.Factory;
import gui.Paddings;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.bytedeco.javacv.JavaFXFrameConverter;

import java.io.File;
import java.util.List;

public final class ProfileCollection implements Displayable {
    private final ImageView image;
    private final VBox panel;

    public ProfileCollection(Factory f, Designer d, CamController c, FrameSaver s, File dir) {
        image = new ImageView();
        final Label pictureLabel = f.createLabel("NO PICTURES TAKEN YET", Pos.CENTER, 30, 30, Double.MAX_VALUE, 30);
        final VBox pictureHolder = f.createVBox(0, Paddings.CONTENT, Pos.CENTER, image);
        final ScrollPane pictureViewport = f.createScrollPane(pictureHolder);
        final VBox picturePanel = f.createVBox(5, Paddings.DEFAULT, Pos.TOP_CENTER, pictureLabel, pictureViewport);
        d.setBorder(Borders.GRAY, pictureLabel, pictureViewport);
        d.setVGrow(Priority.ALWAYS, pictureViewport, picturePanel);

        Button save = f.createButton("SAVE", 200, 30, Double.MAX_VALUE, 30);
        Variable user = new Variable(f);
        user.setName("Name");
        user.setDescription("Provide a name");
        final Label newUserLabel = f.createLabel("ADD NEW USER", Pos.CENTER, 30, 30, Double.MAX_VALUE, 30);
        final VBox creationContent = f.createVBox(5, Paddings.CONTENT, Pos.CENTER, user.getPanel());
        final ScrollPane creationViewport = f.createScrollPane(creationContent);
        final VBox creationPanel = f.createVBox(2, Paddings.DEFAULT, Pos.TOP_CENTER, newUserLabel, creationViewport, save);
        d.setBorder(Borders.GRAY, newUserLabel, creationViewport, save);
        creationViewport.setPrefHeight(100);

        final Label collectionLabel = f.createLabel("USERS", Pos.CENTER, 30, 30, Double.MAX_VALUE, 30);
        final VBox collectionHolder = f.createVBox(5, Paddings.CONTENT, Pos.TOP_CENTER);
        final ScrollPane collectionViewport = f.createScrollPane(collectionHolder);
        final VBox collectionPanel = f.createVBox(2, Paddings.DEFAULT, Pos.TOP_CENTER, collectionLabel, collectionViewport);
        d.setBorder(Borders.GRAY, collectionLabel, collectionViewport);
        d.setVGrow(Priority.ALWAYS, collectionPanel, collectionViewport);
        collectionPanel.setPrefWidth(300);

        final VBox leftPanel = f.createVBox(2, Paddings.DEFAULT, Pos.TOP_CENTER, picturePanel, creationPanel);
        final HBox viewports = f.createHBox(2, Paddings.DEFAULT, Pos.CENTER, leftPanel, collectionPanel);
        panel = f.createVBox(2, Paddings.DEFAULT, Pos.TOP_CENTER, viewports);
        d.setHGrow(Priority.ALWAYS, leftPanel, collectionPanel);
        d.setVGrow(Priority.ALWAYS, viewports);

        final var converter = new JavaFXFrameConverter();
        final List<Node> entries = collectionHolder.getChildren();
        save.setOnAction(saveEvent -> {
            final String name = user.getValue();
            if (name.isBlank()) {
                System.out.println("Missing name to create a new user!");
                return;
            }
            
            final File targetDir = new File(dir.getPath() + "/" + name);
            if (!targetDir.exists() && !targetDir.mkdirs()) {
                System.out.println("Failed to generate user image directory at " + targetDir.getPath());
                return;
            }

            Platform.runLater(() -> {
                pictureLabel.setText("Will be taking 1 picture, look at the camera please");
                try { Thread.sleep(200); }
                catch (Exception e) { throw new RuntimeException(e); }
            });

            final File target = new File(targetDir + "/" + name + "(" + 0 + ")" + ".jpg");
            Platform.runLater(() -> {
                pictureLabel.setText("Snap!");
                final var frame = c.takePicture();
                s.save(target, frame);
                setImage(converter.convert(frame));
                try { Thread.sleep(200); }
                catch (Exception e) { throw new RuntimeException(e); }
            });

            Platform.runLater(() -> pictureLabel.setText("Completed taking pictures"));

            final var item = new Item(f);
            item.setName(name);
            item.setOnDelete(event -> {
                entries.remove(item.getPanel());
                final File[] storedImages = targetDir.listFiles();
                if (storedImages != null) {
                    for (var file : storedImages) {
                        if (file.delete()) System.out.println("Successfully deleted user image");
                        else System.out.println("Failed to delete user image at " + file.getPath());
                    }
                }
                if (targetDir.delete()) System.out.println("Successfully deleted user image directory " + targetDir.getPath());
                else System.out.println("Failed to delete user image directory " + targetDir.getPath());
            });
            entries.add(item.getPanel());
        });
    }

    private void setImage(Image i) {
        image.setImage(i);
        image.setFitWidth(panel.getWidth() - 50);
        image.setFitHeight(panel.getHeight() - 50);
    }

    @Override
    public Region getPanel() {
        return panel;
    }
}
