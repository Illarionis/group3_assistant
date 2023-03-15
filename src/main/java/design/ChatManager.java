package design;

/**
 * Defines functionalities to manage the chat.
 **/
public interface ChatManager<T extends Message> {
    /**
     * Adds a message to the chat.
     *
     * @param m The message that should be added to the chat.
     **/
    void addMessage(T m);
}
