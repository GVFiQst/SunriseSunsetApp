package gv_fiqst.teamvoytestsunrise.ui.fragments.main;

import gv_fiqst.teamvoytestsunrise.location.pojo.LocationInfo;


public interface MainFragmentPresenterContract {
    void start();
    void stop();
    void release();
    void requestUpdate(LocationInfo info);
}
