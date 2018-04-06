package gv_fiqst.teamvoytestsunrise.util;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.res.ResourcesCompat;
import android.util.SparseIntArray;


public class ColorBundle {

    private final SparseIntArray mColors;
    private Context mContext;

    public ColorBundle(Context context, @ColorRes int... preloaded) {
        mColors = new SparseIntArray();
        mContext = context;

        init(preloaded);
    }

    private void init(@ColorRes int[] preloaded) {
        for (int i = 0; i < preloaded.length; i++) {
            mColors.put(preloaded[i], ResourcesCompat.getColor(
                    mContext.getResources(), preloaded[i], mContext.getTheme()
            ));
        }
    }

    public int getColor(int id) {
        int color = mColors.get(id);

        if (color == 0) {
            mColors.put(id, color = ResourcesCompat.getColor(
                    mContext.getResources(), id, mContext.getTheme()
            ));
        }

        return color;
    }

    public void release() {
        mColors.clear();
        mContext = null;
    }
}
