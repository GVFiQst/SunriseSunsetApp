package gv_fiqst.teamvoytestsunrise.ui.fragments.search;


import gv_fiqst.teamvoytestsunrise.model.SavedCacheManager;
import gv_fiqst.teamvoytestsunrise.model.pojo.SavedPlace;

class SearchFragmentPresenter implements SearchFragmentPresenterContract, SavedCacheManager.Listener {
    private SearchFragmentContract mFragment;

    public SearchFragmentPresenter(SearchFragmentContract fragment) {
        mFragment = fragment;
    }

    @Override
    public void start() {
        SavedCacheManager.get()
                .addListener(this);
    }

    @Override
    public void stop() {
        SavedCacheManager.get()
                .removeListener(this);
    }

    @Override
    public void release() {
        mFragment = null;
    }

    @Override
    public void delete(SavedPlace place) {
        SavedCacheManager.get()
                .deletePlace(place);
    }

    @Override
    public void onChangedCache() {
        mFragment.update(SavedCacheManager.get()
                .getSavedPlaces());
    }
}
