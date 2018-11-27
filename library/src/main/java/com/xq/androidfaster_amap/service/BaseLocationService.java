package com.xq.androidfaster_amap.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.xq.androidfaster.FasterInterface;
import com.xq.androidfaster.util.tools.BundleUtil;

public class BaseLocationService extends Service {

    public static final String ACTION_LOCATION = "com.xq.androidfaster_amap.service.BaseLocationService";

    public AMapLocationClient locationClient;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationClient != null)
            locationClient.onDestroy();
    }

    public void startLocation(){
        //定位
        locationClient = new AMapLocationClient(FasterInterface.getApp());
        locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation location) {
                Intent intent = new Intent();
                intent.setAction(ACTION_LOCATION);
                intent.putExtras(new BundleUtil.Builder().putParcelable(BundleUtil.KEY_DATA,location).build());
                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent) ;
            }
        });
        AMapLocationClientOption clientOption = new AMapLocationClientOption();
        clientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        clientOption.setInterval(1000);
        clientOption.setNeedAddress(true);
        clientOption.setMockEnable(true);
        locationClient.setLocationOption(clientOption);
        locationClient.startLocation();
    }

}
