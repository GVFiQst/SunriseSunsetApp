package gv_fiqst.teamvoytestsunrise.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import gv_fiqst.teamvoytestsunrise.util.Util;


public class SunData implements Parcelable {
    public static SunData empty() {
        return new SunData();
    }

    public static boolean isActual(SunData data) {
        return data != null && data.hasData && Util.datesAreOnSameDay(data.mNoon, new Date());
    }

    /**
     * This field guarantees that <code>mSunrise != null && mNoon != null && mSunset != null</code>
     */
    private boolean hasData;
    private Date mSunrise;
    private Date mNoon;
    private Date mSunset;

    private SunData(boolean has, Date sunrise, Date noon, Date sunset) {
        hasData     = has;
        mSunrise    = sunrise;
        mNoon       = noon;
        mSunset     = sunset;
    }

    private SunData() {
        this(false, null, null, null);
    }

    public SunData(Date sunrise, Date noon, Date sunset) {
        this(true, sunrise, noon, sunset);
    }

    public boolean hasData() {
        return hasData;
    }

    public Date getSunrise() {
        return mSunrise;
    }

    public Date getNoon() {
        return mNoon;
    }

    public Date getSunset() {
        return mSunset;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (hasData ? 1 : 0));

        if (hasData) {
            dest.writeLong(mSunrise.getTime());
            dest.writeLong(mNoon.getTime());
            dest.writeLong(mSunset.getTime());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SunData> CREATOR = new Creator<SunData>() {
        @Override
        public SunData createFromParcel(Parcel in) {
            boolean hasData = in.readByte() != 0;

            if (hasData) {
                return new SunData(
                        new Date(in.readLong()),
                        new Date(in.readLong()),
                        new Date(in.readLong())
                );
            }

            return SunData.empty();
        }

        @Override
        public SunData[] newArray(int size) {
            return new SunData[size];
        }
    };
}
