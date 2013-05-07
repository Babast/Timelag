package jtimelag;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;


public class Fenetre extends JFrame {
    private JPanel pan;
    private JRadioButton radioButtonSegment; 
    public static String outil;
    public static ArrayList seg = new ArrayList();
      
    Fenetre(){
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        setTitle("Timelag (www.timelag.fr)");
        setSize(800,600);
        
        pan = new Panneau();
        pan.setBackground(Color.GRAY);
        
        radioButtonSegment = new JRadioButton();
        radioButtonSegment.setText("Segment");
        
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(radioButtonSegment)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pan, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pan, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(radioButtonSegment)
                .addContainerGap(354, Short.MAX_VALUE))
        );
        
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
        
    }
    
     private void radioButtonSegmentActionPerformed(ActionEvent evt) {
         if (radioButtonSegment.isSelected()){
             outil = "Segment";
         }
         else{
             outil = "";
         }
    }         
     
    public void panMousePressed (MouseEvent e){
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
    
    public void panMouseMoved (MouseEvent e){
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

