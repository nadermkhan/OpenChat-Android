package nader.openchat;

public class Message {
	
	private int id;
	private int userId;  // Change this to match "user_id" in JSON
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
		this.created_at = created_at;  // Corrected parameter name
	}
	
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
