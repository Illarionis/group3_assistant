package design;

/**
 * Defines functionalities a data file should have.
 **/
public interface DataFile {
    /**
     * Obtains the data that has been stored.
     *
     * @return A string providing the data that has been saved.
     **/
    String getData();

    /**
     * Deletes the stored data (file).
     **/
    void deleteData();

    /**
     * Stores data to the file, rewriting any existing data.
     *
     * @param s A string encapsulating the data that should be stored.
     **/
    void storeData(String s);
}
