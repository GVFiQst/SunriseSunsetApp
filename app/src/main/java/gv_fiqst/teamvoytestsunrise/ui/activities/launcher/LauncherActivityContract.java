package gv_fiqst.teamvoytestsunrise.ui.activities.launcher;


import android.content.Context;
import android.content.Intent;

public interface LauncherActivityContract {
    Context getContext();

    void startActivityForResult(Intent intent, int requestCode);

    boolean hasPermission(String permission);

    void requestPermissionsSafely(String[] perms, int requsetCode);

    boolean checkGrantResults(int[] grantResults);

    void endSetup(Intent intent);
}
