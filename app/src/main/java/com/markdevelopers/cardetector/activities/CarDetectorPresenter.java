package com.markdevelopers.cardetector.activities;

import com.markdevelopers.cardetector.data.remote.UserRestService;
import com.markdevelopers.cardetector.data.remote.models.VerifyCar;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Archish on 3/26/2017.
 */

public class CarDetectorPresenter implements CarDetectorContract.CarDetectorPresenter {

    CarDetectorContract.CarDetectorView view;
    UserRestService userRestService;

    public CarDetectorPresenter(CarDetectorContract.CarDetectorView view) {
        this.view = view;
        userRestService = UserRestService.getInstance();
    }

    @Override
    public void sendData(String data) {
        userRestService.getApi()
                .sendData(data)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<VerifyCar>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view != null)
                            view.onNetworkException(e);
                    }

                    @Override
                    public void onNext(VerifyCar verifyCar) {
                        if (view != null)
                            view.onResponse(verifyCar);
                    }
                });
    }

    @Override
    public void sendData(String data, String name, String state, MultipartBody.Part part, RequestBody requestBody) {
        userRestService.getApi()
                .sendData(data, name, state, part, requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<VerifyCar>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view != null)
                            view.onNetworkException(e);
                    }

                    @Override
                    public void onNext(VerifyCar verifyCar) {
                        if (view != null)
                            view.onResponse(verifyCar);
                    }
                });
    }
}
