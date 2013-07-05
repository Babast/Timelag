package jtimelag;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Fenetre extends JFrame {
    JButton btOpen = new JButton();
    JButton btSave = new JButton();
    JButton btNew = new JButton();
    JRadioButton radioButtonSegment = new JRadioButton(); 
    JRadioButton radioButtonGomme = new JRadioButton();
    JButton btPlay = new JButton();
    JButton btLoad = new JButton();
    JButton btStop = new JButton();
    JButton btPause = new JButton();
    JSplitPane splitPane = new JSplitPane();
    JSplitPane spPlayer = new JSplitPane();
    public static Panneau pan;
    public static WaveForm waveForm;
    JSpinner jsPasGrille = new JSpinner();
    public static JSlider jsZoomX = new JSlider(0,100);
    public static JSlider jsZoomY = new JSlider(0,50);
    public static JScrollBar jsPosX = new JScrollBar();
       
    Timer timer;
    
    static String outil;
    public static int pasGrille;
    public static int zoomX;
    public static int zoomY;
    public static int posX;
    
    File file;
    public static Player player;
    public static WavSamplesLoader wavSamplesLoader;
    
    Fenetre(){
        
        // Parametrage de la fenetre
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Timelag (www.timelag.fr)");
        setSize(800,600);
        
        // Initialisation des composants:
        pan = new Panneau();
        pan.setBackground(Color.GRAY);
        
        waveForm = new WaveForm();
        waveForm.setBackground(Color.GRAY);
                
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(400);
        splitPane.setLeftComponent(pan);
        splitPane.setRightComponent(waveForm);
          
        btOpen.setText("Ouvrir");       
        btSave.setText("Enregistrer");
        btNew.setText("Nouveau");  
          
        radioButtonSegment.setBackground(Color.LIGHT_GRAY);
        radioButtonSegment.setText("Segment");
        
        radioButtonGomme.setBackground(Color.LIGHT_GRAY);
        radioButtonGomme.setText("Gomme");
        
        jsPasGrille.setToolTipText("Pas de la grille");
        jsPasGrille.setValue(10);
        pasGrille = (int)jsPasGrille.getValue();
        
        jsZoomX.setToolTipText("Zoom X");
        jsZoomX.setValue(100);
        zoomX = (int)jsZoomX.getValue();
        
        jsZoomY.setToolTipText("Zoom Y");
        jsZoomY.setValue(100);
        zoomY = (int)jsZoomY.getValue();
        
        jsPosX.setToolTipText("Position X");
        jsPosX.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        posX = (int)jsPosX.getValue();
               
        btLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jtimelag/open.png")));
        btPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jtimelag/play.png")));
        btStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jtimelag/stop.png")));
        btPause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jtimelag/pause.png")));
        
        
        // Positions des composants:
        JPanel panneauOutils = new JPanel(new GridLayout(10,1,5,5));
        panneauOutils.setBackground(Color.LIGHT_GRAY);
        panneauOutils.add(btOpen);
        panneauOutils.add(btSave);
        panneauOutils.add(btNew);
        panneauOutils.add(radioButtonSegment);
        panneauOutils.add(radioButtonGomme);
        panneauOutils.add(jsPasGrille);
        panneauOutils.add(jsZoomX);
        panneauOutils.add(jsZoomY);
        
        JPanel panneauPlayer = new JPanel(new GridLayout(1,4,5,5));
        panneauPlayer.add(btLoad);
        panneauPlayer.add(btPlay);
        panneauPlayer.add(btPause);
        panneauPlayer.add(btStop);
        
        spPlayer.setOrientation(JSplitPane.VERTICAL_SPLIT);
        spPlayer.setDividerLocation(20);
        spPlayer.setLeftComponent(jsPosX);
        spPlayer.setRightComponent(panneauPlayer);
        
        JPanel panneauDessin = new JPanel();
        panneauDessin.setLayout(new GridLayout(1,1,0,2));
        panneauDessin.add(splitPane);
       
        
        setLayout(new BorderLayout (5,5));
        add(panneauOutils, BorderLayout.WEST);
        add(panneauDessin, BorderLayout.CENTER);
        add(spPlayer, BorderLayout.SOUTH);
        
        // Création des écouteurs d'événements:
        btOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    btOpenActionPerformed(evt);
                } catch ( IOException | LineUnavailableException ex) {
                    Logger.getLogger(Fenetre.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        btSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    btSaveActionPerformed(evt);
                } catch ( IOException | LineUnavailableException ex) {
                    Logger.getLogger(Fenetre.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        btNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    btNewActionPerformed(evt);
                } catch ( IOException | LineUnavailableException ex) {
                    Logger.getLogger(Fenetre.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        radioButtonSegment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                radioButtonSegmentActionPerformed(evt);
            }
        });
        
        radioButtonGomme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                radioButtonGommeActionPerformed(evt);
            }
        });
        
        jsPasGrille.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {
                jsPasGrilleStateChanged(evt);
            }
        });
        
        jsZoomX.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {
                jsZoomXStateChanged(evt);
            }
        });
        
        jsZoomY.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {
                jsZoomYStateChanged(evt);
            }
        });
        
        jsPosX.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent evt) {
                jsPosXAdjustmentValueChanged(evt);
            }
        });
     
        splitPane.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                splitPanePropertyChange(evt);
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
    
    private void splitPanePropertyChange(java.beans.PropertyChangeEvent evt) {                                           
        waveForm.refreshWaveForm = true;
    } 
    
    private void btOpenActionPerformed(ActionEvent evt) throws IOException, LineUnavailableException {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Fichier TimeLag", "lag"));
        fc.setAcceptAllFileFilterUsed(false);
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File lagFile = fc.getSelectedFile();
            Scanner in = new Scanner(new FileReader(lagFile));
            while (in.hasNext()){
                String line = in.next();
                String item[]=line.split(";");
                pan.matrix.seg.add(new Segment(Double.parseDouble(item[0]), Double.parseDouble(item[1]), Double.parseDouble(item[2]), Double.parseDouble(item[3]), false, false));
                repaint();
            }
        }
       }      
    
    private void btSaveActionPerformed(ActionEvent evt) throws IOException, LineUnavailableException {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Fichier TimeLag", "lag"));
        fc.setAcceptAllFileFilterUsed(false);
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            String filePath = f.getPath();
            if(!filePath.toLowerCase().endsWith(".lag"))
            {
                f = new File(filePath + ".lag");
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(f.getPath()), false))) {
                for(int i=0; i< pan.matrix.seg.size();i++){
                   Segment segm = (Segment) pan.matrix.seg.get(i);
                   double x1 = segm.x1;
                   double y1 = segm.y1;
                   double x2 = segm.x2;
                   double y2 = segm.y2;              
                   bw.write(x1+";"+y1+";"+x2+";"+y2);
                   if(i+1 < pan.matrix.seg.size()){
                       bw.newLine();
                   }
                }
            }
        }
    } 
    
    private void btNewActionPerformed(ActionEvent evt) throws IOException, LineUnavailableException {
        pan.matrix.seg.clear();
        repaint(); 
    }   
      
    private void btLoadActionPerformed(ActionEvent evt) throws IOException, LineUnavailableException {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Fichier WAVE", "wav"));
        fc.setAcceptAllFileFilterUsed(false);
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            player = new Player(file);
            wavSamplesLoader = new WavSamplesLoader(file);
            Fenetre.jsZoomX.setMaximum((int)(wavSamplesLoader.audioInputStream.getFrameLength() / pan.getWidth()));
            jsZoomX.setValue(jsZoomX.getMaximum());
            jsPosX.setMaximum((int)wavSamplesLoader.audioInputStream.getFrameLength());
            jsPosX.setValue(0);
            jsZoomY.setValue(0);
            pan.matrix = new Matrix(wavSamplesLoader.audioInputStream.getFrameLength(),wavSamplesLoader.audioInputStream.getFrameLength());   
            repaint();           
        }
    }   
        
    private void btPlayActionPerformed(ActionEvent evt) {
        player.clip.start();
        timer = new Timer(30,new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                TimerTickActionPerformed(evt);
            }
        });
        timer.start();
    }
    
    private void TimerTickActionPerformed(ActionEvent evt) {
           if(Fenetre.player.clip.getFramePosition() <= wavSamplesLoader.audioInputStream.getFrameLength()){
               repaint();
           }
           else{
               timer.stop();
           }
    } 
       
    private void btStopActionPerformed(ActionEvent evt) throws IOException, LineUnavailableException {
        player.clip.stop();
        timer.stop();
        player = new Player(file);
    }  
    
    private void btPauseActionPerformed(ActionEvent evt) {
        player.clip.stop();
        timer.stop();
    }  
        
    private void radioButtonSegmentActionPerformed(ActionEvent evt) {
        if (radioButtonSegment.isSelected()){
             outil = "Segment";
             radioButtonGomme.setSelected(false);
         }
         else{
             outil = "";
         }
    }
    
    private void radioButtonGommeActionPerformed(ActionEvent evt) {
         if (radioButtonGomme.isSelected()){
             outil = "Gomme";
             radioButtonSegment.setSelected(false);
         }
         else{
             outil = "";
         }
    }
    
    private void jsPasGrilleStateChanged(ChangeEvent e) {                                  
        if((int)jsPasGrille.getValue() > 0){
            pasGrille = (int)jsPasGrille.getValue();
            repaint();
        }
        else{
            jsPasGrille.setValue(1);
        }
    } 
    
    private void jsZoomXStateChanged(ChangeEvent e) {                                  
        if(jsZoomX.getValue() > 0){
            zoomX = jsZoomX.getValue();
            waveForm.refreshWaveForm = true;
            repaint();
        }
        else{
            jsZoomX.setValue(1);
        }
    }  
    
    private void jsZoomYStateChanged(ChangeEvent e) {                                  
        if(jsZoomY.getValue() > 0){
            zoomY = jsZoomY.getValue();
            waveForm.refreshWaveForm = true;
            repaint();
        }
        else{
            jsZoomY.setValue(1);
        }
    }  
    
    private void jsPosXAdjustmentValueChanged(AdjustmentEvent e) {                                  
        if((int)jsPosX.getValue() >= 0){
            posX = (int)jsPosX.getValue();
            waveForm.refreshWaveForm = true;
            repaint();
        }
        else{
            jsPosX.setValue(0);
        }
    }     
        
    public boolean CheckArea(Point p){
        // Verifier si le point est dans la zone autorisée
        boolean check = true;
        int x = p.x;
        int y = p.y;
        //Offset y
        int yOff = y + pan.getWidth()-pan.getHeight();
        
        if (x+yOff < pan.getWidth() || y > pan.getHeight() ){
            check = false;
        }
        return check;
    }
    
    public Point PtAlign(Point pt){
       int x = pt.x;
       int y = pt.y;
       
       // Correction de y car la grille débute en bas:
       int yReel = pan.getHeight()-y;

       // Aligner position sur gille
       int diviseur = pasGrille;
       int modX = x%diviseur;
       int modY = yReel%diviseur;
        
       if (modX > 0){
           if ( modX <= diviseur/2){
               pt.x = x - modX;
           }
           else if(modX > diviseur/2){
               pt.x = x - modX + diviseur;
           }
       }
       if (modY > 0){
           if ( modY <= diviseur/2){
               yReel = yReel - modY;
               pt.y = pan.getHeight()-yReel;
           }
           else if(modY > diviseur/2){
               yReel = yReel - modY + diviseur ;
               pt.y = pan.getHeight()-yReel;
           }
       }
       
       return pt;
    
    }
    
    public void panMousePressed (MouseEvent e){       
        if (Fenetre.wavSamplesLoader != null){
            if (CheckArea(e.getPoint())){
                // Aligner point sur la grille
                Point ptAlign = PtAlign(e.getPoint());
                int x = ptAlign.x;
                int y = ptAlign.y; 

                if (e.getClickCount() == 2){
                    if (outil != null){
                        switch (outil){
                            case "Segment":
                                int h = pan.getHeight();
                                int w = pan.getWidth();
                                double segX = (double)x / (double)w;
                                double segY = (double)(y+(w-h)) / (double)w;
                                pan.matrix.seg.add(new Segment(segX, segY, segX, segY, false, true));
                                repaint();
                                break;
                        }
                    }
                }
                else if (e.getClickCount() == 1){
                    if (outil != null){
                        // Annuler l'aligment sur la grille dans le cas des sélections/suppressions:
                        x = e.getX();
                        y = e.getY();
                        majSegPtSelection(x,y);
                        switch (outil){
                            case "Segment":
                                break;
                            case "Gomme":
                                for (int i = 0;i<pan.matrix.seg.size();i++){
                                    Segment segm = (Segment) pan.matrix.seg.get(i);
                                    if (segm.p1Selected || segm.p2Selected ){
                                        pan.matrix.seg.remove(i);
                                    } 
                                 }
                            break;
                                
                        }
                        repaint();
                    }
                }
            }
        }
        
    }
    
    public void majSegPtSelection(int x, int y){
        // Mise à jour des selections de points de segments
        for (int i = 0;i<pan.matrix.seg.size();i++){
            Segment segm = (Segment) pan.matrix.seg.get(i);
            int h = pan.getHeight();
            int w = pan.getWidth();
            int x1 = (int)(segm.x1 * w);
            int y1 = (int)(segm.y1 * w -(w-h));
            int x2 = (int)(segm.x2 * w);
            int y2 = (int)(segm.y2 * w -(w-h));
            
            if (segm.p1Selected){
                segm.p1Selected = false;
            }
            else if (segm.p2Selected){
                segm.p2Selected = false;
            }
            else{
                if(x >= x1-3 && x <= x1+3 && y >= y1-3 && y <= y1+3){
                    segm.p1Selected = true;
                }
                else{
                    segm.p1Selected = false;
                }
                if(x >= x2-3 && x <= x2+3 && y >= y2-3 && y <= y2+3){
                    segm.p2Selected = true;
                }
                else{
                    segm.p2Selected = false;
                }
            }
            pan.matrix.seg.set(i, segm);
        }
    }
    
    public void panMouseMoved (MouseEvent e){
        if (Fenetre.wavSamplesLoader != null){
            if (CheckArea(e.getPoint())){
                Point ptAlign = PtAlign(e.getPoint());
                int x = ptAlign.x;
                int y = ptAlign.y;
                
                int h = pan.getHeight();
                int w = pan.getWidth();
                
                double segX = (double)x / (double)w;
                double segY = (double)(y+(w-h)) / (double)w;
                
                 for (int i = 0;i<pan.matrix.seg.size();i++){
                     Segment segm = (Segment) pan.matrix.seg.get(i);
                     if(segm.p1Selected){
                         segm.x1 = segX;
                         segm.y1 = segY;
                     }
                     else if(segm.p2Selected){
                         segm.x2 = segX;
                         segm.y2 = segY;
                     }
                     pan.matrix.seg.set(i, segm);
                 }
                 repaint();
             }
        } 
        
    }

}

