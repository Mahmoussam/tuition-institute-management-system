/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author el safer
 */
public class allTeachersTable extends javax.swing.JFrame {

    /**
     * Creates new form allTeachersTable
     */
    helper Helper;
    public allTeachersTable() {
        initComponents();
        Helper=helper.getInstance();
        this.setVisible(true);
        try{
        jTable1.setModel(Helper.allTeachersTable_m("SELECT * FROM TEACHERS"));
        }catch(Exception ex){this.dispose();}
        jTable1.addMouseListener(new MouseAdapter() {
  public void mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 1) {
      JTable target = (JTable)e.getSource();
      int row = target.getSelectedRow();
      int column = target.getSelectedColumn();
      try{
          String txt=(String)target.getValueAt(row, column);
          
          seetxt.setText(txt);
      }catch(Exception ex){try {
                        int tx=(int)target.getValueAt(row, column);
                        String txet=Integer.toString(tx);
                        seetxt.setText(txet);
                    } catch (Exception exp) {
                    }}
    }
  }
}); searchtxt.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt){
                refresh();
            }
});
    ELCB1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                //Custom_1.setEditable(true);
                refresh();
                
            }
        });
    }
private void refresh(){
    String s="SELECT * FROM TEACHERS WHERE NAME LIKE ?",s2="SELECT * FROM TEACHERS";
                
                String x=searchtxt.getText();
                String EL=(String) ELCB1.getSelectedItem();
                if(!EL.equals("None")){s+=" AND EL='"+EL+'\'';s2+=" WHERE EL='"+EL+'\'';}
                if(!x.replace(" ", "").equals(""))
        {DefaultTableModel model=Helper.allTeachersTable_m(s, x);
                if(model!=null)jTable1.setModel(model);}
                else {DefaultTableModel model=Helper.allTeachersTable_m(s2);
        if(model!=null)jTable1.setModel(model);}
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        seetxt = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        searchtxt = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        ELCB1 = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        seetxt.setEditable(false);
        seetxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seetxtActionPerformed(evt);
            }
        });

        jLabel1.setText("Search by Name:");

        searchtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchtxtActionPerformed(evt);
            }
        });

        jButton1.setText("Delete Teacher");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Education Level:");

        ELCB1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "None", "صف اول ابتدائى", "صف ثانى ابتدائى", "صف ثالث ابتدائى", "صف رابع ابتدائى", "صف خامس ابتدائى", "صف سادس ابتدائى", "صف اول اعدادى", "صف ثانى اعدادى", "صف ثالث اعدادى", "صف اول ثانوى", "صف ثانى ثانوى", "صف ثالث ثانوى" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(seetxt)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchtxt, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ELCB1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(147, 147, 147)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(searchtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jLabel2)
                    .addComponent(ELCB1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(seetxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void seetxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seetxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_seetxtActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int row=jTable1.getSelectedRow();
        //int col=jTable1.getSelectedColumn();
        if(row<0)Helper.show_error("Please select row first");
        else {
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete Him?", "Delete Teacher", dialogButton);
            if(dialogResult == 0) {
                 String Name=(String) jTable1.getValueAt(row, 0);
                 Helper.deleteTeacher(Name);
                 DefaultTableModel model=Helper.allTeachersTable_m("SELECT * FROM TEACHERS");
                 if(model!=null)jTable1.setModel(model);
            } 
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void searchtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchtxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchtxtActionPerformed

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(allTeachersTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(allTeachersTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(allTeachersTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(allTeachersTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new allTeachersTable().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> ELCB1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField searchtxt;
    private javax.swing.JTextField seetxt;
    // End of variables declaration//GEN-END:variables
}
