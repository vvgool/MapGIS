package com.wiesen.map.gl;

import android.content.res.Resources;
import android.opengl.GLES20;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by ljdy on 2016/7/1.
 */
public class GL20ShaderUtils {

    private String mVertexShaderCode =
            "attribute vec4 vPosition; \n"
            + "void main() \n"
            + "{ \n"
            + " gl_Position = vPosition; \n"
            + "} \n";

    private String mFragmentShaderCode = "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}";


    public GL20ShaderUtils(){

    }

    public GL20ShaderUtils (String vertexShaderCode, String fragmentShaderCode){
        this.mVertexShaderCode = vertexShaderCode;
        this.mFragmentShaderCode = fragmentShaderCode;
    }

    /**
     * 创建一个vertex shader类型(GLES20.GL_VERTEX_SHADER)
     * 或fragment shader类型(GLES20.GL_FRAGMENT_SHADER)
     * @param type  GLES20.GL_VERTEX_SHADER or GLES20.GL_FRAGMENT_SHADER
     * @param shaderCode shader
     * @return shader
     */
    public  int loadShader(int type,String shaderCode) throws Exception {
        int shader = GLES20.glCreateShader(type);
        if (shader != 0) {
            // 将源码添加到shader并编译之
            GLES20.glShaderSource(shader, shaderCode);
            GLES20.glCompileShader(shader);
            int[] compiled = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {
                String error = GLES20.glGetShaderInfoLog(shader);
                GLES20.glDeleteShader(shader);
                throw new Exception(error);
            }
        }
        return shader;
    }


    public void setVertexShaderCode(String vertexShaderCode){
        mVertexShaderCode = vertexShaderCode;
    }

    public void setFragmentShaderCode(String fragmentShaderCode){
        mFragmentShaderCode = fragmentShaderCode;
    }

    public int loadVertexShader() throws Exception {
        return loadShader(GLES20.GL_VERTEX_SHADER,mVertexShaderCode);
    }

    public int loadFragmentShader() throws Exception {
        return loadShader(GLES20.GL_FRAGMENT_SHADER,mFragmentShaderCode);
    }

    public void deleteShader(int shader){
        GLES20.glDeleteShader(shader);
    }


    //从sh脚本中加载shader内容的方法
    public static String loadFromAssetsFile(String fname, Resources r)
    {
        String result=null;
        try
        {
            InputStream in=r.getAssets().open(fname);
            int ch=0;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while((ch=in.read())!=-1)
            {
                baos.write(ch);
            }
            byte[] buff=baos.toByteArray();
            baos.close();
            in.close();
            result=new String(buff,"UTF-8");
            result=result.replaceAll("\\r\\n","\n");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
