package gui;

import java.util.Queue;

public interface Layout {
    /**
     * Obtains the unprocessed messages from the active chat tab.
     *
     * @return A queue of strings with a size >= 0, where each string represents a message the user has typed.
     * @throws IllegalStateException Thrown when there is not a single chat tab open upon calling this method.
     **/
    Queue<String> getUnprocessedMessagesFromActiveTab();

    /**
     * Determines whether the active conversation has any unprocessed messages from the user.
     *
     * @return True, if there is one or more messages that have been unprocessed. <br>
     *         False, otherwise.
     **/
    boolean hasUnprocessedMessagesAtActiveTab();
}
