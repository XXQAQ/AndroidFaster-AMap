package com.xq.androidfaster_map.base.basemap;

import android.location.Location;
import com.xq.androidfaster.base.base.IFasterBaseBehavior;
import com.xq.androidfaster.base.core.Controler;
import com.xq.androidfaster_map.base.baselocation.IBaseLocationPresenter;
import java.util.List;

public interface IBaseMapPresenter extends IBaseLocationPresenter, IBaseMapBehavior {

    ///////////////////////////////////////////////////////////////////////////
    // V
    ///////////////////////////////////////////////////////////////////////////
    @Deprecated
    @Override
    default void setMarkers(List list){
        getMapDelegate().setMarkers(list);
    }

    @Deprecated
    @Override
    default void setDifferentMarkers(final List list){
        getMapDelegate().setDifferentMarkers(list);
    }

    @Deprecated
    @Override
    default void setDifferentMarkers(final List list, boolean isAppend) {
        getMapDelegate().setDifferentMarkers(list,isAppend);
    }

    @Deprecated
    @Override
    default void removeMarkers(final List list) {
        getMapDelegate().removeMarkers(list);
    }

    @Deprecated
    @Override
    default void clearMarkers(){
        getMapDelegate().clearMarkers();
    }

    @Deprecated
    @Override
    default void clearMap() {
        getMapDelegate().clearMap();
    }

    @Deprecated
    @Override
    default void walkRoute(double[][] position) {
        getMapDelegate().walkRoute(position);
    }

    @Deprecated
    @Override
    default void trafficRoute(double[][] position, String city) {
        getMapDelegate().trafficRoute(position,city);
    }

    @Deprecated
    @Override
    default void driverRoute(double[][] position) {
        getMapDelegate().driverRoute(position);
    }

    @Deprecated
    @Override
    default void regionPoi(String keyWord, String city, int page) {
        getMapDelegate().regionPoi(keyWord,city,page);
    }

    @Deprecated
    @Override
    default void nearbyPoi(String keyWord, double[]position, int radius, int page) {
        getMapDelegate().nearbyPoi(keyWord,position,radius,page);
    }

    @Deprecated
    @Override
    default void removeLastRoute() {
        getMapDelegate().removeLastRoute();
    }

    @Deprecated
    @Override
    default void removeLastPoi() {
        getMapDelegate().removeLastPoi();
    }

    @Deprecated
    @Override
    default void hideInfoWindow() {
        getMapDelegate().hideInfoWindow();
    }

    @Deprecated
    @Override
    default void moveMapToArea(double[][] position){
        getMapDelegate().moveMapToArea(position);
    }

    @Deprecated
    @Override
    default void moveMapToPoint(double[] poition){
        getMapDelegate().moveMapToPoint(poition);
    }

    @Deprecated
    @Override
    default void moveMapToPoint(double[] position, int scale) {
        getMapDelegate().moveMapToPoint(position,scale);
    }

    @Deprecated
    @Override
    default void moveMapToLocationPoint(){
        getMapDelegate().moveMapToLocationPoint();
    }

    @Deprecated
    @Override
    default void moveMapToLocationPoint(int scale) {
        getMapDelegate().moveMapToLocationPoint(scale);
    }

    @Deprecated
    @Override
    default void zoomMap(int scale) {
        getMapDelegate().zoomMap(scale);
    }

    @Deprecated
    @Override
    default double[][] getMapArea() {
        return getMapDelegate().getMapArea();
    }

    @Deprecated
    @Override
    default double[] getMapCenter(){
        return getMapDelegate().getMapCenter();
    }

    @Deprecated
    @Override
    default LocationDelegate getLocationDelegate() {
        return getMapDelegate();
    }

    public MapDelegate getMapDelegate();

    public abstract class MapDelegate extends LocationDelegate implements IBaseMapBehavior {

        public MapDelegate(Controler presenter) {
            super(presenter);
        }

        @Override
        public void onReceiveLocation(Location location) {
            super.onReceiveLocation(location);
            if (isFirstLocation())
                moveMapToLocationPoint(100);
        }



        ///////////////////////////////////////////////////////////////////////////
        // V
        ///////////////////////////////////////////////////////////////////////////
        @Deprecated
        @Override
        public void setMarkers(List list) {
            ((IBaseMapBehavior)((IFasterBaseBehavior)getControler()).getBindAnother()).setMarkers(list);
        }

        @Deprecated
        @Override
        public void setDifferentMarkers(final List list) {
            ((IBaseMapBehavior)((IFasterBaseBehavior)getControler()).getBindAnother()).setDifferentMarkers(list);
        }

        @Deprecated
        @Override
        public void setDifferentMarkers(final List list, boolean isAppend) {
            ((IBaseMapBehavior)((IFasterBaseBehavior)getControler()).getBindAnother()).setDifferentMarkers(list,isAppend);
        }

        @Deprecated
        @Override
        public void removeMarkers(final List list) {
            ((IBaseMapBehavior)((IFasterBaseBehavior)getControler()).getBindAnother()).removeMarkers(list);
        }

        @Deprecated
        @Override
        public void clearMarkers() {
            ((IBaseMapBehavior)((IFasterBaseBehavior)getControler()).getBindAnother()).clearMarkers();
        }

        @Deprecated
        @Override
        public void clearMap() {
            ((IBaseMapBehavior)((IFasterBaseBehavior)getControler()).getBindAnother()).clearMap();
        }

        @Deprecated
        @Override
        public void walkRoute(double[][] position) {
            ((IBaseMapBehavior)((IFasterBaseBehavior)getControler()).getBindAnother()).walkRoute(position);
        }

        @Deprecated
        @Override
        public void trafficRoute(double[][] position, String city) {
            ((IBaseMapBehavior)((IFasterBaseBehavior)getControler()).getBindAnother()).trafficRoute(position,city);
        }

        @Deprecated
        @Override
        public void driverRoute(double[][] position) {
            ((IBaseMapBehavior)((IFasterBaseBehavior)getControler()).getBindAnother()).driverRoute(position);
        }

        @Deprecated
        @Override
        public void regionPoi(String keyWord, String city, int page) {
            ((IBaseMapBehavior)((IFasterBaseBehavior)getControler()).getBindAnother()).regionPoi(keyWord,city,page);
        }

        @Deprecated
        @Override
        public void nearbyPoi(String keyWord, double[] position, int radius, int page) {
            ((IBaseMapBehavior)((IFasterBaseBehavior)getControler()).getBindAnother()).nearbyPoi(keyWord,position,radius,page);
        }

        @Deprecated
        @Override
        public void removeLastRoute() {
            ((IBaseMapBehavior)((IFasterBaseBehavior)getControler()).getBindAnother()).removeLastRoute();
        }

        @Deprecated
        @Override
        public void removeLastPoi() {
            ((IBaseMapBehavior)((IFasterBaseBehavior)getControler()).getBindAnother()).removeLastPoi();
        }

        @Deprecated
        @Override
        public void hideInfoWindow() {
            ((IBaseMapBehavior)((IFasterBaseBehavior)getControler()).getBindAnother()).hideInfoWindow();
        }

        @Deprecated
        @Override
        public void moveMapToArea(double[][] position) {
            ((IBaseMapBehavior)((IFasterBaseBehavior)getControler()).getBindAnother()).moveMapToArea(position);
        }

        @Deprecated
        @Override
        public void moveMapToPoint(double[] position) {
            ((IBaseMapBehavior)((IFasterBaseBehavior)getControler()).getBindAnother()).moveMapToPoint(position);
        }

        @Deprecated
        @Override
        public void moveMapToPoint(double[] position, int scale) {
            ((IBaseMapBehavior)((IFasterBaseBehavior)getControler()).getBindAnother()).moveMapToPoint(position,scale);
        }

        @Deprecated
        @Override
        public void moveMapToLocationPoint() {
            ((IBaseMapBehavior)((IFasterBaseBehavior)getControler()).getBindAnother()).moveMapToLocationPoint();
        }

        @Deprecated
        @Override
        public void moveMapToLocationPoint(int scale) {
            ((IBaseMapBehavior)((IFasterBaseBehavior)getControler()).getBindAnother()).moveMapToLocationPoint(scale);
        }

        @Deprecated
        @Override
        public void zoomMap(int scale) {
            ((IBaseMapBehavior)((IFasterBaseBehavior)getControler()).getBindAnother()).zoomMap(scale);
        }

        @Deprecated
        @Override
        public double[][] getMapArea() {
            return ((IBaseMapBehavior)((IFasterBaseBehavior)getControler()).getBindAnother()).getMapArea();
        }

        @Deprecated
        @Override
        public double[] getMapCenter() {
            return ((IBaseMapBehavior)((IFasterBaseBehavior)getControler()).getBindAnother()).getMapCenter();
        }
    }

}
