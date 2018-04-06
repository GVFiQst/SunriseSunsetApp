package gv_fiqst.teamvoytestsunrise.ui.base;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Unbinder;


public class BaseFragment extends Fragment {
    private Unbinder mUnbinder;
    private IBaseActivity mBaseActivity;
    private int mRootId = 0;

    protected int getLayoutId() {
        return 0;
    }

    protected BaseFragment onRecreate() {
        return new BaseFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            mRootId = container.getId();
        }

        int id = getLayoutId();
        if (id != 0) {
            return inflater.inflate(id, container, false);
        }

        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
    }

    protected IBaseActivity getBaseActivity() {
        return mBaseActivity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof BaseActivity) {
            mBaseActivity = (IBaseActivity) context;
        } else {
            throw new IllegalStateException("Base fragment must be attached to IBaseActivity only");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mBaseActivity = null;
    }

    protected int getRootId() {
        return mRootId;
    }

    public boolean checkGrantResults(int[] grantResults) {
        for (int res : grantResults) {
            if (res != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    protected void setUnbinder(Unbinder unbinder) {
        mUnbinder = unbinder;
    }

    /**
     * @return true if onBackPressed() was consumed in this method
     */
    protected boolean onBackPressed() {
        return false;
    }

    public void openFragment(BaseFragment fragment, String tag) {
        if (mBaseActivity != null) {
            mBaseActivity.openFragment(getRootId(), fragment, tag);
        }
    }

    public void openFragment(BaseFragment fragment, String tag, int animEnter, int animEnd) {
        if (mBaseActivity != null) {
            mBaseActivity.openFragment(getRootId(), fragment, tag, animEnter, animEnd);
        }
    }

    public void openFragment(Class<? extends BaseFragment> clazz, Bundle args, String tag) {
        if (mBaseActivity != null) {
            mBaseActivity.openFragment(getRootId(), clazz, args, tag);
        }
    }

    public void openFragment(Class<? extends BaseFragment> clazz, String tag, int animEnter, int animEnd) {
        if (mBaseActivity != null) {
            mBaseActivity.openFragment(getRootId(), clazz, tag, animEnter, animEnd);
        }
    }

    public void openFragment(Class<? extends BaseFragment> clazz, String tag) {
        if (mBaseActivity != null) {
            mBaseActivity.openFragment(getRootId(), clazz, tag);
        }
    }

    public void openFragment(Class<? extends BaseFragment> clazz, Bundle args, String tag, int animEnter, int animEnd) {
        if (mBaseActivity != null) {
            mBaseActivity.openFragment(getRootId(), clazz, args, tag, animEnter, animEnd);
        }
    }
}
