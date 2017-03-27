package com.markdevelopers.cardetector.data.remote;

import com.markdevelopers.cardetector.common.Config;
import com.markdevelopers.cardetector.data.remote.api.UserRestAPI;
import com.markdevelopers.cardetector.network.RxErrorHandlingCallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Archish on 3/26/2017.
 */

public class UserRestService {

    UserRestAPI restAPI;

    public UserRestService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .build();
        restAPI = retrofit.create(UserRestAPI.class);
    }

    public UserRestAPI getApi() {
        return restAPI;
    }

    public static UserRestService getInstance() {
        return new UserRestService();
    }
}
