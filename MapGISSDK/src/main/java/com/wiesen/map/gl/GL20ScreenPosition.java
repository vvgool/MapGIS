package com.wiesen.map.gl;

/**
 * Created by wiesen on 16-7-8.
 */
public class GL20ScreenPosition {
    private static GL20ScreenPosition mGL20ScreenPosition;
    private static float mScreenWidth;
    private static float mScreenHeight;

    public static GL20ScreenPosition getInstance(float width,float height){
        if (mGL20ScreenPosition == null){
            synchronized (GL20ScreenPosition.class){
                if (mGL20ScreenPosition == null){
                    mGL20ScreenPosition = new GL20ScreenPosition();
                }
            }
        }
        mScreenWidth = width;
        mScreenHeight = height;
        return mGL20ScreenPosition;
    }


    public float[] changePX2GL(float X, float Y){
        float point[] = new float[2];
        point[0] = (X/mScreenWidth*2) -1;
        point[1] = 1 - (Y/mScreenHeight*2);
        return point;
    }

    public float getGLX(float X){
        return (X/mScreenWidth*2) -1;
    }

    public float getGLY(float Y){
        return 1 - (Y/mScreenHeight*2);
    }

    public float getMoveX(float lengthX){
        return lengthX/mScreenWidth*2;
    }

    public float getMoveY(float lengthY){
        return -lengthY/mScreenHeight*2;
    }

}
