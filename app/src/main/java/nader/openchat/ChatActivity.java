package nader.openchat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import nader.openchat.R;
import nader.openchat.MessageAdapter;
import nader.openchat.Message;
import nader.openchat.ApiService;
import nader.openchat.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.io.IOException;
import nader.openchat.SendMessageRequest;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private EditText messageEditText;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        // Retrieve deviceId from shared preferences
        String deviceid = SharedPrefManager.getInstance(ChatActivity.this).getDeviceId();
        Log.d("ChatActivity Device ID : ", deviceid);

        // Check if the deviceId is null
        if (deviceid == null || deviceid.isEmpty()) {
            Toast.makeText(ChatActivity.this, "Device ID is missing", Toast.LENGTH_SHORT).show();
            return; // Stop further execution if no deviceId is available
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter with an empty message list and deviceId
        messageAdapter = new MessageAdapter(new ArrayList<Message>(), deviceid, recyclerView);
        recyclerView.setAdapter(messageAdapter);

        fetchMessages(); // Fetch the messages on activity creation

        // Send message button click listener
        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void fetchMessages() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getRecentMessages().enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful()) {
                    messageList = response.body();
                    Log.d("Fetched Messages", response.body().toString()); // Log the fetched messages
                    messageAdapter.setMessages(messageList); // Update the adapter
                } else {
                    Toast.makeText(ChatActivity.this, "Failed to fetch messages", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();

        if (messageText.isEmpty()) {
            Toast.makeText(ChatActivity.this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
            return; // Do not send the message if it is empty
        }

        // Retrieve deviceId from SharedPreferences
        String deviceid = SharedPrefManager.getInstance(ChatActivity.this).getDeviceId();

        // Ensure deviceId is not null or empty before proceeding
        if (deviceid == null || deviceid.isEmpty()) {
            Toast.makeText(ChatActivity.this, "Device ID is missing", Toast.LENGTH_SHORT).show();
            return; // Stop further execution if deviceId is missing
        }

        // Create a SendMessageRequest with deviceId and message
        SendMessageRequest request = new SendMessageRequest(deviceid, messageText);

        // Send the message via API
        ApiService apiService = RetrofitClient.getApiService();
        apiService.sendMessage(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    fetchMessages(); // Fetch messages again after sending a new one
                    messageEditText.setText(""); // Clear the message input field
                } else {
                    try {
                        // Log error response body if the message sending fails
                        Log.d("Response Body", response.errorBody() != null ? response.errorBody().string() : "No response body");
                    } catch (IOException e) {
                        Log.e("Response Body", "Error reading error body", e);
                    }
                    Toast.makeText(ChatActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
