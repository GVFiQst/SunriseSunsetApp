package gv_fiqst.teamvoytestsunrise.ui.fragments.search;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gv_fiqst.teamvoytestsunrise.R;
import gv_fiqst.teamvoytestsunrise.model.SavedCacheManager;
import gv_fiqst.teamvoytestsunrise.model.pojo.SavedPlace;
import gv_fiqst.teamvoytestsunrise.ui.base.BaseFragment;
import gv_fiqst.teamvoytestsunrise.ui.fragments.place.PlaceFragment;
import gv_fiqst.teamvoytestsunrise.util.feedview.FeedViewHelper;

public class SearchFragment extends BaseFragment implements SearchFragmentContract, FeedViewHelper.Callback, SearchAdapter.Listener {
    private static final int RC_AUTOCOMPLETE = 0x27;
    public static final String TAG_SEARCH_FRAGMENT = "fragment.SEARCH";

    @BindView(R.id.btnSearch)
    TextView mSearch;

    private FeedViewHelper mListHelper;
    private RecyclerView mRecyclerView;

    private boolean isSearchShown;

    private SearchFragmentPresenterContract mPresenter;
    private SearchAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        mListHelper = FeedViewHelper.forStandaloneView(
                (ViewGroup) view.findViewById(R.id.list), this
        ).setContentView(mRecyclerView = new RecyclerView(getContext()));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUnbinder(ButterKnife.bind(this, view));

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            int tintColor = ContextCompat.getColor(getContext(), R.color.colorNormalUI);

            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_search);
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable.mutate(), tintColor);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

            mSearch.setCompoundDrawables(drawable, null, null, null);
        }

        mAdapter = new SearchAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        update(null);

        mPresenter = new SearchFragmentPresenter(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        mPresenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();

        mPresenter.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.release();
            mPresenter = null;
        }

        if (mAdapter != null) {
            mAdapter.release();
            mAdapter = null;
        }
    }

    @Override
    protected BaseFragment onRecreate() {
        return new SearchFragment();
    }

    @OnClick(R.id.btnSearch)
    public void searchClick() {
        if (isSearchShown) {
            return;
        }

        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .setFilter(new AutocompleteFilter.Builder()
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                            .build())
                    .build(getActivity());

            startActivityForResult(intent, RC_AUTOCOMPLETE);
            isSearchShown = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorButtonClick(int errorCode) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_AUTOCOMPLETE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getContext(), data);
                SavedPlace savedPlace = new SavedPlace(place.getName().toString(), place.getLatLng());
                SavedCacheManager.get().savePlace(savedPlace);
                openFragment(PlaceFragment.create(savedPlace), PlaceFragment.TAG_PLACE_FRAGMENT);
            }

            isSearchShown = false;
        }
    }

    @Override
    public void update(List<SavedPlace> list) {
        mAdapter.update(list);

        if (list == null || list.isEmpty()) {
            mListHelper.showErrorMessage(getString(R.string.empty), "", -1);
        } else {
            mListHelper.showContent();
        }
    }

    @Override
    public void onSavedPlace(SavedPlace place, int action) {
        switch (action) {
            case SearchAdapter.ACTION_SELECT:
                openFragment(PlaceFragment.create(place), PlaceFragment.TAG_PLACE_FRAGMENT);
                break;

            case SearchAdapter.ACTION_DELETE:
                mPresenter.delete(place);
                break;
        }
    }
}
