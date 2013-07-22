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
        int matrixH = (int)matrix.width;
        int matrixW = matrixH;
                
        int nbSamplePerLine = zoomX;
        int p = posX / nbSamplePerLine;
        int pY = posY / nbSamplePerLine;
        
        // Quadrillage
        g2d.setColor(Color.DARK_GRAY);
        // Lignes horizontales
        for (int i = h+pY; i>0; i = i - Fenetre.pasGrille){
            g2d.drawLine(0, i, w, i);
        }
        //Lignes verticales
        for (int i = 0-p; i<w+p; i = i + Fenetre.pasGrille){
            g2d.drawLine(i, 0, i, h+pY);
        }
        
        // Triangle
        int[] x = new int[3];
        x[0] = 0 - p; 
        x[1] = matrixW - p; 
        x[2] = matrixW - p;
        
        int[] y = new int[3];
        y[0] = h+pY;
        y[1] = 0 - (matrixH - h+pY);
        y[2] = h+pY;
        
        g2d.setColor(Color.WHITE);
        g2d.drawPolygon(new Polygon(x, y, x.length));     
        
        
        // Remplir la zone interdite
        x = new int[3];
        x[0] = 0 - p; 
        x[1] = 0 - p; 
        x[2] = matrixW - p;
        
        y = new int[3];
        y[0] = h+pY;
        y[1] = 0 - (matrixH - h+pY); 
        y[2] = 0 - (matrixH - h+pY);
        
        g2d.setColor(Color.BLACK);
        g2d.fillPolygon(new Polygon(x, y, x.length));   
        
        // Segments
        for (int i = 0;i<matrix.seg.size();i++){
            Segment segm = (Segment) matrix.seg.get(i);
            
            Point ptSegm1 = new Point ((int)segm.x1,(int)segm.y1);
            Point ptSegm2 = new Point ((int)segm.x2,(int)segm.y2);
            
            Point ptPan1 = ObtenirPtPanViaPxMatrix(ptSegm1);
            Point ptPan2 = ObtenirPtPanViaPxMatrix(ptSegm2);
            
            int x1 = ptPan1.x;
            int y1 = ptPan1.y;
            int x2 = ptPan2.x;
            int y2 = ptPan2.y;
                
            g2d.setColor(segm.color);
            g2d.drawLine(x1, y1, x2, y2);

            g2d.setColor(Color.cyan);
            g2d.drawLine(x1, y1, x1 - (h- y1) , h);
            g2d.drawLine(x1, y1, x1, h);
            g2d.drawLine(x2, y2,  x2 - (h- y2),h);
            g2d.drawLine(x2, y2, x2, h);

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
            int clipPos = player.clip.getFramePosition();
            p = (clipPos-posX) / nbSamplePerLine;
            if(zoomX>1){
                g2d.drawLine(p, 0, p, h);  
            }
         }
         
    }
    
    public int ObtenirPositionARejouer(int p, int idSeg){
        int pp;
        
        Segment segm = (Segment) matrix.seg.get(idSeg);
        
        int p1x = (int)(segm.x1);
        int p1y = (int)(segm.y1);
        int p2x = (int)(segm.x2);
        int p2y = (int)(segm.y2);

        pp = p - ( p * ((p2y-p1y)/(p2x-p1x)) + p1y - p1x * ((p2y-p1y)/(p2x-p1x)));
                
        return pp;
    }
    
}