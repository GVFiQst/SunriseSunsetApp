package gv_fiqst.teamvoytestsunrise.util;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Handler;
import android.view.View;

public class ViewFade {
    public static ViewFade with(View view) {
        return new ViewFade(view);
    }

    private View mView;
    private int mDuration;

    public ViewFade(View view) {
        mView = view;
        mDuration = 150;
    }

    public ViewFade setDuration(int duration) {
        mDuration = duration;
        return this;
    }

    public void commit(final int visibility) {
        if (mView == null) {
            return;
        }

        final boolean visible = visibility == View.VISIBLE;
        boolean viewVisible = mView.getVisibility() == View.VISIBLE;
        if (viewVisible != visible) {
            if (visible) {
                mView.setVisibility(View.VISIBLE);
                mView.setAlpha(0);
                mView.invalidate();
            }

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mView.animate()
                            .alpha(visible ? 1 : 0)
                            .setDuration(mDuration)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);

                                    mView.setVisibility(visibility);
                                    mView.setAlpha(1);
                                    release();
                                }
                            }).start();
                }
            });
        } else {
            release();
        }
    }

    private void release() {
        mView = null;
    }
}
