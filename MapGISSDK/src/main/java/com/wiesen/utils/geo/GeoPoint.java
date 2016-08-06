package com.wiesen.utils.geo;


public class GeoPoint {
    private double mLatitude;
    private double mLongitude;
    private double mAltitude;

    /**
     * 构造方法
     * @param longitude 经度
     * @param latitude 纬度
     * @param altitude 高程
     */
    public GeoPoint(final double longitude, final double latitude, final double altitude) {
        this.mLongitude = longitude;
        this.mLatitude = latitude;
        this.mAltitude = altitude;
    }

    /**
     * 构造方法
     * @param longitude 经度
     * @param latitude 纬度
     */
    public GeoPoint(final double longitude, final double latitude) {
        this.mLongitude =longitude ;
        this.mLatitude = latitude ;
    }


    /**
     * 获取高程
     * @return 高程数据
     */
    public double getAltitude() {
        return this.mAltitude;
    }

    /**
     * 设置高程数据
     * @param altitude 高程数据
     */
    public void setAltitude(double altitude) {
        this.mAltitude = altitude;
    }

    /**
     * 获取纬度数据
     * @return 纬度数据
     */
    public double getLatitude() {
        return this.mLatitude;
    }

    /**
     * 获取经度数据
     * @return 经度数据
     */
    public double getLongitude() {
        return this.mLongitude;
    }

    /**
     * 设置纬度数据
     * @param latitude 纬度数据
     */
    public void setLatitude(final double latitude) {
        this.mLatitude = latitude;
    }

    /**
     * 设置经度数据
     * @param longitude 经度数据
     */
    public void setLongitude(final double longitude) {
        this.mLongitude = longitude;
    }

}
