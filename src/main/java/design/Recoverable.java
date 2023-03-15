package design;

/**
 * Defines functionality that is used to restore objects to a previously captured state.
 **/
public interface Recoverable {
    /**
     * Recovers the object to a previously captured state.
     *
     * @param snapshot The string containing all the information needed
     *                 to restore the object to its previous state.
     **/
    void recover(String snapshot);

    /**
     * Stores the state of the object to a string. <br><br>
     * Note: The string should contain all relevant information such that
     *       when it is passed to the recover(String) method, the object
     *       will recover its previous state.
     * @return A string that can be used by the recover(String) method to
     *         restore the object to its previously captured state.
     **/
    String snapshot();
}
