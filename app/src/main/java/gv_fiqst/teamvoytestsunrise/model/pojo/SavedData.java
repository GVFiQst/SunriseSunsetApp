package gv_fiqst.teamvoytestsunrise.model.pojo;

import gv_fiqst.teamvoytestsunrise.location.pojo.LocationInfo;


public class SavedData {
    public LocationInfo info;
    public SunData today;
    public SunData tomorrow;

    public SavedData(LocationInfo info, SunData today, SunData tomorrow) {
        this.info = info;
        this.today = today;
        this.tomorrow = tomorrow;
    }
}
