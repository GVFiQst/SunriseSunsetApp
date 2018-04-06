package gv_fiqst.teamvoytestsunrise.ui.fragments.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import gv_fiqst.teamvoytestsunrise.R;
import gv_fiqst.teamvoytestsunrise.model.pojo.SavedPlace;


class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    public static final int ACTION_SELECT = 0x1;
    public static final int ACTION_DELETE = 0x2;

    private List<SavedPlace> mSavedPlaces;
    private Listener mListener;
    private List<Unbinder> mUnbiners;

    public SearchAdapter(Listener listener) {
        mListener = listener;
    }

    public void update(List<SavedPlace> places) {
        mSavedPlaces = places;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_saved_place, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mSavedPlaces.get(position).name);
    }

    @Override
    public int getItemCount() {
        return mSavedPlaces == null ? 0 : mSavedPlaces.size();
    }

    private void click(int pos) {
        mListener.onSavedPlace(mSavedPlaces.get(pos), ACTION_SELECT);
    }

    private void delete(int pos) {
        mListener.onSavedPlace(mSavedPlaces.get(pos), ACTION_DELETE);
    }

    private void addUnbinder(Unbinder unbinder) {
        if (mUnbiners == null) {
            mUnbiners = new ArrayList<>();
        }

        mUnbiners.add(unbinder);
    }

    public void release() {
        if (mUnbiners != null) {
            for (Unbinder unbinder : mUnbiners) {
                unbinder.unbind();
            }

            mUnbiners.clear();
            mUnbiners = null;
        }

        mListener = null;
    }

    public interface Listener {
        void onSavedPlace(SavedPlace place, int action);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txtPlace)
        TextView mPlaceTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            addUnbinder(ButterKnife.bind(this, itemView));
        }

        public void bind(String name) {
            mPlaceTextView.setText(name);
        }

        @Override
        public void onClick(View v) {
            click(getAdapterPosition());
        }

        @OnClick(R.id.btnDelete)
        public void onDelete() {
            delete(getAdapterPosition());
        }
    }
}
