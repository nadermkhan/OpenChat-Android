package nader.openchat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeAgoConv {
    public static String convert(String dateString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale.ENGLISH);

        try {
            Date date = inputFormat.parse(dateString);
            if (date == null) return "Invalid date";

            // Calculate time difference
            long diffMillis = new Date().getTime() - date.getTime();
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis);
            long hours = TimeUnit.MILLISECONDS.toHours(diffMillis);
            long days = TimeUnit.MILLISECONDS.toDays(diffMillis);

            // Determine "time ago" format
            if (minutes == 0) return "just now";
            if (minutes < 60) return minutes + " minutes ago";
            if (hours < 24) return hours + " hours ago";
            if (days == 1) return "1d ago";

            // If more than a day, return formatted date
            return outputFormat.format(date);
        } catch (ParseException e) {
            return "Invalid date format";
        }
    }

    
}
