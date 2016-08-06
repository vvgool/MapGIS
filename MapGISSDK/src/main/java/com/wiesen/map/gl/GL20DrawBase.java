package com.wiesen.map.gl;


/**
 * Created by wiesen on 2016/7/5.
 */
public abstract class GL20DrawBase {
    protected GL20Factory mGL20Factory;
    //create base program first
    protected abstract void onCreateProgram();
    //getAttribLocation or getUniformLocation
    protected abstract void onGetLocation();
    //draw what you want
    protected abstract void doDraw();

    public void onCreate(){
        mGL20Factory = new GL20Factory();
        onCreateProgram();
        onGetLocation();
    }

    public void onDestroy(){
        if (mGL20Factory!=null) {
            mGL20Factory.onDestroy();
            mGL20Factory = null;
        }
    }


}
