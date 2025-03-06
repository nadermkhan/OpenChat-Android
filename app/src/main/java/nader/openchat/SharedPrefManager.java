package nader.openchat;

//package nader.openchat.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
	
	private static final String PREF_NAME = "openchat_pref";
	private static final String DEVICE_ID_KEY = "device_id";
	private static SharedPrefManager instance;
	private SharedPreferences sharedPreferences;
	
	private SharedPrefManager(Context context) {
		sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
	}
	
	public static SharedPrefManager getInstance(Context context) {
		if (instance == null) {
			instance = new SharedPrefManager(context);
		}
		return instance;
	}
	
	public void saveDeviceId(String deviceId) {
		sharedPreferences.edit().putString(DEVICE_ID_KEY, deviceId).apply();
	}
	
	public String getDeviceId() {
		return sharedPreferences.getString(DEVICE_ID_KEY, "");
	}
}