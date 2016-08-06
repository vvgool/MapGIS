package com.wiesen.map;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.wiesen.location.RowTile;
import com.wiesen.map.layer.RdMap;
import com.wiesen.provider.cache.BitMapCache;
import com.wiesen.provider.cache.TileLoadHandler;
import com.wiesen.utils.MapConstants;
import com.wiesen.utils.geo.GeoUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wiesen on 16-8-3.
 */
public class MapLoading {
    private RdMap mRdMap;
    private static final int TileSize = MapConstants.TILE_SIZE;
    private List<String> mHaveNoTileList = new ArrayList<String>();
    private boolean mMapShowEnabled = true;
    private BitMapCache mBitmapCache;
    private TileLoadHandler mTileLoadHandler;

    public MapLoading(RdMap rdMap,BitMapCache bitMapCache) {
        mRdMap = rdMap;
        mBitmapCache = bitMapCache;
        mTileLoadHandler = new TileLoadHandler(bitMapCache, rdMap);
    }

    public void loadCurrentMap(final Point mapPixelXY , boolean enabled){
        mMapShowEnabled = enabled;
        pixelXYToTileOffsetPixelXY(mapPixelXY);
        loading(mapPixelXY,mRdMap.getZoomLevel());
    }

    /**
     * 获取地图偏移量并更新MapCanvas中的数据
     *
     * @param pixelXY 瓦片中心点经纬度转换而来的地图坐标
     */
    private void pixelXYToTileOffsetPixelXY(final Point pixelXY) {
        mRdMap.freshOffsetPix(-pixelXY.x % TileSize,-pixelXY.y % TileSize);
    }

    /**
     * 屏幕中心坐标减去画布中心到画布左上角距离，
     * 获取到画布左上角地图屏幕坐标，将屏幕坐标
     * 转换为瓦片行列号。
     * 获取画布绘制的行列瓦片数量
     *
     * @param pixelXY 地图中心点地图坐标
     * @param zoomLevel 地图当前层级
     */
    private void loading(final Point pixelXY, final int zoomLevel){
        try {
            int canvasWidth = mRdMap.getLayerDrawable().getWidth();
            int canvasHeight = mRdMap.getLayerDrawable().getHeight();
            Point tileXY = getTileXY(pixelXY);
            int tileNumX = canvasWidth / TileSize;
            int tileNumY = canvasHeight / TileSize;
            RowTile mRowTile = new RowTile(tileXY.x-tileNumX/2, tileXY.y-tileNumY/2, zoomLevel);
            loadMap(tileNumX, tileNumY, mRowTile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取瓦片行列号
     *
     * @param point 瓦片的地图坐标
     * @return Point对象 point.x中为瓦片列号 point.y 中为瓦片行号
     */
    private Point getTileXY(Point point) {
        return GeoUtils.PixelXYToTileXY(point.x, point.y, null);
    }

    private void loadMap(final int numX,final int numY,final RowTile rowTile){
        RowTile pTile = new RowTile(rowTile.x, rowTile.y, rowTile.zoom);
        mHaveNoTileList.clear();
        for (int i = 0; i < numX; i++) {
            pTile.y = rowTile.y;
            for (int j = 0; j < numY; j++) {
                Bitmap bitmap = mBitmapCache.getBitmapFromCache(pTile.toString());
                mRdMap.getMapDrawable().loadSingleDrawable(i,j,mMapShowEnabled?bitmap:null);
                if (bitmap == null){
                    mHaveNoTileList.add(pTile.toString());
                }
                pTile.y++;
            }
            pTile.x++;
        }
        mRdMap.freshTexture();
        if (mHaveNoTileList.size()>0){
            try {
                mTileLoadHandler.loadMapBitmap(mHaveNoTileList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
