package jtimelag;

import java.io.File;
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
            audioInputStream = AudioSystem.getAudioInputStream(file);

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
        
        int N= audioBytes.length;
        audioFrames = new double[N/(bytesPerFrame/channels)];
        for (int i= 0; i < audioFrames.length; i++) {
            audioFrames[i]= ((short) (((audioBytes[2*i+1] & 0xFF) << 8) + (audioBytes[2*i] & 0xFF))) / 32768.0;
        }

        return audioFrames;
    }
    
    
}
