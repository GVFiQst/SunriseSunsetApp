package gv_fiqst.teamvoytestsunrise.util.transition;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.SupportActivity;
import android.transition.Transition;
import android.view.View;
import android.view.Window;

/**
 * Created by GV_FiQst on 01.04.2018.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class DisableExitTransitionHelper extends TransitionHelper {
    private TransitionListener mListener;

    @SuppressLint("RestrictedApi")
    public DisableExitTransitionHelper(SupportActivity activity, View sharedElement) {
        super(activity.getLifecycle());
        Window window = activity.getWindow();

        mListener = new TransitionListener(window, sharedElement);
        window.getSharedElementEnterTransition()
                .addListener(mListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mListener.getWindow()
                .getSharedElementEnterTransition()
                .removeListener(mListener);

        release();
    }

    @Override
    protected void release() {
        super.release();

        mListener.release();
        mListener = null;
    }

    private static class TransitionListener extends TransitionListenerAdapter {
        private Window mWindow;
        private View mSharedElement;

        public TransitionListener(Window window, View sharedElement) {
            mWindow = window;
            mSharedElement = sharedElement;
        }

        @Override
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public void onTransitionEnd(@NonNull Transition transition) {
            mWindow.setSharedElementReturnTransition(null);
            mWindow.setSharedElementReenterTransition(null);
            mSharedElement.setTransitionName(null);
        }

        public Window getWindow() {
            return mWindow;
        }

        public void release() {
            mWindow = null;
            mSharedElement = null;
        }
    }
}
