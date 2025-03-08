package nader.openchat;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable {

    private int id;
    private int userId;
    private String device_id;
    private String username;
    private String message;
    private String created_at;

    // Constructor
    public Message(int id, int userId, String device_id, String username, String message, String created_at) {
        this.id = id;
        this.userId = userId;
        this.device_id = device_id;
        this.username = username;
        this.message = message;
        this.created_at = created_at;
    }

    // Parcelable constructor
    protected Message(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        device_id = in.readString();
        username = in.readString();
        message = in.readString();
        created_at = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeString(device_id);
        dest.writeString(username);
        dest.writeString(message);
        dest.writeString(created_at);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return this.device_id;
    }

    public void setDeviceId(String deviceId) {
        this.device_id = deviceId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(String createdAt) {
        this.created_at = createdAt;
    }
}
