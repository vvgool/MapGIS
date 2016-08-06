package com.wiesen.map.gl;

import android.opengl.GLES20;

/**
 * Created by wiesen on 2016/7/1.
 */
public class GL20ProgramUtils {

    /**
     * 创建Program
     * @param vertexShader 创建的vertexShader 返回的id
     * @param fragmentShader 创建的fragmentShader 返回的id
     * @return 创建的program 返回的id
     * @throws Exception
     */
    public int createProgram(int vertexShader,int fragmentShader) throws Exception {
        // 创建一个空的OpenGL ES Program
        int  program = GLES20.glCreateProgram();
        if (program != 0) {
            // 将vertex shader添加到program
            GLES20.glAttachShader(program, vertexShader);
            // 将fragment shader添加到program
            GLES20.glAttachShader(program, fragmentShader);
            // 创建可执行的 OpenGL ES program
            GLES20.glLinkProgram(program);
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES20.GL_TRUE) {
                String error = GLES20.glGetProgramInfoLog(program);
                GLES20.glDeleteProgram(program);
                throw new Exception(error);
            }
        }
        return program;
    }

}
