/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import dao.QuestionDao;
import dao.RegisterDao;
import helper.DateHelper;
import helper.JDBC_Connection;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import model.Question;
import model.Register;
import model.Student;

/**
 *
 * @author PC
 */
public class ExamFrame extends javax.swing.JFrame {

    private Student _student;
    private Connection _connector;
    private List<Question> _listQuestion;
    private String[] _listAnswer;
    private Timer _timer;
    private int _second, _minute;
    private int _answerCount, _questionIndex;
    private DecimalFormat _dFormat;
    private Register _register;

    /**
     * Creates new form ExamFrame
     */
    public ExamFrame() {
        initComponents();
    }

    public ExamFrame(Student student) {
        _student = student;
        _connector = JDBC_Connection.getConnection();
        _dFormat = new DecimalFormat("00");

        initComponents();
        loadStudentInfo();
        loadRegister();

        examPanel.setVisible(false);
    }

    public void initTimer() {

        _timer = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                _second--;
                String ddSecond = _dFormat.format(_second);
                String ddMinute = _dFormat.format(_minute);
                timerLabel.setText(ddMinute + ":" + ddSecond);

                if (_second == -1) {
                    _second = 59;
                    _minute--;
                    ddSecond = _dFormat.format(_second);
                    ddMinute = _dFormat.format(_minute);
                    timerLabel.setText(ddMinute + ":" + ddSecond);
                }

                if (_minute <= 5) {
                    timerLabel.setForeground(Color.red);
                    alertLabel.setText("Gần hết giờ làm bài!");
                }

                if (_minute == 0 && _second == 0) {
                    _timer.stop();
                    JOptionPane.showMessageDialog(ExamFrame.this, "Hết giờ làm bài !", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                    notifyMark();
                }
            }
        });
    }

    private void loadExamPaneComponent() {
        _second = 0;
        _minute = _register.getThoiGian();
        _answerCount = 0;
        _questionIndex = 0;
        alertLabel.setVisible(false);
        
        initTimer();
//        loadQuestion(_questionIndex);
        
        _timer.start();
    }

    private void loadQuestion(int index)
    {
        questionNumberLabel.setText(String.format("Câu %d:", index+1));
        remainQuestionLabel.setText(String.format("Đã làm %d / %d câu", _answerCount, _register.getSoCauThi()));
        
        Question question = _listQuestion.get(index);
        questionTextArea.setText(question.getNoiDung());
        aButton.setText("A: " + question.getA());
        bButton.setText("B: " + question.getB());
        cButton.setText("C: " + question.getC());
        dButton.setText("D: " + question.getD());
    }
    
    private void loadStudentInfo() {
        studentIDLabel.setText("Mã sinh viên: " + _student.getMasv());
        studentNameLabel.setText("Họ tên: " + _student.getHo() + " " + _student.getTen());
        studentClassIDLabel.setText("Mã lớp: " + _student.getMaLop());
        studentBirthdayLabel.setText("Ngày sinh: " + DateHelper.toString2(DateHelper.toDate(_student.getNgaySinh())));
        studentAddressLabel.setText("Địa chỉ: " + _student.getDiaChi());
    }

    private void loadRegister() {
        String sql = "{call dbo.SP_Get_Register_For_Student(?)}";
        DefaultTableModel model = (DefaultTableModel) chooseSubjectTable.getModel();

        try {
            PreparedStatement ps = _connector.prepareStatement(sql);
            ps.setString(1, _student.getMasv());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector vt = new Vector();
                vt.add(rs.getString("MAMH"));
                vt.add(rs.getString("TENMH"));
                vt.add(DateHelper.toString2(rs.getDate("NGAYTHI")));
                vt.add(rs.getInt("LAN"));
                vt.add(rs.getInt("SOCAUTHI"));
                vt.add(rs.getInt("THOIGIAN"));
                vt.add(rs.getString("TRINHDO"));
                vt.add(rs.getString("DATHI"));

                model.addRow(vt);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        chooseSubjectPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        chooseSubjectTable = new javax.swing.JTable();
        studentIDLabel = new javax.swing.JLabel();
        studentBirthdayLabel = new javax.swing.JLabel();
        studentNameLabel = new javax.swing.JLabel();
        studentAddressLabel = new javax.swing.JLabel();
        studentClassIDLabel = new javax.swing.JLabel();
        startExamButton = new javax.swing.JButton();
        examPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        timerLabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        questionTextArea = new javax.swing.JTextArea();
        cButton = new javax.swing.JButton();
        aButton = new javax.swing.JButton();
        bButton = new javax.swing.JButton();
        dButton = new javax.swing.JButton();
        remainQuestionLabel = new javax.swing.JLabel();
        titleLabel = new javax.swing.JLabel();
        questionNumberLabel = new javax.swing.JLabel();
        alertLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1010, 720));
        setPreferredSize(new java.awt.Dimension(1010, 720));

        chooseSubjectPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("CHỌN MÔN THI");
        chooseSubjectPanel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 50, -1, -1));

        chooseSubjectTable.setAutoCreateRowSorter(true);
        chooseSubjectTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã môn", "Tên môn", "Ngày thi", "Lần thi", "Số câu thi", "Thời gian thi (phút)", "Trình độ", "Đã thi (X)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(chooseSubjectTable);
        if (chooseSubjectTable.getColumnModel().getColumnCount() > 0) {
            chooseSubjectTable.getColumnModel().getColumn(0).setResizable(false);
            chooseSubjectTable.getColumnModel().getColumn(1).setResizable(false);
            chooseSubjectTable.getColumnModel().getColumn(2).setResizable(false);
            chooseSubjectTable.getColumnModel().getColumn(3).setResizable(false);
            chooseSubjectTable.getColumnModel().getColumn(3).setPreferredWidth(30);
            chooseSubjectTable.getColumnModel().getColumn(4).setResizable(false);
            chooseSubjectTable.getColumnModel().getColumn(4).setPreferredWidth(30);
            chooseSubjectTable.getColumnModel().getColumn(5).setResizable(false);
            chooseSubjectTable.getColumnModel().getColumn(6).setResizable(false);
            chooseSubjectTable.getColumnModel().getColumn(6).setPreferredWidth(30);
            chooseSubjectTable.getColumnModel().getColumn(7).setResizable(false);
            chooseSubjectTable.getColumnModel().getColumn(7).setPreferredWidth(30);
        }

        chooseSubjectPanel.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 281, 1010, 280));

        studentIDLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        studentIDLabel.setText("jLabel2");
        chooseSubjectPanel.add(studentIDLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 130, -1, -1));

        studentBirthdayLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        studentBirthdayLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        studentBirthdayLabel.setText("jLabel2");
        chooseSubjectPanel.add(studentBirthdayLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 130, -1, -1));

        studentNameLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        studentNameLabel.setText("jLabel2");
        chooseSubjectPanel.add(studentNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 180, -1, -1));

        studentAddressLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        studentAddressLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        studentAddressLabel.setText("jLabel2");
        chooseSubjectPanel.add(studentAddressLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 180, -1, -1));

        studentClassIDLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        studentClassIDLabel.setText("jLabel2");
        chooseSubjectPanel.add(studentClassIDLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 230, -1, -1));

        startExamButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        startExamButton.setText("Bắt đầu thi");
        startExamButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startExamButtonActionPerformed(evt);
            }
        });
        chooseSubjectPanel.add(startExamButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 600, 150, 50));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        timerLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        timerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timerLabel.setText("jLabel2");

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton1.setText("Nộp bài");

        questionTextArea.setColumns(20);
        questionTextArea.setRows(5);
        jScrollPane3.setViewportView(questionTextArea);

        cButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cButton.setText("C");
        cButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cButtonActionPerformed(evt);
            }
        });

        aButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        aButton.setText("A");
        aButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aButtonActionPerformed(evt);
            }
        });

        bButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bButton.setText("B");
        bButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bButtonActionPerformed(evt);
            }
        });

        dButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        dButton.setText("D");
        dButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dButtonActionPerformed(evt);
            }
        });

        remainQuestionLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        remainQuestionLabel.setText("jLabel3");

        titleLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("THI TRẮC NGHIỆM");

        questionNumberLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        questionNumberLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        questionNumberLabel.setText("jLabel2");

        alertLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        alertLabel.setText("jLabel3");

        javax.swing.GroupLayout examPanelLayout = new javax.swing.GroupLayout(examPanel);
        examPanel.setLayout(examPanelLayout);
        examPanelLayout.setHorizontalGroup(
            examPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(examPanelLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(timerLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(titleLabel)
                .addGap(333, 333, 333))
            .addGroup(examPanelLayout.createSequentialGroup()
                .addGroup(examPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(examPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                        .addComponent(jScrollPane1))
                    .addGroup(examPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(examPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(remainQuestionLabel)
                            .addComponent(alertLabel))))
                .addGroup(examPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(examPanelLayout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addGroup(examPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(examPanelLayout.createSequentialGroup()
                                .addComponent(cButton, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(dButton, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, examPanelLayout.createSequentialGroup()
                                .addComponent(aButton, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(bButton, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 665, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(examPanelLayout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(questionNumberLabel)))
                .addGap(0, 71, Short.MAX_VALUE))
        );
        examPanelLayout.setVerticalGroup(
            examPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(examPanelLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(examPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timerLabel)
                    .addComponent(titleLabel))
                .addGroup(examPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(examPanelLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(remainQuestionLabel)
                        .addGap(33, 33, 33)
                        .addComponent(alertLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(examPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(questionNumberLabel)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addGroup(examPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(aButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(50, 50, 50)
                        .addGroup(examPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(106, 106, 106))))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1010, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 720, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(chooseSubjectPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(examPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(chooseSubjectPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(examPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startExamButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startExamButtonActionPerformed
        int index = chooseSubjectTable.getSelectedRow();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn môn thi !", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!chooseSubjectTable.getValueAt(index, 7).toString().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bạn đã thi môn này rồi !", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String subjectID = chooseSubjectTable.getValueAt(index, 0).toString();
        String subjectName = chooseSubjectTable.getValueAt(index, 1).toString();
        String classID = _student.getMaLop();
        int examTime = Integer.valueOf(chooseSubjectTable.getValueAt(index, 3).toString());
        int questionCount = Integer.valueOf(chooseSubjectTable.getValueAt(index, 4).toString());
        String level = chooseSubjectTable.getValueAt(index, 6).toString();

        _register = RegisterDao.getRegisterById(subjectID, classID, examTime);
        
        String noti = String.format("Môn thi: %s\nLần thi: %d\nNgày thi: %s\nBạn chắc chắn muốn thi môn này chứ ?",
                subjectName, _register.getLan(), _register.getNgayThi());

        Object[] optionText = {"Vào thi", "Suy nghĩ lại"};
        int option = JOptionPane.showOptionDialog(this, noti, "Bắt đầu thi",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, optionText, optionText[1]);
        
        if(option == JOptionPane.YES_OPTION)
        {
            _listQuestion = QuestionDao.getExam(questionCount, subjectID, level);
            _listAnswer = new String[_listQuestion.size()];
            loadExamPaneComponent();
            chooseSubjectPanel.setVisible(false);
            examPanel.setVisible(true);
        }
    }//GEN-LAST:event_startExamButtonActionPerformed

    private void aButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aButtonActionPerformed
        _listAnswer[_questionIndex] = "A";
        loadQuestion(++_questionIndex);
    }//GEN-LAST:event_aButtonActionPerformed

    private void bButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bButtonActionPerformed
        _listAnswer[_questionIndex] = "B";
        loadQuestion(++_questionIndex);
    }//GEN-LAST:event_bButtonActionPerformed

    private void cButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cButtonActionPerformed
        _listAnswer[_questionIndex] = "C";
        loadQuestion(++_questionIndex);
    }//GEN-LAST:event_cButtonActionPerformed

    private void dButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dButtonActionPerformed
        _listAnswer[_questionIndex] = "D";
        loadQuestion(++_questionIndex);
    }//GEN-LAST:event_dButtonActionPerformed

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
            java.util.logging.Logger.getLogger(ExamFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ExamFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ExamFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ExamFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ExamFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aButton;
    private javax.swing.JLabel alertLabel;
    private javax.swing.JButton bButton;
    private javax.swing.JButton cButton;
    private javax.swing.JPanel chooseSubjectPanel;
    private javax.swing.JTable chooseSubjectTable;
    private javax.swing.JButton dButton;
    private javax.swing.JPanel examPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel questionNumberLabel;
    private javax.swing.JTextArea questionTextArea;
    private javax.swing.JLabel remainQuestionLabel;
    private javax.swing.JButton startExamButton;
    private javax.swing.JLabel studentAddressLabel;
    private javax.swing.JLabel studentBirthdayLabel;
    private javax.swing.JLabel studentClassIDLabel;
    private javax.swing.JLabel studentIDLabel;
    private javax.swing.JLabel studentNameLabel;
    private javax.swing.JLabel timerLabel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
