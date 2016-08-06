package com.wiesen.interf;

public interface IMapTileCallBack {
    /**
     * 地图瓦片初始化完成
     */
    void TilePrePareOk();

    /**
     * 瓦片初始化失败
     */
    void TilePrePareError();
}
