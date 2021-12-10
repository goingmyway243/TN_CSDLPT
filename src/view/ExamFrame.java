/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import dao.AnswerDao;
import dao.ClassroomDao;
import dao.QuestionDao;
import dao.RegisterDao;
import dao.SubjectDao;
import dao.TranscriptDao;
import helper.DateHelper;
import helper.JDBC_Connection;
import helper.TableCellRenderHelper;
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
import model.Answer;
import model.Question;
import model.Register;
import model.Student;
import model.Transcript;

/**
 *
 * @author PC
 */
public class ExamFrame extends javax.swing.JFrame {

    private Student _student;
    private Connection _connector;
    private List<Question> _listQuestion;
    private String[] _listAnswer;
    private Vector<javax.swing.JButton> _listButtons;
    private Timer _timer;
    private int _second, _minute;
    private int _answerCount, _questionIndex;
    private DecimalFormat _dFormat;
    private Register _register;
    private boolean _testMode;

    /**
     * Creates new form ExamFrame
     */
    public ExamFrame() {
        initComponents();
    }

    public ExamFrame(Register register) {
        _testMode = true;

        _register = register;
        _connector = JDBC_Connection.getConnection();
        _dFormat = new DecimalFormat("00");

        _listQuestion = QuestionDao.getExam(_register.getSoCauThi(), _register.getMamh(), _register.getTrinhDo());
        _listAnswer = new String[_listQuestion.size()];

        initComponents();
        loadExamPaneComponent();

        chooseSubjectPanel.setVisible(false);
        examPanel.setVisible(true);
        resultPanel.setVisible(false);
        resultDetailPanel.setVisible(false);

        detailResultTable.setShowGrid(true);
        detailResultTable.getColumnModel().getColumn(3).setCellRenderer(new TableCellRenderHelper());
        detailResultTable.getColumnModel().getColumn(2).setCellRenderer(new TableCellRenderHelper());

        returnButton.setText("Thoát");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public ExamFrame(Student student) {
        _testMode = false;
        _student = student;
        _connector = JDBC_Connection.getConnection();
        _dFormat = new DecimalFormat("00");

        initComponents();
        loadStudentInfo();
        loadRegister();

        examPanel.setVisible(false);
        resultPanel.setVisible(false);
        resultDetailPanel.setVisible(false);

        detailResultTable.setShowGrid(true);
        detailResultTable.getColumnModel().getColumn(3).setCellRenderer(new TableCellRenderHelper());
        detailResultTable.getColumnModel().getColumn(2).setCellRenderer(new TableCellRenderHelper());

        chooseSubjectTable.getRowSorter().toggleSortOrder(7);
    }

    private void notifyMark() {
        float mark = 0;
        int correctCount = 0;

        for (int i = 0; i < _listQuestion.size(); i++) {
            if (_listAnswer[i] == null) {
                continue;
            }

            if (_listAnswer[i].equals(_listQuestion.get(i).getDapAn())) {
                mark += (1.0f / _register.getSoCauThi()) * 10;
                correctCount++;
            }
        }

        mark = helper.Mark.round((float) mark);
        if (!_testMode) {
            saveResult(mark);
        }

        examPanel.setVisible(false);
        resultPanel.setVisible(true);

        resultTotalCorrectLabel.setText(String.format("%d / %d", correctCount, _register.getSoCauThi()));
        resultMarkLabel.setText(String.valueOf(mark));
    }

    private void saveResult(float mark) {
        TranscriptDao.addTranscript(new Transcript(_student.getMasv(), _register.getMamh(), _register.getLan(),
                _register.getNgayThi(), mark));

        Transcript transcript = TranscriptDao.getTranscriptById(_student.getMasv(), _register.getMamh(), _register.getLan());
        int examID = transcript.getBaiThi();
        for (int i = 0; i < _listAnswer.length; i++) {
            Answer answer = new Answer();
            answer.setBaiThi(examID);
            answer.setStt(i + 1);
            answer.setCauHoi(_listQuestion.get(i).getCauHoi());
            answer.setDaChon(_listAnswer[i]);

            AnswerDao.addAnswer(answer);
        }
    }

    private void loadDetailInfos() {
        if (_testMode) {
            detailSubjectLabel.setText(SubjectDao.getSubjectById(_register.getMamh()).getTenmh());
            detailDateLabel.setText(DateHelper.toString2(DateHelper.toDate(_register.getNgayThi())));
            detailExamTimeLabel.setText("" + _register.getLan());
        } else {
            detailClassLabel.setText(ClassroomDao.getClassroomById(_student.getMaLop()).getTenLop());
            detailNameLabel.setText(_student.getHo() + " " + _student.getTen());
            detailSubjectLabel.setText(SubjectDao.getSubjectById(_register.getMamh()).getTenmh());
            detailDateLabel.setText(DateHelper.toString2(DateHelper.toDate(_register.getNgayThi())));
            detailExamTimeLabel.setText("" + _register.getLan());
        }

        loadDetailResultTable();
    }

    private void loadDetailResultTable() {
        DefaultTableModel model = (DefaultTableModel) detailResultTable.getModel();
        model.setRowCount(0);

        for (int i = 0; i < _listQuestion.size(); i++) {
            Vector vt = new Vector();
            Question question = _listQuestion.get(i);
            vt.add(i + 1);
            vt.add(question.getCauHoi());
            vt.add(question.getNoiDung());
            vt.add(String.format("A: %s\nB: %s\nC: %s\nD: %s",
                    question.getA(), question.getB(), question.getC(), question.getD()));
            vt.add(question.getDapAn());
            vt.add(_listAnswer[i]);
            model.addRow(vt);
        }
    }

    private void initTimer() {

        _timer = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                --_second;
                String ddSecond = _dFormat.format(_second);
                String ddMinute = _dFormat.format(_minute);
                timerLabel.setText(ddMinute + ":" + ddSecond);

                if (_second == -1) {
                    _second = 59;
                    --_minute;
                    ddSecond = _dFormat.format(_second);
                    ddMinute = _dFormat.format(_minute);
                    timerLabel.setText(ddMinute + ":" + ddSecond);
                }

                if (_minute <= 5) {
                    timerLabel.setForeground(Color.red);
                    alertLabel.setText("Gần hết giờ làm bài!");
                    alertLabel.setVisible(true);
                }

                if (_minute == 0 && _second == 0) {
                    _timer.stop();
                    JOptionPane.showMessageDialog(ExamFrame.this, "Hết giờ làm bài !", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    notifyMark();
                }
            }
        });
        _timer.start();
    }

    private void initQuestionButtons() {
        _listButtons = new Vector<>();
        for (int i = 1; i <= _register.getSoCauThi(); i++) {
            javax.swing.JButton questionButton = new javax.swing.JButton("Câu " + i);
            questionButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
            questionButton.setVisible(true);
            questionButton.setSize(50, 50);
            questionButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    _questionIndex = Integer.valueOf(questionButton.getText().split(" ")[1]) - 1;
                    loadQuestion(_questionIndex);
                    updateAnswerButtonState();
                }
            });
            QuestionButtonsPanel.add(questionButton);
            _listButtons.add(questionButton);
        }
    }

    private void updateAnswerButtonState() {
        javax.swing.JButton[] listButton = {aButton, bButton, cButton, dButton};
        for (int i = 0; i < listButton.length; i++) {
            listButton[i].setBackground(null);
            if (_listAnswer[_questionIndex] != null) {
                if (_listAnswer[_questionIndex].equalsIgnoreCase(listButton[i].getText().substring(0, 1))) {
                    listButton[i].setBackground(new Color(153, 153, 255));
                }
            }
        }
    }

    private void loadExamPaneComponent() {
        _second = 0;
        _minute = _register.getThoiGian();
        _answerCount = 0;
        _questionIndex = 0;
        alertLabel.setVisible(false);
        initTimer();
        initQuestionButtons();
        loadQuestion(_questionIndex);

        String ddSecond = _dFormat.format(_second);
        String ddMinute = _dFormat.format(_minute);
        timerLabel.setText(ddMinute + ":" + ddSecond);
        remainQuestionLabel.setText(String.format("Đã làm %d / %d câu", _answerCount, _register.getSoCauThi()));
    }

    private void loadQuestion(int index) {
        questionNumberLabel.setText(String.format("Câu %d:", index + 1));

        Question question = _listQuestion.get(index);
        questionTextArea.setText(question.getNoiDung());
        aButton.setText("A: " + question.getA());
        bButton.setText("B: " + question.getB());
        cButton.setText("C: " + question.getC());
        dButton.setText("D: " + question.getD());

        nextButton.setVisible(index >= _register.getSoCauThi() - 1 ? false : true);
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
        model.setNumRows(0);

        try {
            PreparedStatement ps = _connector.prepareStatement(sql);
            ps.setString(1, _student.getMasv());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector vt = new Vector();
                String subjectID = rs.getString("MAMH");
                int examTime = rs.getInt("LAN");

                vt.add(subjectID);
                vt.add(rs.getString("TENMH"));
                vt.add(DateHelper.toString2(rs.getDate("NGAYTHI")));
                vt.add(examTime);
                vt.add(rs.getInt("SOCAUTHI"));
                vt.add(rs.getInt("THOIGIAN"));
                vt.add(rs.getString("TRINHDO"));
                vt.add(RegisterDao.isTakeExam(_student.getMasv(), subjectID, examTime) == 1 ? "X" : "");

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
        logoutButton = new javax.swing.JButton();
        examPanel = new javax.swing.JPanel();
        questionButtonsScrollPane = new javax.swing.JScrollPane();
        QuestionButtonsPanel = new javax.swing.JPanel();
        timerLabel = new javax.swing.JLabel();
        submitButton = new javax.swing.JButton();
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
        nextButton = new javax.swing.JButton();
        resultPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        viewResultDetailButton = new javax.swing.JButton();
        returnButton = new javax.swing.JButton();
        resultTotalCorrectLabel = new javax.swing.JLabel();
        resultMarkLabel = new javax.swing.JLabel();
        resultDetailPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        detailResultTable = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        detailClassLabel = new javax.swing.JLabel();
        detailNameLabel = new javax.swing.JLabel();
        detailSubjectLabel = new javax.swing.JLabel();
        detailDateLabel = new javax.swing.JLabel();
        detailExamTimeLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        returnResultButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1010, 720));
        setResizable(false);

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

        logoutButton.setText("Đăng xuất");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });
        chooseSubjectPanel.add(logoutButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 10, -1, 50));

        examPanel.setPreferredSize(new java.awt.Dimension(1010, 650));

        questionButtonsScrollPane.setBackground(new java.awt.Color(255, 255, 255));

        QuestionButtonsPanel.setLayout(new java.awt.GridLayout(0, 2));
        questionButtonsScrollPane.setViewportView(QuestionButtonsPanel);

        timerLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        timerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timerLabel.setText("jLabel2");

        submitButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        submitButton.setText("Nộp bài");
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });

        questionTextArea.setColumns(20);
        questionTextArea.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        questionTextArea.setLineWrap(true);
        questionTextArea.setRows(5);
        questionTextArea.setWrapStyleWord(true);
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

        remainQuestionLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        remainQuestionLabel.setForeground(new java.awt.Color(0, 153, 51));
        remainQuestionLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        remainQuestionLabel.setText("jLabel3");

        titleLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("THI TRẮC NGHIỆM");

        questionNumberLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        questionNumberLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        questionNumberLabel.setText("jLabel2");

        alertLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        alertLabel.setForeground(new java.awt.Color(255, 0, 0));
        alertLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        alertLabel.setText("jLabel3");

        nextButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        nextButton.setText("Câu tiếp theo");
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout examPanelLayout = new javax.swing.GroupLayout(examPanel);
        examPanel.setLayout(examPanelLayout);
        examPanelLayout.setHorizontalGroup(
            examPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(examPanelLayout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(timerLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(titleLabel)
                .addGap(333, 333, 333))
            .addGroup(examPanelLayout.createSequentialGroup()
                .addGroup(examPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(examPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(submitButton, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                        .addComponent(questionButtonsScrollPane))
                    .addGroup(examPanelLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(examPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(alertLabel)
                            .addComponent(remainQuestionLabel))))
                .addGroup(examPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(examPanelLayout.createSequentialGroup()
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
                        .addGap(71, 71, 71))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, examPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))))
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
                        .addComponent(questionButtonsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(remainQuestionLabel)
                        .addGap(38, 38, 38)
                        .addComponent(alertLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(examPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(submitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
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

        resultPanel.setPreferredSize(new java.awt.Dimension(1010, 650));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setText("Kết quả");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Tổng số câu đúng:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("Tổng điểm:");

        viewResultDetailButton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        viewResultDetailButton.setText("Xem kết quả");
        viewResultDetailButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewResultDetailButtonActionPerformed(evt);
            }
        });

        returnButton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        returnButton.setText("Trở về");
        returnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnButtonActionPerformed(evt);
            }
        });

        resultTotalCorrectLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        resultTotalCorrectLabel.setForeground(new java.awt.Color(51, 51, 255));
        resultTotalCorrectLabel.setText("jLabel3");

        resultMarkLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        resultMarkLabel.setForeground(new java.awt.Color(0, 153, 0));
        resultMarkLabel.setText("jLabel3");

        javax.swing.GroupLayout resultPanelLayout = new javax.swing.GroupLayout(resultPanel);
        resultPanel.setLayout(resultPanelLayout);
        resultPanelLayout.setHorizontalGroup(
            resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultPanelLayout.createSequentialGroup()
                .addGroup(resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(resultPanelLayout.createSequentialGroup()
                        .addGap(446, 446, 446)
                        .addComponent(jLabel2))
                    .addGroup(resultPanelLayout.createSequentialGroup()
                        .addGap(285, 285, 285)
                        .addComponent(returnButton, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(195, 195, 195)
                        .addComponent(viewResultDetailButton, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(resultPanelLayout.createSequentialGroup()
                        .addGap(367, 367, 367)
                        .addGroup(resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(64, 64, 64)
                        .addGroup(resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(resultMarkLabel)
                            .addComponent(resultTotalCorrectLabel))))
                .addContainerGap(198, Short.MAX_VALUE))
        );
        resultPanelLayout.setVerticalGroup(
            resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultPanelLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jLabel2)
                .addGap(69, 69, 69)
                .addGroup(resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(resultTotalCorrectLabel))
                .addGap(102, 102, 102)
                .addGroup(resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(resultMarkLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 192, Short.MAX_VALUE)
                .addGroup(resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(viewResultDetailButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(returnButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(106, 106, 106))
        );

        resultDetailPanel.setPreferredSize(new java.awt.Dimension(1010, 650));

        detailResultTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Câu số", "Nội dung", "Các chọn lựa", "Đáp án", "Đã chọn"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(detailResultTable);
        if (detailResultTable.getColumnModel().getColumnCount() > 0) {
            detailResultTable.getColumnModel().getColumn(0).setResizable(false);
            detailResultTable.getColumnModel().getColumn(0).setPreferredWidth(30);
            detailResultTable.getColumnModel().getColumn(1).setResizable(false);
            detailResultTable.getColumnModel().getColumn(1).setPreferredWidth(30);
            detailResultTable.getColumnModel().getColumn(2).setResizable(false);
            detailResultTable.getColumnModel().getColumn(2).setPreferredWidth(200);
            detailResultTable.getColumnModel().getColumn(3).setResizable(false);
            detailResultTable.getColumnModel().getColumn(3).setPreferredWidth(200);
            detailResultTable.getColumnModel().getColumn(4).setResizable(false);
            detailResultTable.getColumnModel().getColumn(4).setPreferredWidth(30);
            detailResultTable.getColumnModel().getColumn(5).setResizable(false);
            detailResultTable.getColumnModel().getColumn(5).setPreferredWidth(30);
        }

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Lớp:");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel16.setText("Họ tên:");

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel17.setText("Môn thi:");

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel18.setText("Ngày thi:");

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel19.setText("Lần thi :");

        detailClassLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        detailClassLabel.setText("##");

        detailNameLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        detailNameLabel.setText("##");

        detailSubjectLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        detailSubjectLabel.setText("subjectLabel");

        detailDateLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        detailDateLabel.setText("dateLabel");

        detailExamTimeLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        detailExamTimeLabel.setText("##");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel6.setText("Chi tiết bài thi");

        returnResultButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        returnResultButton.setText("Quay lại");
        returnResultButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnResultButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout resultDetailPanelLayout = new javax.swing.GroupLayout(resultDetailPanel);
        resultDetailPanel.setLayout(resultDetailPanelLayout);
        resultDetailPanelLayout.setHorizontalGroup(
            resultDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(resultDetailPanelLayout.createSequentialGroup()
                .addGap(88, 88, 88)
                .addGroup(resultDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(resultDetailPanelLayout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(32, 32, 32)
                        .addComponent(detailNameLabel))
                    .addGroup(resultDetailPanelLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(50, 50, 50)
                        .addComponent(detailClassLabel)))
                .addGap(242, 242, 242)
                .addGroup(resultDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18))
                .addGap(18, 18, 18)
                .addGroup(resultDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(resultDetailPanelLayout.createSequentialGroup()
                        .addComponent(detailDateLabel)
                        .addGap(234, 234, 234)
                        .addComponent(jLabel19)
                        .addGap(18, 18, 18)
                        .addComponent(detailExamTimeLabel))
                    .addComponent(detailSubjectLabel))
                .addContainerGap(128, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, resultDetailPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(resultDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(returnResultButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(429, 429, 429))
        );
        resultDetailPanelLayout.setVerticalGroup(
            resultDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultDetailPanelLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel6)
                .addGap(57, 57, 57)
                .addGroup(resultDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(detailClassLabel)
                    .addGroup(resultDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(detailSubjectLabel)
                        .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(resultDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(detailNameLabel)
                    .addGroup(resultDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(detailDateLabel)
                        .addComponent(jLabel18))
                    .addComponent(jLabel19)
                    .addComponent(detailExamTimeLabel))
                .addGap(57, 57, 57)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(returnResultButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(chooseSubjectPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1035, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(examPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1035, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(resultPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1035, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(resultDetailPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1035, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(chooseSubjectPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(examPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(resultPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(resultDetailPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE))
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

        if (option == JOptionPane.YES_OPTION) {
            _listQuestion = QuestionDao.getExam(questionCount, subjectID, level);
            _listAnswer = new String[_listQuestion.size()];
            loadExamPaneComponent();
            chooseSubjectPanel.setVisible(false);
            examPanel.setVisible(true);
        }
    }//GEN-LAST:event_startExamButtonActionPerformed

    private void aButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aButtonActionPerformed
        if (_listAnswer[_questionIndex] == null) {
            _answerCount++;
            remainQuestionLabel.setText(String.format("Đã làm %d / %d câu", _answerCount, _register.getSoCauThi()));
            _listButtons.get(_questionIndex).setBackground(new Color(155, 155, 155));
        }
        _listAnswer[_questionIndex] = "A";
        updateAnswerButtonState();
    }//GEN-LAST:event_aButtonActionPerformed

    private void bButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bButtonActionPerformed
        if (_listAnswer[_questionIndex] == null) {
            _answerCount++;
            remainQuestionLabel.setText(String.format("Đã làm %d / %d câu", _answerCount, _register.getSoCauThi()));
            _listButtons.get(_questionIndex).setBackground(new Color(155, 155, 155));
        }
        _listAnswer[_questionIndex] = "B";
        updateAnswerButtonState();
    }//GEN-LAST:event_bButtonActionPerformed

    private void cButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cButtonActionPerformed
        if (_listAnswer[_questionIndex] == null) {
            _answerCount++;
            remainQuestionLabel.setText(String.format("Đã làm %d / %d câu", _answerCount, _register.getSoCauThi()));
            _listButtons.get(_questionIndex).setBackground(new Color(155, 155, 155));
        }
        _listAnswer[_questionIndex] = "C";
        updateAnswerButtonState();
    }//GEN-LAST:event_cButtonActionPerformed

    private void dButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dButtonActionPerformed
        if (_listAnswer[_questionIndex] == null) {
            _answerCount++;
            remainQuestionLabel.setText(String.format("Đã làm %d / %d câu", _answerCount, _register.getSoCauThi()));
            _listButtons.get(_questionIndex).setBackground(new Color(155, 155, 155));
        }
        _listAnswer[_questionIndex] = "D";
        updateAnswerButtonState();
    }//GEN-LAST:event_dButtonActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        loadQuestion(++_questionIndex);
        updateAnswerButtonState();
    }//GEN-LAST:event_nextButtonActionPerformed

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitButtonActionPerformed
        Object[] option = {"Có", "Không"};
        if (JOptionPane.showOptionDialog(this, "Bạn có chắc muốn nộp bài chứ ?", "Nộp bài",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[1]) == JOptionPane.YES_OPTION) {
            _timer.stop();
            notifyMark();
        }
    }//GEN-LAST:event_submitButtonActionPerformed

    private void returnButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnButtonActionPerformed
        if (_testMode) {
            this.dispose();
        } else {
            resultPanel.setVisible(false);
            chooseSubjectPanel.setVisible(true);
            loadRegister();
        }
    }//GEN-LAST:event_returnButtonActionPerformed

    private void viewResultDetailButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewResultDetailButtonActionPerformed
        resultPanel.setVisible(false);
        resultDetailPanel.setVisible(true);
        loadDetailInfos();
    }//GEN-LAST:event_viewResultDetailButtonActionPerformed

    private void returnResultButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnResultButtonActionPerformed
        resultPanel.setVisible(true);
        resultDetailPanel.setVisible(false);
    }//GEN-LAST:event_returnResultButtonActionPerformed

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        StudentLoginFrame loginFrame = new StudentLoginFrame();
        loginFrame.setLocationRelativeTo(this);
        loginFrame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_logoutButtonActionPerformed

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
    private javax.swing.JPanel QuestionButtonsPanel;
    private javax.swing.JButton aButton;
    private javax.swing.JLabel alertLabel;
    private javax.swing.JButton bButton;
    private javax.swing.JButton cButton;
    private javax.swing.JPanel chooseSubjectPanel;
    private javax.swing.JTable chooseSubjectTable;
    private javax.swing.JButton dButton;
    private javax.swing.JLabel detailClassLabel;
    private javax.swing.JLabel detailDateLabel;
    private javax.swing.JLabel detailExamTimeLabel;
    private javax.swing.JLabel detailNameLabel;
    private javax.swing.JTable detailResultTable;
    private javax.swing.JLabel detailSubjectLabel;
    private javax.swing.JPanel examPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JButton logoutButton;
    private javax.swing.JButton nextButton;
    private javax.swing.JScrollPane questionButtonsScrollPane;
    private javax.swing.JLabel questionNumberLabel;
    private javax.swing.JTextArea questionTextArea;
    private javax.swing.JLabel remainQuestionLabel;
    private javax.swing.JPanel resultDetailPanel;
    private javax.swing.JLabel resultMarkLabel;
    private javax.swing.JPanel resultPanel;
    private javax.swing.JLabel resultTotalCorrectLabel;
    private javax.swing.JButton returnButton;
    private javax.swing.JButton returnResultButton;
    private javax.swing.JButton startExamButton;
    private javax.swing.JLabel studentAddressLabel;
    private javax.swing.JLabel studentBirthdayLabel;
    private javax.swing.JLabel studentClassIDLabel;
    private javax.swing.JLabel studentIDLabel;
    private javax.swing.JLabel studentNameLabel;
    private javax.swing.JButton submitButton;
    private javax.swing.JLabel timerLabel;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JButton viewResultDetailButton;
    // End of variables declaration//GEN-END:variables
}
