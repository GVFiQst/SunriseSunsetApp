package gv_fiqst.teamvoytestsunrise.retrofit;


import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;

class RetrofitManagerImpl implements RetrofitManager {

    public static RetrofitManager create(String baseUrl, Factory factory) {
        return new RetrofitManagerImpl(baseUrl, factory);
    }

    private Retrofit mRetrofit;
    private final Map<Class, Object> mApis = new HashMap<>();

    public RetrofitManagerImpl(String baseUrl, Factory factory) {
        mRetrofit = factory.build(baseUrl);
    }

    @Override
    public <T> T getApi(Class<T> cls) {
        T api = (T) mApis.get(cls);
        if (api == null) {
            api = mRetrofit.create(cls);
            mApis.put(cls, api);
        }

        return api;
    }
}
