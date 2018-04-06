package gv_fiqst.teamvoytestsunrise.retrofit;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public interface RetrofitManager {
    <T> T getApi(Class<T> cls);

    Factory DEFAULT = new Factory() {
        @Override
        public Retrofit build(String baseUrl) {
            return new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(baseUrl)
                    .build();
        }
    };

    interface Factory {
        Retrofit build(String baseUrl);
    }

    class Creator {
        public static RetrofitManager create(String baseUrl, Factory factory) {
            return RetrofitManagerImpl.create(baseUrl, factory);
        }

        private Creator() {
        }
    }
}
