package com.wiesen.test.provider;

import java.nio.charset.Charset;

public class ByteUtils {

    /**
     * short类型转byte数组
     * @param data 转换的数据
     * @return bytes
     */
    public static byte[] getBytes(short data)
    {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        return bytes;
    }

    /**
     * char类型转byte数组
     * @param data 转换的数据
     * @return bytes
     */
    public static byte[] getBytes(char data)
    {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data);
        bytes[1] = (byte) (data >> 8);
        return bytes;
    }

    /**
     * int类型转byte数组
     * @param data 转换的数据
     * @return bytes
     */
    public static byte[] getBytes(int data)
    {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        bytes[2] = (byte) ((data & 0xff0000) >> 16);
        bytes[3] = (byte) ((data & 0xff000000) >> 24);
        return bytes;
    }

    /**
     * long类型转byte数组
     * @param data 转换的数据
     * @return bytes
     */
    public static byte[] getBytes(long data)
    {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data >> 8) & 0xff);
        bytes[2] = (byte) ((data >> 16) & 0xff);
        bytes[3] = (byte) ((data >> 24) & 0xff);
        bytes[4] = (byte) ((data >> 32) & 0xff);
        bytes[5] = (byte) ((data >> 40) & 0xff);
        bytes[6] = (byte) ((data >> 48) & 0xff);
        bytes[7] = (byte) ((data >> 56) & 0xff);
        return bytes;
    }

    /**
     * float类型转byte数组
     * @param data 转换的数据
     * @return bytes
     */
    public static byte[] getBytes(float data)
    {
        int intBits = Float.floatToIntBits(data);
        return getBytes(intBits);
    }

    /**
     * double类型转byte数组
     * @param data 转换的数据
     * @return bytes
     */
    public static byte[] getBytes(double data)
    {
        long intBits = Double.doubleToLongBits(data);
        return getBytes(intBits);
    }

    /**
     * 设置不同的编码格式的返回byte数组
     * @param data 要转换的数据类型
     * @param charsetName 设置的编码格式
     * @return bytes
     */
    public static byte[] getBytes(String data, String charsetName)
    {
        Charset charset = Charset.forName(charsetName);
        return data.getBytes(charset);
    }

    /**
     * String类型数据转换为byte数组
     * @param data String类型数据
     * @return GBK类型的byte数组
     */
    public static byte[] getBytes(String data)
    {
        return getBytes(data, "GBK");
    }


    /**
     * byte数组转换为short类型数据
     * @param bytes 要转换的byte数组
     * @return short
     */
    public static short getShort(byte[] bytes)
    {
        return (short) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }


    /**
     * byte数组转换为char类型数据
     * @param bytes 要转换byte数组
     * @return char
     */
    public static char getChar(byte[] bytes)
    {
        return (char) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }

    /**
     * byte数组转换为int类型数据
     * @param bytes 要转换byte数组
     * @return int
     */
    public static int getInt(byte[] bytes)
    {
        return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 & (bytes[2] << 16)) | (0xff000000 & (bytes[3] << 24));
    }

    /**
     * byte数组转换为long类型数据
     * @param bytes 要转换的byte数组
     * @return long
     */
    public static long getLong(byte[] bytes)
    {
        return(0xffL & (long)bytes[0]) | (0xff00L & ((long)bytes[1] << 8)) | (0xff0000L & ((long)bytes[2] << 16)) | (0xff000000L & ((long)bytes[3] << 24))
                | (0xff00000000L & ((long)bytes[4] << 32)) | (0xff0000000000L & ((long)bytes[5] << 40)) | (0xff000000000000L & ((long)bytes[6] << 48)) | (0xff00000000000000L & ((long)bytes[7] << 56));
    }

    /**
     * byte数组转换为float类型数据
     * @param bytes 要转换byte数组
     * @return float
     */
    public static float getFloat(byte[] bytes)
    {
        return Float.intBitsToFloat(getInt(bytes));
    }

    /**
     * byte数组转换为double类型数据
     * @param bytes 要转换byte数组
     * @return double
     */
    public static double getDouble(byte[] bytes)
    {
        long l = getLong(bytes);
        return Double.longBitsToDouble(l);
    }

    /**
     *  设置不同编码格式返回数据
     * @param bytes 要转换byte数组
     * @param charsetName 设置的编码格式
     * @return string
     */
    public static String getString(byte[] bytes, String charsetName)
    {
        return new String(bytes, Charset.forName(charsetName));
    }

    /**
     * byte数组转换为String类型数据
     * @param bytes 要转换byte数组
     * @return GBK类型的string
     */
    public static String getString(byte[] bytes)
    {
        return getString(bytes, "GBK");
    }

    /**
     * 截取需要的byte数组
     * @param bytes 原byte数组
     * @param off 开始
     * @param len 截取长度
     * @return 截取后的byte数组
     */
    public static byte[] copyBytes(byte[] bytes, int off, int len) {
        byte[] data = new byte[len];
        System.arraycopy(bytes, off, data, 0, len);
        return data;
    }
}
