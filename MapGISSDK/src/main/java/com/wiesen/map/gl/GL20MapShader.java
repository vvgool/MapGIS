package com.wiesen.map.gl;

/**
 * Created by wiesen on 16-8-3.
 */
public class GL20MapShader {
    public static String getVertexShaderString(){
        return "uniform mat4 uMVPMatrix; //总变换矩阵\n" +
                "attribute vec3 aPosition;  //顶点位置\n" +
                "attribute vec2 aTexCoor;    //顶点纹理坐标\n" +
                "varying vec2 vTextureCoord;  //用于传递给片元着色器的变量\n" +
                "void main()\n" +
                "{\n" +
                "   gl_Position = uMVPMatrix * vec4(aPosition,1); //根据总变换矩阵计算此次绘制此顶点位置\n" +
                "   vTextureCoord = aTexCoor;//将接收的纹理坐标传递给片元着色器\n" +
                "}";
    }

    public static String getFragmentShaderString(){
        return "precision mediump float;\n" +
                "varying vec2 vTextureCoord; //接收从顶点着色器过来的参数\n" +
                "uniform sampler2D sTexture;//纹理内容数据\n" +
                "void main()\n" +
                "{\n" +
                "   //给此片元从纹理中采样出颜色值\n" +
                "   gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                "}";
    }
}
