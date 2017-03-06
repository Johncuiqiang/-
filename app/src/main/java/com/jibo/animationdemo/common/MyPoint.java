package com.jibo.animationdemo.common;

import android.graphics.Point;

/**
 * Created by acer on 2016/11/9.
 */

public class MyPoint extends Point {

    public int ox;//用于记录原始坐标x
    public int oy;//用于记录原始坐标y
    public String pointName;

    public MyPoint(int x, int y) {
        super(x, y);
        this.ox = x;
        this.oy = y;
    }

    public MyPoint(int x, int y, String pointName) {
        super(x, y);
        this.ox = x;
        this.oy = y;
        this.pointName = pointName;
    }

    public String getPointName() {
        return pointName;
    }
}
