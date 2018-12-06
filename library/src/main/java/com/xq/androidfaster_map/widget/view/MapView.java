package com.xq.androidfaster_map.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.TextureMapView;

public class MapView extends TextureMapView {

    public MapView(Context context) {
        super(context);
    }

    public MapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public MapView(Context context, AMapOptions aMapOptions) {
        super(context, aMapOptions);
    }

}
