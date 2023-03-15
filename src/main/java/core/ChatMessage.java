package core;

import design.Message;
import design.Recoverable;
import gui.MessageBox;

/**
 * Provides a message that can be displayed on the chat and is capable of beign stored.
 **/
public final class ChatMessage extends MessageBox implements Message, Recoverable {
    private final String sId;

    public ChatMessage(String content, String sendDate, String senderId) {
        super(content, sendDate);
        sId = senderId;
    }

    @Override
    public String getContent() {
        /* *
         * Task:
         * Obtain the content from the super class MessageBox
         *
         * Note:
         * This can only be finished if the MessageBox is finished.
         * */
        return null;
    }

    @Override
    public String getMessageSentDate() {
        /* *
         * Task:
         * Obtain the date the message has been sent from the super class.
         *
         * Note:
         * This can only be finished if the MessageBox is finished.
         * */
        return null;
    }

    @Override
    public String getSenderId() {
        return sId;
    }

    @Override
    public String snapshot() {
        /* *
         * Task:
         * Store the message content, sent date and sender id to a string in a fashion
         * such that the recover(String) method can reconstruct the message using the
         * information contained in the string.
         * */
        final String content = getContent();
        final String date = getMessageSentDate();
        return null;
    }

    @Override
    public void recover(String snapshot) {
        /* *
         * Task:
         * Use the string provided by the snapshot method to restore the message
         * as described in the snapshot string.
         * */
    }
}
