package gv_fiqst.teamvoytestsunrise.ui.activities.launcher;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import gv_fiqst.teamvoytestsunrise.R;
import gv_fiqst.teamvoytestsunrise.model.SavedCacheManager;
import gv_fiqst.teamvoytestsunrise.model.pojo.SavedData;
import gv_fiqst.teamvoytestsunrise.model.pojo.SunData;
import gv_fiqst.teamvoytestsunrise.ui.activities.main.MainActivity;
import gv_fiqst.teamvoytestsunrise.ui.activities.main.MainInitData;
import gv_fiqst.teamvoytestsunrise.util.Util;

class LauncherActivityPresenter implements LauncherActivityPresenterContract {
    private static final int RC_LOC_PERMS = 0x1;
    private static final int RC_GPS_ENABLE = 0x2;

    private LauncherActivityContract mActivity;
    private MainInitData mData;
    private Step mStep;
    private Handler mHandler;

    public LauncherActivityPresenter(LauncherActivityContract activity) {
        mHandler = new Handler();
        mActivity = activity;
        mData = new MainInitData();
        mStep = new Step1();

        // Wait 1 second for user to see animations
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                next();
            }
        }, 1000);
    }

    @Override
    public void next() {
        Step last = mStep;
        mStep = mStep.next(this, mData, mActivity);

        if (mStep == null) {
            mActivity.endSetup(MainActivity.getIntent(mActivity.getContext(), mData));
        } else if (last != mStep) {
            next();
        }
    }

    @Override
    public void release() {
        mActivity = null;
        mData = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_GPS_ENABLE) {
            final boolean gpsEnabled = ((LocationManager) mActivity.getContext().getSystemService(Context.LOCATION_SERVICE))
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // avoid frames skipping. Give opportunity to View to redraw itself
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mData.gpsEnabled = gpsEnabled;
                    next();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_LOC_PERMS) {
            final boolean result = mActivity.checkGrantResults(grantResults);

            // avoid frames skipping. Give opportunity to View to redraw itself
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mData.hasPermissions = result;
                    next();
                }
            });
        }
    }

    private static abstract class Step {
        private boolean pass = false;

        public Step next(LauncherActivityPresenterContract init, MainInitData state, LauncherActivityContract activity){
            if (pass) {
                return getNext();
            }

            pass = true;
            return nextImpl(init, state, activity);
        }

        protected abstract Step getNext();
        protected abstract Step nextImpl(LauncherActivityPresenterContract init, MainInitData state, LauncherActivityContract activity);
    }

    private static class Step1 extends Step {

        @Override
        protected Step getNext() {
            return new Step2();
        }

        @Override
        protected Step nextImpl(LauncherActivityPresenterContract init, MainInitData state, LauncherActivityContract activity) {
            if (!activity.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    || !activity.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                activity.requestPermissionsSafely(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, RC_LOC_PERMS);

                state.hasPermissions = false;
                return this;
            }

            state.hasPermissions = true;
            return getNext();
        }
    }

    private static class Step2 extends Step {

        @Override
        protected Step getNext() {
            return new Step3();
        }

        @Override
        protected Step nextImpl(final LauncherActivityPresenterContract init, final MainInitData state, final LauncherActivityContract activity) {
            boolean gpsEnabled = ((LocationManager) activity.getContext().getSystemService(Context.LOCATION_SERVICE))
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!gpsEnabled) {
                new AlertDialog.Builder(activity.getContext())
                        .setTitle(R.string.gps_disabled)
                        .setMessage(R.string.no_gps_msg)
                        .setCancelable(false)
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                state.gpsEnabled = false;
                                init.next();
                            }
                        })
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), RC_GPS_ENABLE);
                            }
                        })
                        .show();

                return this;
            }

            state.gpsEnabled = true;
            return getNext();
        }
    }

    private static class Step3 extends Step {

        @Override
        protected Step getNext() {
            return null;
        }

        @Override
        protected Step nextImpl(LauncherActivityPresenterContract init, MainInitData state, LauncherActivityContract activity) {
            SavedData data = SavedCacheManager.get().getSavedData();

            if (data != null) {
                if (SunData.isActual(data.today)) {
                    state.mDataToday = data.today;
                    state.mDataTomorrow = data.tomorrow;
                } else if (SunData.isActual(data.tomorrow)) {
                    state.mDataToday = data.tomorrow;
                }

                state.mInfo = data.info;
            }

            state.hasInternet = Util.hasInternet();
            return getNext();
        }
    }
}
