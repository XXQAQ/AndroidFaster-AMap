package com.xq.androidfaster_map.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.xq.androidfaster_map.bean.behavior.MarkerBehavior;
import com.xq.worldbean.bean.entity.base.BaseTitleBean;

public class MarkerBean extends BaseTitleBean implements MarkerBehavior, Parcelable {

    protected double latitude;
    protected double longitude;

    public MarkerBean() {
    }

    public MarkerBean(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public MarkerBean(double latitude, double longitude, CharSequence title) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
    }

    public MarkerBean(double latitude, double longitude, CharSequence title,Object tag) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.tag = tag;
    }

    public MarkerBean(String id,double latitude, double longitude, CharSequence title,Object tag) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.tag = tag;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest,flags);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }

    protected MarkerBean(Parcel in) {
        super(in);
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static final Creator<MarkerBean> CREATOR = new Creator<MarkerBean>() {
        @Override
        public MarkerBean createFromParcel(Parcel source) {
            return new MarkerBean(source);
        }

        @Override
        public MarkerBean[] newArray(int size) {
            return new MarkerBean[size];
        }
    };
}
