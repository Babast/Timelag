package jtimelag;

import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import static jtimelag.Fenetre.wavSamplesLoader;

public class Panneau extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);
        
        if (Fenetre.wavSamplesLoader != null){
            // Waveform
            int yOffset = this.getHeight() - 30;
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(0, yOffset-30, this.getWidth(), yOffset*2);

            if (wavSamplesLoader.audioInputStream.markSupported()) {
                try {
                    wavSamplesLoader.audioInputStream.reset();
                } catch (IOException ex) {
                    Logger.getLogger(Panneau.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            g2d.setColor(Color.RED);
            int nbSamplePerLine = (int)(wavSamplesLoader.audioInputStream.getFrameLength() / this.getWidth());
            int y1,y2;

            for (int i = 0; i < this.getWidth();i++){
                double wavSamples[] = wavSamplesLoader.getAudioSamples(nbSamplePerLine);
                y1=0;
                y2=0;
                for(int j = 0; (j < wavSamples.length); j++){
                    int yValue = (int)(wavSamples[j] * 30);
                    if(yValue > y1){
                        y1 = yValue ;
                    }
                    if(yValue < y2) {
                        y2 = yValue;
                    }
                }

                g2d.setColor(new Color(0,100,255));
                g2d.drawLine(i, y1 + yOffset, i, y2 + yOffset);
                g2d.setColor(new Color(0,100,150));
                g2d.drawLine(i, y1/3 + yOffset, i, y2/3 + yOffset);
            }

            // Dessiner Triangle base
            int[] x = new int[3];
            int[] y = new int[3];
            int hauteurTriangle = this.getHeight()-60;
            
            x[0]=0; 
            x[1]=this.getWidth(); 
            x[2]=this.getWidth();
            
            y[0]=hauteurTriangle; 
            y[1]=hauteurTriangle - this.getWidth(); 
            y[2]=hauteurTriangle;
            
            g2d.setColor(Color.WHITE);
            g2d.drawPolygon(new Polygon(x, y, x.length));     
            
            // Remplir la zone interdite
            x = new int[4];
            y = new int[4];
            
            x[0]=0; 
            x[1]=this.getWidth(); 
            x[2]=this.getWidth();
            x[3]=0;
            
            y[0]=hauteurTriangle; 
            y[1]=hauteurTriangle - this.getWidth(); 
            y[2]=hauteurTriangle - this.getHeight();
            y[3]=0;
            
            g2d.setColor(Color.BLACK);
            g2d.fillPolygon(new Polygon(x, y, x.length));     


            
           

            
            // Dessiner segments
            for (int i = 0;i<Fenetre.seg.size();i++){
                Segment segm = (Segment) Fenetre.seg.get(i);
                g2d.setColor(Color.MAGENTA);
                g2d.drawLine(segm.x1, segm.y1, segm.x2, segm.y2);

                g2d.setColor(Color.cyan);
                g2d.drawLine(segm.x1, segm.y1, segm.x1 - (hauteurTriangle- segm.y1) , hauteurTriangle);
                g2d.drawLine(segm.x1, segm.y1, segm.x1, hauteurTriangle);
                g2d.drawLine(segm.x2, segm.y2,  segm.x2 - (hauteurTriangle- segm.y2),hauteurTriangle);
                g2d.drawLine(segm.x2, segm.y2, segm.x2, hauteurTriangle);

                g2d.setColor(Color.ORANGE);
                g2d.drawLine(segm.x1 - (hauteurTriangle- segm.y1), hauteurTriangle , segm.x1 - (hauteurTriangle- segm.y1), this.getHeight());
                g2d.drawLine(segm.x2 - (hauteurTriangle- segm.y2), hauteurTriangle, segm.x2 - (hauteurTriangle- segm.y2), this.getHeight());
                g2d.setColor(Color.RED);
                g2d.drawLine(segm.x1, hauteurTriangle, segm.x1, this.getHeight());
                g2d.drawLine(segm.x2, hauteurTriangle, segm.x2, this.getHeight());

                g2d.setColor(Color.GREEN);
                g2d.drawRect(segm.x1-3,segm.y1-3, 6, 6);
                g2d.drawRect(segm.x2-3,segm.y2-3, 6, 6);
            }
        }
    }

}