package com.markdevelopers.cardetector.activities;

import com.markdevelopers.cardetector.common.BaseContract;
import com.markdevelopers.cardetector.data.remote.models.InformationWrapper;

/**
 * Created by Archish on 3/26/2017.
 */

public interface InformationContract {
    interface  InformationView extends BaseContract.BaseView{
        void onResponse(InformationWrapper informationWrapper);
    }
    interface InformationPresenter{
        void getData();
    }
}
