package gv_fiqst.teamvoytestsunrise.ui.activities.launcher;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import gv_fiqst.teamvoytestsunrise.R;
import gv_fiqst.teamvoytestsunrise.ui.base.BaseActivity;
import gv_fiqst.teamvoytestsunrise.util.transition.TransitionHelper;


public class LauncherActivity extends BaseActivity implements LauncherActivityContract {
    private static final int RC_LOC_PERMS = 0x1;
    private static final int RC_GPS_ENABLE = 0x2;

    @BindView(R.id.imgLogo)
    View mTransitionView1;

    private LauncherActivityPresenterContract mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        setUnbinder(ButterKnife.bind(this));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#55000000"));
        }

        mPresenter = new LauncherActivityPresenter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.release();
            mPresenter = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    public void endSetup(Intent intent) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            // Shared element transition is a two-way transition, it mast have enter and exit.
            // In my case it should be only one-way transition because I want to finish LauncherActivity
            // when the MainActivity is opened. TransitionHelper is a workaround.
            startActivity(intent,
                    TransitionHelper.seEnterOnlyTransition(
                            this, mTransitionView1,
                            getString(R.string.launcher_transition_logo)
                    )
            );
        } else {
            startActivity(intent);
            finish();
        }
    }
}
