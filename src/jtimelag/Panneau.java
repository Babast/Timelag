package jtimelag;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import static jtimelag.Fenetre.wavSamplesLoader;

public class Panneau extends JPanel {
    boolean refreshWaveForm;
    BufferedImage waveForm;
    
    Panneau(){
        refreshWaveForm = true;
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                panneauComponentResized(evt);
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);
        
        if (Fenetre.wavSamplesLoader != null){
            
            // Waveform
            if(refreshWaveForm){
                // Recalcul du waveform et stockage dans le bufferedImage
                waveForm = new BufferedImage(this.getWidth(), 60, BufferedImage.TYPE_INT_RGB);  
                Graphics2D gWaveForm = waveForm.createGraphics(); 
               
                gWaveForm.setColor(Color.DARK_GRAY);
                gWaveForm.fillRect(0, 0, waveForm.getWidth(), waveForm.getHeight());

                if (wavSamplesLoader.audioInputStream.markSupported()) {
                    try {
                        wavSamplesLoader.audioInputStream.reset();
                    } catch (IOException ex) {
                        Logger.getLogger(Panneau.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                gWaveForm.setColor(Color.RED);
                int nbSamplePerLine = (int)(wavSamplesLoader.audioInputStream.getFrameLength() / waveForm.getWidth());
                int y1,y2;

                for (int i = 0; i < waveForm.getWidth(); i++){
                    double wavSamples[] = wavSamplesLoader.getAudioSamples(nbSamplePerLine);
                    y1=0;
                    y2=0;
                    for(int j = 0; (j < wavSamples.length); j++){
                        int yValue = (int)(wavSamples[j] * waveForm.getHeight()/2);
                        if(yValue > y1){
                            y1 = yValue ;
                        }
                        if(yValue < y2) {
                            y2 = yValue;
                        }
                    }
                    int yOffset = waveForm.getHeight()/2;
                    gWaveForm.setColor(new Color(0,100,255));
                    gWaveForm.drawLine(i, y1+yOffset, i, y2+yOffset);
                    gWaveForm.setColor(new Color(0,100,150));
                    gWaveForm.drawLine(i, (y1/3)+yOffset, i, (y2/3)+yOffset);
                }
                refreshWaveForm = false;
            }
            
             g2d.drawImage(waveForm, null, 0, this.getHeight() - 60);

             // Quadrillage
            g2d.setColor(Color.DARK_GRAY);
            for (int i = 0; i<this.getHeight()-60; i = i + Fenetre.pasGrille){
                g2d.drawLine(0, i, this.getWidth(), i);
            }
            for (int i = 0; i<this.getWidth(); i = i + Fenetre.pasGrille){
                g2d.drawLine(i, 0, i, this.getHeight()-60);
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

    private void panneauComponentResized(java.awt.event.ComponentEvent evt) {
        refreshWaveForm = true;
    }
    
}