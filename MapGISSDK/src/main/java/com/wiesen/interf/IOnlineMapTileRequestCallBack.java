package com.wiesen.interf;

import android.graphics.Bitmap;

import com.wiesen.location.RowTile;


public interface IOnlineMapTileRequestCallBack {
    /**
     * 获取瓦片失败
     * @param tile 瓦片信息
     */
    void getTileBitmapFailed(RowTile tile);

    /**
     * 获取瓦片成功
     * @param tile 瓦片信息
     * @param bitmap 瓦片图片数据
     */
    void getTileBitmapSucceed(RowTile tile, Bitmap bitmap);
}
