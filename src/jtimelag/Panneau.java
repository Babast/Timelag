package jtimelag;

import java.awt.*;
import javax.swing.*;
import static jtimelag.Fenetre.*;

public class Panneau extends JPanel {
    Matrix matrix;
    
    Panneau(){
        matrix = new Matrix(this.getWidth(),this.getWidth());   
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);
        
        int h = this.getHeight();
        int w = this.getWidth();
        
        // Quadrillage
        g2d.setColor(Color.DARK_GRAY);
        // Lignes horizontales
        for (int i = h; i>0; i = i - Fenetre.pasGrille){
            g2d.drawLine(0, i, w, i);
        }
        //Lignes verticales
        for (int i = 0; i<w; i = i + Fenetre.pasGrille){
            g2d.drawLine(i, 0, i, h);
        }
        
        // Triangle
        int[] x = new int[3];
        x[0]=0; 
        x[1]=(int)matrix.width;//w; 
        x[2]=(int)matrix.width;//w;
        
        int[] y = new int[3];
        y[0]=h;
        y[1]=h - (int)matrix.width;//w; 
        y[2]=h;
        
        g2d.setColor(Color.WHITE);
        g2d.drawPolygon(new Polygon(x, y, x.length));     
        
        
        // Remplir la zone interdite
        x = new int[4];
        x[0]=0; 
        x[1]=0; 
        x[2]=(int)matrix.width;//w;
        x[3]=(int)matrix.width;//w;
        
        y = new int[4];
        y[0]=h;
        y[1]=0; 
        y[2]=0;
        y[3]=h-(int)matrix.width;//w;
        
        g2d.setColor(Color.BLACK);
        g2d.fillPolygon(new Polygon(x, y, x.length));   
        
        // Segments
        for (int i = 0;i<matrix.seg.size();i++){
                Segment segm = (Segment) matrix.seg.get(i);
                int x1 = (int)(segm.x1 * w);
                int y1 = (int)(segm.y1 * w -(w-h));
                int x2 = (int)(segm.x2 * w);
                int y2 = (int)(segm.y2 * w -(w-h));
                
                g2d.setColor(Color.MAGENTA);
                g2d.drawLine(x1, y1, x2, y2);

                g2d.setColor(Color.cyan);
                g2d.drawLine(x1, y1, x1 - (h- y1) , h);
                g2d.drawLine(x1, y1, x1, h);
                g2d.drawLine(x2, y2,  x2 - (h- y2),h);
                g2d.drawLine(x2, y2, x2, h);

                g2d.setColor(Color.ORANGE);
                g2d.drawLine(x1 - (h- y1), h , x1 - (h- y1), h);
                g2d.drawLine(x2 - (h- y2), h, x2 - (h- y2), h);
                g2d.setColor(Color.RED);
                g2d.drawLine(x1, h, x1, h);
                g2d.drawLine(x2, h, x2, h);

                g2d.setColor(Color.GREEN);
                if(segm.p1Selected){
                    g2d.drawRect(x1-3,y1-3, 6, 6);
                }
                else if(segm.p2Selected){
                    g2d.drawRect(x2-3,y2-3, 6, 6);
                }
            }
        
         if (wavSamplesLoader != null){
            // Curseur de lecture
            g2d.setColor(Color.GREEN);
            int nbSamplePerLine = zoomX;
            int clipPos = player.clip.getFramePosition();
            int p = (clipPos-posX) / nbSamplePerLine;
            if(zoomX>1){
                g2d.drawLine(p, 0, p, h);  
            }
         }         
    }
    
}