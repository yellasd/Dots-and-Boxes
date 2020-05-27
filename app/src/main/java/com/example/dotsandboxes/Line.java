package com.example.dotsandboxes;

import android.graphics.Point;

import java.util.ArrayList;

class Line {
    int row=0,column=0;
    boolean isHorizontal=false;
    ArrayList<Point> arrayList;
    Line(){}
    Line(int row, int column, boolean isHorizontal){
        this.row=row;
        this.column=column;
        this.isHorizontal=isHorizontal;

        arrayList=new ArrayList<>(0);
    }
}
