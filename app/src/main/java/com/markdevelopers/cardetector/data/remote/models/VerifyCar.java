package com.markdevelopers.cardetector.data.remote.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Archish on 3/26/2017.
 */

public class VerifyCar implements Parcelable{
    boolean status;
    String message;

    public VerifyCar(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    protected VerifyCar(Parcel in) {
        status = in.readByte() != 0;
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VerifyCar> CREATOR = new Creator<VerifyCar>() {
        @Override
        public VerifyCar createFromParcel(Parcel in) {
            return new VerifyCar(in);
        }

        @Override
        public VerifyCar[] newArray(int size) {
            return new VerifyCar[size];
        }
    };

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
