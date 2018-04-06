package gv_fiqst.teamvoytestsunrise.ui.fragments.main;


import android.os.Parcel;
import android.os.Parcelable;

import gv_fiqst.teamvoytestsunrise.location.pojo.LocationInfo;
import gv_fiqst.teamvoytestsunrise.model.pojo.SunData;

public class MainFragmentState implements Parcelable {
    public static MainFragmentState copy(MainFragmentState state) {
        if (state == null) {
            // I don't need NPE :)
            return new MainFragmentState();
        }

        MainFragmentState copy      = new MainFragmentState();
        copy.hasInternet            = state.hasInternet;
        copy.hasPermissions         = state.hasPermissions;
        copy.gpsEnabled             = state.gpsEnabled;
        copy.isServicePresent       = state.isServicePresent;
        copy.mInfo                  = state.mInfo;
        copy.mDataToday             = state.mDataToday;
        copy.mDataTomorrow          = state.mDataTomorrow;

        return copy;
    }

    public boolean      hasInternet;
    public boolean      hasPermissions;
    public boolean      gpsEnabled;
    public boolean      isServicePresent;
    public LocationInfo mInfo;
    public SunData      mDataToday;
    public SunData      mDataTomorrow;

    public MainFragmentState() {
    }

    public MainFragmentState(boolean hasInternet, boolean hasPermissions, boolean gpsEnabled,
                             boolean isCurrentlyUpdating, boolean isServicePresent,
                             LocationInfo mInfo, SunData mDataToday, SunData mDataTomorrow) {
        this.hasInternet            = hasInternet;
        this.hasPermissions         = hasPermissions;
        this.gpsEnabled             = gpsEnabled;
        this.isServicePresent       = isServicePresent;
        this.mInfo                  = mInfo;
        this.mDataToday             = mDataToday;
        this.mDataTomorrow          = mDataTomorrow;
    }

    public MainFragmentState setHasInternet(boolean hasInternet) {
        this.hasInternet = hasInternet;
        return this;
    }

    public MainFragmentState setHasPermissions(boolean hasPermissions) {
        this.hasPermissions = hasPermissions;
        return this;
    }

    public MainFragmentState setGpsEnabled(boolean gpsEnabled) {
        this.gpsEnabled = gpsEnabled;
        return this;
    }
    public MainFragmentState setServicePresent(boolean servicePresent) {
        isServicePresent = servicePresent;
        return this;
    }

    public MainFragmentState setInfo(LocationInfo mInfo) {
        this.mInfo = mInfo;
        return this;
    }

    public MainFragmentState setDataToday(SunData mDataToday) {
        this.mDataToday = mDataToday;
        return this;
    }

    public MainFragmentState setDataTomorrow(SunData mDataTomorrow) {
        this.mDataTomorrow = mDataTomorrow;
        return this;
    }

    protected MainFragmentState(Parcel in) {
        hasInternet = in.readByte() != 0;
        hasPermissions = in.readByte() != 0;
        gpsEnabled = in.readByte() != 0;
        isServicePresent = in.readByte() != 0;
        mInfo = in.readParcelable(LocationInfo.class.getClassLoader());
        mDataToday = in.readParcelable(SunData.class.getClassLoader());
        mDataTomorrow = in.readParcelable(SunData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (hasInternet ? 1 : 0));
        dest.writeByte((byte) (hasPermissions ? 1 : 0));
        dest.writeByte((byte) (gpsEnabled ? 1 : 0));
        dest.writeByte((byte) (isServicePresent ? 1 : 0));
        dest.writeParcelable(mInfo, flags);
        dest.writeParcelable(mDataToday, flags);
        dest.writeParcelable(mDataTomorrow, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MainFragmentState> CREATOR = new Creator<MainFragmentState>() {
        @Override
        public MainFragmentState createFromParcel(Parcel in) {
            return new MainFragmentState(in);
        }

        @Override
        public MainFragmentState[] newArray(int size) {
            return new MainFragmentState[size];
        }
    };
}
