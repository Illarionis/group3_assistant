package engine;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public final class Data {
    private final File dir;

    public Data(File dir) {
        if (dir.exists() && !dir.isDirectory()) {
            throw new IllegalArgumentException("Provided a file instead of a directory.");
        } else if (!dir.exists() && !dir.mkdirs()) {
            throw new IllegalArgumentException("Could not create the local directory " + dir.getPath());
        }
        this.dir = dir;
    }

    private void decrypt(char[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = --arr[i];
        }
    }

    private void encrypt(char[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = ++arr[i];
        }
    }

    public boolean delete() {
        return delete(dir);
    }

    public boolean delete(File f) {
        if (!f.exists()) {
            return false;
        } else if (f.isFile()) {
            return f.delete();
        }
        final File[] files = f.listFiles();
        if (files != null) {
            for (var file : files) {
                if (!file.delete()) {
                    throw new IllegalStateException("Failed to delete file " + file.getPath());
                }
            }
        }
        return f.delete();
    }

    public boolean delete(String accountId) {
        final File targetDir = Path.of(dir.getPath(), accountId).toFile();
        return delete(targetDir);
    }

    public boolean delete(String accountId, String fileId) {
        final File f = Path.of(dir.getPath(), accountId, fileId).toFile();
        return delete(f);
    }

    public File[] read(String accountId) {
        final File targetDir = Path.of(dir.getPath(), accountId).toFile();
        if (targetDir.exists() && !targetDir.isDirectory()) {
            throw new IllegalStateException("Failed to find a directory for " + accountId);
        } else if (!targetDir.exists()) {
            return new File[0];
        }
        final File[] files = targetDir.listFiles();
        return Objects.requireNonNullElseGet(files, () -> new File[0]);
    }

    public String read(File f) {
        if (!f.exists()) {
            throw new IllegalArgumentException("Provided a non-existent file " + f.getPath());
        }
        try {
            final FileReader r = new FileReader(f);
            final char[] buffer = new char[(int) f.length()];
            final boolean failed = r.read(buffer) != buffer.length;
            r.close();
            if (failed) {
                throw new IllegalStateException("Failed to fully read the file " + f.getPath());
            }
            decrypt(buffer);
            return String.valueOf(buffer);
        } catch (IOException e) {
            throw new IllegalStateException("Could not retrieve data due to exception " + e);
        }
    }

    public void store(String accountId, String fileId, String data) {
        final File targetDir = Path.of(dir.getPath(), accountId).toFile();
        if (!targetDir.exists() && !targetDir.mkdirs()) {
            throw new IllegalStateException("Failed to create directory for " + accountId);
        }
        final File targetFile = Path.of(targetDir.getPath(), fileId).toFile();
        if (!targetFile.exists()) {
            try {
                if (!targetFile.createNewFile()) {
                    throw new IllegalStateException("Failed to create file " + targetFile.getPath());
                }
            } catch (IOException e) {
                throw new IllegalStateException("Could not create file due to exception " + e);
            }
        }
        try {
            final FileWriter w = new FileWriter(targetFile);
            final char[] arr = data.toCharArray();
            encrypt(arr);
            w.write(arr);
            w.close();
        } catch (IOException e) {
            throw new IllegalStateException("Could not store data due to exception " + e);
        }
    }
}
