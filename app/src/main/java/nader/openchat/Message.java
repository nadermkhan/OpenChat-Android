package nader.openchat;

//package nader.openchat.models;

public class Message {
	
	private String text;
	private String timestamp;
	
	// Getter and Setter methods
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}