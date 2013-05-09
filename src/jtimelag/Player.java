package jtimelag;

import java.io.*;
import javax.sound.sampled.*;

public class Player {
   Clip clip;
   
   Player(File file) throws IOException, LineUnavailableException{
       try {
           AudioInputStream audioInputStream;
           audioInputStream = AudioSystem.getAudioInputStream(file);
           clip = AudioSystem.getClip();
           clip.open(audioInputStream);
       } catch (UnsupportedAudioFileException | IOException ex) {
           System.out.println(ex.getMessage());
       }
   }
   
}
