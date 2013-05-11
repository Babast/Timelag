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
          if (bytesPerFrame == AudioSystem.NOT_SPECIFIED) {
            // some audio formats may have unspecified frame size
            // in that case we may read any amount of bytes
            bytesPerFrame = 1;
          } 
          // Set an arbitrary buffer size of 1024 frames.
          int numBytes = (int)audioInputStream.getFrameLength(); //1024 * bytesPerFrame; 
          byte[] audioBytes = new byte[numBytes];
          try {
            int numBytesRead = 0;
            int numFramesRead = 0;
            // Try to read numBytes bytes from the file.
            while ((numBytesRead = 
              audioInputStream.read(audioBytes)) != -1) {
              // Calculate the number of frames actually read.
              numFramesRead = numBytesRead / bytesPerFrame;
              totalFramesRead += numFramesRead;
              // Here, do something useful with the audio data that's 
              // now in the audioBytes array...
            }
            
            audioFrames = new int[audioBytes.length/bytesPerFrame];
            
            for (int i = 0; i < audioBytes.length/bytesPerFrame; i++) { // read in the samples
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
