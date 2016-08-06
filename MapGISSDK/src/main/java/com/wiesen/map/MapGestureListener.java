package com.wiesen.map;

import android.view.MotionEvent;

/**
 * Created by wiesen on 2016/6/15.
 */
public abstract class MapGestureListener {

    public float mDownXPoint;
    public float mDownYPoint;
    private boolean isMorePoint = false;

    public abstract void onTouchClick(MotionEvent event);

    public abstract void onTouchMove(MotionEvent event);

    public abstract void onTouchTwoPointDown(MotionEvent event);

    public abstract void onTouchTwoPointMove(MotionEvent event);

    public abstract void onTouchMorePointUp(MotionEvent event);

    public boolean onTouchEvent(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            mDownXPoint = event.getX();
            mDownYPoint = event.getY();
        }

        if (event.getAction() == MotionEvent.ACTION_UP){
            if (event.getX() == mDownXPoint && event.getY() == mDownYPoint && !isMorePoint){
                onTouchClick(event);
            }
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE){
            if (isMorePoint){
                onTouchTwoPointMove(event);
            }else {
                if (event.getX() != mDownXPoint || event.getY() != mDownYPoint) {
                    onTouchMove(event);
                }
            }
        }

        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_DOWN){
            if (event.getPointerCount() == 2) {
                isMorePoint = true;
                onTouchTwoPointDown(event);
            }
        }

        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_UP){
            isMorePoint = false;
            onTouchMorePointUp(event);
        }

        return true;
    }


}
