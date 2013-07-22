package jtimelag;

import java.awt.Color;

public class Segment {
    long x1, y1, x2, y2;
    Color color;
    boolean p1Selected;
    boolean p2Selected;
     
    public Segment(long x1,long y1,long x2,long y2, Color color, boolean p1Selected, boolean p2Selected){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.p1Selected = p1Selected;
        this.p2Selected = p2Selected;
    }
    
}
