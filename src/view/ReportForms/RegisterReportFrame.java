/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.ReportForms;

import helper.DateHelper;
import helper.JDBC_Connection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Nguyen Hai Dang
 */
public class RegisterReportFrame extends javax.swing.JFrame {

    /**
     * Creates new form RegisterReportFrame
     */
    public RegisterReportFrame() {
        initComponents();
        reportScrollPane.getVerticalScrollBar().setUnitIncrement(10);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fromDateChooser = new com.toedter.calendar.JDateChooser();
        toDateChooser = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        requestButton = new javax.swing.JButton();
        reportScrollPane = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        currentSiteInfoLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        currentSiteTable = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        currentSiteSumLabel = new javax.swing.JLabel();
        otherSiteInfoLabel = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        otherSiteTable = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        otherSiteSumLabel = new javax.swing.JLabel();
        currentSiteDateLabel = new javax.swing.JLabel();
        otherSiteDateLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        fromDateChooser.setDateFormatString("dd/MM/yyyy");

        toDateChooser.setDateFormatString("dd/MM/yyyy");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Từ ngày");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Đến ngày");

        requestButton.setText("Xem danh sách đăng ký");
        requestButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                requestButtonActionPerformed(evt);
            }
        });

        currentSiteInfoLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        currentSiteInfoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        currentSiteInfoLabel.setText("DANH SÁCH ĐĂNG KÝ THI TRẮC NGHIỆM CƠ SỞ 1");

        currentSiteTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Tên lớp", "Tên môn học", "Giảng viên đăng ký", "Số câu thi", "Ngày thi", "Đã thi (X)", "Ghi chú"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(currentSiteTable);
        if (currentSiteTable.getColumnModel().getColumnCount() > 0) {
            currentSiteTable.getColumnModel().getColumn(0).setResizable(false);
            currentSiteTable.getColumnModel().getColumn(0).setPreferredWidth(50);
            currentSiteTable.getColumnModel().getColumn(1).setResizable(false);
            currentSiteTable.getColumnModel().getColumn(2).setResizable(false);
            currentSiteTable.getColumnModel().getColumn(3).setResizable(false);
            currentSiteTable.getColumnModel().getColumn(4).setResizable(false);
            currentSiteTable.getColumnModel().getColumn(4).setPreferredWidth(50);
            currentSiteTable.getColumnModel().getColumn(5).setResizable(false);
            currentSiteTable.getColumnModel().getColumn(6).setResizable(false);
            currentSiteTable.getColumnModel().getColumn(6).setPreferredWidth(50);
            currentSiteTable.getColumnModel().getColumn(7).setResizable(false);
        }

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Tổng số lượt đăng ký:");

        currentSiteSumLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        currentSiteSumLabel.setText("##");

        otherSiteInfoLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        otherSiteInfoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        otherSiteInfoLabel.setText("DANH SÁCH ĐĂNG KÝ THI TRẮC NGHIỆM CƠ SỞ 2");

        otherSiteTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Tên lớp", "Tên môn học", "Giảng viên đăng ký", "Số câu thi", "Ngày thi", "Đã thi (X)", "Ghi chú"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(otherSiteTable);
        if (otherSiteTable.getColumnModel().getColumnCount() > 0) {
            otherSiteTable.getColumnModel().getColumn(0).setResizable(false);
            otherSiteTable.getColumnModel().getColumn(0).setPreferredWidth(50);
            otherSiteTable.getColumnModel().getColumn(1).setResizable(false);
            otherSiteTable.getColumnModel().getColumn(2).setResizable(false);
            otherSiteTable.getColumnModel().getColumn(3).setResizable(false);
            otherSiteTable.getColumnModel().getColumn(4).setResizable(false);
            otherSiteTable.getColumnModel().getColumn(4).setPreferredWidth(50);
            otherSiteTable.getColumnModel().getColumn(5).setResizable(false);
            otherSiteTable.getColumnModel().getColumn(6).setResizable(false);
            otherSiteTable.getColumnModel().getColumn(6).setPreferredWidth(50);
            otherSiteTable.getColumnModel().getColumn(7).setResizable(false);
        }

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Tổng số lượt đăng ký:");

        otherSiteSumLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        otherSiteSumLabel.setText("##");

        currentSiteDateLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        currentSiteDateLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        currentSiteDateLabel.setText("TỪ NGÀY dd/MM/yyyy ĐẾN NGÀY dd/MM/yyyy");

        otherSiteDateLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        otherSiteDateLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        otherSiteDateLabel.setText("TỪ NGÀY dd/MM/yyyy ĐẾN NGÀY dd/MM/yyyy");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 789, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(otherSiteSumLabel))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 789, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(currentSiteSumLabel)))
                .addContainerGap(20, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(currentSiteDateLabel))
                    .addComponent(currentSiteInfoLabel))
                .addGap(256, 256, 256))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(otherSiteDateLabel))
                    .addComponent(otherSiteInfoLabel))
                .addGap(261, 261, 261))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(currentSiteInfoLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(currentSiteDateLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(currentSiteSumLabel))
                .addGap(52, 52, 52)
                .addComponent(otherSiteInfoLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(otherSiteDateLabel)
                .addGap(30, 30, 30)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(otherSiteSumLabel))
                .addGap(29, 29, 29))
        );

        reportScrollPane.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jLabel1)
                .addGap(39, 39, 39)
                .addComponent(fromDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(45, 45, 45)
                .addComponent(toDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91))
            .addComponent(reportScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 846, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(319, 319, 319)
                .addComponent(requestButton, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(toDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(fromDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addComponent(requestButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(reportScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void requestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_requestButtonActionPerformed
        Date fromDate = fromDateChooser.getDate();
        Date toDate = toDateChooser.getDate();
        
        if(fromDate == null || toDate == null)
            return;
        
        
        String datePattern = "dd/MM/yyyy";
        String periodText = "\nTỪ NGÀY " + DateHelper.toString(fromDate, datePattern)
                + " ĐẾN NGÀY " + DateHelper.toString(toDate, datePattern);
        
        currentSiteDateLabel.setText(periodText);
        otherSiteDateLabel.setText(periodText);
        
        DefaultTableModel currentSiteModel = (DefaultTableModel) currentSiteTable.getModel();
        DefaultTableModel otherSiteModel = (DefaultTableModel) otherSiteTable.getModel();
        
        currentSiteModel.setNumRows(0);
        otherSiteModel.setNumRows(0);
        
        String sql = "{call dbo.SP_Xem_Danh_Sach_Dang_Ky(?,?)}";
        Connection connector = JDBC_Connection.getJDBCConnection();
        try {
            PreparedStatement ps = connector.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(fromDate.getTime()));
            ps.setDate(2, new java.sql.Date(toDate.getTime()));
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector vt = new Vector();
                String site = rs.getString("MACS");
                vt.add(site.equals("CS1")?currentSiteModel.getRowCount()+1:otherSiteModel.getRowCount()+1);
                vt.add(rs.getString("TENLOP"));
                vt.add(rs.getString("TENMH"));
                vt.add(rs.getString("TENGV"));
                vt.add(rs.getInt("SOCAUTHI"));
                vt.add(new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate("NGAYTHI")));
                vt.add(rs.getString("DATHI"));
                
                if (site.equals("CS1")) {
                    currentSiteModel.addRow(vt);
                }
                else {
                    otherSiteModel.addRow(vt);
                }
            }
            currentSiteSumLabel.setText(String.valueOf(currentSiteModel.getRowCount()));
            otherSiteSumLabel.setText(String.valueOf(otherSiteModel.getRowCount()));
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_requestButtonActionPerformed

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
            java.util.logging.Logger.getLogger(RegisterReportFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RegisterReportFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RegisterReportFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RegisterReportFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RegisterReportFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel currentSiteDateLabel;
    private javax.swing.JLabel currentSiteInfoLabel;
    private javax.swing.JLabel currentSiteSumLabel;
    private javax.swing.JTable currentSiteTable;
    private com.toedter.calendar.JDateChooser fromDateChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel otherSiteDateLabel;
    private javax.swing.JLabel otherSiteInfoLabel;
    private javax.swing.JLabel otherSiteSumLabel;
    private javax.swing.JTable otherSiteTable;
    private javax.swing.JScrollPane reportScrollPane;
    private javax.swing.JButton requestButton;
    private com.toedter.calendar.JDateChooser toDateChooser;
    // End of variables declaration//GEN-END:variables
}
