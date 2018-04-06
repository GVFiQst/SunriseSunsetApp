package gv_fiqst.teamvoytestsunrise.ui.activities.main;

import android.os.Parcel;
import android.os.Parcelable;

import gv_fiqst.teamvoytestsunrise.location.pojo.LocationInfo;
import gv_fiqst.teamvoytestsunrise.model.pojo.SunData;



public class MainInitData implements Parcelable {
    public boolean hasInternet;
    public boolean gpsEnabled;
    public boolean hasPermissions;
    public SunData mDataToday;
    public SunData mDataTomorrow;
    public LocationInfo mInfo;

    public MainInitData() {
    }

    protected MainInitData(Parcel in) {
        hasInternet = in.readByte() != 0;
        gpsEnabled = in.readByte() != 0;
        hasPermissions = in.readByte() != 0;
        mDataToday = in.readParcelable(SunData.class.getClassLoader());
        mDataTomorrow = in.readParcelable(SunData.class.getClassLoader());
        mInfo = in.readParcelable(LocationInfo.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (hasInternet ? 1 : 0));
        dest.writeByte((byte) (gpsEnabled ? 1 : 0));
        dest.writeByte((byte) (hasPermissions ? 1 : 0));
        dest.writeParcelable(mDataToday, flags);
        dest.writeParcelable(mDataTomorrow, flags);
        dest.writeParcelable(mInfo, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MainInitData> CREATOR = new Creator<MainInitData>() {
        @Override
        public MainInitData createFromParcel(Parcel in) {
            return new MainInitData(in);
        }

        @Override
        public MainInitData[] newArray(int size) {
            return new MainInitData[size];
        }
    };
}
