package gv_fiqst.teamvoytestsunrise.ui.fragments.sundata;


import android.text.style.CharacterStyle;

import gv_fiqst.teamvoytestsunrise.model.pojo.SunData;

public class SunDataState {
    public String title;
    public CharacterStyle errorSpan;
    public String errorString;
    public String cityName;
    public SunData today;
    public SunData tomorrow;

    public SunDataState() {
    }

    public SunDataState(String title, CharacterStyle errorSpan,
                        String errorString, String cityName,
                        SunData today, SunData tomorrow) {
        this.title = title;
        this.errorSpan = errorSpan;
        this.errorString = errorString;
        this.cityName = cityName;
        this.today = today;
        this.tomorrow = tomorrow;
    }

    public String getTitle() {
        return title;
    }

    public SunDataState setTitle(String title) {
        this.title = title;
        return this;
    }

    public CharacterStyle getErrorSpan() {
        return errorSpan;
    }

    public SunDataState setErrorSpan(CharacterStyle errorSpan) {
        this.errorSpan = errorSpan;
        return this;
    }

    public String getErrorString() {
        return errorString;
    }

    public SunDataState setErrorString(String errorString) {
        this.errorString = errorString;
        return this;
    }

    public String getCityName() {
        return cityName;
    }

    public SunDataState setCityName(String cityName) {
        this.cityName = cityName;
        return this;
    }

    public SunData getToday() {
        return today;
    }

    public SunDataState setToday(SunData today) {
        this.today = today;
        return this;
    }

    public SunData getTomorrow() {
        return tomorrow;
    }

    public SunDataState setTomorrow(SunData tomorrow) {
        this.tomorrow = tomorrow;
        return this;
    }
}
