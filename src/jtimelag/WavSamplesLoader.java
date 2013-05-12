package jtimelag;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class WavSamplesLoader {
    
    int audioFrames[];
    
    WavSamplesLoader(File file){
        int totalFramesRead = 0;
        try {
          AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
          int bytesPerFrame = audioInputStream.getFormat().getFrameSize();
          int numBytes = (int)audioInputStream.getFrameLength(); 
          byte[] audioBytes = new byte[numBytes];
          try {
            int numBytesRead = 0;
            int numFramesRead = 0;
            while ((numBytesRead = 
              audioInputStream.read(audioBytes)) != -1) {
              numFramesRead = numBytesRead / bytesPerFrame;
              totalFramesRead += numFramesRead;
            }
            
            audioFrames = new int[audioBytes.length/bytesPerFrame];
            
            for (int i = 0; i < audioBytes.length/bytesPerFrame; i++) { 
              //16 bit, litte endian (the first is the least significant)
                int LSB=(int)audioBytes[i*2] & 0xff;
                int MSB=(int)audioBytes[1+i*2] & 0xff;

                audioFrames[i]=((MSB<<8)+LSB);
                if((audioBytes[i*2+1]&0x80)!=0)//Check the sign
                    audioFrames[i]=-(65536-audioFrames[i]);
            }

          } catch (Exception ex) { 
            // Handle the error...
          }
        } catch (UnsupportedAudioFileException | IOException e) {
          // Handle the error...
        }
    }
    
}
