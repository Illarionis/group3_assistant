package design;

/**
 * Defines the properties that define a message.
 **/
public interface Message {
    /**
     * Obtains the content of the message.
     *
     * @return A string with a length >= 1.
     **/
    String getContent();

    /**
     * Obtains the date when this message was send.
     *
     * @return A string with a length >= 1.
     **/
    String getMessageSentDate();

    /**
     * Obtains the account id of the sender.
     *
     * @return A string with a length >= 1.
     **/
    String getSenderId();
}
