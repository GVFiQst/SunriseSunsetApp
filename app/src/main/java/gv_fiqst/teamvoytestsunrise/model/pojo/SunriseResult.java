package gv_fiqst.teamvoytestsunrise.model.pojo;


public class SunriseResult {
    public SunriseData results;
    public String status;

    public SunriseResult(SunriseData results, String status) {
        this.results = results;
        this.status = status;
    }
}
