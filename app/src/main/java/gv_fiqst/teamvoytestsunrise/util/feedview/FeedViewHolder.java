package gv_fiqst.teamvoytestsunrise.util.feedview;


import android.view.View;
import android.view.ViewGroup;

public class FeedViewHolder<Model> extends FeedViewHelper {
    private ViewHolder<Model> mViewHolder;

    public FeedViewHolder(ViewGroup container, ViewHolder<Model> viewHolder, Callback callback) {
        super(container, callback);
        mViewHolder = viewHolder;
        setContentView(mViewHolder.createView(container));
    }

    public void updateAndShow(Model model) {
        mViewHolder.bindView(model);
        showContent();
    }

    @Override
    public void release() {
        super.release();

        if (mViewHolder != null) {
            mViewHolder.release();
            mViewHolder = null;
        }
    }

    public interface ViewHolder<Model> {
        View createView(ViewGroup container);
        void bindView(Model model);
        void release();
    }
}
