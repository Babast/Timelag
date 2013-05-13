package jtimelag;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import static jtimelag.Fenetre.wavSamplesLoader;

public class WaveForm extends JPanel{
    
    @Override
    protected void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);

        // Waveform
         if (Fenetre.wavSamplesLoader != null){     
             if (wavSamplesLoader.audioInputStream.markSupported()) {
                 try { 
                     wavSamplesLoader.audioInputStream.reset();
                 } catch (IOException ex) {
                     Logger.getLogger(Panneau.class.getName()).log(Level.SEVERE, null, ex);
                 }
             }
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

