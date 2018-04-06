package gv_fiqst.teamvoytestsunrise.util.transition;

import android.annotation.SuppressLint;
import android.support.v4.app.SupportActivity;

class SharedElementEnterOnlyTransitionHelper extends TransitionHelper {
    private SupportActivity mActivity;

    @SuppressLint("RestrictedApi")
    protected SharedElementEnterOnlyTransitionHelper(SupportActivity activity) {
        super(activity.getLifecycle());
        mActivity = activity;
    }

    @Override
    public void onStop() {
        super.onStop();

        mActivity.finish();
        release();
    }

    @Override
    protected void release() {
        super.release();

        mActivity = null;
    }
}
