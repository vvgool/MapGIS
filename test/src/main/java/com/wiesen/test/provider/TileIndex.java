package com.wiesen.test.provider;

/**
 * Copyright  @2016 珞珈德毅. All rights reserved.
 * <p>Description: 自定义瓦片索引类</p>
 * @ClassName      TileIndex
 * @Package        com.ljdy.provider
 * @Author         vvgool
 * @Time           2016/4/14
 */
public class TileIndex {
    public int iOffset;      //瓦片起始位置
    public int iTileLen;     //瓦片大小

    /**
     * 构造方法
     * @param off 瓦片起始位置
     * @param len 瓦片大小
     */
    public TileIndex(int off, int len) {
        this.iOffset = off;
        this.iTileLen = len;
    }
}
