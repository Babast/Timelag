package jtimelag;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import static jtimelag.Fenetre.*;

public class WaveForm extends JPanel{
    boolean refreshWaveForm;
    BufferedImage bufWaveForm;
    
    WaveForm(){
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
        
        int h = this.getHeight();
        int w = this.getWidth();
        
        int pY = posY / zoomX;
         
        if (wavSamplesLoader != null){
            
            Fenetre.jsZoomX.setMaximum((int)(wavSamplesLoader.audioInputStream.getFrameLength() / w));
            
            // Waveform
            if(refreshWaveForm){
                // Recalcul du waveform et stockage dans le bufferedImage
                bufWaveForm = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);  
                Graphics2D gWaveForm = bufWaveForm.createGraphics(); 
               
                gWaveForm.setColor(Color.DARK_GRAY);
                gWaveForm.fillRect(0, 0, w, h);

                if (wavSamplesLoader.audioInputStream.markSupported()) {
                    try {
                        wavSamplesLoader.audioInputStream.reset();
                        wavSamplesLoader.audioInputStream.skip(wavSamplesLoader.audioInputStream.getFormat().getFrameSize()*posX);
                    } catch (IOException ex) {
                        Logger.getLogger(Panneau.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                gWaveForm.setColor(Color.RED);
                int nbSamplePerLine = zoomX;
                int y1,y2;

                for (int i = 0; i < w; i++){
                    double wavSamples[] = wavSamplesLoader.getAudioSamples(nbSamplePerLine);
                    y1=0;
                    y2=0;
                    for(int j = 0; (j < wavSamples.length); j++){
                        int yValue = (int)(wavSamples[j] * bufWaveForm.getHeight()/2 * zoomY);
                        if(yValue > y1){
                            y1 = yValue ;
                        }
                        if(yValue < y2) {
                            y2 = yValue;
                        }
                    }
                    int yOffset = h/2;
                    gWaveForm.setColor(new Color(0,100,255));
                    gWaveForm.drawLine(i, y1+yOffset, i, y2+yOffset);
                    gWaveForm.setColor(new Color(0,100,150));
                    gWaveForm.drawLine(i, (y1/3)+yOffset, i, (y2/3)+yOffset);
                }
                refreshWaveForm = false;
            }
            
            g2d.drawImage(bufWaveForm, null, 0, 0);
            
            
            // Curseur de lecture
            g2d.setColor(Color.GREEN);
            int nbSamplePerLine = zoomX;
            int clipPos = player.clip.getFramePosition();
            int p = (clipPos-posX) / nbSamplePerLine;
            if(p>w && player.clip.isRunning()){
                Fenetre.jsPosX.setValue(Fenetre.jsPosX.getValue()+p*nbSamplePerLine);
                return;
            }
            if(zoomX>1){
                g2d.drawLine(p, 0, p, w);  
            }

            
            // Curseurs segments
            for (int i = 0; i<pan.matrix.seg.size(); i++){
                Segment segm = (Segment) pan.matrix.seg.get(i);
                int hPan = Fenetre.pan.getHeight();

                Point ptSegm1 = new Point ((int)segm.x1,(int)segm.y1);
                Point ptSegm2 = new Point ((int)segm.x2,(int)segm.y2);

                Point ptPan1 = ObtenirPtPanViaPxMatrix(ptSegm1);
                Point ptPan2 = ObtenirPtPanViaPxMatrix(ptSegm2);

                int x1 = ptPan1.x;
                int y1 = ptPan1.y;
                int x2 = ptPan2.x;
                int y2 = ptPan2.y;
            
                g2d.setColor(Color.ORANGE);
                g2d.drawLine(x1 - (hPan+pY- y1), 0 , x1 - (hPan+pY- y1), h);
                g2d.drawLine(x2 - (hPan+pY- y2), 0, x2 - (hPan+pY- y2), h);
                g2d.setColor(Color.RED);
                g2d.drawLine(x1, 0, x1, h);
                g2d.drawLine(x2, 0, x2, h);
            }
        }
    }
    
    private void panneauComponentResized(java.awt.event.ComponentEvent evt) {
        refreshWaveForm = true;
    }
    
}
