package com.wiesen.test.provider;


public class MapTileIndex {
    public int m_layer;//层号
    public int m_row;//列号
    public int m_line;//行号（图片名）
    public int m_picLen;//图片大小
    public int m_picLoc;//图片相对与数据段位置

    /**
     * 解析索引
     * @param bytes 将byte数组解析为瓦片索引信息
     */
    public void decodeIndexBytes(byte[] bytes) {
        byte[] layerName = ByteUtils.copyBytes(bytes, 0, 1);
        byte[] lineName = ByteUtils.copyBytes(bytes, 1, 4);
        byte[] picName = ByteUtils.copyBytes(bytes, 5, 4);
        byte[] picLoc = ByteUtils.copyBytes(bytes, 9, 4);
        byte[] picLen = ByteUtils.copyBytes(bytes, 13, 4);
        m_layer = FileUtils.byteToInt(layerName);
        m_row = FileUtils.byteToInt(lineName);
        m_line = FileUtils.byteToInt(picName);
        m_picLen = FileUtils.byteToInt(picLen);
        m_picLoc = FileUtils.byteToInt(picLoc);
    }

    public String getKey(){
        return m_layer+"_"+m_row+"_"+m_line;
    }

    public TileIndex getValue(){
        return new TileIndex(m_picLoc,m_picLen);
    }
}
