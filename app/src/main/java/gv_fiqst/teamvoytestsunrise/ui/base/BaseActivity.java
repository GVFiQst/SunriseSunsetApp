package gv_fiqst.teamvoytestsunrise.ui.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import butterknife.Unbinder;


public abstract class BaseActivity extends AppCompatActivity implements IBaseActivity {
    private Unbinder mUnbinder;
    private final Map<Integer, Stack<BaseFragment>> mOpenedFragments = new HashMap<>();
    private final Map<String, BaseFragment> mCachedFragments = new HashMap<>();

    private int[] mFragmentAnimIn = new int[]{0, 0};
    private int[] mFragmentAnimOut = new int[]{0, 0};

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mOpenedFragments.clear();
        mCachedFragments.clear();

        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkGrantResults(int[] grantResults) {
        for (int res : grantResults) {
            if (res != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    protected void setDefaultFragmentAnimIn(int in, int out) {
        mFragmentAnimIn[0] = in;
        mFragmentAnimIn[1] = out;
    }

    protected void setDefaultFragmentAnimOut(int in, int out) {
        mFragmentAnimOut[0] = in;
        mFragmentAnimOut[1] = out;
    }

    public void setUnbinder(Unbinder unbinder) {
        mUnbinder = unbinder;
    }

    public boolean tryCloseFragment(@IdRes int rootView) {
        return tryCloseFragment(rootView, mFragmentAnimOut[0], mFragmentAnimOut[1]);
    }

    public boolean tryCloseFragment(@IdRes int rootView, int animEnter, int animEnd) {
        final Stack<BaseFragment> stack = getFragmentStack(rootView);
        Fragment fg;

        if (stack.isEmpty()) {
            fg = getSupportFragmentManager()
                    .findFragmentById(rootView);

            if (fg == null || !(fg instanceof BaseFragment)) {
                return false;
            }
        } else {
            fg = stack.pop();
        }

        if (fg != null && !((BaseFragment) fg).onBackPressed() && !stack.isEmpty()) {
            // Try to put the old fragment if it is found
            final FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();

            tryApplyAnimations(transaction, animEnter, animEnd);

            BaseFragment fragment = stack.pop().onRecreate();
            stack.push(fragment);

            transaction.replace(rootView, fragment)
                    .commit();

            onNewFragment(fragment);
            return true;
        }

        return false;
    }

    public BaseFragment find(String tag) {
        BaseFragment fragment = mCachedFragments.get(tag);

        if (fragment == null) {
            fragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(tag);

            if (fragment != null) {
                mCachedFragments.put(tag, fragment);
            }
        }

        return fragment;
    }

    @Override
    public void openFragment(@IdRes int rootView, BaseFragment fragment, String tag) {
        openFragment(rootView, fragment, tag, mFragmentAnimIn[0], mFragmentAnimIn[1]);
    }

    @Override
    public void openFragment(@IdRes int rootView, BaseFragment fragment, String tag, int animEnter, int animEnd) {
        if (!ensureFragment(fragment)) {
            return;
        }

        final FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        tryApplyAnimations(transaction, animEnter, animEnd);
        transaction.replace(rootView, fragment, tag)
                .commit();

        getFragmentStack(rootView).push(fragment);
        mCachedFragments.put(tag, fragment);

        onNewFragment(fragment);
    }

    @Override
    public void openFragment(@IdRes int rootView, Class<? extends BaseFragment> clazz, Bundle args, String tag) {
        openFragment(rootView, clazz, args, tag, mFragmentAnimIn[0], mFragmentAnimIn[1]);
    }

    @Override
    public void openFragment(@IdRes int rootView, Class<? extends BaseFragment> clazz, String tag, int animEnter, int animEnd) {
        openFragment(rootView, clazz, null, tag, animEnter, animEnd);
    }

    @Override
    public void openFragment(@IdRes int rootView, Class<? extends BaseFragment> clazz, String tag) {
        openFragment(rootView, clazz, null, tag);
    }

    @Override
    public void openFragment(@IdRes int rootView, Class<? extends BaseFragment> clazz, Bundle args, String tag, int animEnter, int animEnd) {
        if (!tryCached(rootView, clazz, args, tag, animEnter, animEnd)) {
            Fragment fg = Fragment.instantiate(this, clazz.getName(), args);

            if (fg instanceof BaseFragment) {
                openFragment(rootView, (BaseFragment) fg, tag, animEnter, animEnd);
            } else {
                throw new IllegalArgumentException("Fragment must be subclass of BaseFragment");
            }
        }
    }

    private boolean ensureFragment(BaseFragment fragment) {
        for (Iterator<Stack<BaseFragment>> it = mOpenedFragments.values().iterator(); it.hasNext(); ) {
            Stack<BaseFragment> stack = it.next();

            if (stack.contains(fragment)) {
                return false;
            }
        }

        return true;
    }

    protected void onNewFragment(BaseFragment fragment) {
    }

    private boolean tryCached(int rootView, Class<? extends BaseFragment> clazz, Bundle args, String tag, int animEnter, int animEnd) {
        for (Iterator<Map.Entry<String, BaseFragment>> it = mCachedFragments.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, BaseFragment> entry = it.next();

            if (entry.getValue().getClass() == clazz) {
                if (args != null) {
                    entry.getValue().setArguments(args);
                }

                openFragment(rootView, entry.getValue(), tag, animEnter, animEnd);
                return true;
            }
        }

        return false;
    }

    private void tryApplyAnimations(FragmentTransaction transaction, int animEnter, int animEnd) {
        if (animEnter != 0 || animEnd != 0) {
            transaction.setCustomAnimations(animEnter, animEnd);
        }
    }

    @NonNull
    private Stack<BaseFragment> getFragmentStack(@IdRes int rootView) {
        Stack<BaseFragment> stack = mOpenedFragments.get(rootView);
        if (stack == null) {
            stack = new Stack<>();
            mOpenedFragments.put(rootView, stack);
        }

        return stack;
    }

    @Override
    public Context getContext() {
        return this;
    }
}
