package engine;

import design.DataFile;
import design.DataSecurity;

import java.io.File;

/**
 * Provides a file to load and save data.
 **/
public final class SecuredFile extends DataSecurity implements DataFile {
    private final File dataFile;

    public SecuredFile(File f) {
        dataFile = f;
    }

    @Override
    protected String decrypt(String s) {
        /* *
         * Task:
         * Decrypt the encrypted string to obtain the string encapsulating the data that has been saved.
         * */
        return null;
    }

    @Override
    protected String encrypt(String s) {
        /* *
         * Task:
         * Encrypt the string in whatever fashion you want.
         * */
        return null;
    }

    @Override
    public String getData() {
        /* *
         * Task:
         * Read the entire file to obtain the encrypted string.
         * Decrypt the encrypted string and return the decrypted version.
         * */
        final String s = "";

        return decrypt(s);
    }

    @Override
    public void deleteData() {
        if (!dataFile.delete()) {
            System.out.println("Failed to delete file " + dataFile);
        }
    }

    @Override
    public void storeData(String s) {
        /* *
         * Task:
         * Store the encrypted string.
         * */
        final String encrypted = encrypt(s);
    }
}
