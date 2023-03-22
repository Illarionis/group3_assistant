package engine;

import java.io.File;
import java.io.IOException;

public final class DataReader {
    public String read(File f) {
        if (!f.exists()) {
            throw new IllegalArgumentException("Can not read non-existing file " + f.getPath());
        }
        try {
            final java.io.FileReader r = new java.io.FileReader(f);
            final char[] buffer = new char[(int) f.length()];
            final boolean failed = r.read(buffer) != buffer.length;
            r.close();
            if (failed) {
                throw new IllegalStateException("Failed to fully read the file " + f.getPath());
            }
            return String.valueOf(buffer);
        } catch (IOException e) {
            throw new IllegalStateException("Could not retrieve data due to exception " + e);
        }
    }
}
