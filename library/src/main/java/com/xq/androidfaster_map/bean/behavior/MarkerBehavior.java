package com.xq.androidfaster_map.bean.behavior;

import com.xq.worldbean.bean.behavior.BaseBehavior;
import com.xq.worldbean.bean.behavior.CoordinateBehavior;
import com.xq.worldbean.bean.behavior.TitleBehavior;

public interface MarkerBehavior extends BaseBehavior, CoordinateBehavior, TitleBehavior {

    @Override
    default double getX() {
        return getLongitude();
    }

    @Override
    default double getY() {
        return getLatitude();
    }

    @Override
    default double getZ() {
        return 0;
    }

    public double getLatitude();

    default void setLatitude(double latitude) {
    }

    public double getLongitude();

    default void setLongitude(double longitude) {
    }

}
