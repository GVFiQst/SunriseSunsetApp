package gv_fiqst.teamvoytestsunrise.ui.fragments.place;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import gv_fiqst.teamvoytestsunrise.R;
import gv_fiqst.teamvoytestsunrise.model.pojo.SavedPlace;
import gv_fiqst.teamvoytestsunrise.ui.base.BaseFragment;
import gv_fiqst.teamvoytestsunrise.ui.fragments.sundata.SunDataFragment;
import gv_fiqst.teamvoytestsunrise.ui.fragments.sundata.SunDataState;

public class PlaceFragment extends SunDataFragment implements PlaceFragmentContract {
    public static final String TAG_PLACE_FRAGMENT = "fragment.PLACE";

    private static final String KEY_SAVED_PLACE = "gv_fiqst.teamvoytestsunrise.ui.fragments.place.PlaceFragment.KEY_SAVED_PLACE";
    private static final String KEY_SUN_DATA_BUNDLE = "gv_fiqst.teamvoytestsunrise.ui.fragments.place.PlaceFragment.KEY_SUN_DATA_BUNDLE";

    public static PlaceFragment create(SavedPlace place) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_SAVED_PLACE, place);

        PlaceFragment fg = new PlaceFragment();
        fg.setArguments(args);
        return fg;
    }

    private SavedPlace mPlace;
    private SunDataBundle mSunData;
    private PlaceFragmentPresenterContract mPresenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_SAVED_PLACE)) {
            update(
                    (SavedPlace) savedInstanceState.getParcelable(KEY_SAVED_PLACE),
                    (SunDataBundle) savedInstanceState.getParcelable(KEY_SUN_DATA_BUNDLE)
            );
        } else if (getArguments() != null && getArguments().containsKey(KEY_SAVED_PLACE)) {
            update((SavedPlace) getArguments().getParcelable(KEY_SAVED_PLACE), null);
        } else {
            throw new IllegalArgumentException("No saved place arguments");
        }

        mPresenter = new PlaceFragmentPresenter(this);
        mPresenter.loadFor(mPlace);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mPlace != null) outState.putParcelable(KEY_SAVED_PLACE, mPlace);
        if (mSunData != null) outState.putParcelable(KEY_SUN_DATA_BUNDLE, mSunData);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mPresenter != null) {
            mPresenter.release();
            mPresenter = null;
        }
    }

    @Override
    protected BaseFragment onRecreate() {
        return PlaceFragment.create(mPlace);
    }

    @Override
    public void update(SavedPlace savedPlace, SunDataBundle sunDataBundle) {
        mPlace = savedPlace;
        mSunData = sunDataBundle;

        update(new SunDataState(
                getString(R.string.location),
                null, null,
                mPlace.name,
                sunDataBundle != null
                        ? sunDataBundle.today
                        : null,
                sunDataBundle != null
                        ? sunDataBundle.tomorrow
                        : null
        ));
    }
}
