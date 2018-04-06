package gv_fiqst.teamvoytestsunrise.ui.fragments.place;

import gv_fiqst.teamvoytestsunrise.model.pojo.SavedPlace;


public interface PlaceFragmentPresenterContract {
    void loadFor(SavedPlace place);
    void release();
}
