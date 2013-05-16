package jtimelag;

public class Segment {
    double x1, y1, x2, y2;
    boolean p1Selected;
    boolean p2Selected;
     
    public Segment(double x1,double y1,double x2,double y2, boolean p1Selected, boolean p2Selected){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.p1Selected = p1Selected;
        this.p2Selected = p2Selected;
    }
    
}
