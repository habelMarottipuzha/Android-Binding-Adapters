package in.habel.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by habel on 24/4/17.
 */

@SuppressWarnings("WeakerAccess")
public class DateHelper {

    public static String getFormattedDate(long millis) {
        return getFormattedDate(millis, "H:mm aaa");
    }

    public static String getFormattedDate(long millis, String format) {
        return getFormattedDate(millis, format, true);
    }

    public static String getFormattedDate(long millis, String format, boolean intelligent) {
        Date date = new Date(millis);
        DateFormat formatter = new SimpleDateFormat(format, Locale.ENGLISH);
        return formatter.format(date);
    }

}
