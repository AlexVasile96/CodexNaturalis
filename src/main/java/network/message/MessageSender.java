package network.message;

/**
 * This classtakes a message that the server needs to send to a client or vice versa and adds a String that specifies the type of message wrapped.
 * In this way, when a client or server receives the message it can first read the message type and then decide  which operation to carry out
 */

public class MessageSender {
    private MessagesEnum messagesEnum;
    private String messageToSend;
    public MessageSender(MessagesEnum messagesEnum, String messageToSend) {
        this.messagesEnum = messagesEnum;
        this.messageToSend = messageToSend;
    }
    public MessagesEnum getMessages() {
        return messagesEnum;
    }

    public String getMessageToSend() {
        return messageToSend;
    }
}
