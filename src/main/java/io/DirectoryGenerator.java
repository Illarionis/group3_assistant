package io;

import java.io.File;

/**
 * Provides a tool to generate (sub)directories.
 **/
public final class DirectoryGenerator {
    /**
     * Generates all directories a directory path consists of. <br>
     * Remark: If the directory already exists, then nothing will happen upon calling this method.
     *
     * @param directoryPath The path to the directory that should be generated.
     **/
    public void generate(String directoryPath) {
        final File dir = new File(directoryPath);
        if (dir.exists() && dir.isFile()) {
            throw new IllegalArgumentException("Provided a file path instead of a directory path");
        } else if (!dir.exists() && !dir.mkdirs()) {
            throw new IllegalStateException("Failed to create directory " + directoryPath);
        }
    }
}
