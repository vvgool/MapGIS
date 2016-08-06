package com.wiesen.test.provider;

import android.os.Environment;

import java.io.File;


public class FileUtils {

    /**
     * 获取路径下所有文件夹
     * @param path 路径
     * @return
     */
    public static File[] getFileList(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            return file.listFiles();
        }
        return null;
    }

    /**
     * 获取文件夹下所有的文件夹
     * @param file 文件夹名
     * @return
     */
    public static File[] getFileList(File file) {
        if (file.isDirectory()) {
            return file.listFiles();
        }
        return null;
    }

    public static File getFile(File file) {
        if (file.isFile()) {
            return file;
        }
        return null;
    }

    /**
     * 判断是否有sd卡
     * @return
     */
    public static boolean isExistStorage() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     *  根据路径创建文件夹
     * @param path
     */
    public static void createFile(String path){
        File file = new File(path);
        if (!file.exists()){
            file.mkdirs();
        }
    }

    /**
     * 返回系统自带内存的路径
     * @return
     */
    public static String getStoragePath() {
        if (isExistStorage()) {
            return Environment.getExternalStorageDirectory().getPath();
        }
        return "";
    }


    /**
     * byte数组转int型数据
     * @param b
     * @return
     */
    public static int byteToInt(byte[] b) {

        int mask = 0xff;
        int temp = 0;
        int n = 0;
        for (int i = 0; i < b.length; i++) {
            n <<= 8;
            temp = b[i] & mask;
            n |= temp;
        }
        return n;
    }

    public static byte[] getBytesShort(short data) {
        byte[] bytes = new byte[2];
        bytes[1] = (byte) (data & 0xff);
        bytes[0] = (byte) ((data & 0xff00) >> 8);

        return bytes;
    }

    public static byte[] copybytes(byte[] byte1, byte[] byte2) {
        byte[] date = null;
        if (byte1 != null && byte2 != null) {
            date = new byte[byte1.length + byte2.length];
            System.arraycopy(byte1, 0, date, 0, byte1.length);
            System.arraycopy(byte2, 0, date, byte1.length, byte2.length);
        }
        return date;
    }

    /**
     * 根据新老路径更改文件夹的名字
     * @param oldPath
     * @param newPath
     * @return
     */
    public static boolean reNameFile(String oldPath, String newPath){
        File newFile = new File(newPath);
        if (!oldPath.equals(newPath)){
            if (newFile.exists()){
                newFile.delete();
            }
        }

        File oldFile = new File(oldPath);
        if (oldFile.renameTo(newFile)){
            return true;
        }
        return false;
    }
}
