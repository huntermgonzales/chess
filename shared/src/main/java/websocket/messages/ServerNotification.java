package websocket.messages;

public class ServerNotification extends ServerMessage{
    String message;

    public ServerNotification(ServerMessageType type) {
        super(type);
    }

    public void addMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        if (message == null) {
            return "";
        }
        return message;
    }

}
