package com.wiesen.utils;

import java.io.File;

/**
 * Created by wiesen on 16-8-3.
 */
public class MapConstants {
    public static final int TILE_SIZE = 256;
    public static final int MAX_ZOOM = 20;
    public static final int MIN_ZOOM = 1;
    public final static double Default_Longitude =/*116.403875*/114.31667;
    public final static double Default_Latitude =/*39.915168*/30.51667;
    public static final double EarthRadius = 6378137;
    public static final double MinLatitude = -85.05112878;
    public static final double MaxLatitude = 85.05112878;
    public static final double MinLongitude = -180;
    public static final double MaxLongitude = 180;
    public final static int Default_Zoom = 17;//初始层级


    public static final String DefaultPath = FileUtils.getStoragePath() + File.separator + "RdMap";
    //工程目录
    public static final String offOnlinePath = FileUtils.getStoragePath() + File.separator + "RdMap" + File.separator + "Map";

    //瓦片地图缓存目录
    public static final String onlinePath = offOnlinePath + File.separator + "Cache";
    public static final String ROOT_URL = "";

    public  enum  MapType{
        map3d,//3d地图
        mapsta,//卫星地图
        maptra //交通地图
    }
}
