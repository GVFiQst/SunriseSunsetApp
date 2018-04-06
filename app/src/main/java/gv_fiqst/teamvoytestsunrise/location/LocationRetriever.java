package gv_fiqst.teamvoytestsunrise.location;


import android.content.Context;

import gv_fiqst.teamvoytestsunrise.location.pojo.LocationInfo;

public interface LocationRetriever {
    void addLocationListener(Listener l);
    void removeLocationListener(Listener l);
    void start();
    void stop();
    boolean serviceIsAvailable();

    interface Listener {
        void onLocationUpdate(LocationInfo info);
    }

    class Impl {
        public static LocationRetriever get() {
            return LocationRetrieverImpl.get();
        }

        private Impl() {
        }
    }

    class Init {
        public static void init(Context context) {
            LocationRetrieverImpl.init(context);
        }

        private Init() {
        }
    }
}
