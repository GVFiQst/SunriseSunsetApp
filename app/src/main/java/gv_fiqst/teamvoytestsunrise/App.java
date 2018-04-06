package gv_fiqst.teamvoytestsunrise;

import android.app.Application;

import gv_fiqst.teamvoytestsunrise.location.LocationRetriever;
import gv_fiqst.teamvoytestsunrise.model.SavedCacheManager;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LocationRetriever.Init.init(this);
        SavedCacheManager.init(this);
    }
}
