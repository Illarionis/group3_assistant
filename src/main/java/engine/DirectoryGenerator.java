package engine;

import java.io.File;

public final class DirectoryGenerator {
    public void generate(String directoryPath) {
        final File dir = new File(directoryPath);
        if (dir.exists() && dir.isFile()) {
            throw new IllegalArgumentException("Provided a file path instead of a directory path");
        } else if (!dir.exists() && !dir.mkdirs()) {
            throw new IllegalStateException("Failed to create directory " + directoryPath);
        }
    }
}
