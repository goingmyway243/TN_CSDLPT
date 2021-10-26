/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.DepartForms;

import dao.DepartmentDao;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import model.Department;

/**
 *
 * @author Tozaff
 */
public class EditDepartForm extends javax.swing.JFrame {

    /**
     * Creates new form AddDepartForm
     */
    Department depart = new Department();
    DepartmentDao departDao = new DepartmentDao();
    static String maCS = "CS1";
    public EditDepartForm() {
        initComponents();
        setArlet(false);
    }
    public EditDepartForm(String data) {
        initComponents();
        setArlet(false);
        
        depart = departDao.getDepartmentById(data);
        
        setInput();
        tf_MaKh.setEnabled(false);
        
        
    }
    
    private void getInput()
    {
        depart.setMakh(tf_MaKh.getText().trim());
        depart.setTenkh(tf_TenKh.getText().trim());
        depart.setMacs(cbxMaCS.getSelectedItem().toString());
    }
    private void setInput()
    {
        tf_MaKh.setText(depart.getMakh());
        tf_TenKh.setText(depart.getTenkh());
        cbxMaCS.setSelectedItem(depart.getMacs());
    }
    
    public void setArlet(boolean shit) {
        arletMaKh.setVisible(shit);
        arletTenKh.setVisible(shit);

    }

    private void createAlert(JLabel label, String alertContent) {
        label.setText(alertContent);
        label.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tf_MaKh = new javax.swing.JTextField();
        tf_TenKh = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        arletMaKh = new javax.swing.JLabel();
        arletTenKh = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cbxMaCS = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Mã khoa");

        jLabel2.setText("Tên khoa");

        jButton1.setText("Xác nhận");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel3.setText("Sửa Khoa");

        arletMaKh.setForeground(new java.awt.Color(255, 0, 0));
        arletMaKh.setText("Không bỏ trống Mã khoa");

        arletTenKh.setForeground(new java.awt.Color(255, 0, 0));
        arletTenKh.setText("Không bỏ trống Tên khoa");

        jLabel4.setText("Mã cơ sở");

        cbxMaCS.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "CS1", "CS2" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(207, 207, 207)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tf_MaKh, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                            .addComponent(tf_TenKh, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                            .addComponent(cbxMaCS, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(arletMaKh)
                            .addComponent(arletTenKh)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(216, 216, 216)
                        .addComponent(jButton1)))
                .addContainerGap(75, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel3)
                .addGap(61, 61, 61)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tf_MaKh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(arletMaKh))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_TenKh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(arletTenKh))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cbxMaCS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addComponent(jButton1)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try {
            getInput();
            boolean check = true;
            //reset arlet
            setArlet(false);

            //set arlet

            //2
            if (depart.getTenkh().length() == 0) {
                createAlert(arletTenKh, "Không bỏ trống Tên khoa");
                check = false;
            }
            else if(depart.getTenkh().matches( ".{1,40}")==false)
            {createAlert(arletTenKh, "Tối đa 40 kí tự");
                check = false;

            }

            //after the check
            if (check) {
                departDao.updateDepartment(depart);
                JOptionPane.showMessageDialog(rootPane, "Sửa thành công");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            JOptionPane.showMessageDialog(rootPane, "Sửa thất bại");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(EditDepartForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditDepartForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditDepartForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditDepartForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EditDepartForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel arletMaKh;
    private javax.swing.JLabel arletTenKh;
    private javax.swing.JComboBox cbxMaCS;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField tf_MaKh;
    private javax.swing.JTextField tf_TenKh;
    // End of variables declaration//GEN-END:variables
}
