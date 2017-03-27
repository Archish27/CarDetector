package com.markdevelopers.cardetector.data.remote.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Archish on 3/26/2017.
 */

public class Information implements Parcelable {
    String data, state, name, image, time;

    public Information(String data, String state, String name, String image, String time) {
        this.data = data;
        this.state = state;
        this.name = name;
        this.image = image;
        this.time = time;
    }

    protected Information(Parcel in) {
        data = in.readString();
        state = in.readString();
        name = in.readString();
        image = in.readString();
        time = in.readString();
    }

    public static final Creator<Information> CREATOR = new Creator<Information>() {
        @Override
        public Information createFromParcel(Parcel in) {
            return new Information(in);
        }

        @Override
        public Information[] newArray(int size) {
            return new Information[size];
        }
    };

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(data);
        parcel.writeString(state);
        parcel.writeString(name);
        parcel.writeString(image);
        parcel.writeString(time);
    }
}
