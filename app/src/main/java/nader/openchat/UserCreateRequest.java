package nader.openchat;
public class UserCreateRequest {
    private String device_id;

    public UserCreateRequest(String device_id) {
        this.device_id = device_id;
    }

    public String getDevice_id() {
        return device_id;
    }
}
