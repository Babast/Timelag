package timelag;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;

public class Fenetre extends javax.swing.JFrame {
    String selectedTool = "";
    ArrayList  seg = new ArrayList();
    Point mouseP;
    
    public Fenetre() {
        initComponents();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jRadioButtonSegment = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(64, 64, 64));
        jPanel1.setToolTipText("");
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel1MousePressed(evt);
            }
        });
        jPanel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jPanel1MouseMoved(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 472, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jRadioButtonSegment.setText("segment");
        jRadioButtonSegment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonSegmentActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jRadioButtonSegment)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jRadioButtonSegment)
                .addContainerGap(354, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButtonSegmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonSegmentActionPerformed
        if (jRadioButtonSegment.isSelected()){
            selectedTool = "segment";
        }
        else{
            selectedTool = "";
        }
    }//GEN-LAST:event_jRadioButtonSegmentActionPerformed
    
    private void jPanel1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseMoved
        mouseP = evt.getPoint();
        for (int i = 0;i<seg.size();i++){
            Segment segm = (Segment) seg.get(i);
            if(segm.p1Selected){
                segm.x1 = mouseP.x;
                segm.y1 = mouseP.y;
            }
            else if(segm.p2Selected){
                segm.x2 = mouseP.x;
                segm.y2 = mouseP.y;
            }
            seg.set(i, segm);
        }
        dessiner();
               
    }//GEN-LAST:event_jPanel1MouseMoved

    private void jPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MousePressed
        switch (selectedTool){
            case "segment":
                if(evt.getClickCount() == 1){
                     for (int i = 0;i<seg.size();i++){
                         Segment segm = (Segment) seg.get(i);
                         if (segm.p1Selected){
                             segm.p1Selected = false;
                         }
                         else if (segm.p2Selected){
                             segm.p2Selected = false;
                         }
                         else{
                           if(mouseP.x >= segm.x1-3 && mouseP.x <= segm.x1+3 && mouseP.y >= segm.y1-3 && mouseP.y <= segm.y1+3){
                             segm.p1Selected = true;
                            }
                            else{
                                segm.p1Selected = false;
                            }
                            if(mouseP.x >= segm.x2-3 && mouseP.x <= segm.x2+3 && mouseP.y >= segm.y2-3 && mouseP.y <= segm.y2+3){
                                segm.p2Selected = true;
                            }
                            else{
                                segm.p2Selected = false;
                            }  
                         }
                         seg.set(i, segm);
                     }
                }
                else if (evt.getClickCount() == 2){
                    seg.add(new Segment(mouseP.x,mouseP.y,mouseP.x,mouseP.y,false,true));
                }
                dessiner();
                break;
        }  
    }//GEN-LAST:event_jPanel1MousePressed
            
    private void dessiner(){
        
        int w = jPanel1.getWidth();
        int h = jPanel1.getHeight();
        
        Graphics g = jPanel1.getGraphics();
        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);

        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(0, 0, w, h);

        for (int i = 0;i<seg.size();i++){
            Segment segm = (Segment) seg.get(i);
            g2d.setColor(Color.MAGENTA);
            g2d.drawLine(segm.x1, segm.y1, segm.x2, segm.y2);
            g2d.setColor(Color.GREEN);
            g2d.drawRect(segm.x1-3,segm.y1-3, 6, 6);
            g2d.drawRect(segm.x2-3,segm.y2-3, 6, 6);
        }
        g2d.dispose();
        
    }
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Fenetre.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Fenetre.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Fenetre.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Fenetre.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Fenetre().setVisible(true);
            }
        });

    }
    
  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButtonSegment;
    // End of variables declaration//GEN-END:variables
}
