package com.markdevelopers.cardetector.data.remote.api;

import com.markdevelopers.cardetector.data.remote.models.InformationWrapper;
import com.markdevelopers.cardetector.data.remote.models.VerifyCar;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Archish on 3/26/2017.
 */

public interface UserRestAPI {

    @FormUrlEncoded
    @POST("verify.php")
    Observable<VerifyCar> sendData(@Field("data") String data);

    @Multipart
    @POST("insert_data.php")
    Observable<VerifyCar> sendData(@Query("data") String data
            , @Query("name") String name
            , @Query("state") String state
            , @Part MultipartBody.Part image, @Part("image") RequestBody postimage
    );

    @GET("information.php")
    Observable<InformationWrapper> getData();

}
