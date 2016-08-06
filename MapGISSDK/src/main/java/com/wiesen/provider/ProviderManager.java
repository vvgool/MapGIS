package com.wiesen.provider;

import com.wiesen.interf.IMapTileHandlerCallBack;
import com.wiesen.provider.base.IProvider;
import com.wiesen.provider.base.LoopRunnable;
import com.wiesen.provider.cache.BitMapCache;
import com.wiesen.provider.local.OfflineMapProvider;
import com.wiesen.provider.net.OnlineMapProvider;
import com.wiesen.utils.FileUtils;
import com.wiesen.utils.MapConstants;
import com.wiesen.utils.geo.MapOptions;

/**
 * Created by wiesen on 16-8-3.
 */
public class ProviderManager implements IProvider{
    private final BitMapCache mBitMapCache;
    private final IMapTileHandlerCallBack iMapTileHandlerCallBack;
    private MapOptions mMapOptions;
    private OfflineMapProvider mOfflineMapProvider;
    private OnlineMapProvider mOnlineMapProvider;

    public ProviderManager(BitMapCache imageCache, IMapTileHandlerCallBack deuTileLoadHandler) {
        mBitMapCache = imageCache;
        iMapTileHandlerCallBack = deuTileLoadHandler;
        mMapOptions = MapOptions.getInstance();
        initFileDir();
    }

    @Override
    public LoopRunnable getProviderRunnable() {
        if (mMapOptions.getMapOffOnline()){
            if (mOfflineMapProvider == null){
                mOfflineMapProvider = new OfflineMapProvider(mBitMapCache, iMapTileHandlerCallBack);
            }
            return mOfflineMapProvider.getProviderRunnable();
        }else {
            if (mOnlineMapProvider == null){
                mOnlineMapProvider = new OnlineMapProvider(mBitMapCache, iMapTileHandlerCallBack);
            }
            return mOnlineMapProvider.getProviderRunnable();
        }
    }

    /**
     * 初始化在线离线文件夹
     */
    private void initFileDir(){
        FileUtils.createFile(MapConstants.offOnlinePath);
        FileUtils.createFile(MapConstants.onlinePath);
    }
}
