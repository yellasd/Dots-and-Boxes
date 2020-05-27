package com.example.dotsandboxes;

import android.graphics.Point;

import java.util.ArrayList;

public class Turn {
    Line line;
    int who;
    ArrayList<Point> boxes;
    Turn(Line line,int who){
        this.line=line;
        this.who=who;
    }
}
