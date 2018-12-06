package com.xq.androidfaster_map.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.TextureMapView;

public class FasterMapView extends TextureMapView {

    public FasterMapView(Context context) {
        super(context);
    }

    public FasterMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public FasterMapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public FasterMapView(Context context, AMapOptions aMapOptions) {
        super(context, aMapOptions);
    }

}
