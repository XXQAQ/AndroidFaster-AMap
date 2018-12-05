package com.xq.androidfaster_map.basemap;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
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
import com.xq.androidfaster_map.bean.behavior.MarkBehavior;
import com.xq.androidfaster_map.util.overlay.BusRouteOverlay;
import com.xq.androidfaster_map.util.overlay.DrivingRouteOverlay;
import com.xq.androidfaster_map.util.overlay.PoiOverlay;
import com.xq.androidfaster_map.util.overlay.RideRouteOverlay;
import com.xq.androidfaster_map.util.overlay.RouteOverlay;
import com.xq.androidfaster_map.util.overlay.WalkRouteOverlay;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public interface IBaseMapView<T extends IBaseMapPresenter> extends IAbsMapView<T> {

    @Override
    default void setMarks(List<MarkBehavior> list){
        getMapDelegate().setMarks(list);
    }

    @Override
    default void setDifferentMarks(final List<MarkBehavior> list){
        getMapDelegate().setDifferentMarks(list);
    }

    @Override
    default void setDifferentMarks(final List<MarkBehavior> list, boolean isAppend) {
        getMapDelegate().setDifferentMarks(list,isAppend);
    }

    @Override
    default void removeMarks(final List<MarkBehavior> list) {
        getMapDelegate().removeMarks(list);
    }

    @Override
    default void clearMarkes(){
        getMapDelegate().clearMarkes();
    }

    @Override
    default void clearMap() {
        getMapDelegate().clearMap();
    }

    @Override
    default void walk(double[][] position) {
        getMapDelegate().walk(position);
    }

    @Override
    default void traffic(double[][] position, String city) {
        getMapDelegate().traffic(position,city);
    }

    @Override
    default void driver(double[][] position) {
        getMapDelegate().driver(position);
    }

    @Override
    default void poi(String keyWord, String city, int page) {
        getMapDelegate().poi(keyWord,city,page);
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
    default void moveMapToLocationPoint(){
        getMapDelegate().moveMapToLocationPoint();
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

        public RouteSearch routeSearch;

        public RouteOverlay lastRouteOverlay;
        public PoiOverlay lastPoiOverlay;

        public MapDelegate(IAbsView view) {
            super(view);
        }

        @Override
        public void afterOnCreate(Bundle savedInstanceState) {
            super.afterOnCreate(savedInstanceState);

            mapView = (TextureMapView) getRootView().findViewById(getContext().getResources().getIdentifier("mapView", "id", getContext().getPackageName()));

            mapView.onCreate(savedInstanceState);

            map = mapView.getMap();

            initMapView();
        }

        @Override
        public void onResume() {
            super.onResume();
            mapView.onResume();
        }

        @Override
        public void onPause() {
            super.onPause();
            mapView.onPause();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
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
                    MarkBehavior behavior = (MarkBehavior) marker.getObject();
                    return getWindowView(behavior);
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
                    afterMapClick(latLng.latitude,latLng.longitude);
                }
            });

            map.setOnMapLongClickListener(new AMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    afterMapLongClick(latLng.latitude,latLng.longitude);
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

                        overlay.removeFromMap();
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
        public void setMarks(List<MarkBehavior> list){

            List<Marker> list_mark = new LinkedList<>();

            for (MarkBehavior behavior : list)
            {
                MarkerOptions markerOption = new MarkerOptions();
                markerOption.period(200);
                markerOption.position(new LatLng(behavior.getLatitude(),behavior.getLongitude()));
                markerOption.title(behavior.getTitle()).snippet(behavior.getLittleTitle());
                markerOption.draggable(false);//设置Marker可拖动
                markerOption.icons(getMarkerDescript(behavior));
                markerOption.setFlat(false);//设置marker平贴地图效果

                Marker marker = map.addMarker(markerOption);
                marker.setObject(behavior);

                Animation animation = new ScaleAnimation(0,1,0,1);
                animation.setDuration(MARKERANIMATE_DURATION);
                animation.setInterpolator(new AccelerateInterpolator());
                marker.setAnimation(animation);
                marker.startAnimation();

                list_mark.add(marker);
            }

            list_marker.addAll(list_mark);
        }

        @Override
        public void setDifferentMarks(List<MarkBehavior> list) {
            setDifferentMarks(list,false);
        }

        @Override
        public void setDifferentMarks(final List<MarkBehavior> list,boolean isAppend){

            final List<MarkBehavior> list_new = new LinkedList<>();
            final List<MarkBehavior> list_newCopy = new LinkedList<>();
            final List<MarkBehavior> list_remove = new LinkedList<>();

            //遍历所有旧Marker，当发现旧marker在新集合中不存在的时候，则在原集合中删除且标记到删除集合中
            for (Marker marker : list_marker)
            {
                MarkBehavior markBehavior = (MarkBehavior) marker.getObject();
                if (!list.contains(markBehavior))
                {
                    list_remove.add(markBehavior);
                }
            }

            if (!isAppend)
                removeMarks(list_remove);

            for (Marker marker : list_marker)
            {
                MarkBehavior markBehavior = (MarkBehavior) marker.getObject();
                list_newCopy.add(markBehavior);
            }

            //遍历所有新markerBehavior，只要该marker未添加到地图上，则将Marker标记到新集合中
            for (MarkBehavior new_markBehavior : list)
            {
                if (!list_newCopy.contains(new_markBehavior))
                {
                    list_new.add(new_markBehavior);
                }
            }

            setMarks(list_new);
        }

        @Override
        public void removeMarks(final List<MarkBehavior> list) {

            List<Marker> list_remove = new LinkedList();
            for (Marker marker : list_marker)
            {
                if (list.contains(marker.getObject()))
                    list_remove.add(marker);
            }
            reallyRemoveMarks(list_remove);
        }

        @Override
        public void clearMarkes(){
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
        public void walk(double[][] position) {
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(new RouteSearch.FromAndTo(new LatLonPoint(position[0][0],position[0][1]),new LatLonPoint(position[1][0],position[1][1])));
            routeSearch.calculateWalkRouteAsyn(query);
        }

        @Override
        public void traffic(double[][] position,String city) {
            RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(new RouteSearch.FromAndTo(new LatLonPoint(position[0][0],position[0][1]),new LatLonPoint(position[1][0],position[1][1])), RouteSearch.BUS_DEFAULT, city,1);
            routeSearch.calculateBusRouteAsyn(query);
        }

        @Override
        public void driver(double[][] position) {
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(new RouteSearch.FromAndTo(new LatLonPoint(position[0][0],position[0][1]),new LatLonPoint(position[1][0],position[1][1])), RouteSearch.WALK_DEFAULT, null, null, "");
            routeSearch.calculateDriveRouteAsyn(query);
        }

        @Override
        public void poi(String keyWord,String city,int page){

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

            map.animateCamera(CameraUpdateFactory.newLatLngBounds( boundsBuilder.build(),15));
        }

        @Override
        public void moveMapToPoint(double[] position){
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(position[0],position[1]),15));
        }

        @Override
        public void moveMapToLocationPoint(){
            if (getPresenter().getLocation() != null)
            {
                moveMapToPoint(new double[]{getPresenter().getLocation().getLatitude(),getPresenter().getLocation().getLongitude()});
            }
            else
            {
                afterGetLocationErro();
            }
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
        protected abstract void afterGetLocationErro();

        //重写该方法返回定位点图标
        protected abstract int getLocationIcon();

        //重写该方法返回定位圆圈颜色
        protected abstract int getLocationRadiusColor();

        //重写该方法返回Marker样式
        protected abstract ArrayList<BitmapDescriptor> getMarkerDescript(MarkBehavior behavior);

        //重写该方法返回弹窗样式
        protected abstract View getWindowView(MarkBehavior behavior);

        //标记点击后调用
        protected abstract void afterMarkerClick(Marker marker);

        //地图状态改变后调用
        protected abstract void afterMapStatusChangeFinish(CameraPosition cameraPosition);

        //点击地图后调用
        protected abstract void afterMapClick(double lat,double lon);

        //长按地图后调用
        protected abstract void afterMapLongClick(double lat,double lon);

        //路线规划结束后调用
        protected abstract void afterGetRouteFinish(RouteResult result,boolean isSuccess);

        //兴趣点搜索结束后调用
        protected abstract void afterGetPoiFinish(PoiResult result,boolean isSuccess);

    }

}