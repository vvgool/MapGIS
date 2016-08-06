package com.wiesen.provider.local;

import android.graphics.Bitmap;

import com.wiesen.interf.IMapTileHandlerCallBack;
import com.wiesen.provider.base.IProvider;
import com.wiesen.provider.base.LoopRunnable;
import com.wiesen.provider.cache.BitMapCache;
import com.wiesen.utils.geo.MapOptions;

import java.util.List;

/**
 * Created by wiesen on 16-8-4.
 */
public class OfflineMapProvider implements IProvider {
    private final BitMapCache mBitMapCache;
    private final IMapTileHandlerCallBack iMapTileHandlerCallBack;
    private MapOptions mMapOptions;

    public OfflineMapProvider(BitMapCache cache, IMapTileHandlerCallBack deuMapTileHandlerCallBack) {
        mBitMapCache = cache;
        iMapTileHandlerCallBack = deuMapTileHandlerCallBack;
        mMapOptions = MapOptions.getInstance();
    }

    @Override
    public LoopRunnable getProviderRunnable() {
        return new OfflineRunnable(mBitMapCache, iMapTileHandlerCallBack);
    }


    private class OfflineRunnable extends LoopRunnable {

        public OfflineRunnable(BitMapCache cache, IMapTileHandlerCallBack deuMapTileHandlerCallBack) {
            super(cache, deuMapTileHandlerCallBack);
        }

        @Override
        public void handlerTiles(List<String> rowTiles, BitMapCache mBitmapCache, IMapTileHandlerCallBack iMapTileHandlerCallBack) {
            super.handlerTiles(rowTiles,mBitmapCache, iMapTileHandlerCallBack);
        }

        @Override
        public boolean handlerTileBitmap(String rowTile, BitMapCache bitMapCache) {
            try {
                MapDecodeJNI decodeDeuMap = OfflineTilePro.getInstance().getDecodeDeuMapByType(mMapOptions.getMapType());
                if (decodeDeuMap != null) {
                    Bitmap bitmap = decodeDeuMap.queryBitmapByIndex(rowTile);
                    if (bitmap != null){
                        bitMapCache.addBitmapToCache(rowTile,bitmap);
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return false;
        }

    }
}
