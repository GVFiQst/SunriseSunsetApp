package gv_fiqst.teamvoytestsunrise.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;



public interface IBaseActivity {
    Context getContext();
    void requestPermissionsSafely(String[] permissions, int requestCode);
    boolean hasPermission(String permission);

    void openFragment(@IdRes int rootView, BaseFragment fragment, String tag);
    void openFragment(@IdRes int rootView, BaseFragment fragment, String tag, int animEnter, int animEnd);
    void openFragment(@IdRes int rootView, Class<? extends BaseFragment> clazz, Bundle args, String tag);
    void openFragment(@IdRes int rootView, Class<? extends BaseFragment> clazz, String tag, int animEnter, int animEnd);
    void openFragment(@IdRes int rootView, Class<? extends BaseFragment> clazz, String tag);
    void openFragment(@IdRes int rootView, Class<? extends BaseFragment> clazz, Bundle args, String tag, int animEnter, int animEnd);
}
