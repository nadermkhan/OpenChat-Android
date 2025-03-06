package nader.openchat;

//package nader.openchat.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
	
	private static final String BASE_URL = "https://cymasas.com";
	private static Retrofit retrofit;
	
	public static ApiService getApiService() {
		if (retrofit == null) {
			retrofit = new Retrofit.Builder()
			.baseUrl(BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.build();
		}
		return retrofit.create(ApiService.class);
	}
}