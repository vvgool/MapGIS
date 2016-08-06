package com.wiesen.map.gl;

import android.opengl.GLES20;

/**
 * Created by wiesen on 2016/7/1.
 */
public class GL20Factory {
    private int mVertexShader;
    private int mFragmentShader;
    private int mProgram;
    private GL20ShaderUtils mShaderUtils = null;
    private GL20ProgramUtils mProgramUtils = null;


    public GL20Factory createShader(GL20ShaderUtils shaderUtils) throws Exception {
        if (shaderUtils == null)
            throw new NullPointerException("GL20ShaderUtils is null,please create GL20ShaderUtils class");

        mShaderUtils = shaderUtils;
        mVertexShader = mShaderUtils.loadVertexShader();
        mFragmentShader = mShaderUtils.loadFragmentShader();
        return this;
    }


    public GL20Factory createProgram() throws Exception {
        if (mShaderUtils == null) {
            createShader(new GL20ShaderUtils());
        }
        mProgramUtils = new GL20ProgramUtils();
        mProgram = mProgramUtils.createProgram(mVertexShader, mFragmentShader);
        return this;
    }



    public int getAttribLocation(String name){
        if (mProgramUtils == null) return -1;
        return GLES20.glGetAttribLocation(mProgram, name);
    }

    public int getUniformLocation(String name){
        if (mProgramUtils == null) return -1;
        return GLES20.glGetUniformLocation(mProgram,name);
    }

    public void onDestroy(){
        if (mShaderUtils != null){
            mShaderUtils = null;
        }
        if (mProgramUtils != null){
            deleteProgram();
            mProgramUtils = null;
        }
    }

    public void useProgram() {
        GLES20.glUseProgram(mProgram);
    }

    public void deleteProgram(){
        GLES20.glDeleteShader(mFragmentShader);
        GLES20.glDeleteShader(mVertexShader);
        GLES20.glDeleteProgram(mProgram);
        mProgram = mVertexShader = mFragmentShader=0;
    }

    public int getProgram() {
        return mProgram;
    }
}
