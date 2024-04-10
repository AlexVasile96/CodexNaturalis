package network.message;

/**
 * This classtakes a message that the server needs to send to a client or vice versa and adds a String that specifies the type of message wrapped.
 * In this way, when a client or server receives the message it can first read the message type and then decide  which operation to carry out
 */

public class MessageSender {
    private MessagesEnum messagesEnum;
    private String toSend;
    public MessageSender(MessagesEnum messagesEnum, String toSend) {
        this.messagesEnum = messagesEnum;
        this.toSend = toSend;
    }
    public MessagesEnum getMessages() {
        return messagesEnum;
    }
    public void setMessages(MessagesEnum messagesEnum) {
        this.messagesEnum = messagesEnum;
    }
    public String getToSend() {
        return toSend;
    }
    public void setToSend(String toSend) {
        this.toSend = toSend;
    }

}
