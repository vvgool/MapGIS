package com.wiesen.provider.cache;


import com.wiesen.interf.IMapTileHandlerCallBack;
import com.wiesen.map.layer.RdMap;
import com.wiesen.provider.ProviderManager;
import com.wiesen.provider.base.LoopRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TileLoadHandler implements IMapTileHandlerCallBack {
    private final BitMapCache mImageCache;
    private RdMap mRdMap;
    private ProviderManager mProviderManager;
    private ExecutorService mExecutor;
    private List<LoopRunnable> mRunnableList ;
    private boolean mThreadIsRunning = false;
    /**
     * 构造方法
     * @param bitMapCache 瓦片缓存对象
     * @param rdMap DeuMap 对象
     */
    public TileLoadHandler(BitMapCache bitMapCache, RdMap rdMap) {
        this.mRdMap = rdMap;
        this.mImageCache = bitMapCache;
        mProviderManager = new ProviderManager(mImageCache,this);
        mExecutor = Executors.newSingleThreadExecutor();
        mRunnableList = new ArrayList<LoopRunnable>();
    }

    /**
     * 加载地图瓦片
     * @param haveNoBitmapList
     * @throws IOException
     */
    public void loadMapBitmap(List<String> haveNoBitmapList) throws IOException {
        synchronized (mRunnableList) {
            LoopRunnable runnable = mProviderManager.getProviderRunnable();
            runnable.addTile(haveNoBitmapList);
            mRunnableList.add(runnable);
            if (!mThreadIsRunning) {
                if (mRunnableList.size() > 0) {
                    mExecutor.submit(runnable);
                    mRunnableList.clear();
                    mThreadIsRunning = true;
                }
            }
        }
    }

    /**
     * 加载下一数据
     */
    private void threadNext(){
        synchronized (mRunnableList) {
            mThreadIsRunning = false;
            if (!mThreadIsRunning) {
                if (mRunnableList.size() > 0) {
                    LoopRunnable runnable = mRunnableList.get(mRunnableList.size() - 1);
                    mExecutor.submit(runnable);
                    mRunnableList.clear();
                    mThreadIsRunning = true;
                }
            }
        }
    }




    @Override
    public void onTileCompletedSuccess() {
        mRdMap.loadMap();
    }

    @Override
    public void onTileCompletedFail() {

    }

    @Override
    public void onTileHashCodeEnd() {
        threadNext();
    }
}
