package gv_fiqst.teamvoytestsunrise.ui.fragments.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import gv_fiqst.teamvoytestsunrise.R;
import gv_fiqst.teamvoytestsunrise.ui.base.BaseFragment;
import gv_fiqst.teamvoytestsunrise.ui.fragments.sundata.SunDataFragment;
import gv_fiqst.teamvoytestsunrise.ui.fragments.sundata.SunDataState;
import gv_fiqst.teamvoytestsunrise.util.Util;


public class MainFragment extends SunDataFragment implements MainFragmentContract {
    public static final String TAG_MAIN_FRAGMENT = "fragment.MAIN";

    private static final int RC_LOC_PERMS = 0x25;
    private static final int RC_GPS_ENABLE = 0x26;
    private static final String KEY_STATE = "gv_fiqst.teamvoytestsunrise.ui.fragments.main.MainFragment.KEY_STATE";

    public static MainFragment create(MainFragmentState state) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_STATE, state);

        MainFragment fragment = new MainFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private MainFragmentPresenterContract mPresenter;
    private LocationManager mLocationManager;
    private MainFragmentState mState;
    private Handler mHandler;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLocationManager = ((LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE));

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_STATE)) {
            update((MainFragmentState) savedInstanceState.getParcelable(KEY_STATE));
        } else if (getArguments() != null && getArguments().containsKey(KEY_STATE)) {
            update((MainFragmentState) getArguments().getParcelable(KEY_STATE));
        } else {
            throw new IllegalArgumentException("No state arguments");
        }

        mHandler = new Handler();
        mPresenter = new MainFragmentPresenter(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mPresenter != null) {
            mPresenter.start();
        }
        updateProviders();

        EventBus.getDefault().register(this);
    }

    private void updateProviders() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mState.gpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                mState.hasInternet = Util.hasInternet();

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.removeCallbacks(this);
                        update(mState);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mPresenter != null) {
            mPresenter.stop();
        }

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.release();
            mPresenter = null;
        }

        mHandler = null;
    }

    @Override
    protected BaseFragment onRecreate() {
        return MainFragment.create(mState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(KEY_STATE, mState);
    }


    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(Event event) {
        // For escaping posting thread
        final Handler handler = new Handler(Looper.getMainLooper());

        switch (event) {
            case EVENT_INTERNET_CHANGED:
                final boolean hasInternet = Util.hasInternet();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mState.hasInternet = hasInternet;
                        update(mState);
                    }
                });
                break;
            case EVENT_GPS_CHANGED:
                final boolean hasGps = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mState.gpsEnabled = hasGps;
                        update(mState);
                    }
                });
                break;
        }
    }

    @Override
    public void update(MainFragmentState state) {
        mState = state;
        String city = null;
        CharacterStyle span = null;
        String spanText = null;
        boolean noCity = mState.mInfo == null;

        if (!noCity) {
            city = mState.mInfo.mCityName;
        }

        if (!mState.isServicePresent) {
            span = new StyleSpan(Typeface.ITALIC);
            spanText = getString(R.string.no_google);
        } else if (!mState.hasInternet) {
            span = new StyleSpan(Typeface.ITALIC);
            spanText = getString(R.string.no_internet);
        } else if (!mState.hasPermissions) {
            span = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    requestPermissions();
                }
            };
            spanText = getString(R.string.no_permissions);
        } else if (!mState.gpsEnabled) {
            span = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    enableGps();
                }
            };
            spanText = getString(R.string.gps_disabled);
        }

        if (noCity && span != null) {
            city = getString(R.string.unknown);
        }

        update(new SunDataState()
                .setCityName(city)
                .setTitle(getString(R.string.your_current_location))
                .setErrorSpan(span)
                .setErrorString(spanText)
                .setToday(state.mDataToday)
                .setTomorrow(state.mDataTomorrow));
    }

    private void enableGps() {
        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), RC_GPS_ENABLE);
    }

    private void requestPermissions() {
        getBaseActivity().requestPermissionsSafely(new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        }, RC_LOC_PERMS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RC_LOC_PERMS) {
            mState.hasPermissions = checkGrantResults(grantResults);

            update(mState);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GPS_ENABLE) {
            mState.gpsEnabled = mLocationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            update(mState);
        }
    }

    @Override
    public MainFragmentState getCurrentState() {
        return MainFragmentState.copy(mState);
    }

    public enum Event {
        EVENT_INTERNET_CHANGED,
        EVENT_GPS_CHANGED
    }
}
