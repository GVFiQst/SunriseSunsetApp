package gv_fiqst.teamvoytestsunrise.util;


import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.Date;

public class Util {
    private Util() {
    }

    public static int dpToPx(DisplayMetrics dm, int dp) {
        return (int) (dm.density * dp);
    }

    public static int dpToPx(Resources res, int dp) {
        return dpToPx(res.getDisplayMetrics(), dp);
    }

    public static int dpToPx(Context context, int dp) {
        return dpToPx(context.getResources(), dp);
    }

    @NonNull
    public static ColorStateList tint(int color) {
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_pressed}  // pressed
        };

        int[] colors = new int[] {
                color, color, color, color
        };

        return new ColorStateList(states, colors);
    }

    public static float dist(LatLng c1, LatLng c2) {
        float[] results = new float[1];
        Location.distanceBetween(
                c1.latitude, c1.longitude, c2.latitude, c2.longitude, results
        );

        return results[0];
    }

    public static boolean datesAreOnSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * This method doesn't 100% guarantees an internet connection.
     * In case of mobile data, there could be occasions where the method
     * returns <code>true</code> but actual internet connection is not available
     */
    public static boolean hasInternet(Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public static boolean hasInternet() {
        try {
            return Runtime.getRuntime()
                    .exec("/system/bin/ping -c 1 8.8.8.8")
                    .waitFor() == 0;
        } catch (Exception e) {
            Log.e("lox", "Util.hasInternet(): ", e);
            return false;
        }
    }

    public static Date tomorrowDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_YEAR, 1);
        return c.getTime();
    }
}
