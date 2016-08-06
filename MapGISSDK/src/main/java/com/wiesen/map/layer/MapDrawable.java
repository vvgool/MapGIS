package com.wiesen.map.layer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.wiesen.utils.MapConstants;


/**
 * Created by wiesen on 16-7-9.
 */
public class MapDrawable {
    private static final int MAP_SIZE = MapConstants.TILE_SIZE;
    private static final int GRID_SIZE = 32;
    private int mMapWidth;
    private int mMapHeight;
    private Bitmap mDefaultBitmap;
    private Canvas mMapCanvas;
    private Bitmap mMapBitmap;

    public MapDrawable(){
        mDefaultBitmap = getDefaultBitmap();
        mMapCanvas = new Canvas();
    }


    /**
     * when the screen size changed to be fresh bitmap size
     * @param width width length
     * @param height height length
     */
    protected void changeMapLayerSize(int width,int height){
        if (mMapBitmap != null){
            mMapBitmap.recycle();
            mMapBitmap = null;
        }
        mMapBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.RGB_565);
        mMapCanvas.setBitmap(mMapBitmap);
    }

    protected void onDraw(Canvas canvas){
        canvas.drawBitmap(mMapBitmap,0,0,null);
    }

    /**
     * load one map picture in the sure where
     * @param x number of the width
     * @param y number of the height
     * @param bitmap which you want to draw bitmap
     */
    public void loadSingleDrawable(int x,int y,Bitmap bitmap){
        mMapCanvas.drawBitmap(mDefaultBitmap,x*MAP_SIZE,y*MAP_SIZE,null);
        if (bitmap != null){
            mMapCanvas.drawBitmap(bitmap,x*MAP_SIZE,y*MAP_SIZE,null);
        }
    }

    /**
     * create map default bitmap
     * @return Bitmap
     */
    private Bitmap getDefaultBitmap(){
        Bitmap bitmap = Bitmap.createBitmap(MAP_SIZE,MAP_SIZE, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.DKGRAY);
        canvas.drawColor(Color.GRAY);
        for (int x = 0;x<(MAP_SIZE/GRID_SIZE);x++){
            canvas.drawLine(0,x*GRID_SIZE,MAP_SIZE,x*GRID_SIZE,paint);
        }
        for (int y = 0;y<(MAP_SIZE/GRID_SIZE);y++){
            canvas.drawLine(y*GRID_SIZE,0,y*GRID_SIZE,MAP_SIZE,paint);
        }
        return bitmap;
    }


    public void onDestroy() {
        if (mDefaultBitmap != null){
            mDefaultBitmap.recycle();
            mDefaultBitmap = null;
        }

        if (mMapBitmap != null){
            mMapBitmap .recycle();
            mMapBitmap = null;
        }

        if (mMapCanvas != null){
            mMapCanvas = null;
        }
    }
}
