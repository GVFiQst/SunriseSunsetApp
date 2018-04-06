package gv_fiqst.teamvoytestsunrise.util.transition;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.SupportActivity;
import android.util.Pair;
import android.view.View;


/**
 * Workaround class for transitions between activities
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class TransitionHelper implements LifecycleObserver {

    public static Bundle seEnterOnlyTransition(SupportActivity activity, View sharedElement, String name) {
        return seEnterOnlyTransition(activity, Pair.create(sharedElement, name));
    }

    @SafeVarargs
    @SuppressLint("RestrictedApi")
    public static Bundle seEnterOnlyTransition(SupportActivity activity, Pair<View, String>... elements) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, elements);
        activity.getLifecycle().addObserver(new SharedElementEnterOnlyTransitionHelper(activity));

        return options.toBundle();
    }

    @SuppressLint("RestrictedApi")
    public static void disableExitTransition(SupportActivity activity, View sharedElement) {
        activity.getLifecycle().addObserver(new DisableExitTransitionHelper(
                activity, sharedElement
        ));
    }

    private Lifecycle mLifecycle;

    protected TransitionHelper(Lifecycle lifecycle) {
        mLifecycle = lifecycle;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
    }

    protected void release() {
        mLifecycle.removeObserver(this);
        mLifecycle = null;
    }
}
