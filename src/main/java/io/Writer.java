package io;

import java.io.File;
import java.io.FileWriter;

public final class Writer {
    public void append(File f, String s) {
        try {
            final var fileWriter = new FileWriter(f);
            fileWriter.append(s);
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("Failed to write data to file " + f.getPath() + " due to exception " + e);
        }
    }

    public void write(File f, String s) {
        try {
            final var fileWriter = new FileWriter(f);
            fileWriter.write(s);
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("Failed to write data to file " + f.getPath() + " due to exception " + e);
        }
    }
}
