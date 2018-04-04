package in.zuppbikes.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class DateUtils {
    static String[] days = {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};

    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;
    public static final int SUNDAY = 7;

    /**
     * Check if the time supplied in milliseconds is today's date
     *
     * @param iTimeMilliSeconds time in milliseconds
     * @return Boolean
     */
    public static boolean isToday(long iTimeMilliSeconds) {
        Date aConvertedDate = new Date(iTimeMilliSeconds);
        Calendar aConvertedCal = Calendar.getInstance();
        aConvertedCal.setTime(aConvertedDate);
        Calendar aCurrentCal = Calendar.getInstance();
        return aConvertedCal.get(Calendar.DAY_OF_MONTH) == aCurrentCal.get(Calendar.DAY_OF_MONTH) && aConvertedCal.get(Calendar.MONTH) == aCurrentCal.get(Calendar.MONTH) && aConvertedCal.get(Calendar.YEAR) == aCurrentCal.get(Calendar.YEAR);
    }

    /**
     * Checks if the supplied time in milliseconds is before today's date
     *
     * @param iTimeMilliSeconds time in milliseconds
     * @return
     */
    public static boolean isBefore(long iTimeMilliSeconds) {
        Date aConvertedDate = new Date(iTimeMilliSeconds);
        Calendar aConvertedCal = Calendar.getInstance();
        aConvertedCal.setTime(aConvertedDate);
        return aConvertedDate.before(Calendar.getInstance().getTime());
    }

    public static String formatFromServer(String iDate) {
        SimpleDateFormat aFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        try {
            aFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = aFormat.parse(iDate);
            SimpleDateFormat postFormatter = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            postFormatter.setTimeZone(TimeZone.getDefault());
            return postFormatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String formatSlotDate(String iDate) {
        SimpleDateFormat aFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            aFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = aFormat.parse(iDate);
            SimpleDateFormat postFormatter = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            postFormatter.setTimeZone(TimeZone.getDefault());
            return postFormatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * Formats date in the format "dd MMM yyyy"
     *
     * @param iTimeMilliSeconds time in milliseconds
     * @return String
     */
    public static String formatSendServer(long iTimeMilliSeconds) {
        SimpleDateFormat aFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        Date aConvertedDate = new Date(iTimeMilliSeconds);
        return aFormatter.format(aConvertedDate);
    }

    /**
     * Formats date in the format "dd.MM.yyyy"
     *
     * @param iTime time in milliseconds
     * @return String
     */
    public static String format(long iTime) {
        SimpleDateFormat aFormatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm a", Locale.getDefault());
        Date aDate = new Date(iTime);
        return aFormatter.format(aDate);
    }

    /**
     * Formats date in the format "MMMM yyyy"
     *
     * @param iTime time in milliseconds
     * @return String
     */
    public static String formatNavigationMonth(long iTime) {
        SimpleDateFormat aFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        Date aDate = new Date(iTime);
        return aFormatter.format(aDate);
    }

    /**
     * Formats date in the format "EE, dd MMM"
     *
     * @param iTime time in milliseconds
     * @return String
     */
    public static String formatListDate(long iTime) {
        SimpleDateFormat aFormatter = new SimpleDateFormat("EE, dd MMM", Locale.getDefault());
        Date aDate = new Date(iTime);
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        return aFormatter.format(aDate);
    }
}
