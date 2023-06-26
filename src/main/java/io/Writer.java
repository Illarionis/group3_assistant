package io;

import java.io.File;
import java.io.FileWriter;

public final class Writer {
    public boolean append(File f, String s) {
        try {
            final var writer = new FileWriter(f);
            writer.append(s);
            writer.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean write(File f, String s) {
        try {
            final var writer = new FileWriter(f);
            writer.write(s);
            writer.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
