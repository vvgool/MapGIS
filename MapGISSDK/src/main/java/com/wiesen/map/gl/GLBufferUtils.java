package com.wiesen.map.gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by wiesen on 2016/7/5.
 */
public class GLBufferUtils {

    /**
     * create FloatBuffer form float[]
     * @param floats the float[]
     * @return FloatBuffer
     */
    public static FloatBuffer buildFloatBuffer(float[] floats){
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(floats.length*4);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(floats);
        floatBuffer.position(0);
        return floatBuffer;
    }

    /**
     * create ShortBuffer from short[]
     * @param shorts the short[]
     * @return ShortBuffer
     */
    public static ShortBuffer buildShortBuffer(short[] shorts){
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(shorts.length*2);
        byteBuffer.order(ByteOrder.nativeOrder());
        ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
        shortBuffer.put(shorts);
        shortBuffer.position(0);
        return shortBuffer;
    }

    public static ByteBuffer buildByteBuffer(int[] ints){
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(ints.length*2);
        byteBuffer.order(ByteOrder.nativeOrder());
        byteBuffer.position(0);
        return byteBuffer;
    }
}
