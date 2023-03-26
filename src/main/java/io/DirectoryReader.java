package io;

import java.io.File;
import java.util.Objects;

public final class DirectoryReader {
    public File[] getFiles(String directoryPath) {
        final File dir = new File(directoryPath);
        if (dir.exists() && dir.isDirectory()) {
            final File[] files = dir.listFiles();
            return Objects.requireNonNullElse(files, new File[0]);
        }
        return new File[0];
    }
}
