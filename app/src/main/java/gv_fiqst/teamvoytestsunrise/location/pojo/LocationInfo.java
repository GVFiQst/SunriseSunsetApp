package gv_fiqst.teamvoytestsunrise.location.pojo;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class LocationInfo implements Parcelable {
    public String mCityName;
    public LatLng mCoords;
    public float  mAccuracy;

    public LocationInfo(String cityName, LatLng coords, float accuracy) {
        mCityName = cityName;
        mCoords = coords;
        mAccuracy = accuracy;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCityName);
        dest.writeParcelable(mCoords, flags);
        dest.writeFloat(mAccuracy);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LocationInfo> CREATOR = new Creator<LocationInfo>() {
        @Override
        public LocationInfo createFromParcel(Parcel in) {
            return new LocationInfo(
                    in.readString(),
                    (LatLng) in.readParcelable(LatLng.class.getClassLoader()),
                    in.readFloat()
            );
        }

        @Override
        public LocationInfo[] newArray(int size) {
            return new LocationInfo[size];
        }
    };

    public static class Builder {
        public String mCityName;
        public LatLng mCoords;
        public float mAccuracy;

        public Builder setCityName(String mCityName) {
            this.mCityName = mCityName;
            return this;
        }

        public Builder setCoords(LatLng mCoords) {
            this.mCoords = mCoords;
            return this;
        }

        public Builder setAccuracy(float mAccuracy) {
            this.mAccuracy = mAccuracy;
            return this;
        }

        public LocationInfo build() {
            return new LocationInfo(mCityName, mCoords, mAccuracy);
        }
    }
}
