package com.wiesen.map;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.wiesen.map.layer.RdMap;

/**
 * Created by wiesen on 16-8-3.
 */
public class MapView extends GLSurfaceView implements RdMap.RequestRenderFresh {
    private RdMap mRdMap;
    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRdMap = new RdMap(context);
        setEGLContextClientVersion(2);
        setRenderer(mRdMap);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mRdMap.setOnRequestRenderFresh(this);
    }

    public MapView(Context context) {
        this(context,null);
    }

    public RdMap getRdMap(){
        return mRdMap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mRdMap.onTouchEvent(event);
        return true;
    }

    @Override
    public void onFresh(Runnable runnable) {
        queueEvent(runnable);
        requestRender();
    }
}
