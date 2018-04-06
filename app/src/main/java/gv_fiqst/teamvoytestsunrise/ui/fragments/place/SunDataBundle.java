package gv_fiqst.teamvoytestsunrise.ui.fragments.place;

import android.os.Parcel;
import android.os.Parcelable;

import gv_fiqst.teamvoytestsunrise.model.pojo.SunData;


class SunDataBundle implements Parcelable {
    public SunData today;
    public SunData tomorrow;

    public SunDataBundle() {
    }

    protected SunDataBundle(Parcel in) {
        today = in.readParcelable(SunData.class.getClassLoader());
        tomorrow = in.readParcelable(SunData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(today, flags);
        dest.writeParcelable(tomorrow, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SunDataBundle> CREATOR = new Creator<SunDataBundle>() {
        @Override
        public SunDataBundle createFromParcel(Parcel in) {
            return new SunDataBundle(in);
        }

        @Override
        public SunDataBundle[] newArray(int size) {
            return new SunDataBundle[size];
        }
    };
}
