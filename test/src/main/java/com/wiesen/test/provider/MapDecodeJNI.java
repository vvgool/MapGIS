package com.wiesen.test.provider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wiesen on 16-8-4.
 */
public class MapDecodeJNI {
    private static final String TAG="JNI";
    private MapTileLabs mTileLabs;
    private Map<String,TileIndex> mTileMap = new HashMap<String,TileIndex>();
    private final String mPath;

    public MapDecodeJNI(String path) {
        mPath = path;
        mTileLabs = new MapTileLabs();
        getTileIndex(mPath);
    }

    public void parseIndex(String labs){
        mTileLabs.decodeJson(labs);
        Log.d(TAG,labs);
        decodeTileInfo(mPath,mTileLabs.index_start,mTileLabs.index_len);
    }

    public void setTileInfo(byte[] info){
        int length = info.length / 17;
        for (int i = 0; i < length; i++) {
            byte[] index = ByteUtils.copyBytes(info, i * 17, 17);
            MapTileIndex tileIndex = new MapTileIndex();
            tileIndex.decodeIndexBytes(index);
            mTileMap.put(tileIndex.getKey(),tileIndex.getValue());
        }
    }

    /**
     * 根据key： 层_行_列 获取该瓦片在数据段中相对起始点及瓦片长度，读取文件中瓦片
     *
     * @param key 层_行_列
     * @return bitmap 瓦片Bitmap
     * @throws Exception
     */
    public Bitmap queryBitmapByIndex(String key) throws Exception {
        Bitmap bitmap = null;
        if (mTileMap != null && mTileMap.containsKey(key)) {
            TileIndex tileBean = mTileMap.get(key);
            byte[] temp = getTileMap(mPath,tileBean.iOffset+mTileLabs.data_start,tileBean.iTileLen);
            bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            temp = null;
        }
        return bitmap;
    }

    public native void getTileIndex(String path);

    public native void decodeTileInfo(String path,int start,int length);

    public native byte[] getTileMap(String path,int start,int length);

    static {
        System.loadLibrary("test_jni");
    }
}
