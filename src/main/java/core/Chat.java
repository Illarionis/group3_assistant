package core;

import design.ChatManager;
import design.Recoverable;
import gui.ChatPane;

/**
 * Provides a modifiable chat that can be stored.
 **/
public final class Chat extends ChatPane implements ChatManager<ChatMessage>, Recoverable {
    /**
     * Creates a new empty chat.
     **/
    public Chat() {
        /* Place Holder */
    }

    /**
     * Creates a chat from a snapshot.
     *
     * @param snapshot A string describing the messages the chat should have.
     **/
    public Chat(String snapshot) {
        recover(snapshot);
    }

    @Override
    public void addMessage(ChatMessage m) {
        final var messages = getChildren();
        if (!messages.contains(m)) {
            messages.add(m);
        }
    }

    @Override
    public void recover(String snapshot) {
        /* *
         * Task:
         * Obtain each message snap from the snapshot such that all
         * messages that have been stored in the snapshot can be recovered.
         * */
    }

    @Override
    public String snapshot() {
        /* *
         * Task:
         * Store each message snap in a string in such a fashion that the string
         * can be used by the recover(String) method to rebuild the list of messages.
         * */
        for (var node : getChildren()) {
            final ChatMessage m = (ChatMessage) node;
            final String messageSnap = m.snapshot();

        }
        return null;
    }
}
