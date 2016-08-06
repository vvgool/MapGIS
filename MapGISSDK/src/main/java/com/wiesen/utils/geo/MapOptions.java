package com.wiesen.utils.geo;


import com.wiesen.utils.MapConstants;

public class MapOptions {
    private boolean mIsCompassEnabled = false;//设置是否允许指南针，默认不允许。

    private boolean mIsOverlookingGesturesEnabled = false;//设置是否允许俯视手势，默认不允许

    private boolean mIsRotateGesturesEnabled = false;//设置是否允许旋转手势，默认不允许

    private boolean mIsScaleControlEnabled = true;//  设置是否显示比例尺控件

    private boolean mIsScrollGesturesEnabled = true;//设置是否允许拖拽手势，默认允许

    private boolean mIsZoomControlsEnabled = true;//设置是否显示缩放控件

    private boolean mIsZoomGesturesEnabled = true;// 设置是否允许缩放手势

    private MapStatus mMapStatus;//设置地图初始化时的地图状态， 默认地图中心点为北京天安门，缩放级别为 12.0

    private MapConstants.MapType mMapType = MapConstants.MapType.mapsta;//当前显示地图类型

    private boolean mDeuMapEnabled = true;//设置地图是否显示

    private boolean mIsOffOnline = true;//设置地图是否离线

    private MapStatusChanged mMapStatusChanged;

    private MapOptions() {
        mMapStatus = new MapStatus(new GeoPoint(MapConstants.Default_Longitude
                ,MapConstants.Default_Latitude),MapConstants.Default_Zoom);
    }

    public static MapOptions getInstance(){
        return InstanceHolder.mMapOptions;
    }

    private static class InstanceHolder{
        private static MapOptions mMapOptions = new MapOptions();
    }

    /**
     * 设置是否允许指南针[暂时未涉及]
     * @param enabled true 允许 false 不允许
     */
    public void compassEnabled(boolean enabled) {
        this.mIsCompassEnabled = enabled;
    }

    /**
     * 获取是否允许指南针设置值
     * @return true 允许 false 不允许
     */
    public boolean getCompassEnabled() {
        return this.mIsCompassEnabled;
    }

    /**
     * 设置当前地图中心状态
     * @param status  MapStatus 对象
     */
    public void setMapStatus(MapStatus status) {
        this.mMapStatus = status;
        if (mMapStatusChanged != null){
            mMapStatusChanged.onMapStatusChanged();
        }
    }

    /**
     * 获取当前地图中心状态对象
     * @return MapStatus 对象
     */
    public MapStatus getMapStatus() {
        return this.mMapStatus;
    }

    /**
     * 设置地图的类型:影像图  矢量图
     * @param mapType MapConstants.MAP_TYPE_NORMAL 影像图 MapConstants.MAP_TYPE_SATELLITE 矢量图
     */
    public void setMapType(MapConstants.MapType mapType) {
        this.mMapType = mapType;
        if (mMapStatusChanged != null){
            mMapStatusChanged.onMapStatusChanged();
        }
    }

    /**
     * 获取地图的类型
     * @return 获取当前地图类型
     */
    public MapConstants.MapType getMapType() {
        return this.mMapType;
    }

    /**
     * 设置是否允许俯视手势[目前未涉及]
     * @param enabled true 允许 false 不允许
     */
    public void overlookingGesturesEnabled(boolean enabled) {
        this.mIsOverlookingGesturesEnabled = enabled;
    }

    /**
     * 获取设置是否允许俯视手势
     * @return true 允许 false 不允许
     */
    public boolean getOverlookingGesturesEnabled() {
        return this.mIsOverlookingGesturesEnabled;
    }

    /**
     * 设置是否允许旋转手势
     * @param enabled true 允许 false 不允许
     */
    public void rotateGesturesEnabled(boolean enabled) {
        this.mIsRotateGesturesEnabled = enabled;
    }

    /**
     * 获取是否允许旋转手势
     * @return true 允许 false　不允许
     */
    public boolean getRotateGesturesEnabled() {
        return this.mIsRotateGesturesEnabled;
    }

    /**
     * 设置是否显示比例尺控件
     * @param enabled　true 允许 false 不允许
     */
    public void scaleControlEnabled(boolean enabled) {
        this.mIsScaleControlEnabled = enabled;
    }

    /**
     * 获取是否显示比例尺
     * @return true 允许 false 不允许
     */
    public boolean getScaleControlEnabled() {
        return this.mIsScaleControlEnabled;
    }

    /**
     * 设置是否允许拖拽手势
     * @param enabled true 允许 false 不允许
     */
    public void scrollGesturesEnabled(boolean enabled) {
        this.mIsScrollGesturesEnabled = enabled;
    }

    /**
     * 获取是否允许拖拽
     * @return true 允许 false 不允许
     */
    public boolean getScrollGesturesEnabled() {
        return this.mIsScrollGesturesEnabled;
    }

    /**
     * 设置是否显示缩放控件
     * @param enabled true 显示 false 不显示
     */
    public void zoomControlsEnabled(boolean enabled) {
        this.mIsZoomControlsEnabled = enabled;
    }

    /**
     * 获取是否显示缩放控件
     * @return true 显示 false 不显示
     */
    public boolean getZoomControlsEnabled() {
        return this.mIsZoomControlsEnabled;
    }

    /**
     * 设置是否允许缩放手势
     * @param enabled true 允许 false 不允许
     */
    public void zoomGesturesEnabled(boolean enabled) {
        this.mIsZoomGesturesEnabled = enabled;
    }

    /**
     * 获取是否允许缩放手势
     * @return true 允许 false 不允许
     */
    public boolean getZoomGesturesEnabled() {
        return this.mIsZoomGesturesEnabled;
    }

    /**
     * 设置地图是否显示
     * @param enabled true 显示 false 不显示
     */
    public void MapEnabled(boolean enabled) {
        this.mDeuMapEnabled = enabled;
        if (mMapStatusChanged != null){
            mMapStatusChanged.onMapStatusChanged();
        }
    }

    /**
     * 获取是否显示地图
     * @return true 显示 false 不显示
     */
    public boolean getDeuMapEnabled() {
        return this.mDeuMapEnabled;
    }

    /**
     * 设置地图是否离线
     * @param enabled true 离线 false 在线
     */
    public void setMapOffOnline(boolean enabled) {
        this.mIsOffOnline = enabled;
        if (mMapStatusChanged != null){
            mMapStatusChanged.onMapStatusChanged();
        }
    }

    /**
     * 获取当前是否为离线模式
     * @return true 离线 false 在线
     */
    public boolean getMapOffOnline() {
        return this.mIsOffOnline;
    }

    public void setOnMapStatusChangedListener(MapStatusChanged mapStatusChanged){
        mMapStatusChanged = mapStatusChanged;
    }

    public interface MapStatusChanged{
        void onMapStatusChanged();
    }

}
