package nader.openchat;

public class SendMessageRequest {
    private String device_id;
    private String message;

    public SendMessageRequest(String device_id, String message) {
        this.device_id = device_id;
        this.message = message;
    }

    public String getDevice_id() { return device_id; }
    public String getMessage() { return message; }
}