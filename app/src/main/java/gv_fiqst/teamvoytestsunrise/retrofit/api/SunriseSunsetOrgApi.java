package gv_fiqst.teamvoytestsunrise.retrofit.api;


import gv_fiqst.teamvoytestsunrise.model.pojo.SunriseResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SunriseSunsetOrgApi {
    String BASE_URL = "https://api.sunrise-sunset.org/";

    @GET("json?formatted=0")
    Call<SunriseResult> fetchData(@Query("lat") float lat, @Query("lng") float lng, @Query("date") String date);
}
