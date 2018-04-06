package gv_fiqst.teamvoytestsunrise.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import gv_fiqst.teamvoytestsunrise.model.pojo.SunriseResult;
import gv_fiqst.teamvoytestsunrise.retrofit.RetrofitManager;
import gv_fiqst.teamvoytestsunrise.retrofit.api.SunriseSunsetOrgApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class SunriseManagerImpl {
    static final SunriseManagerImpl sInstance = new SunriseManagerImpl();

    private RetrofitManager mRetrofitManager;
    private SimpleDateFormat mDateFormat;

    private SunriseManagerImpl() {
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        mRetrofitManager = RetrofitManager.Creator.create(
                SunriseSunsetOrgApi.BASE_URL,
                new RetrofitManager.Factory() {
                    @Override
                    public Retrofit build(String baseUrl) {
                        Gson gson = new GsonBuilder()
                                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                                .create();

                        return new Retrofit.Builder()
                                .baseUrl(baseUrl)
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .build();
                    }
                }
        );
    }


    Call<SunriseResult> fetchData(float lat, float lng, Date date, final Callback<SunriseResult> listener) {
        final Call<SunriseResult> call = mRetrofitManager.getApi(SunriseSunsetOrgApi.class)
                .fetchData(lat, lng, mDateFormat.format(date));

        call.enqueue(listener);
        return call;
    }
}
