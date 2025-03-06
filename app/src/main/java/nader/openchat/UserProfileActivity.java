package nader.openchat;

//package nader.openchat.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import nader.openchat.R;
import nader.openchat.network.ApiService;
import nader.openchat.network.RetrofitClient;
import nader.openchat.utils.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {
	
	private TextView userNameTextView;
	private TextView userBioTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
		
		userNameTextView = findViewById(R.id.userNameTextView);
		userBioTextView = findViewById(R.id.userBioTextView);
		
		loadUserProfile();
	}
	
	private void loadUserProfile() {
		String deviceId = SharedPrefManager.getInstance(this).getDeviceId();
		
		ApiService apiService = RetrofitClient.getApiService();
		apiService.getUserDetails(deviceId).enqueue(new Callback<User>() {
			@Override
			public void onResponse(Call<User> call, Response<User> response) {
				if (response.isSuccessful()) {
					User user = response.body();
					userNameTextView.setText(user.getName());
					userBioTextView.setText(user.getBio());
				}
			}
			
			@Override
			public void onFailure(Call<User> call, Throwable t) {
				// Handle failure
			}
		});
	}
}