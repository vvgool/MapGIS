package com.wiesen.provider.base;


import com.wiesen.interf.IMapTileHandlerCallBack;
import com.wiesen.provider.cache.BitMapCache;

import java.util.ArrayList;
import java.util.List;


public abstract class LoopRunnable implements Runnable {
    private BitMapCache mBitmapCache;
    private IMapTileHandlerCallBack iMapTileHandlerCallBack;
    private List<String> mRowTiles = new ArrayList<String>();
    public int mSucceedCount = 0;
    public int mFailedCount = 0;

    public LoopRunnable(BitMapCache cache, IMapTileHandlerCallBack deuMapTileHandlerCallBack){
        this.mBitmapCache = cache;
        this.iMapTileHandlerCallBack = deuMapTileHandlerCallBack;
    }

    public void addTile(List<String> rowTiles){
        if (rowTiles != null){
            mRowTiles.clear();
            mRowTiles.addAll(rowTiles);
        }
    }

    public void handlerTiles(List<String> rowTiles, BitMapCache mBitmapCache, IMapTileHandlerCallBack iMapTileHandlerCallBack){
        for (String rowTileString : rowTiles){
            if(handlerTileBitmap(rowTileString,mBitmapCache)){
                ++mSucceedCount;
            }else {
                ++mFailedCount;
            }
        }
        if (mFailedCount == mRowTiles.size()){
            iMapTileHandlerCallBack.onTileCompletedFail();
        }else {
            iMapTileHandlerCallBack.onTileCompletedSuccess();
        }
        iMapTileHandlerCallBack.onTileHashCodeEnd();
    }

    public abstract boolean handlerTileBitmap(String rowTile, BitMapCache bitMapCache);

    @Override
    public void run() {
        if (mRowTiles.size() >0) {
            handlerTiles(mRowTiles, mBitmapCache, iMapTileHandlerCallBack);
        }
    }


}
