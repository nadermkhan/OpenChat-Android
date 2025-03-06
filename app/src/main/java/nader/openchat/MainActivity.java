package nader.openchat;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
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
	
	private EditText deviceIdEditText;
	private Button registerButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		deviceIdEditText = findViewById(R.id.deviceIdEditText);
		registerButton = findViewById(R.id.registerButton);
		deviceIdEditText.setText(NaderUIDGenerator.getDeviceUID(MainActivity.this));
		registerButton.setOnClickListener(v -> registerUser());
	}
	
	private void registerUser() {
    String deviceId = deviceIdEditText.getText().toString().trim();

    if (deviceId.isEmpty()) {
        Toast.makeText(MainActivity.this, "Device ID is required", Toast.LENGTH_SHORT).show();
        return;
    }

    // Create UserCreateRequest object
    UserCreateRequest request = new UserCreateRequest(deviceId);

    ApiService apiService = RetrofitClient.getApiService();
    apiService.createUser(request).enqueue(new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            if (response.isSuccessful()) {
                Log.d("API_RESPONSE", "Success: " + response.body()); 
                SharedPrefManager.getInstance(MainActivity.this).saveDeviceId(deviceId);
                startActivity(new Intent(MainActivity.this, ChatActivity.class));
                finish();
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
            Log.e("API_ERROR", "Network request failed", t); // Logs the error with stack trace
            Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_SHORT).show();
        }
    });
}

}