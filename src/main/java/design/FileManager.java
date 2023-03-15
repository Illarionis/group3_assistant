package design;

/**
 * Defines functionalities to manage account wise associated data.
 **/
public interface FileManager {
    /**
     * Obtains all data files of an account that are associated with this file manager.
     *
     * @param accountId A string representing the account id that is requesting all his data files.
     * @return All data files that have been associated with the account id within this file manager.
     **/
    DataFile[] getFiles(String accountId);

    /**
     * Creates a new file to save data.
     *
     * @param accountId A string providing the account id that is requesting a new file.
     * @return A new data file associated with the account id within this file manager.
     **/
    DataFile createNewFile(String accountId);
}
