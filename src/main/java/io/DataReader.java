package io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Provides a tool that reads an entire file to a single string.
 **/
public final class DataReader {
    /**
     * Reads the entirety of a file to a string.
     *
     * @param f The file that should be read.
     * @return A string representing all the data the file contains.
     **/
    public String read(File f) {
        if (!f.exists()) {
            throw new IllegalArgumentException("Can not read non-existing file " + f.getPath());
        }
        try {
            final FileReader r = new FileReader(f);
            final char[] buffer = new char[(int) f.length()];
            final boolean failed = r.read(buffer) != buffer.length;
            r.close();
            if (failed) {
                throw new IllegalStateException("Failed to fully read the file " + f.getPath());
            }
            final String encrypted = String.valueOf(buffer);
            return DataSecurity.decrypt(encrypted);
        } catch (IOException e) {
            throw new IllegalStateException("Could not retrieve data due to exception " + e);
        }
    }
}
