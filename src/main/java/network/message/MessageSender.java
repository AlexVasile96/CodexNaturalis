package network.message;

/**
 * This classtakes a message that the server needs to send to a client or vice versa and adds a String that specifies the type of message wrapped.
 * In this way, when a client or server receives the message it can first read the message type and then decide  which operation to carry out
 */

public class MessageSender {
    private Messages messages;
    private String toSend;
    public MessageSender(Messages messages, String toSend) {
        this.messages = messages;
        this.toSend = toSend;
    }
    public Messages getMessages() {
        return messages;
    }
    public void setMessages(Messages messages) {
        this.messages = messages;
    }
    public String getToSend() {
        return toSend;
    }
    public void setToSend(String toSend) {
        this.toSend = toSend;
    }

}
