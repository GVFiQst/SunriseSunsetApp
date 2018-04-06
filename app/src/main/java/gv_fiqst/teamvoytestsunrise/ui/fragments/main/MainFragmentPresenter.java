package gv_fiqst.teamvoytestsunrise.ui.fragments.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;

import gv_fiqst.teamvoytestsunrise.location.LocationRetriever;
import gv_fiqst.teamvoytestsunrise.location.pojo.LocationInfo;
import gv_fiqst.teamvoytestsunrise.model.SavedCacheManager;
import gv_fiqst.teamvoytestsunrise.model.SunriseManager;
import gv_fiqst.teamvoytestsunrise.model.pojo.SunData;
import gv_fiqst.teamvoytestsunrise.util.Util;

/**
 * Created by GV_FiQst on 04.04.2018.
 */

class MainFragmentPresenter implements MainFragmentPresenterContract, LocationRetriever.Listener {
    private MainFragmentContract mFragment;
    private boolean isServicePresent;
    private boolean isReleased = false;

    private SunriseManager mSunriseManager;

    public MainFragmentPresenter(MainFragmentContract fragment) {
        mFragment = fragment;

        MainFragmentState state = mFragment.getCurrentState();
        state.isServicePresent = isServicePresent = LocationRetriever.Impl.get()
                .serviceIsAvailable();

        if (isServicePresent) {
            LocationRetriever.Impl.get()
                    .addLocationListener(this);
        } else {
            state.mInfo = null;
            state.mDataToday = null;
            state.mDataTomorrow = null;
        }

        mSunriseManager = SunriseManager.Default.create();
        update(state);
    }

    @Override
    public void start() {
        if (isServicePresent) {
            LocationRetriever.Impl.get()
                    .start();
        }
    }

    @Override
    public void stop() {
        if (isServicePresent) {
            LocationRetriever.Impl.get()
                    .stop();
        }
    }

    @Override
    public void release() {
        isReleased = true;

        if (isServicePresent) {
            LocationRetriever.Impl.get()
                    .removeLocationListener(this);
        }

        if (mSunriseManager != null) {
            mSunriseManager.stopCalls();
            mSunriseManager = null;
        }

        mFragment = null;
    }

    @Override
    public void requestUpdate(LocationInfo info) {
        if (isReleased) {
            return;
        }

        if (isServicePresent) {
            onLocationUpdate(info);
        }
    }

    @Override
    public void onLocationUpdate(LocationInfo info) {
        if (isReleased) {
            return;
        }

        MainFragmentState state = mFragment.getCurrentState();

        if (state.mInfo == null || state.mDataToday == null || state.mDataTomorrow == null
                || Util.dist(state.mInfo.mCoords, info.mCoords) > 10000 // 10km
                || !state.mInfo.mCityName.equals(info.mCityName)) {
            state.mInfo = info;

            if (!SunData.isActual(state.mDataToday)) {
                state.mDataToday = null;
            }

            if (!SunData.isActual(state.mDataTomorrow)) {
                state.mDataTomorrow = null;
            }

            update(state);
            loadData(state, info);
        }
    }

    private void update(MainFragmentState state) {
        if (isReleased) {
            return;
        }

        mFragment.update(state);

        LocationInfo info = null;
        SunData today = null;
        SunData tomorrow = null;

        if (state != null) {
            info = state.mInfo;
            today = state.mDataToday;
            tomorrow = state.mDataTomorrow;
        }

        SavedCacheManager.get()
                .saveLastKnownLocation(info, today, tomorrow);
    }

    private void loadData(final MainFragmentState state, LocationInfo info) {
        if (isReleased) {
            return;
        }

        if (state.mDataToday == null) {
            mSunriseManager.fetchData(
                    (float) info.mCoords.latitude,
                    (float) info.mCoords.longitude,
                    new Date(),
                    new SunriseManager.FetchListener() {
                        @Override
                        public void onResult(@NonNull SunData data, @Nullable Throwable t) {
                            state.mDataToday = data;
                            update(state);
                        }
                    });
        }

        if (state.mDataTomorrow == null) {
            mSunriseManager.fetchData(
                    (float) info.mCoords.latitude,
                    (float) info.mCoords.longitude,
                    Util.tomorrowDate(),
                    new SunriseManager.FetchListener() {
                        @Override
                        public void onResult(@NonNull SunData data, @Nullable Throwable t) {
                            state.mDataTomorrow = data;
                            update(state);
                        }
                    });
        }
    }
}
