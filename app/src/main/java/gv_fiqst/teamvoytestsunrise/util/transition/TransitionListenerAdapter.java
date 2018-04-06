package gv_fiqst.teamvoytestsunrise.util.transition;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.transition.Transition;

/**
 * Created by GV_FiQst on 01.04.2018.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public abstract class TransitionListenerAdapter implements Transition.TransitionListener {
    @Override public void onTransitionStart(@NonNull Transition transition) {}
    @Override public void onTransitionEnd(@NonNull Transition transition) {}
    @Override public void onTransitionCancel(@NonNull Transition transition) {}
    @Override public void onTransitionPause(@NonNull Transition transition) {}
    @Override public void onTransitionResume(@NonNull Transition transition) {}
}
