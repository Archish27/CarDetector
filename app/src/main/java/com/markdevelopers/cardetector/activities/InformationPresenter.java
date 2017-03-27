package com.markdevelopers.cardetector.activities;

import com.markdevelopers.cardetector.data.remote.UserRestService;
import com.markdevelopers.cardetector.data.remote.models.InformationWrapper;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Archish on 3/26/2017.
 */

public class InformationPresenter implements InformationContract.InformationPresenter {

    InformationContract.InformationView view;
    UserRestService userRestService;

    public InformationPresenter(InformationContract.InformationView view) {
        this.view = view;
        userRestService = UserRestService.getInstance();
    }

    @Override
    public void getData() {
        userRestService.getApi().
                getData().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<InformationWrapper>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view != null)
                            view.onNetworkException(e);
                    }

                    @Override
                    public void onNext(InformationWrapper informationWrapper) {
                        if (view != null)
                            view.onResponse(informationWrapper);
                    }
                });
    }
}
