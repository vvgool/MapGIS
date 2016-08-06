package com.wiesen.provider.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.wiesen.interf.IMapTileHandlerCallBack;
import com.wiesen.interf.IOnlineMapTileRequestCallBack;
import com.wiesen.location.RowTile;
import com.wiesen.provider.base.IProvider;
import com.wiesen.provider.base.LoopRunnable;
import com.wiesen.provider.cache.BitMapCache;
import com.wiesen.provider.net.wmts.WMTSInfo;
import com.wiesen.provider.net.wmts.WMTSUtils;
import com.wiesen.utils.MapConstants;
import com.wiesen.utils.geo.MapOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by wiesen on 16-8-4.
 */
public class OnlineMapProvider implements IProvider {
    private final BitMapCache mBitMapCache;
    private final IMapTileHandlerCallBack iMapTileHandlerCallBack;
    private MapOptions mMapOptions;
    private int mType;

    public OnlineMapProvider(BitMapCache cache, IMapTileHandlerCallBack deuMapTileHandlerCallBack) {
        mBitMapCache = cache;
        iMapTileHandlerCallBack = deuMapTileHandlerCallBack;
        mMapOptions = MapOptions.getInstance();
    }

    @Override
    public LoopRunnable getProviderRunnable() {
        return new onLineMapLoop(mBitMapCache,iMapTileHandlerCallBack);
    }
    private Bitmap getFileBitmap(String tile) throws FileNotFoundException{
        String[] tileStr = tile.split("_");
        String path = MapConstants.onlinePath + File.separator+mType+File.separator+tileStr[0] +File.separator + tileStr[1] + File.separator + tileStr[2]+".deu";
        File file = new File(path);
        if (!file.exists()){
            return null;
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        Bitmap bitmap = null;
        if (fileInputStream != null){
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Bitmap.Config.RGB_565;
            bitmap = BitmapFactory.decodeStream(fileInputStream,null,opt);
        }
        return bitmap;
    }

    private class onLineMapLoop extends LoopRunnable implements IOnlineMapTileRequestCallBack {

        private WMTSInfo wmtsInfo = new WMTSInfo();
        private boolean isWmts = false;

        public onLineMapLoop(BitMapCache cache, IMapTileHandlerCallBack mapTileHandlerCallBack) {
            super(cache, mapTileHandlerCallBack);
            if (mMapOptions.getMapType() == MapConstants.MapType.map3d){
                mType = 0;
            }else if (mMapOptions.getMapType() == MapConstants.MapType.mapsta){
                mType = 1;
            }else if (mMapOptions.getMapType() == MapConstants.MapType.maptra){
                mType =2;
            }
        }

        @Override
        public void handlerTiles(List<String> rowTiles, BitMapCache mBitmapCache, IMapTileHandlerCallBack iMapTileHandlerCallBack) {
            isWmts= WMTSUtils.connectToMapServerGetInfo(wmtsInfo);
//            if (isWmts) {
            for (String rowTileString : rowTiles) {
                if (handlerTileBitmap(rowTileString, mBitmapCache)) {
                    ++mSucceedCount;
                } else {
                    ++mFailedCount;
                }
            }
            if (mSucceedCount > 0) {
                iMapTileHandlerCallBack.onTileCompletedSuccess();
            }
//            }
            iMapTileHandlerCallBack.onTileHashCodeEnd();
        }

        @Override
        public boolean handlerTileBitmap(String rowTile, BitMapCache bitMapCache) {
            Bitmap bitmap = null;
            try {
                bitmap = getFileBitmap(rowTile);
                if (bitmap != null){
                    bitMapCache.addBitmapToCache(rowTile.toString(),bitmap);
                    return true;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            if (bitmap == null && isWmts){
                String[] tileStr = rowTile.split("_");
                return ApiClient.sendToServer(new RowTile(Integer.valueOf(tileStr[1]), Integer.valueOf(tileStr[2]),Integer.valueOf(tileStr[0])),mType,wmtsInfo,this);
            }
            return false;
        }

        @Override
        public void getTileBitmapFailed(RowTile tile) {

        }

        @Override
        public void getTileBitmapSucceed(RowTile tile, Bitmap bitmap) {
            mBitMapCache.addBitmapToCache(tile.toString(),bitmap);
        }
    }
}
