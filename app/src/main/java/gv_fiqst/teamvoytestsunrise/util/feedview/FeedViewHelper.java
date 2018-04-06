package gv_fiqst.teamvoytestsunrise.util.feedview;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import gv_fiqst.teamvoytestsunrise.R;

public class FeedViewHelper {

    public static FeedViewHelper forActivity(Activity activity, Callback callback) {
        return new FeedViewHelper(activity, callback);
    }

    public static FeedViewHelper forFragment(LayoutInflater inflater, ViewGroup container, Callback callback) {
        return new FeedViewHelper(inflater, container, callback);
    }

    public static FeedViewHelper forStandaloneView(ViewGroup parent, Callback callback) {
        return new FeedViewHelper(parent, callback);
    }

    public static <T> FeedViewHolder<T> forViewHolder(
            ViewGroup parent, FeedViewHolder.ViewHolder<T> holder, Callback callback
    ) {
        return new FeedViewHolder<>(parent, holder, callback);
    }

    @BindView(R.id.layoutError)
    View mLayoutError;

    @BindView(R.id.textView)
    TextView mErrorTextView;

    @BindView(R.id.button)
    Button mErrorButton;

    @BindView(R.id.layoutLoading)
    View mLayoutLoading;

    @BindView(R.id.textLoading)
    TextView mLoadingTextView;

    @BindView(R.id.content)
    ViewGroup mContentView;

    private Unbinder mUnbinder;
    private View mInflated;
    private Callback mCallback;
    private int mErrorCode = -1;
    private boolean isReleased = false;

    /* For standalone views */
    protected FeedViewHelper(ViewGroup container, Callback callback) {
        this(LayoutInflater.from(container.getContext()), container, callback);

        container.addView(mInflated, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
    }

    /* For fragments */
    private FeedViewHelper(LayoutInflater inflater, ViewGroup container, Callback callback) {
        init(inflater.inflate(R.layout.feed_view, container, false), callback);
    }

    /* For activities */
    private FeedViewHelper(Activity activity, Callback callback) {
        activity.setContentView(R.layout.feed_view);
        init(activity.getWindow().getDecorView(), callback);
    }

    private void init(View inflated, Callback callback) {
        mInflated = inflated;
        mUnbinder = ButterKnife.bind(this, mInflated);
        mCallback = callback;

        mLayoutError.setVisibility(View.GONE);
        mContentView.setVisibility(View.GONE);
        mLayoutLoading.setVisibility(View.GONE);
    }

    private static void fixWrapContent(View view) {
        int visibility = view.getVisibility();
        view.setVisibility(visibility == View.GONE ? View.VISIBLE : View.GONE);
        view.setVisibility(visibility);

        view.requestLayout();
    }

    public void showErrorMessage(@NonNull String msg, @Nullable String buttonText, int errorCode) {
        if (isReleased) return;

        mErrorCode = errorCode;
        mContentView.setVisibility(View.GONE);
        mLayoutLoading.setVisibility(View.GONE);
        mLayoutError.setVisibility(View.VISIBLE);

        mErrorTextView.setText(msg);
        if (buttonText != null && !buttonText.isEmpty()) {
            mErrorButton.setVisibility(View.VISIBLE);
            mErrorButton.setText(buttonText);
        } else {
            mErrorButton.setVisibility(View.GONE);
        }

        mLayoutError.requestLayout();
    }

    public void showLoading(String text) {
        if (isReleased) return;

        mContentView.setVisibility(View.GONE);
        mLayoutError.setVisibility(View.GONE);
        mLayoutLoading.setVisibility(View.VISIBLE);

        if (text != null && !text.isEmpty()) {
            mLoadingTextView.setVisibility(View.VISIBLE);
            mLoadingTextView.setText(text);
        } else {
            mLoadingTextView.setVisibility(View.GONE);
        }

        mLayoutLoading.requestLayout();
    }

    public void showContent() {
        if (isReleased) return;

        mErrorCode = -1;
        mLayoutError.setVisibility(View.GONE);
        mLayoutLoading.setVisibility(View.GONE);
        mContentView.setVisibility(View.VISIBLE);

        mContentView.requestLayout();
    }

    public void hide() {
        if (isReleased) return;

        mErrorCode = -1;
        mLayoutError.setVisibility(View.GONE);
        mLayoutLoading.setVisibility(View.GONE);
        mContentView.setVisibility(View.GONE);
    }

    public FeedViewHelper setContentView(Context context, @LayoutRes int layoutRes) {
        if (!isReleased) {
            LayoutInflater
                    .from(context)
                    .inflate(layoutRes, mContentView, true);
        }

        return this;
    }

    public FeedViewHelper setContentView(View view) {
        if (!isReleased) {
            mContentView.addView(view, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));

            fixWrapContent(view);
        }

        return this;
    }

    public FeedViewHelper setBackground(Drawable background) {
        ViewCompat.setBackground(mInflated, background);

        return this;
    }

    public FeedViewHelper setBackground(@ColorInt int color) {
        mInflated.setBackgroundColor(color);

        return this;
    }

    public View getView() {
        return mInflated;
    }

    public View getContentView() {
        return mContentView;
    }

    public Button getErrorButton() {
        return mErrorButton;
    }

    @OnClick(R.id.button)
    public void onButtonClick() {
        if (mCallback != null) {
            mCallback.onErrorButtonClick(mErrorCode);
        }
    }

    public void release() {
        isReleased = true;

        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }

        mInflated = null;
        mCallback = null;
    }

    public TextView getErrorTextView() {
        return mErrorTextView;
    }

    public interface Callback {
        void onErrorButtonClick(int errorCode);
    }
}
