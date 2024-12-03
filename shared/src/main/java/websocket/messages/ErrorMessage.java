package websocket.messages;

public class ErrorMessage extends ServerMessage{

    String errorMessage;

    public ErrorMessage(ServerMessageType type) {
        super(type);
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
