package core;

import design.DataFile;
import design.FileManager;
import engine.SecuredFile;

import java.io.File;
import java.nio.file.Path;
import java.util.UUID;

/**
 * Provides a database to access and store data files.
 **/
public final class Database implements FileManager {
    private final String rootDirectory;

    /**
     * Creates a database that uses a specific directory to store all its data files.
     *
     * @param fileRootDirectory The root directory where the database should access and store datafiles.
     **/
    public Database(String fileRootDirectory) {
        rootDirectory = fileRootDirectory;
    }

    @Override
    public DataFile[] getFiles(String accountId) {
        final Path accountDataDirectory = Path.of(rootDirectory, accountId);
        final File directory = accountDataDirectory.toFile();
        final File[] files = directory.listFiles();
        if (files != null) {
            final SecuredFile[] securedFiles = new SecuredFile[files.length];
            for (int i = 0; i < files.length; i++) {
                securedFiles[i] = new SecuredFile(files[i]);
            }
            return securedFiles;
        } else {
            return new DataFile[0];
        }
    }

    @Override
    public DataFile createNewFile(String accountId) {
        final UUID fileId = UUID.randomUUID();
        final Path newFilePath = Path.of(rootDirectory, fileId.toString());
        final File newFile = newFilePath.toFile();
        try {
            if (newFile.createNewFile()) {
                return new SecuredFile(newFile);
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("File creation resulted into error: " + e);
            return null;
        }
    }
}
