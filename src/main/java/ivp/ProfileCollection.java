package ivp;

import design.Displayable;
import engine.Item;
import engine.Variable;
import gui.Borders;
import gui.Designer;
import gui.Factory;
import gui.Paddings;
import io.Generator;
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

import java.io.File;
import java.util.List;

public final class ProfileCollection implements Displayable {
    private final ImageView image;
    private final VBox panel;

    public ProfileCollection(Generator g, Factory f, Designer d, CamController c, FrameSaver s, File dir, File predict) {
        final File representations = new File("src/main/resources/ivp/database/representations_facenet512.pkl");

        image = new ImageView();
        Button takePicture = f.createButton("NO USER LOADED", 30, 30, Double.MAX_VALUE, 30);
        final VBox pictureHolder = f.createVBox(0, Paddings.CONTENT, Pos.CENTER, image);
        final ScrollPane pictureViewport = f.createScrollPane(pictureHolder);
        final VBox picturePanel = f.createVBox(5, Paddings.DEFAULT, Pos.TOP_CENTER, takePicture, pictureViewport);
        d.setBorder(Borders.GRAY, takePicture, pictureViewport);
        d.setVGrow(Priority.ALWAYS, pictureViewport, picturePanel);
        picturePanel.setPrefHeight(600);
        picturePanel.setPrefWidth(600);

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

        final Picture picture = (file) -> {
            takePicture.setText("Snap!");
            final var frame = c.takePicture();
            s.save(file, frame);
            final Image i = new Image(file.getAbsolutePath());
            image.setImage(i);
            if (representations.exists() && !representations.delete()) throw new IllegalStateException("Failed to delete face-net pkl so can not add a new user!");
            g.generate(predict);
        };

        Runnable sleep = () -> {
            try { Thread.sleep(200); }
            catch (Exception e) { throw new RuntimeException(e); }
        };

        Platform.runLater(sleep);

        final List<Node> entries = collectionHolder.getChildren();

        final Entry entry = (file) -> {
            final var item = new Item(f);
            item.setName(file.getName().replace(".jpg", ""));
            item.setOnDelete(deleteEvent -> {
                entries.remove(item.getPanel());
                if (file.delete()) System.out.println("Successfully deleted user image directory " + file.getPath());
                else System.out.println("Failed to delete user image directory " + file.getPath());
                if (representations.exists()) representations.delete();
                if (image.getImage().getUrl().equals(file.getAbsolutePath())) {
                    image.setImage(null);
                    takePicture.setOnAction(null);
                    takePicture.setText("NO USER LOADED");
                }
            });
            item.setOnAction(clickEvent -> {
                final Image i = new Image(file.getAbsolutePath());
                setImage(i);
                takePicture.setText("TAKE A NEW PICTURE");
                takePicture.setOnAction(captureEvent -> Platform.runLater(() -> picture.take(file)));
            });
            entries.add(item.getPanel());
            return item;
        };

        save.setOnAction(saveEvent -> {
            final String name = user.getValue();
            if (name.isBlank()) {
                System.out.println("Missing name to create a new user!");
                return;
            }
            user.setName("");

            final File targetFile = new File(dir.getPath() + "/" + name + ".jpg");
            g.generate(targetFile);

            Platform.runLater(() -> takePicture.setText("Will be taking 1 picture, look at the camera please"));
            Platform.runLater(sleep);
            Platform.runLater(() -> picture.take(targetFile));
            Platform.runLater(sleep);
            Platform.runLater(() -> takePicture.setText("Completed taking pictures"));
            Platform.runLater(sleep);
            Platform.runLater(() -> takePicture.setText("TAKE A NEW PICTURE"));
            takePicture.setOnAction(captureEvent -> Platform.runLater(() -> picture.take(targetFile)));
            entry.generate(targetFile);
        });

        final File[] files = dir.listFiles();
        if (files == null) return;
        for (var file : files) {
            if (file.getName().equals(representations.getName())) continue;
            if (file.isFile()) {
                final String name = file.getName();
                final var item = entry.generate(file);
                item.setName(name);
            }
        }
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

    @FunctionalInterface
    private interface Picture {
        void take(File f);
    }

    @FunctionalInterface
    private interface Entry {
        Item generate(File f);
    }
}
