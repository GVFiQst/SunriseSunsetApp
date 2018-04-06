package gv_fiqst.teamvoytestsunrise.ui.fragments.search;


import gv_fiqst.teamvoytestsunrise.model.pojo.SavedPlace;

public interface SearchFragmentPresenterContract {
    void start();
    void stop();
    void release();
    void delete(SavedPlace place);
}
