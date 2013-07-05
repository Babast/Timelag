package jtimelag;

import java.util.ArrayList;

public class Matrix {
   long height;
   long width;
   
   ArrayList seg;   
            
    Matrix(long height, long width){
        this.seg = new ArrayList();
        this.height = height;
        this.width = width;
    }
    
}
