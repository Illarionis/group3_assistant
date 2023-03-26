package io;

import java.io.File;
import java.util.Objects;

/**
 * Provides a tool to obtain all files a directory directly contains.
 **/
public final class DirectoryReader {
    /**
     * Lists all files a directory directly contains.
     *
     * @param directoryPath The path to the directory for which the files should be listed.
     * @return A file array with a length >= 1, if the directory exists and contains one or more files.<br>
     *         A file array with a length == 0, otherwise.
     **/
    public File[] getFiles(String directoryPath) {
        final File dir = new File(directoryPath);
        if (dir.exists() && dir.isDirectory()) {
            final File[] files = dir.listFiles();
            return Objects.requireNonNullElse(files, new File[0]);
        }
        return new File[0];
    }
}
