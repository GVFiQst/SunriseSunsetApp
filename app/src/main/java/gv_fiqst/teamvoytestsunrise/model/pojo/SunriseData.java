package gv_fiqst.teamvoytestsunrise.model.pojo;

import java.util.Date;


public class SunriseData {
    public Date sunrise;
    public Date sunset;
    public Date solar_noon;
    public long day_length;
    public Date civil_twilight_begin;
    public Date civil_twilight_end;
    public Date nautical_twilight_begin;
    public Date nautical_twilight_end;
    public Date astronomical_twilight_begin;
    public Date astronomical_twilight_end;

    public SunriseData(Date sunrise, Date sunset, Date solar_noon,
                       long day_length, Date civil_twilight_begin,
                       Date civil_twilight_end, Date nautical_twilight_begin,
                       Date nautical_twilight_end, Date astronomical_twilight_begin,
                       Date astronomical_twilight_end) {
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.solar_noon = solar_noon;
        this.day_length = day_length;
        this.civil_twilight_begin = civil_twilight_begin;
        this.civil_twilight_end = civil_twilight_end;
        this.nautical_twilight_begin = nautical_twilight_begin;
        this.nautical_twilight_end = nautical_twilight_end;
        this.astronomical_twilight_begin = astronomical_twilight_begin;
        this.astronomical_twilight_end = astronomical_twilight_end;
    }
}
