package io;

import java.io.File;
import java.io.FileReader;

public final class Reader {
    public String read(File f) {
        try {
            final char[] data = new char[(int) f.length()];
            final var fileReader = new FileReader(f);
            final boolean succeeded = fileReader.read(data) == data.length;
            fileReader.close();
            if (succeeded) return String.valueOf(data);
            else return "";
        } catch (Exception e) {
            System.out.println("Failed to read data of file " + f.getPath() + " due to exception " + e);
            return "";
        }
    }
}
