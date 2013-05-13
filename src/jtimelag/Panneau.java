package jtimelag;

import java.awt.*;
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
        
        // Dessiner Triangle base
        g2d.setColor(Color.black);
        g2d.drawLine(0, this.getHeight(), this.getHeight(), 0);
        g2d.drawLine(0, this.getHeight(), 0, this.getHeight());
        g2d.drawLine(this.getHeight(), 0, this.getHeight(), this.getHeight());
        
        // Dessiner segments
        for (int i = 0;i<Fenetre.seg.size();i++){
           Segment segm = (Segment) Fenetre.seg.get(i);
           g2d.setColor(Color.MAGENTA);
           g2d.drawLine(segm.x1, segm.y1, segm.x2, segm.y2);
           
           g2d.setColor(Color.cyan);
           g2d.drawLine(segm.x1, segm.y1, segm.x1 - (this.getHeight()- segm.y1) , this.getHeight());
           g2d.drawLine(segm.x1, segm.y1, segm.x1, this.getHeight());
           g2d.drawLine(segm.x2, segm.y2,  segm.x2 - (this.getHeight()- segm.y2), this.getHeight());
           g2d.drawLine(segm.x2, segm.y2, segm.x2, this.getHeight());
           
           g2d.setColor(Color.GREEN);
           g2d.drawRect(segm.x1-3,segm.y1-3, 6, 6);
           g2d.drawRect(segm.x2-3,segm.y2-3, 6, 6);
        }
        
        // Waveform
         if (Fenetre.wavSamplesLoader != null){         
                g2d.setColor(Color.RED);
                int nbSamplePerLine = (int)(wavSamplesLoader.audioInputStream.getFrameLength() / this.getHeight());
                int y1,y2;  
                for (int i = 0; i < this.getHeight();i++){
                    double wavSamples[] = wavSamplesLoader.getAudioSamples(nbSamplePerLine);
                    y1=0;
                    y2=0;
                    
                    for(int j = 0; (j < wavSamples.length); j++){
                        int yValue = (int)(wavSamples[j] * this.getHeight());
                        if(yValue > y1){
                             y1 = yValue ;
                        }
                        if(yValue < y2) {
                             y2 = yValue;
                        }
                    }
                    g2d.setColor(Color.RED);
                    g2d.drawLine(i, y1/6 + this.getHeight() / 2, i, y2/6 + this.getHeight() / 2);
                }
         }

    }

}