package nader.openchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
    private Button sendButton, policyButton;
    private String deviceid;

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("NEW_MESSAGES".equals(intent.getAction())) {
                List<Message> newMessages = intent.getParcelableArrayListExtra("messages");
                if (newMessages != null) {
                    messageList.clear();
                    messageList.addAll(newMessages);
                    messageAdapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(messageList.size() - 1);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        policyButton = findViewById(R.id.policyButton);

        // Retrieve deviceId from shared preferences
        deviceid = SharedPrefManager.getInstance(ChatActivity.this).getDeviceId();
        Log.d("ChatActivity Device ID : ", deviceid);

        // Check if the deviceId is null
        if (deviceid == null || deviceid.isEmpty()) {
            Toast.makeText(ChatActivity.this, "Device ID is missing", Toast.LENGTH_SHORT).show();
            return;
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter with an empty message list and deviceId
        messageAdapter = new MessageAdapter(new ArrayList<>(), deviceid, recyclerView);
        recyclerView.setAdapter(messageAdapter);

        fetchMessages(); // Fetch the messages on activity creation

        sendButton.setOnClickListener(v -> sendMessage());

        // Hide policy button while typing
        messageEditText.setOnFocusChangeListener((v, hasFocus) -> {
            policyButton.setVisibility(hasFocus ? View.GONE : View.VISIBLE);
        });

        // Start Background Service
        startService(new Intent(this, MessageFetchService.class));

        // Register BroadcastReceiver
        registerReceiver(messageReceiver, new IntentFilter("NEW_MESSAGES"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(messageReceiver);
        stopService(new Intent(this, MessageFetchService.class)); // Stop service when chat closes
    }

    private void fetchMessages() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getRecentMessages().enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful()) {
                    messageList = response.body();
                    messageAdapter.setMessages(messageList);
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
            return;
        }

        SendMessageRequest request = new SendMessageRequest(deviceid, messageText);
        ApiService apiService = RetrofitClient.getApiService();
        apiService.sendMessage(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    fetchMessages(); // Fetch messages again after sending a new one
                    messageEditText.setText(""); // Clear the message input field
                } else {
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