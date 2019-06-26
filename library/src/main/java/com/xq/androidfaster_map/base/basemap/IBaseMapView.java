package com.xq.androidfaster_map.base.basemap;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.Projection;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.AlphaAnimation;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.Path;
import com.amap.api.services.route.RidePath;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.xq.androidfaster.base.abs.AbsViewDelegate;
import com.xq.androidfaster.base.abs.IAbsView;
import com.xq.androidfaster.util.tools.ScreenUtils;
import com.xq.androidfaster_map.bean.behavior.MarkerBehavior;
import com.xq.androidfaster_map.util.amap.overlay.BusRouteOverlay;
import com.xq.androidfaster_map.util.amap.overlay.DrivingRouteOverlay;
import com.xq.androidfaster_map.util.amap.overlay.PoiOverlay;
import com.xq.androidfaster_map.util.amap.overlay.RideRouteOverlay;
import com.xq.androidfaster_map.util.amap.overlay.RouteOverlay;
import com.xq.androidfaster_map.util.amap.overlay.WalkRouteOverlay;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public interface IBaseMapView<T extends IBaseMapPresenter> extends IAbsMapView<T> {

    @Override
    default void setMarkers(List list){
        getMapDelegate().setMarkers(list);
    }

    @Override
    default void setDifferentMarkers(final List list){
        getMapDelegate().setDifferentMarkers(list);
    }

    @Override
    default void setDifferentMarkers(final List list, boolean isAppend) {
        getMapDelegate().setDifferentMarkers(list,isAppend);
    }

    @Override
    default void removeMarkers(final List list) {
        getMapDelegate().removeMarkers(list);
    }

    @Override
    default void clearMarkers(){
        getMapDelegate().clearMarkers();
    }

    @Override
    default void clearMap() {
        getMapDelegate().clearMap();
    }

    @Override
    default void walkRoute(double[][] position) {
        getMapDelegate().walkRoute(position);
    }

    @Override
    default void trafficRoute(double[][] position, String city) {
        getMapDelegate().trafficRoute(position,city);
    }

    @Override
    default void driverRoute(double[][] position) {
        getMapDelegate().driverRoute(position);
    }

    @Override
    default void regionPoi(String keyWord, String city, int page) {
        getMapDelegate().regionPoi(keyWord,city,page);
    }

    @Override
    default void nearbyPoi(String keyWord, double[]position, int radius, int page) {
        getMapDelegate().nearbyPoi(keyWord,position,radius,page);
    }

    @Override
    default void removeLastRoute() {
        getMapDelegate().removeLastRoute();
    }

    @Override
    default void removeLastPoi() {
        getMapDelegate().removeLastPoi();
    }

    @Override
    default void hideInfoWindow() {
        getMapDelegate().hideInfoWindow();
    }

    @Override
    default void moveMapToArea(double[][] position){
        getMapDelegate().moveMapToArea(position);
    }

    @Override
    default void moveMapToPoint(double[] poition){
        getMapDelegate().moveMapToPoint(poition);
    }

    @Override
    default void moveMapToPoint(double[] position, int scale) {
        getMapDelegate().moveMapToPoint(position,scale);
    }

    @Override
    default void moveMapToLocationPoint(){
        getMapDelegate().moveMapToLocationPoint();
    }

    @Override
    default void moveMapToLocationPoint(int scale) {
        getMapDelegate().moveMapToLocationPoint(scale);
    }

    @Override
    default void zoomMap(int scale) {
        getMapDelegate().zoomMap(scale);
    }

    @Override
    default double[][] getMapArea() {
        return getMapDelegate().getMapArea();
    }

    @Override
    default double[] getMapCenter(){
        return getMapDelegate().getMapCenter();
    }

    public MapDelegate getMapDelegate();

    public abstract class MapDelegate<T extends IBaseMapPresenter> extends AbsViewDelegate<T> implements IAbsMapView<T> {

        public static int MARKERANIMATE_DURATION = 500;

        public TextureMapView mapView ;

        public AMap map;

        public CopyOnWriteArrayList<Marker> list_marker = new CopyOnWriteArrayList<>();
        public Marker lastMarker;
        public RouteOverlay lastRouteOverlay;
        public PoiOverlay lastPoiOverlay;

        protected RouteSearch routeSearch;

        public MapDelegate(IAbsView view) {
            super(view);
        }

        @Override
        public void create(Bundle savedInstanceState) {
            super.create(savedInstanceState);

            mapView = (TextureMapView) getRootView().findViewById(getContext().getResources().getIdentifier("mapView", "id", getContext().getPackageName()));

            mapView.onCreate(savedInstanceState);

            map = mapView.getMap();

            initMapView();
        }

        @Override
        public void visible() {
            super.visible();
            mapView.onResume();
        }

        @Override
        public void invisible() {
            super.invisible();
            mapView.onPause();
        }

        @Override
        public void destroy() {
            super.destroy();
            mapView.onDestroy();
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            mapView.onSaveInstanceState(outState);
        }

        protected void initMapView(){

            //定位点初始化
            MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(getLocationIcon()));
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
            myLocationStyle.interval(1000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
            myLocationStyle.strokeColor(Color.TRANSPARENT);// 设置圆形的边框颜色
            myLocationStyle.radiusFillColor(getLocationRadiusColor());// 设置圆形的填充颜色
            map.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
            map.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

            //map相关设置
            AMap.InfoWindowAdapter adapter = new AMap.ImageInfoWindowAdapter() {
                @Override
                public long getInfoWindowUpdateTime() {
                    return 0;
                }

                @Override
                public View getInfoWindow(Marker marker) {
                    return getWindowView(marker);
                }

                @Override
                public View getInfoContents(Marker marker) {
                    return null;
                }
            };
            map.setInfoWindowAdapter(adapter);

            //map相关监听
            map.setOnMapClickListener(new AMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    afterMapClick(new double[]{latLng.latitude,latLng.longitude});
                }
            });

            map.setOnMapLongClickListener(new AMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    afterMapLongClick(new double[]{latLng.latitude,latLng.longitude});
                }
            });

            map.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    lastMarker = marker;

                    afterMarkerClick(marker);

                    marker.showInfoWindow();

                    return true;
                }
            });

            map.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {

                }

                @Override
                public void onCameraChangeFinish(CameraPosition cameraPosition) {
                    afterMapStatusChangeFinish(cameraPosition);
                }
            });

            routeSearch = new RouteSearch(getContext());
            routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
                @Override
                public void onBusRouteSearched(BusRouteResult result, int errorCode) {
                    onRouteSearch(result,errorCode);
                }

                @Override
                public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
                    onRouteSearch(result,errorCode);
                }

                @Override
                public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
                    onRouteSearch(result,errorCode);
                }

                @Override
                public void onRideRouteSearched(RideRouteResult result, int errorCode) {
                    onRouteSearch(result,errorCode);
                }

                public void onRouteSearch(RouteResult result, int errorCode){

                    List list_path = null;
                    if (result instanceof WalkRouteResult)
                        list_path = ((WalkRouteResult) result).getPaths();
                    else    if (result instanceof BusRouteResult)
                        list_path = ((BusRouteResult) result).getPaths();
                    else    if (result instanceof DriveRouteResult)
                        list_path = ((DriveRouteResult) result).getPaths();
                    else    if (result instanceof RideRouteResult)
                        list_path = ((RideRouteResult) result).getPaths();

                    if (result != null && list_path != null && list_path.size() > 0)
                    {
                        final Path path = (Path) list_path.get(0);

                        RouteOverlay overlay = null;
                        if (result instanceof WalkRouteResult)
                            overlay = new WalkRouteOverlay(getContext(), map, (WalkPath) path, result.getStartPos(), result.getTargetPos());
                        else    if (result instanceof BusRouteResult)
                            overlay = new BusRouteOverlay(getContext(), map, (BusPath) path, result.getStartPos(), result.getTargetPos());
                        else    if (result instanceof DriveRouteResult)
                            overlay = new DrivingRouteOverlay(getContext(), map, (DrivePath) path, result.getStartPos(), result.getTargetPos(),null);
                        else    if (result instanceof RideRouteResult)
                            overlay = new RideRouteOverlay(getContext(), map, (RidePath) path, result.getStartPos(), result.getTargetPos());

                        overlay.addToMap();
                        overlay.zoomToSpan();

                        lastRouteOverlay = overlay;

                        afterGetRouteFinish(result,true);
                    }
                    else
                    {
                        afterGetRouteFinish(result,false);
                    }
                }
            });
        }

        @Override
        public void setMarkers(List list){

            List<Marker> list_newMarker = new LinkedList<>();

            for (Object o : list)
            {
                MarkerBehavior behavior = (MarkerBehavior) o;
                MarkerOptions markerOption = new MarkerOptions();
                markerOption.period(200);
                markerOption.position(new LatLng(behavior.getLatitude(),behavior.getLongitude()));
                if (!TextUtils.isEmpty(behavior.getTitle()))    markerOption.title(behavior.getTitle().toString());
                markerOption.draggable(false);//设置Marker可拖动
                markerOption.icons(getMarkerDescriptor(behavior));
                markerOption.setFlat(false);//设置marker平贴地图效果

                Marker marker = map.addMarker(markerOption);
                marker.setObject(behavior);

                Animation animation = new ScaleAnimation(0,1,0,1);
                animation.setDuration(MARKERANIMATE_DURATION);
                animation.setInterpolator(new AccelerateInterpolator());
                marker.setAnimation(animation);
                marker.startAnimation();

                list_newMarker.add(marker);
            }

            list_marker.addAll(list_newMarker);
        }

        @Override
        public void setDifferentMarkers(List list) {
            setDifferentMarkers(list,false);
        }

        @Override
        public void setDifferentMarkers(final List list, boolean isAppend){

            final List<MarkerBehavior> list_old = new LinkedList<>();
            final List<MarkerBehavior> list_remove = new LinkedList<>();
            final List<MarkerBehavior> list_newAdd = new LinkedList<>();

            for (Marker marker : list_marker)
            {
                MarkerBehavior behavior = (MarkerBehavior) marker.getObject();
                list_old.add(behavior);
            }

            //遍历所有旧MarkerBehavior，当发现旧marker在新list中不存在的时候，则在标记到删除集合中
            for (MarkerBehavior behavior : list_old)
            {
                if (!list.contains(behavior))
                {
                    list_remove.add(behavior);
                }
            }
            if (!isAppend)
                removeMarkers(list_remove);

            //遍历所有新list的MarkerBehavior，只要该marker未添加到地图上，则标记到添加集合中
            for (Object o : list)
            {
                if (!list_old.contains(o))
                {
                    list_newAdd.add((MarkerBehavior) o);
                }
            }

            setMarkers(list_newAdd);
        }

        @Override
        public void removeMarkers(final List list) {

            List<Marker> list_remove = new LinkedList();
            for (Marker marker : list_marker)
            {
                if (list.contains(marker.getObject()))
                    list_remove.add(marker);
            }
            reallyRemoveMarks(list_remove);
        }

        @Override
        public void clearMarkers(){
            reallyRemoveMarks(list_marker);
        }

        protected void reallyRemoveMarks(final List<Marker> list) {

            for (Marker marker : list)
            {
                Animation animation = new AlphaAnimation(1,0);
                animation.setDuration(MARKERANIMATE_DURATION);
                animation.setInterpolator(new LinearInterpolator());
                marker.setAnimation(animation);
                marker.startAnimation();
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (final Marker marker : list)
                    {
                        marker.remove();
                    }
                }
            },MARKERANIMATE_DURATION);

            list_marker.removeAll(list);
        }

        @Override
        public void clearMap() {
            map.clear(true);
            lastMarker = null;
            list_marker.clear();
        }

        @Override
        public void walkRoute(double[][] position) {
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(new RouteSearch.FromAndTo(new LatLonPoint(position[0][0],position[0][1]),new LatLonPoint(position[1][0],position[1][1])));
            routeSearch.calculateWalkRouteAsyn(query);
        }

        @Override
        public void trafficRoute(double[][] position, String city) {
            RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(new RouteSearch.FromAndTo(new LatLonPoint(position[0][0],position[0][1]),new LatLonPoint(position[1][0],position[1][1])), RouteSearch.BUS_DEFAULT, city,1);
            routeSearch.calculateBusRouteAsyn(query);
        }

        @Override
        public void driverRoute(double[][] position) {
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(new RouteSearch.FromAndTo(new LatLonPoint(position[0][0],position[0][1]),new LatLonPoint(position[1][0],position[1][1])), RouteSearch.WALK_DEFAULT, null, null, "");
            routeSearch.calculateDriveRouteAsyn(query);
        }

        @Override
        public void regionPoi(String keyWord, String city, int page){

            PoiSearch.Query query = new PoiSearch.Query(keyWord, "", city);
            query.setPageSize(10);
            query.setPageNum(page);

            PoiSearch poiSearch = new PoiSearch(getContext(), query);
            poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
                @Override
                public void onPoiSearched(PoiResult poiResult, int i) {
                    List<PoiItem> poiItems = poiResult.getPois();
                    if (poiItems != null && poiItems != null && poiItems.size() > 0)
                    {
                        PoiOverlay poiOverlay = new PoiOverlay(map, poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();

                        lastPoiOverlay = poiOverlay;

                        afterGetPoiFinish(poiResult,true);
                    }
                    else
                    {
                        afterGetPoiFinish(poiResult,false);
                    }
                }

                @Override
                public void onPoiItemSearched(PoiItem poiItem, int i) {

                }
            });
            poiSearch.searchPOIAsyn();
        }

        @Override
        public void nearbyPoi(String keyWord, double[]position, int radius, int page) {

            PoiSearch.Query query = new PoiSearch.Query(keyWord, "", "");
            query.setPageSize(10);
            query.setPageNum(page);

            PoiSearch poiSearch = new PoiSearch(getContext(), query);
            poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(position[0],position[1]),radius));
            poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
                @Override
                public void onPoiSearched(PoiResult poiResult, int i) {
                    List<PoiItem> poiItems = poiResult.getPois();
                    if (poiItems != null && poiItems != null && poiItems.size() > 0)
                    {
                        PoiOverlay poiOverlay = new PoiOverlay(map, poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();

                        lastPoiOverlay = poiOverlay;

                        afterGetPoiFinish(poiResult,true);
                    }
                    else
                    {
                        afterGetPoiFinish(poiResult,false);
                    }
                }

                @Override
                public void onPoiItemSearched(PoiItem poiItem, int i) {

                }
            });
            poiSearch.searchPOIAsyn();
        }

        @Override
        public void removeLastRoute() {
            if (lastRouteOverlay != null)
            {
                lastRouteOverlay.removeFromMap();
                lastRouteOverlay = null;
            }
        }

        @Override
        public void removeLastPoi(){
            if (lastPoiOverlay != null)
            {
                lastPoiOverlay.removeFromMap();
                lastPoiOverlay = null;
            }
        }

        @Override
        public void hideInfoWindow() {
            if (lastMarker != null)
            {
                lastMarker.hideInfoWindow();
            }
        }

        @Override
        public void moveMapToArea(double[][] position){

            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            for(int i=0;i<position.length;i++)
                boundsBuilder.include(new LatLng(position[i][0],position[i][1]));

            int padding = ScreenUtils.dip2px( 100);
            map.animateCamera(CameraUpdateFactory.newLatLngBounds( boundsBuilder.build(),padding));
        }

        @Override
        public void moveMapToPoint(double[] position){
            map.animateCamera(CameraUpdateFactory.changeLatLng(new LatLng(position[0],position[1])));
        }

        @Override
        public void moveMapToPoint(double[] position, int scale) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(position[0],position[1]),scaleToZoom(scale)));
        }

        @Override
        public void moveMapToLocationPoint(){
            if (getBindPresenter().getLocation() != null)
                moveMapToPoint(new double[]{getBindPresenter().getLocation().getLatitude(),getBindPresenter().getLocation().getLongitude()});
            else
                afterGetLocationErro();
        }

        @Override
        public void moveMapToLocationPoint(int scale) {
            if (getBindPresenter().getLocation() != null)
                moveMapToPoint(new double[]{getBindPresenter().getLocation().getLatitude(),getBindPresenter().getLocation().getLongitude()},scale);
            else
                afterGetLocationErro();
        }

        @Override
        public void zoomMap(int scale) {
            map.animateCamera(CameraUpdateFactory.zoomTo(scaleToZoom(scale)));
        }

        //缩放换算
        protected int scaleToZoom(int scale) {
            if (scale <= 10) return 19;
            else if (scale <= 25) return 18;
            else if (scale <= 50) return 17;
            else if (scale <= 100) return 16;
            else if (scale <= 200) return 15;
            else if (scale <= 500) return 14;
            else if (scale <= 1000) return 13;
            else if (scale <= 2000) return 12;
            else if (scale <= 5000) return 11;
            else if (scale <= 10000) return 10;
            else if (scale <= 20000) return 9;
            else if (scale <= 30000) return 8;
            else if (scale <= 50000) return 7;
            else if (scale <= 100000) return 6;
            else if (scale <= 200000) return 5;
            else if (scale <= 500000) return 4;
            else if (scale <= 1000000) return 3;
            else if (scale > 1000000) return 2;
            return 20;
        }

        @Override
        public double[][] getMapArea() {

            Projection projection = map.getProjection();

            LatLng topLeft = projection.fromScreenLocation(new Point(0,0));
            LatLng bottomRight = projection.fromScreenLocation(new Point(mapView.getWidth(),mapView.getHeight()));

            return new double[][]{new double[]{topLeft.latitude,topLeft.longitude},new double[]{bottomRight.latitude,bottomRight.longitude}};
        }

        @Override
        public double[] getMapCenter(){
            LatLng latLng = map.getCameraPosition().target;
            return new double[]{latLng.latitude,latLng.longitude};
        }

        //重写该方法处理定位失败后逻辑
        public abstract void afterGetLocationErro();

        //重写该方法返回定位点图标
        protected abstract int getLocationIcon();

        //重写该方法返回定位圆圈颜色
        protected abstract int getLocationRadiusColor();

        //重写该方法返回Marker样式
        protected abstract ArrayList<BitmapDescriptor> getMarkerDescriptor(MarkerBehavior behavior);

        //重写该方法返回弹窗样式
        protected abstract View getWindowView(Marker marker);

        //标记点击后调用
        protected abstract void afterMarkerClick(Marker marker);

        //地图状态改变后调用
        protected abstract void afterMapStatusChangeFinish(CameraPosition cameraPosition);

        //点击地图后调用
        protected abstract void afterMapClick(double[] position);

        //长按地图后调用
        protected abstract void afterMapLongClick(double[] position);

        //路线规划结束后调用
        protected abstract void afterGetRouteFinish(RouteResult result,boolean isSuccess);

        //兴趣点搜索结束后调用
        protected abstract void afterGetPoiFinish(PoiResult result,boolean isSuccess);

    }

}