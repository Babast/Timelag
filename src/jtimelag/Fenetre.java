package jtimelag;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;
import javax.swing.*;

public class Fenetre extends JFrame {
    JRadioButton radioButtonSegment; 
    JButton btPlay = new JButton();
    JButton btLoad = new JButton();
    JButton btStop = new JButton();
    JButton btPause = new JButton();
    JPanel pan;
    JPanel waveForm;
    
    public static String outil;
    public static ArrayList seg = new ArrayList();
    public static ArrayList matrix = new ArrayList();
     
    final JFileChooser fc = new JFileChooser();
    
    File file;
    Player player;
    public static WavSamplesLoader wavSamplesLoader;
    
    Fenetre(){
        
        // Parametrage de la fenetre
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Timelag (www.timelag.fr)");
        setSize(600,500);
        
        // Parametrage des composants:
        pan = new Panneau();
        pan.setBackground(Color.GRAY);
        
        radioButtonSegment = new JRadioButton();
        radioButtonSegment.setBackground(Color.LIGHT_GRAY);
        radioButtonSegment.setText("Segment");
        
        btLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jtimelag/open.png")));
        btPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jtimelag/play.png")));
        btStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jtimelag/stop.png")));
        btPause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jtimelag/pause.png")));
        
        
        // Positions des composants:
        JPanel panneauOutils = new JPanel(new GridLayout(5,1,5,5));
        panneauOutils.setBackground(Color.LIGHT_GRAY);
        panneauOutils.add(radioButtonSegment);
        
        JPanel panneauPlayer = new JPanel(new GridLayout(1,4,5,5));
        panneauPlayer.add(btLoad);
        panneauPlayer.add(btPlay);
        panneauPlayer.add(btPause);
        panneauPlayer.add(btStop);
        
        JPanel panneauDessin = new JPanel();
        panneauDessin.setLayout(new GridLayout(1,1,0,2));
       
        panneauDessin.add(pan);
        
        setLayout(new BorderLayout (5,5));
        add(panneauOutils, BorderLayout.WEST);
        add(panneauDessin, BorderLayout.CENTER);
        add(panneauPlayer, BorderLayout.SOUTH);
        
        // Création des écouteurs d'événements:
        radioButtonSegment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                radioButtonSegmentActionPerformed(evt);
            }
        });

        pan.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent evt) {
                panMouseMoved(evt);
            }
        });
        
        pan.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                panMousePressed(evt);
            }
        });
        
        btLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    btLoadActionPerformed(evt);
                } catch ( IOException | LineUnavailableException ex) {
                    Logger.getLogger(Fenetre.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
       
        btPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btPlayActionPerformed(evt);
            }
        });
        
        btStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    btStopActionPerformed(evt);
                } catch (IOException | LineUnavailableException ex) {
                    Logger.getLogger(Fenetre.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        btPause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btPauseActionPerformed(evt);
            }
        });
    }
    
    private void btLoadActionPerformed(ActionEvent evt) throws IOException, LineUnavailableException {
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            player = new Player(file);
            wavSamplesLoader = new WavSamplesLoader(file);
            matrix.clear();
            matrix.add(new Matrix(wavSamplesLoader.audioInputStream.available() / wavSamplesLoader.audioInputStream.getFormat().getFrameSize()));
            repaint();
        }
    }   
        
    private void btPlayActionPerformed(ActionEvent evt) {
        player.clip.start();
    }  
    
    private void btStopActionPerformed(ActionEvent evt) throws IOException, LineUnavailableException {
        player.clip.stop();
        player = new Player(file);
    }  
    
    private void btPauseActionPerformed(ActionEvent evt) {
        player.clip.stop();
    }  
        
    private void radioButtonSegmentActionPerformed(ActionEvent evt) {
         if (radioButtonSegment.isSelected()){
             outil = "Segment";
         }
         else{
             outil = "";
         }
    }         
    
    public boolean CheckArea(Point p){
        boolean check = true;
        int x = p.x;
        int y = p.y;
        //Offset y
        int yOff = y + pan.getWidth()-pan.getHeight()+60;
        
        if (x+yOff < pan.getWidth() || y > pan.getHeight()-60 ){
            check = false;
        }
        return check;
    }
     
    public void panMousePressed (MouseEvent e){       
        if (CheckArea(e.getPoint())){
            if (e.getClickCount() == 2){
                if (outil != null){
                    switch (outil){
                        case "Segment":
                            seg.add(new Segment(e.getX(),e.getY(),e.getX(),e.getY(),false,true));
                            break;
                    }
                }
            }
            else if (e.getClickCount() == 1){
                if (outil != null){
                    switch (outil){
                        case "Segment":
                            for (int i = 0;i<seg.size();i++){
                                Segment segm = (Segment) seg.get(i);
                                if (segm.p1Selected){
                                    segm.p1Selected = false;
                                }
                                else if (segm.p2Selected){
                                    segm.p2Selected = false;
                                }
                                else{
                                    if(e.getX() >= segm.x1-3 && e.getX() <= segm.x1+3 && e.getY() >= segm.y1-3 && e.getY() <= segm.y1+3){
                                        segm.p1Selected = true;
                                    }
                                    else{
                                        segm.p1Selected = false;
                                    }
                                    
                                    if(e.getX() >= segm.x2-3 && e.getX() <= segm.x2+3 && e.getY() >= segm.y2-3 && e.getY() <= segm.y2+3){
                                        segm.p2Selected = true;
                                    }
                                    else{
                                        segm.p2Selected = false;
                                    }
                                }
                                seg.set(i, segm);
                            }
                    }
                    repaint();
                }
            }
        }
    }
    
    public void panMouseMoved (MouseEvent e){
         if (CheckArea(e.getPoint())){
             for (int i = 0;i<seg.size();i++){
                 Segment segm = (Segment) seg.get(i);
                 if(segm.p1Selected){
                     segm.x1 = e.getX();
                     segm.y1 = e.getY();
                 }
                 else if(segm.p2Selected){
                     segm.x2 = e.getX();
                     segm.y2 = e.getY();
                 }
                 seg.set(i, segm);
             }
             repaint();
         }
    }

}

