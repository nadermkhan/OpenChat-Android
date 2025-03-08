package nader.openchat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import java.util.ArrayList;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageFetchService extends Service {
    private static final String TAG = "MessageFetchService";
    private static final String CHANNEL_ID = "MessageFetchServiceChannel";
    private static final int FETCH_INTERVAL =3000; // Fetch messages every 5 seconds

    private Handler handler = new Handler();
    private ApiService apiService;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        apiService = RetrofitClient.getApiService();
        Log.d(TAG, "Service Created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1, getNotification());
        handler.post(fetchMessagesRunnable);
        return START_STICKY;
    }

    private final Runnable fetchMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            fetchMessages();
            handler.postDelayed(this, FETCH_INTERVAL);
        }
    };

    private void fetchMessages() {
        apiService.getRecentMessages().enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Message> newMessages = response.body();
                    Intent broadcastIntent = new Intent("NEW_MESSAGES");
                    broadcastIntent.putParcelableArrayListExtra("messages", new ArrayList<>(newMessages));
                    sendBroadcast(broadcastIntent);
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Log.e(TAG, "Error fetching messages", t);
            }
        });
    }

    private Notification getNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Chat Service Running")
                .setContentText("Fetching new messages...")
                .setSmallIcon(R.drawable.ic_send)
                .build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID, "Message Fetch Service", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(fetchMessagesRunnable);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
