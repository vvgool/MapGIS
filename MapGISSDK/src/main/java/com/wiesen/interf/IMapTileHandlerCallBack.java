package com.wiesen.interf;


public interface IMapTileHandlerCallBack {
    /**
     * 获取图片成功
     */
    void onTileCompletedSuccess();

    /**
     * 获取图片失败
     */
    void onTileCompletedFail();

    /**
     * 当前界面瓦片获取完成
     */
    void onTileHashCodeEnd();
}
