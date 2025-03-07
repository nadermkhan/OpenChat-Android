package nader.openchat;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import nader.openchat.NaderUIDGenerator;
import nader.openchat.R;
import nader.openchat.ApiService;
import nader.openchat.RetrofitClient;
import nader.openchat.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String PRODUCT_KEY_REGEX = "^[A-Z0-9]{5}-[A-Z0-9]{5}-[A-Z0-9]{5}-[A-Z0-9]{5}-[A-Z0-9]{5}$";
    
    private EditText deviceIdEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceIdEditText = findViewById(R.id.deviceIdEditText);
        registerButton = findViewById(R.id.registerButton);

        deviceIdEditText.setText(NaderUIDGenerator.getDeviceUID(MainActivity.this));

        // **Check if device ID is already saved and go to ChatActivity**
        String savedDeviceId = SharedPrefManager.getInstance(MainActivity.this).getDeviceId();
        if (savedDeviceId != null && !savedDeviceId.isEmpty()) {
            startChatActivity();
            return;
        }

        registerButton.setOnClickListener(v -> registerUser());
    }

    // Validate Device Key
    public static boolean isValidKey(String productKey) {
        return productKey != null && productKey.matches(PRODUCT_KEY_REGEX);
    }

    private void registerUser() {
        String deviceId = deviceIdEditText.getText().toString().trim();

        if (deviceId.isEmpty() || !isValidKey(deviceId)) {
            Toast.makeText(MainActivity.this, "Invalid Device ID", Toast.LENGTH_SHORT).show();
            return;
        }

        UserCreateRequest request = new UserCreateRequest(deviceId);
        ApiService apiService = RetrofitClient.getApiService();
        apiService.createUser(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("API_RESPONSE", "Success: " + response.body());
                    SharedPrefManager.getInstance(MainActivity.this).saveDeviceId(deviceId);
                    startChatActivity();
                } else {
                    try {
                        Log.d("API_RESPONSE", "Failed: " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e("API_RESPONSE", "Error reading error body", e);
                    }
                    Toast.makeText(MainActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API_ERROR", "Network request failed", t);
                Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startChatActivity() {
        startActivity(new Intent(MainActivity.this, ChatActivity.class));
        finish();
    }
}
