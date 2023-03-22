package engine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DataWriter {
    private void createFile(File f) {
        try {
            if (!f.createNewFile()) {
                throw new IllegalStateException("Failed to create file " + f.getPath());
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not create file due to exception " + e);
        }
    }

    public void write(File f, String s) {
        if (!f.exists()) {
            createFile(f);
        }
        try {
            final FileWriter w = new FileWriter(f);
            w.write(s);
            w.close();
        } catch (IOException e) {
            throw new IllegalStateException("Could not store data due to exception " + e);
        }
    }
}
