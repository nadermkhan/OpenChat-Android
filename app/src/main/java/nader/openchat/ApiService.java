package nader.openchat;

//package nader.openchat.network;

import java.util.List;

import nader.openchat.Message;
import nader.openchat.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
	
	@POST("/users")
	Call<Void> createUser(@Body UserCreateRequest userCreateRequest);
	
	@GET("/users/by-device/{device_id}")
	Call<User> getUserDetails(@Path("device_id") String deviceId);
	
	@POST("/messages")
	Call<Void> sendMessage(@Body SendMessageRequest request);
	
	@GET("/messages")
	Call<List<Message>> getRecentMessages();
	
	@POST("/users/{id}/like")
	Call<Void> likeUser(@Path("id") String userId);
	
	@POST("/users/{id}/rate")
	Call<Void> rateUser(@Path("id") String userId, @Body int rating);
}