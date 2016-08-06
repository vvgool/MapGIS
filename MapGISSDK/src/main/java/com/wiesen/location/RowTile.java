package com.wiesen.location;

import java.io.Serializable;


public class RowTile implements Serializable {
    public int x;
    public int y;
    public int zoom;

    /**
     * 构造方法
     * @param x 列号
     * @param y 行号
     * @param zoom 层级号
     */
    public RowTile(int x, int y, int zoom) {
        this.x = x;
        this.y = y;
        this.zoom = zoom;
    }

    @Override
    public String toString() {
        return zoom + "_" + x + "_" + y;
    }
}
