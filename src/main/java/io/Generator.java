package io;

import java.io.File;

public final class Generator {
    public boolean generate(File f) {
        try {
            final var parent = f.getParentFile();
            if (parent.exists() || parent.mkdirs()) return f.createNewFile();
            else return false;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate file " + f.getPath() + " due to exception " + e);
        }
    }
}
