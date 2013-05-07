package timelag;

public class Segment {
    int x1, y1, x2, y2;
    boolean selected;
    
    public Segment(int x1,int y1,int x2,int y2, boolean selected){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.selected = selected;
    }

}
