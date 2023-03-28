package io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Provides a tool that writes a single string to a file.
 **/
public final class DataWriter {
    private void createFile(File f) {
        try {
            if (!f.createNewFile()) {
                throw new IllegalStateException("Failed to create file " + f.getPath());
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not create file due to exception " + e);
        }
    }

    /**
     * Writes a string to a file. <br>
     * Remark: Any previously stored data in the file will be lost upon calling this method.
     *
     * @param f The file to which the data should be written.
     * @param s A string representing the data that should be written.
     **/
    public void write(File f, String s) {
        if (!f.exists()) {
            createFile(f);
        }
        try {
            final String encrypted = DataSecurity.encrypt(s);
            final FileWriter w = new FileWriter(f);
            w.write(encrypted);
            w.close();
        } catch (IOException e) {
            throw new IllegalStateException("Could not store data due to exception " + e);
        }
    }
}
