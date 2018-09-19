package util;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateTimeFormats {
    //Calculate time duration with reface to GMT for display purpose
    public String isCommentedAgo(String datetime) {
        String result = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));

            Date past = format.parse(datetime);
            Date now = new Date();

            if (TimeUnit.MILLISECONDS.toMillis(now.getTime() - past.getTime()) > 0 && TimeUnit.MILLISECONDS.toMillis(now.getTime() - past.getTime()) < 60000) {
                result = Constants.JUST_NOW;
            } else if (TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) > 0 && TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) < 60) {
                result = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + Constants.MINUTE_AGO;
            } else if (TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) > 0 && TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) < 24) {
                result = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) + Constants.HOURS_AGO;
            } else if (TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) > 0) {
                result = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + Constants.DAY_AGO;
            }

        } catch (Exception j) {
            j.printStackTrace();
        }
        return result;
    }


}
