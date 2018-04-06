package gv_fiqst.teamvoytestsunrise.ui.activities.launcher;


import android.content.Intent;
import android.support.annotation.NonNull;

public interface LauncherActivityPresenterContract {
    void next();
    void release();
    void onActivityResult(int requestCode, int resultCode, Intent data);
    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
