package com.azra2.common;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YMRetrofit {

    private static Retrofit     retrofit;
    private static final String     BASE_URL = "https://azra.sabayawork.com/api/";
//    public static final String      IMAGE_URL = "http://3azra2.sabayawork.com/uploads/avatar/";

//    private static final String     BASE_URL = "http://10.10.10.131/3azra2/public/api/";
//    public static final String      IMAGE_URL = "http://10.10.10.152/3azra2/public/uploads/avatar/";
//    private static final String     BASE_URL="http://10.10.10.152:8000/api/";
//    public static final String      IMAGE_URL = "http://10.10.10.152:8000/uploads/avatar/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
