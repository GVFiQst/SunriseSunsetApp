package gv_fiqst.teamvoytestsunrise.ui.fragments.place;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;

import gv_fiqst.teamvoytestsunrise.model.SunriseManager;
import gv_fiqst.teamvoytestsunrise.model.pojo.SavedPlace;
import gv_fiqst.teamvoytestsunrise.model.pojo.SunData;
import gv_fiqst.teamvoytestsunrise.util.Util;

class PlaceFragmentPresenter implements PlaceFragmentPresenterContract {
    private PlaceFragmentContract mFragment;
    private SunriseManager mSunriseManager;

    public PlaceFragmentPresenter(PlaceFragmentContract fragment) {
        mFragment = fragment;
        mSunriseManager = SunriseManager.Default.create();
    }

    @Override
    public void loadFor(final SavedPlace place) {
        final SunDataBundle bundle = new SunDataBundle();

        mSunriseManager.fetchData(
                (float) place.location.latitude,
                (float) place.location.longitude,
                new Date(),
                new SunriseManager.FetchListener() {
                    @Override
                    public void onResult(@NonNull SunData data, @Nullable Throwable t) {
                        bundle.today = data;

                        // Just in case :)
                        if (mFragment != null) {
                            mFragment.update(place, bundle);
                        }
                    }
                });

        mSunriseManager.fetchData(
                (float) place.location.latitude,
                (float) place.location.longitude,
                Util.tomorrowDate(),
                new SunriseManager.FetchListener() {
                    @Override
                    public void onResult(@NonNull SunData data, @Nullable Throwable t) {
                        bundle.tomorrow = data;

                        // Just in case :)
                        if (mFragment != null) {
                            mFragment.update(place, bundle);
                        }
                    }
                });
    }

    @Override
    public void release() {
        mFragment = null;

        if (mSunriseManager != null) {
            mSunriseManager.stopCalls();
            mSunriseManager = null;
        }
    }
}
