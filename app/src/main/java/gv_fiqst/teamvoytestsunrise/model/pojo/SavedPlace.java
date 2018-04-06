package gv_fiqst.teamvoytestsunrise.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import gv_fiqst.teamvoytestsunrise.util.Util;


public class SavedPlace implements Parcelable {
    public String name;
    public LatLng location;

    public SavedPlace(String name, LatLng location) {
        this.name = name;
        this.location = location;
    }

    protected SavedPlace(Parcel in) {
        name = in.readString();
        location = in.readParcelable(LatLng.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(location, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SavedPlace> CREATOR = new Creator<SavedPlace>() {
        @Override
        public SavedPlace createFromParcel(Parcel in) {
            return new SavedPlace(in);
        }

        @Override
        public SavedPlace[] newArray(int size) {
            return new SavedPlace[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SavedPlace that = (SavedPlace) o;

        if (!name.equals(that.name)) return false;
        return Util.dist(location, that.location) < 1000; //1 км похибки
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + location.hashCode();
        return result;
    }
}
