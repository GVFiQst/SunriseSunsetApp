package gv_fiqst.teamvoytestsunrise.model;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;

import gv_fiqst.teamvoytestsunrise.model.pojo.SunData;

public interface SunriseManager {
    void fetchData(float lat, float lng, Date date, final FetchListener listener);
    void stopCalls();

    class Default {
        public static SunriseManager create() {
            return new SunriseDelegate(SunriseManagerImpl.sInstance);
        }
    }

    interface FetchListener {
        void onResult(@NonNull SunData data, @Nullable Throwable t);
    }
}
