package gv_fiqst.teamvoytestsunrise.ui.activities.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gv_fiqst.teamvoytestsunrise.R;
import gv_fiqst.teamvoytestsunrise.ui.base.BaseActivity;
import gv_fiqst.teamvoytestsunrise.ui.base.BaseFragment;
import gv_fiqst.teamvoytestsunrise.ui.fragments.main.MainFragment;
import gv_fiqst.teamvoytestsunrise.ui.fragments.main.MainFragmentState;
import gv_fiqst.teamvoytestsunrise.ui.fragments.search.SearchFragment;
import gv_fiqst.teamvoytestsunrise.util.ViewFade;


public class MainActivity extends BaseActivity {
    private static final String KEY_STATE = "gv_fiqst.teamvoytestsunrise.ui.activities.main.MainActivity.KEY_STATE";

    public static Intent getIntent(Context context, MainInitData data) {
        return new Intent(context, MainActivity.class)
                .putExtra(KEY_STATE, data);
    }

    @BindView(R.id.fabSearch)
    View mFabSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUnbinder(ButterKnife.bind(this));

        setDefaultFragmentAnimIn(R.anim.slide_in_right, R.anim.slide_out_left);
        setDefaultFragmentAnimOut(R.anim.slide_in_left, R.anim.slide_out_right);

        MainInitData data = parseIntent(getIntent());
        openFragment(R.id.root, MainFragment.create(
                new MainFragmentState(
                        data.hasInternet, data.hasPermissions, data.gpsEnabled,
                        false, true, data.mInfo, data.mDataToday, data.mDataTomorrow
                )
        ), MainFragment.TAG_MAIN_FRAGMENT);
    }

    @Override
    protected void onStart() {
        super.onStart();

        registerReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onNewFragment(BaseFragment fragment) {
        super.onNewFragment(fragment);

        ViewFade.with(mFabSearch)
                .setDuration(300)
                .commit((fragment instanceof MainFragment) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (!tryCloseFragment(R.id.root)) {

            // Another workaround for bugs
            // caused by shared element transitions
            finish();
        }
    }

    @OnClick(R.id.fabSearch)
    public void openSearch() {
        openFragment(R.id.root, SearchFragment.class, SearchFragment.TAG_SEARCH_FRAGMENT);
    }

    private MainInitData parseIntent(Intent intent) {
        MainInitData data = null;
        if (intent.hasExtra(KEY_STATE)) {
            data = intent.getParcelableExtra(KEY_STATE);
        }

        if (data == null) {
            throw new IllegalStateException("To start this activity use intent " +
                    "from method MainActivity.getIntent(Context, MainActivityState)");
        }

        return data;
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        registerReceiver(mMainReceiver, intentFilter);
    }

    private void unregisterReceiver() {
        unregisterReceiver(mMainReceiver);
    }

    private final BroadcastReceiver mMainReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                return;
            }

            MainFragment.Event event = null;
            switch (intent.getAction()) {
                case ConnectivityManager.CONNECTIVITY_ACTION:
                    event = MainFragment.Event
                            .EVENT_INTERNET_CHANGED;
                    break;

                case LocationManager.PROVIDERS_CHANGED_ACTION:
                    event = MainFragment.Event
                            .EVENT_GPS_CHANGED;
                    break;
            }

            if (event != null) {
                EventBus.getDefault().post(event);
            }
        }
    };
}
