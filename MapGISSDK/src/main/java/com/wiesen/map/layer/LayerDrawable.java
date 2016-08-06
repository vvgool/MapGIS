package com.wiesen.map.layer;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.wiesen.utils.MapConstants;


/**
 * Created by wiesen on 16-7-9.
 */
public class LayerDrawable {
    private static final int MAP_SIZE = MapConstants.TILE_SIZE;
    private int mLayerWidth;
    private int mLayerHeight;
    private Canvas mLayerCanvas;
    private Bitmap mLayerBitmap;
    private MapDrawable mMapDrawable;


    public LayerDrawable(){
        mLayerCanvas = new Canvas();
        mMapDrawable = new MapDrawable();
    }


    protected void onLayerSizeChanged(int width,int height){
        mLayerWidth = MAP_SIZE * (width % MAP_SIZE==0 ? width / MAP_SIZE +2 : width / MAP_SIZE + 3);
        mLayerHeight= MAP_SIZE * (height % MAP_SIZE==0 ? height / MAP_SIZE +2 : height / MAP_SIZE + 3);
        if (mLayerBitmap != null){
            mLayerBitmap.recycle();
            mLayerBitmap = null;
        }
        mLayerBitmap = Bitmap.createBitmap(mLayerWidth,mLayerHeight, Bitmap.Config.RGB_565);
        mLayerCanvas.setBitmap(mLayerBitmap);
        mMapDrawable.changeMapLayerSize(mLayerWidth,mLayerHeight);
    }

    protected void doDraw(){

        onDraw(mLayerCanvas);
    }

    protected void onDraw(Canvas canvas){
        mMapDrawable.onDraw(canvas);
    }

    /**
     * get layer width
     * @return int
     */
    public int getWidth(){
        return mLayerWidth;
    }

    /**
     * get layer height
     * @return int
     */
    public int getHeight(){
        return mLayerHeight;
    }

    /**
     * get layer bitmap
     * @return bitmap
     */
    protected Bitmap getBitmap(){
        doDraw();
        return mLayerBitmap;
    }

    protected MapDrawable getMapDrawable(){
        return mMapDrawable;
    }

    /**
     * onDestroy
     */
    protected void onDestroy(){
        if (mMapDrawable != null){
            mMapDrawable.onDestroy();
            mMapDrawable = null;
        }

        if (mLayerBitmap != null){
            mLayerBitmap.recycle();
            mLayerBitmap = null;
        }

        if (mLayerCanvas != null){
            mLayerCanvas = null;
        }
    }

}
