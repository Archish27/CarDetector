package com.markdevelopers.cardetector.activities;

import com.markdevelopers.cardetector.common.BaseContract;
import com.markdevelopers.cardetector.data.remote.models.VerifyCar;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Archish on 3/24/2017.
 */

public interface CarDetectorContract {
    interface CarDetectorView extends BaseContract.BaseView {
        void onResponse(VerifyCar verifyCar);
    }

    interface CarDetectorPresenter {
        void sendData(String data);

        void sendData(String data, String name, String state, MultipartBody.Part image, RequestBody requestBody);
    }
}
