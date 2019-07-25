package com.xq.androidfaster_map.base.baselocation;

import android.location.Location;

import com.xq.androidfaster.base.core.Controler;

public interface IBaseLocationBehavior<T extends Controler> extends Controler<T> {

    //开始定位
    public void startLocation();

    //获取定位
    public Location getLocation();

    //是否第一次定位
    public boolean isFirstLocation();

}
