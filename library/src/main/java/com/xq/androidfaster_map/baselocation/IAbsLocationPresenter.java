package com.xq.androidfaster_map.baselocation;


import android.location.Location;

import com.xq.androidfaster.base.abs.IAbsPresenter;
import com.xq.androidfaster.base.abs.IAbsView;


public interface IAbsLocationPresenter<T extends IAbsView> extends IAbsPresenter<T> {

    //开始定位
    public void startLocation();

    //获取定位
    public Location getLocation();

}
