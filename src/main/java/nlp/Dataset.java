package nlp;

import design.Action;
import design.Displayable;
import engine.Data;
import gui.Borders;
import gui.Designer;
import gui.Factory;
import gui.Paddings;
import io.Generator;
import io.Reader;
import io.Writer;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class Dataset implements Displayable {
    private final Action read, write;
    private final VBox panel;
    private File csv, flag;

    public Dataset(Factory f, Designer d, Generator g, Reader r, Writer w) {
        final Button add  = f.createButton("ADD", 30, 30, Double.MAX_VALUE, 30);
        final Button save = f.createButton("SAVE", 30, 30, Double.MAX_VALUE, 30);
        final HBox buttons = f.createHBox(2, Paddings.DEFAULT, Pos.CENTER, add, save);
        final VBox content = f.createVBox(5, Paddings.CONTENT, Pos.TOP_CENTER);
        final ScrollPane viewport = f.createScrollPane(content);
        panel = f.createVBox(2, Paddings.DEFAULT, Pos.CENTER, viewport, buttons);
        panel.setFillWidth(true);

        d.setBorder(Borders.GRAY, add, save, viewport);
        d.setHGrow(Priority.ALWAYS, add, save);
        VBox.setVgrow(viewport, Priority.ALWAYS);

        final List<Data> collection = new ArrayList<>();
        final List<Node> nodes = content.getChildren();
        final Entry entry = () -> {
            final var data = new Data(f, d);
            data.setOnDelete(event -> {
                nodes.remove(data.getPanel());
                collection.remove(data);
            });
            nodes.add(data.getPanel());
            collection.add(data);
            return data;
        };

        read = () -> {
            if (csv == null) return;
            final String s = r.read(csv);
            final String[] lines = s.split("\n");
            for (int i = 1; i < lines.length; i++) {
                final String[] values = lines[i].split(",");
                final var data = entry.generate();
                data.setX(values[0]);
                data.setY(values[1]);
            }
        };

        write = () -> {
            if (csv == null || flag == null || flag.exists() || collection.size() == 0) return;
            var data = collection.get(0);
            final var stringBuilder = new StringBuilder("X,Y\n");
            stringBuilder.append(data.getX()).append(",").append(data.getY());
            for (int i = 1; i < collection.size(); i++) stringBuilder.append("\n").append(data.getX()).append(",").append(data.getY());
            w.write(csv, stringBuilder.toString());
            g.generate(flag);
        };

        add.setOnAction(event -> entry.generate());
        save.setOnAction(event -> write.execute());
    }

    public void load(File f) {
        csv = f;
        read.execute();
    }

    public void setOnSaved(File f) {
        flag = f;
    }

    @Override
    public Region getPanel() {
        return panel;
    }

    @FunctionalInterface
    private interface Entry {
        Data generate();
    }
}
