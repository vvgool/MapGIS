package com.wiesen.interf;


import com.wiesen.utils.geo.MapOptions;
import com.wiesen.utils.geo.GeoPoint;

public interface IMapControl {
    /**
     * 地图放大
     */
    void zoomIn();

    /**
     * 地图缩小
     */
    void zoomOut();

    /**
     * 获取当前地图层级
     * @return 层级值
     */
    int getZoomLevel();

    /**
     * 设置当前层级
     * @param zoomLevel 层级值
     */
    void setZoomLevel(int zoomLevel);

    /**
     * 获取地图中心点经纬度信息
     * @return GeoPoint 对象
     */
    GeoPoint getMapCenter();

    /**
     * 设置地图中心点经纬度信息
     * @param geoPoint GeoPoint对象
     */
    void setMapCenter(GeoPoint geoPoint);

    /**
     * 获取地图设置对象
     * @return MapOptions对象
     */
    MapOptions getMapOptions();

}
