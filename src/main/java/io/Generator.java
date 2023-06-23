package io;

import java.io.File;

public final class Generator {
    public boolean generate(File f) {
        try {
            return f.createNewFile();
        } catch (Exception e) {
            System.out.println("Failed to generate file " + f.getPath() + " due to exception " + e);
            return false;
        }
    }
}
