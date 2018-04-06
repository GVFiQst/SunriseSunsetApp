package gv_fiqst.teamvoytestsunrise.model;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import gv_fiqst.teamvoytestsunrise.location.pojo.LocationInfo;
import gv_fiqst.teamvoytestsunrise.model.pojo.SavedData;
import gv_fiqst.teamvoytestsunrise.model.pojo.SavedPlace;
import gv_fiqst.teamvoytestsunrise.model.pojo.SunData;
import gv_fiqst.teamvoytestsunrise.util.SharedPrefUtil;
import gv_fiqst.teamvoytestsunrise.util.Util;


public class SavedCacheManager {
    private static final String KEY_SAVED_CACHE = "gv_fiqst.teamvoytestsunrise.model.SavedCacheManager.KEY_SAVED_CACHE";
    private static final String KEY_SAVED_PLACES = "gv_fiqst.teamvoytestsunrise.model.SavedCacheManager.KEY_SAVED_PLACES";

    private static SavedCacheManager ourInstance;

    public static void init(Context context) {
        if (ourInstance != null) {
            throw new IllegalStateException("LastKnownLocationManager is already initialized");
        }

        ourInstance = new SavedCacheManager(context);
    }

    public static SavedCacheManager get() {
        return ourInstance;
    }

    private SharedPrefUtil mPrefUtil;
    private SavedData mLastKnownLocation;
    private List<SavedPlace> mSavedPlaces;
    private Gson mGson;

    private final List<Listener> mListeners = new ArrayList<>();

    private SavedCacheManager(Context context) {
        mPrefUtil = new SharedPrefUtil(context);
        mGson = new Gson();

        update();
    }

    private void update() {
        mLastKnownLocation = mGson.fromJson(
                mPrefUtil.getString(KEY_SAVED_CACHE, "null"),
                SavedData.class
        );

        mSavedPlaces = mGson.fromJson(
                mPrefUtil.getString(KEY_SAVED_PLACES, "[]"),
                new TypeToken<List<SavedPlace>>(){}.getType()
        );
    }

    public SavedData getSavedData() {
        return mLastKnownLocation;
    }

    public void saveLastKnownLocation(LocationInfo info, SunData today, SunData tomorrow) {
        SavedData data = new SavedData(info, today, tomorrow);

        mPrefUtil.saveString(KEY_SAVED_CACHE, mGson.toJson(mLastKnownLocation = data))
                .apply();

        fireListeners();
    }

    public void savePlace(SavedPlace place) {
        mSavedPlaces.add(0, place);
        savePlaces();
    }

    private void savePlaces() {
        mPrefUtil.saveString(KEY_SAVED_PLACES, mGson.toJson(mSavedPlaces))
                .apply();

        fireListeners();
    }

    public void deletePlace(SavedPlace place) {
        for (int i = 0; i < mSavedPlaces.size(); i++) {
            if (mSavedPlaces.get(i).name.equals(place.name)
                    || Util.dist(mSavedPlaces.get(i).location, place.location) < 500) {
                mSavedPlaces.remove(i);
            }
        }

        savePlaces();
    }


    public List<SavedPlace> getSavedPlaces() {
        return new ArrayList<>(mSavedPlaces);
    }

    public void addListener(Listener listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
            listener.onChangedCache();
        }
    }

    public void removeListener(Listener listener) {
        mListeners.remove(listener);
    }

    private void fireListeners() {
        for (Listener l : new ArrayList<>(mListeners)) {
            l.onChangedCache();
        }
    }

    public interface Listener {
        void onChangedCache();
    }
}
