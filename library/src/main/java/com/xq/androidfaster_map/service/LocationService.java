package com.xq.androidfaster_map.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.xq.androidfaster.util.tools.BundleUtil;

public class LocationService extends Service {

    public static final String ACTION_LOCATION = "com.xq.androidfaster_map.service.LocationService";

    private static Location location;

    public AMapLocationClient locationClient;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationClient = new AMapLocationClient(getBaseContext());
        locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation location) {
                LocationService.location = location;

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationClient != null)
            locationClient.onDestroy();
    }

    public static Location getLocation() {
        return location;
    }
}
