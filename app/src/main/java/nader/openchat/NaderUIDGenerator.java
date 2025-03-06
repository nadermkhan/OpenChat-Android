package nader.openchat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.UUID;

public class NaderUIDGenerator {

    private static final String PREFS_NAME = "DeviceUID";
    private static final String PREFS_KEY = "unique_id";
    private static final String FILE_NAME = "device_uid.txt";
    
    public static String getDeviceUID(Context context) {
        String uid = getUIDFromStorage(context);
        if (uid != null) return uid; 
        
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String hardwareInfo = Build.MANUFACTURER + Build.MODEL + Build.HARDWARE + Build.FINGERPRINT;

        String base = androidId + hardwareInfo;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            base += Build.SERIAL; // SERIAL deprecated after Oreo
        }

        String hashedUID = hashSHA256(base);
        String formattedUID = formatToProductKey(hashedUID);

        saveUID(context, formattedUID);
        return formattedUID;
    }

    private static String hashSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return UUID.randomUUID().toString().replace("-", "");
        }
    }

    private static String formatToProductKey(String hash) {
        return hash.substring(0, 5).toUpperCase() + "-" +
               hash.substring(5, 10).toUpperCase() + "-" +
               hash.substring(10, 15).toUpperCase() + "-" +
               hash.substring(15, 20).toUpperCase() + "-" +
               hash.substring(20, 25).toUpperCase();
    }

    private static void saveUID(Context context, String uid) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(PREFS_KEY, uid).apply();

        File file = new File(context.getExternalFilesDir(null), FILE_NAME);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(uid.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getUIDFromStorage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String storedUID = prefs.getString(PREFS_KEY, null);
        if (storedUID != null) return storedUID;

        File file = new File(context.getExternalFilesDir(null), FILE_NAME);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] data = new byte[(int) file.length()];
                fis.read(data);
                return new String(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
