package com.wiesen.provider.local;

import android.os.AsyncTask;

import com.wiesen.interf.IMapTileCallBack;
import com.wiesen.utils.MapConstants;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class OfflineTilePro {
    private Map<MapConstants.MapType, String> mDeuMapPath;
    private Map<MapConstants.MapType, MapDecodeJNI> mDecodeMaps;
    private IMapTileCallBack mMapTileCallBack;
    private final String normal = "normal.dat";
    private final String satellite = "map.dat";

    /**
     * 单例模式获取TileProviderOptions 对象
     * @return  对象
     */
    public static OfflineTilePro getInstance(){

        return InstanceHolder.M_OFFLINE_TILE_PRO;
    }

    private static class InstanceHolder{
        private static final OfflineTilePro M_OFFLINE_TILE_PRO = new OfflineTilePro();
    }

    /**
     * 构造函数
     */
    private OfflineTilePro() {
        mDeuMapPath = new HashMap<MapConstants.MapType, String>();
        mDecodeMaps = new HashMap<MapConstants.MapType, MapDecodeJNI>();
        addOffOnlineMapType(MapConstants.MapType.maptra, normal);
        addOffOnlineMapType(MapConstants.MapType.mapsta, satellite);
    }

    /**
     * 添加离线模式下的地图类型
     * @param type 类型
     * @param path 地图文件路径
     */
    public void addOffOnlineMapType(MapConstants.MapType type, String path) {
        try {
            String p_path = MapConstants.offOnlinePath+ File.separator + path;
            this.mDeuMapPath.put(type, p_path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据地图类型获取该类型地图文件路径
     * @param type 地图类型
     * @return 地图文件路径
     */
    public String getMapPathByType(MapConstants.MapType type) {
        if (mDeuMapPath.containsKey(type)) {
            return this.mDeuMapPath.get(type);
        }
        return null;
    }

    /**
     * 初始化地图索引
     */
    public void InitMapIndex() {
        new DecodeDeuMapIndex().execute();
    }

    /**
     * 设置地图索引完成监听接口
     * @param callBack IDeuMapTileCallBack 接口
     */
    public void setOnDecodeDeuMapIndexListener(IMapTileCallBack callBack) {
        this.mMapTileCallBack = callBack;
    }

    /**
     * 通过地图类型获取地图解析对象
     * @param type 地图类型
     * @return DecodeDeuMap 对象
     */
    public MapDecodeJNI getDecodeDeuMapByType(MapConstants.MapType type) {
        if (mDecodeMaps.containsKey(type)) {
            return mDecodeMaps.get(type);
        }
        return null;
    }

    /**
     * 清除地图类型集合
     */
    public void clearTile() {
        this.mDeuMapPath.clear();
        for (MapConstants.MapType type : mDecodeMaps.keySet()) {
            try {
                mDecodeMaps.get(type).clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.mDecodeMaps.clear();
    }

    /**
     * 异步解析自定义瓦片索引
     */
    private class DecodeDeuMapIndex extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                for (MapConstants.MapType type : mDeuMapPath.keySet()) {
                    mDecodeMaps.put(type, new MapDecodeJNI(mDeuMapPath.get(type)));
                }
            } catch (Exception e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (mMapTileCallBack != null) {
                if (aBoolean) {
                    mMapTileCallBack.TilePrePareOk();
                } else {
                    mMapTileCallBack.TilePrePareError();
                }
            }
        }
    }
}
