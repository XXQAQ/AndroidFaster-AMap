package com.xq.androidfaster_map.bean.entity;

import com.xq.androidfaster_map.bean.behavior.MarkBehavior;

public class AddressBean implements MarkBehavior{

    private String address;
    private double latitude;
    private double longitude;
    private Object tag;

    public AddressBean() {
    }

    public AddressBean(String address, double latitude, double longitude, Object tag) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddressBean that = (AddressBean) o;

        if (Double.compare(that.latitude, latitude) != 0) return false;
        if (Double.compare(that.longitude, longitude) != 0) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        return tag != null ? tag.equals(that.tag) : that.tag == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = address != null ? address.hashCode() : 0;
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AddressBean{" +
                "address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", tag=" + tag +
                '}';
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public String getTitle() {
        return address;
    }

    @Override
    public String getLittleTitle() {
        return null;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }



}
