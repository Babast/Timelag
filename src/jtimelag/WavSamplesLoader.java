package jtimelag;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class WavSamplesLoader {
    
    AudioInputStream audioInputStream;
    
    WavSamplesLoader(File file){
        try {  
            FileInputStream fs = new FileInputStream(file.getPath());
            BufferedInputStream myStream = new BufferedInputStream(fs);
            audioInputStream = AudioSystem.getAudioInputStream(myStream); 
            if (audioInputStream.markSupported()) { 
                audioInputStream.mark(Integer.MAX_VALUE); 
            }
                        
        } catch (UnsupportedAudioFileException | IOException ex) {
            Logger.getLogger(WavSamplesLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public double[] getAudioSamples(int nbSamples){
        
        double audioFrames[];
        
        int bytesPerFrame = audioInputStream.getFormat().getFrameSize();
        int numBytes = nbSamples * bytesPerFrame; 
        int channels = audioInputStream.getFormat().getChannels();

        byte[] audioBytes = new byte[numBytes];
        
        try {
            audioInputStream.read(audioBytes, 0, numBytes);
        } catch (IOException ex) {
            Logger.getLogger(WavSamplesLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        audioFrames = new double[audioBytes.length/(bytesPerFrame/channels)];
        for (int i= 0; i < audioFrames.length; i++) {
            audioFrames[i]= ((short) (((audioBytes[2*i+1] & 0xFF) << 8) + (audioBytes[2*i] & 0xFF))) / 32768.0;
        }

        return audioFrames;
        
    }
    
    
}
