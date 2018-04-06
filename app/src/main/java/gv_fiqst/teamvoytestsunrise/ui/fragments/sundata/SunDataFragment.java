package gv_fiqst.teamvoytestsunrise.ui.fragments.sundata;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.ImageViewCompat;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.transitionseverywhere.TransitionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import gv_fiqst.teamvoytestsunrise.R;
import gv_fiqst.teamvoytestsunrise.model.pojo.SunData;
import gv_fiqst.teamvoytestsunrise.ui.base.BaseFragment;
import gv_fiqst.teamvoytestsunrise.util.ColorBundle;
import gv_fiqst.teamvoytestsunrise.util.Util;
import gv_fiqst.teamvoytestsunrise.util.feedview.FeedViewHelper;
import gv_fiqst.teamvoytestsunrise.util.feedview.FeedViewHolder;


public class SunDataFragment extends BaseFragment implements FeedViewHelper.Callback {
    @BindView(R.id.txtTitle)
    TextView mTitleTextView;

    @BindView(R.id.txtLocation)
    TextView mLocationTextView;

    @BindView(R.id.imgLocation)
    ImageView mLocationImageView;

    private FeedViewHolder<SunData> mTodayHelper;
    private FeedViewHolder<SunData> mTomorrowHelper;

    private ColorBundle mColors;

    private SunDataState mState;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sun_data;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mColors = new ColorBundle(getContext(),
                R.color.colorAccent, R.color.colorDisabled,
                R.color.colorErrorUI, R.color.colorNormalUI,
                R.color.colorUpdatingUI, R.color.colorLogoText
        );

        mTodayHelper = FeedViewHelper.forViewHolder(
                (ViewGroup) view.findViewById(R.id.rootToday),
                new DateViewHolder(getString(R.string.today), mColors),
                this
        );
        mTomorrowHelper = FeedViewHelper.forViewHolder(
                (ViewGroup) view.findViewById(R.id.rootTomorrow),
                new DateViewHolder(getString(R.string.tomorrow), mColors),
                this
        );
        setUnbinder(ButterKnife.bind(this, view));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mTodayHelper != null) {
            mTodayHelper.release();
            mTodayHelper = null;
        }

        if (mTomorrowHelper != null) {
            mTomorrowHelper.release();
            mTomorrowHelper = null;
        }

        if (mColors != null) {
            mColors.release();
            mColors = null;
        }
    }

    @Override
    public void onErrorButtonClick(int errorCode) {

    }

    private void initHolder(FeedViewHolder<SunData> holder) {
        TextView textView = holder.getErrorTextView();
        textView.setTypeface(ResourcesCompat.getFont(getContext(), R.font.font_berlin_sans), Typeface.BOLD);
        textView.setTextColor(mColors.getColor(R.color.colorLogoText));
    }

    public void update(SunDataState state) {
        mState = state;

        mTitleTextView.setText(state.title);
        updateCityUI();
        updateDate(mTodayHelper, state.today);
        updateDate(mTomorrowHelper, state.tomorrow);
    }

    private void updateDate(FeedViewHolder<SunData> holder, SunData data) {
        if (data != null && data.hasData()) {
            holder.updateAndShow(data);
        } else {
            if (mState.cityName != null && !mState.cityName.isEmpty()) {
                holder.showLoading("");
            } else {
                holder.hide();
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void updateCityUI() {
        String city = getString(R.string.searching);
        boolean error = mState.errorSpan != null;
        boolean searching = true;

        if (mState.cityName != null && !mState.cityName.isEmpty()) {
            city = mState.cityName;
            searching = false;
        } else if (error) {
            city = getString(R.string.unknown);
            searching = false;
        }

        SpannableStringBuilder spanBuilder = new SpannableStringBuilder(city);
        if (error) {
            spanBuilder.append("\n");
            spanBuilder.append(mState.errorString);
            spanBuilder.setSpan(mState.errorSpan,
                    spanBuilder.length() - mState.errorString.length(),
                    spanBuilder.length(), 0);
        }

        int color = error
                ? mColors.getColor(R.color.colorErrorUI) : searching
                ? mColors.getColor(R.color.colorUpdatingUI)
                : mColors.getColor(R.color.colorNormalUI);

        int drawableRes = error
                ? R.drawable.ic_location_off : searching
                ? R.drawable.ic_location_searching
                : R.drawable.ic_location_on;

        if (error) {
            mLocationTextView.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            mLocationTextView.setMovementMethod(null);
        }

        ImageViewCompat.setImageTintList(mLocationImageView, Util.tint(color));
        mLocationImageView.setImageResource(drawableRes);

        TransitionManager.beginDelayedTransition((ViewGroup) getView());

        mLocationTextView.setTextColor(color);
        mLocationTextView.setText(spanBuilder);
    }

    /* Not private because of ButterKnife */
    static class DateViewHolder implements FeedViewHolder.ViewHolder<SunData> {
        @BindView(R.id.txtTitle)
        TextView mTitleView;

        @BindView(R.id.txtSunrise)
        TextView mSunriseView;

        @BindView(R.id.txtNoon)
        TextView mNoonView;

        @BindView(R.id.txtSunset)
        TextView mSunsetView;

        @BindView(R.id.imgSunrise)
        ImageView mSunriseImageView;

        @BindView(R.id.imgNoon)
        ImageView mNoonImageView;

        @BindView(R.id.imgSunset)
        ImageView mSunsetImageView;

        private ViewGroup mContainer;

        private Unbinder mUnbinder;
        private String mTitle;
        private ColorBundle mColors;

        public DateViewHolder(String title, ColorBundle bundle) {
            mTitle = title;
            mColors = bundle;
        }

        @Override
        public View createView(ViewGroup container) {
            View view = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.content_sunrise, container, false);

            mUnbinder = ButterKnife.bind(this, view);
            mContainer = container;
            return view;
        }

        @Override
        public void bindView(SunData data) {
            TransitionManager.beginDelayedTransition(mContainer);
            mTitleView.setText(mTitle);

            if (data == null || !data.hasData()) {
                mContainer.setVisibility(View.INVISIBLE);
                return;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

            mSunriseView.setText(dateFormat.format(data.getSunrise()));
            mNoonView.setText(dateFormat.format(data.getNoon()));
            mSunsetView.setText(dateFormat.format(data.getSunset()));

            Date now = new Date();
            checkDate(now, data.getSunrise(), mSunriseImageView, mSunriseView);
            checkDate(now, data.getNoon(), mNoonImageView, mNoonView);
            checkDate(now, data.getSunset(), mSunsetImageView, mSunsetView);
            mContainer.setVisibility(View.VISIBLE);
        }

        @Override
        public void release() {
            if (mUnbinder != null) {
                mUnbinder.unbind();
                mUnbinder = null;
            }

            mColors = null;
            mContainer = null;
        }

        private void checkDate(Date standard, Date dateToCheck, ImageView imageView, TextView textView) {
            if (dateToCheck.before(standard)) {
                ImageViewCompat.setImageTintList(imageView, Util.tint(mColors.getColor(R.color.colorDisabled)));
                textView.setTextColor(mColors.getColor(R.color.colorDisabled));
            } else {
                ImageViewCompat.setImageTintList(imageView, Util.tint(mColors.getColor(R.color.colorAccent)));
                textView.setTextColor(mColors.getColor(R.color.colorNormalUI));
            }
        }
    }

}
