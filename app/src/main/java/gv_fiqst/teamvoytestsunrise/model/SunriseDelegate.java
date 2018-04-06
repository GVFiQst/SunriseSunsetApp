package gv_fiqst.teamvoytestsunrise.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gv_fiqst.teamvoytestsunrise.model.pojo.SunData;
import gv_fiqst.teamvoytestsunrise.model.pojo.SunriseData;
import gv_fiqst.teamvoytestsunrise.model.pojo.SunriseResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


class SunriseDelegate implements SunriseManager {
    private SunriseManagerImpl mImpl;
    private List<Call<SunriseResult>> mCalls;

    public SunriseDelegate(SunriseManagerImpl impl) {
        mImpl = impl;
        mCalls = new ArrayList<>();
    }

    @Override
    public void fetchData(float lat, float lng, Date date, final FetchListener listener) {
        Call<SunriseResult> call = mImpl.fetchData(lat, lng, date, new Callback<SunriseResult>() {
            @Override
            public void onResponse(Call<SunriseResult> call, Response<SunriseResult> response) {
                if (call.isCanceled()) {
                    return;
                }

                SunriseResult result = response.body();

                if (result != null) {
                    SunriseData data = result.results;
                    listener.onResult(new SunData(data.sunrise, data.solar_noon, data.sunset), null);
                } else {
                    listener.onResult(SunData.empty(), new Exception("Can't load sun data"));
                }

                mCalls.remove(call);
            }

            @Override
            public void onFailure(Call<SunriseResult> call, Throwable t) {
                if (call.isCanceled()) {
                    return;
                }

                listener.onResult(SunData.empty(), t);
                mCalls.remove(call);
            }
        });

        mCalls.add(call);
    }


    @Override
    public void stopCalls() {
        for (Call<SunriseResult> call : new ArrayList<>(mCalls)) {
            call.cancel();
        }

        mCalls.clear();
    }
}
