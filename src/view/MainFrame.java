/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import dao.ClassroomDao;
import dao.DepartmentDao;
import dao.QuestionDao;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TeacherDao;
import helper.DateHelper;
import helper.JDBC_Connection;
import helper.TableCellRenderHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.Classroom;
import model.Department;
import model.Question;
import model.Student;
import model.Subject;
import model.Teacher;

/**
 *
 * @author vivau
 */
public class MainFrame extends javax.swing.JFrame {

    private List<Subject> _listSubject;
    private List<Classroom> _listClassroom;
    private List<Department> _listDepartment;
    private List<Teacher> _listTeacher;
    private List<Student> _listStudent;

    private String _role;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        loadBranchComboBox(sysBranchComboBox1);

        configWithRole(_role);
        setOtherTabSystemEnable(false);
        configRpResultScrollPane3();
    }

    private void setOtherTabSystemEnable(boolean enable) {
        for (int i = 1; i < mainTabbedPane.getTabCount(); i++) {
            mainTabbedPane.setEnabledAt(i, enable);
        }
    }

    private void configWithRole(String role) {
        if (role == null) {
            sysSignUpButton.setEnabled(false);
            sysLogOutButton.setEnabled(false);
            return;
        }

        if (role.equals("GIANGVIEN")) {
            sysSignUpButton.setEnabled(false);
        } else {
            sysSignUpButton.setEnabled(true);
        }
        sysLogOutButton.setEnabled(true);
    }

    private void configRpResultScrollPane3() {
        rpResultScrollPane3.getVerticalScrollBar().setUnitIncrement(10);
        rpResultScrollPane3.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    private void loadDepartmentTable(JTable table) {
        _listDepartment = DepartmentDao.getAllDepartments();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (Department depart : _listDepartment) {
            model.addRow(depart.toArray());
        }
    }

    private void loadClassTable(JTable table) {
        _listClassroom = ClassroomDao.getAllClassrooms();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (Classroom classroom : _listClassroom) {
            model.addRow(classroom.toArray());
        }
    }

    private void loadTeacherTable(JTable table) {
        _listTeacher = TeacherDao.getAllTeachers();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (Teacher teacher : _listTeacher) {
            model.addRow(teacher.toArray());
        }
    }

    private void loadStudentTable(JTable table) {
        _listStudent = StudentDao.getAllStudents();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (Student student : _listStudent) {
            model.addRow(student.toArray());
        }
    }

    private void loadSubjectTable(JTable table) {
        _listSubject = SubjectDao.getAllSubjects();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (Subject subject : _listSubject) {
            model.addRow(subject.toArray());
        }
    }

    private void loadBranchComboBox(JComboBox comboBox) {
        Connection connector = JDBC_Connection.getPublisherConnection();
        String sql = "SELECT * FROM Get_Subcribers";

        try {
            PreparedStatement ps = connector.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                comboBox.addItem(rs.getString("TENCN"));
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

    }

    private void loadSubjectComboBox(JComboBox comboBox) {
        _listSubject = SubjectDao.getAllSubjects();
        for (Subject subject : _listSubject) {
            comboBox.addItem(subject.getTenmh());
        }
    }

    private void loadClassComboBox(JComboBox comboBox) {
        _listClassroom = ClassroomDao.getAllClassrooms();
        for (Classroom classroom : _listClassroom) {
            comboBox.addItem(classroom.getTenLop());
        }
    }

    private void loadTeacherIDComboBox(JComboBox comboBox) {
        _listTeacher = TeacherDao.getAllTeachers();
        for (Teacher teacher : _listTeacher) {
            comboBox.addItem(teacher.getMagv());
        }
    }

    private String convertMarkToString(double mark) {
        String result = "";
        int integer = (int) mark;
        double decimal = mark - integer;
        String slDecimal = String.valueOf(decimal).length() > 4 ? String.valueOf(decimal).substring(2, 4) : String.valueOf(decimal).substring(2);
        String tmp[] = {String.valueOf(integer), slDecimal};
        for (int i = 0; i < tmp.length; i++) {
            int value = Integer.valueOf(tmp[i]);
            if (i == 1 && value == 0) {
                break;
            }

            int oneDigit;
            boolean unitMode = false;
            boolean tenCase = false;

            if (i == 1) {
                result += "phẩy ";
            }

            while (value >= 0) {
                if (i == 0) {
                    oneDigit = value;
                    value = -1;
                } else {
                    value = (int) (value % 10) == 0 ? (int) (value / 10) : value;

                    if (value > 9) {
                        oneDigit = (int) (value / 10);
                        value = (int) (value % 10);
                        unitMode = true;
                    } else {
                        oneDigit = value;
                        value = -1;
                    }
                }

                switch (oneDigit) {
                    case 0: {
                        result += "không ";
                        break;
                    }
                    case 1: {
                        if (i == 1 && unitMode && value != -1) {
                            tenCase = true;
                            result += "mười ";
                            break;
                        }
                        result += (i == 1 && unitMode && value == -1 && !tenCase) ? "mốt " : "một ";
                        break;
                    }
                    case 2: {
                        result += "hai ";
                        break;
                    }
                    case 3: {
                        result += "ba ";
                        break;
                    }
                    case 4: {
                        result += "bốn ";
                        break;
                    }
                    case 5: {
                        result += (i == 1 && unitMode && value == -1) ? "lăm " : "năm ";
                        break;
                    }
                    case 6: {
                        result += "sáu ";
                        break;
                    }
                    case 7: {
                        result += "bảy ";
                        break;
                    }
                    case 8: {
                        result += "tám ";
                        break;
                    }
                    case 9: {
                        result += "chín ";
                        break;
                    }
                    case 10: {
                        result += "mười ";
                    }
                    default:
                        break;
                }
                if (unitMode && value != -1 && !tenCase) {
                    result += "mươi ";
                }
            }
        }
        result = result.trim();
        result = result.substring(0, 1).toUpperCase() + result.substring(1);
        return result;
    }

    private boolean checkStudentID(String studentID) {
        String pattern = "^[a-zA-Z]{1}[\\d]{2}[a-zA-Z]{2}[\\d]{3}$";
        if (!studentID.matches(pattern)) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập chính xác mã sinh viên\n(ví dụ: N18CN001)");
            return false;
        }

        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainTabbedPane = new javax.swing.JTabbedPane();
        tabSystem = new javax.swing.JPanel();
        systemOptionPanel = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        sysSignInButton = new javax.swing.JButton();
        sysLogOutButton = new javax.swing.JButton();
        sysSignUpButton = new javax.swing.JButton();
        jSeparator14 = new javax.swing.JSeparator();
        jSeparator15 = new javax.swing.JSeparator();
        systemFormPanel1 = new javax.swing.JPanel();
        sysLoginButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        sysPasswordTextField1 = new javax.swing.JPasswordField();
        sysUsernameTextField1 = new javax.swing.JTextField();
        sysBranchComboBox1 = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        systemFormPanel2 = new javax.swing.JPanel();
        sysCreateAccountButton2 = new javax.swing.JButton();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        sysPasswordTextField2 = new javax.swing.JPasswordField();
        sysUsernameTextField2 = new javax.swing.JTextField();
        sysRoleComboBox2 = new javax.swing.JComboBox<>();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        sysReTypePasswordTextField2 = new javax.swing.JPasswordField();
        sysTeacherIDComboBox2 = new javax.swing.JComboBox<>();
        jLabel49 = new javax.swing.JLabel();
        tabCategories = new javax.swing.JPanel();
        categoriesOptionPanel = new javax.swing.JPanel();
        jSeparator10 = new javax.swing.JSeparator();
        ctDepartmentButton = new javax.swing.JButton();
        ctClassButton = new javax.swing.JButton();
        jSeparator11 = new javax.swing.JSeparator();
        ctSubjectButton = new javax.swing.JButton();
        ctTeacherButton = new javax.swing.JButton();
        ctStudentButton = new javax.swing.JButton();
        jSeparator12 = new javax.swing.JSeparator();
        categoriesFormPanel1 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        ctDepartmentTable = new javax.swing.JTable();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        ctDepartmentIDTextField = new javax.swing.JTextField();
        ctDepartmentNameTextField = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        ctDepartmentBranchComboBox = new javax.swing.JComboBox();
        ctAddButton1 = new javax.swing.JButton();
        ctEditButton1 = new javax.swing.JButton();
        ctSaveButton1 = new javax.swing.JButton();
        ctRemoveButton1 = new javax.swing.JButton();
        ctUndoButton1 = new javax.swing.JButton();
        ctReloadButton1 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        ctBranchComboBox1 = new javax.swing.JComboBox<>();
        categoriesFormPanel2 = new javax.swing.JPanel();
        ctAddButton2 = new javax.swing.JButton();
        ctEditButton2 = new javax.swing.JButton();
        ctSaveButton2 = new javax.swing.JButton();
        ctRemoveButton2 = new javax.swing.JButton();
        ctUndoButton2 = new javax.swing.JButton();
        ctReloadButton2 = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        ctBranchComboBox2 = new javax.swing.JComboBox<>();
        jScrollPane6 = new javax.swing.JScrollPane();
        ctClassTable = new javax.swing.JTable();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        ctClassIDTextField = new javax.swing.JTextField();
        ctClassNameTextField = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        ctClassDepartmentComboBox = new javax.swing.JComboBox();
        categoriesFormPanel3 = new javax.swing.JPanel();
        ctAddButton3 = new javax.swing.JButton();
        ctEditButton3 = new javax.swing.JButton();
        ctSaveButton3 = new javax.swing.JButton();
        ctRemoveButton3 = new javax.swing.JButton();
        ctUndoButton3 = new javax.swing.JButton();
        ctReloadButton3 = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        ctBranchComboBox3 = new javax.swing.JComboBox<>();
        jScrollPane7 = new javax.swing.JScrollPane();
        ctTeacherTable = new javax.swing.JTable();
        ctTeacherLastNameTextField = new javax.swing.JTextField();
        ctTeacherIDTextField = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        ctTeacherFirstNameTextField = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        ctTeacherDegreeTextField = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        ctTeacherDepartmentComboBox = new javax.swing.JComboBox();
        categoriesFormPanel4 = new javax.swing.JPanel();
        ctAddButton4 = new javax.swing.JButton();
        ctEditButton4 = new javax.swing.JButton();
        ctSaveButton4 = new javax.swing.JButton();
        ctRemoveButton4 = new javax.swing.JButton();
        ctUndoButton4 = new javax.swing.JButton();
        ctReloadButton4 = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        ctBranchComboBox4 = new javax.swing.JComboBox<>();
        jScrollPane8 = new javax.swing.JScrollPane();
        ctStudentTable = new javax.swing.JTable();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        ctStudentIDTextField = new javax.swing.JTextField();
        ctStudentLastNameTextField = new javax.swing.JTextField();
        ctStudentFirstNameTextField = new javax.swing.JTextField();
        ctStudentBirthDayTextField = new com.toedter.calendar.JDateChooser();
        jLabel39 = new javax.swing.JLabel();
        ctStudentAddressTextField = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        ctStudentClassComboBox = new javax.swing.JComboBox();
        categoriesFormPanel5 = new javax.swing.JPanel();
        ctAddButton5 = new javax.swing.JButton();
        ctEditButton5 = new javax.swing.JButton();
        ctSaveButton5 = new javax.swing.JButton();
        ctRemoveButton5 = new javax.swing.JButton();
        ctUndoButton5 = new javax.swing.JButton();
        ctReloadButton5 = new javax.swing.JButton();
        jLabel41 = new javax.swing.JLabel();
        ctBranchComboBox5 = new javax.swing.JComboBox<>();
        jScrollPane9 = new javax.swing.JScrollPane();
        ctSubjectTable = new javax.swing.JTable();
        jLabel42 = new javax.swing.JLabel();
        ctSubjectIDTextField = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        ctSubjectNameTextField = new javax.swing.JTextField();
        tabMajor = new javax.swing.JPanel();
        majorOptionPanel = new javax.swing.JPanel();
        jSeparator8 = new javax.swing.JSeparator();
        mjQuestionManagementButton = new javax.swing.JButton();
        mjMarkManagementButton = new javax.swing.JButton();
        jSeparator9 = new javax.swing.JSeparator();
        mjTestExamButton = new javax.swing.JButton();
        jSeparator13 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        tabReport = new javax.swing.JPanel();
        reportOptionPanel = new javax.swing.JPanel();
        jSeparator5 = new javax.swing.JSeparator();
        rpRegisterButton = new javax.swing.JButton();
        rpTranscriptButton = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        rpResultButton = new javax.swing.JButton();
        reportFormPanel1 = new javax.swing.JPanel();
        rpStudentIDTextField1 = new javax.swing.JTextField();
        rpSubjectComboBox1 = new javax.swing.JComboBox<>();
        rpExamTimeComboBox1 = new javax.swing.JComboBox<>();
        rpViewResultButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        rpResultPanel1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        rpDataTable1 = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        rpClassLabel1 = new javax.swing.JLabel();
        rpNameLabel1 = new javax.swing.JLabel();
        rpSubjectLabel1 = new javax.swing.JLabel();
        rpDateLabel1 = new javax.swing.JLabel();
        rpExamTimeLabel1 = new javax.swing.JLabel();
        reportFormPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        rpClassComboBox2 = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        rpSubjectComboBox2 = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        rpExamTimeComboBox2 = new javax.swing.JComboBox<>();
        rpViewResultButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        rpDataTable2 = new javax.swing.JTable();
        reportFormPanel3 = new javax.swing.JPanel();
        rpFromDateChooser3 = new com.toedter.calendar.JDateChooser();
        rpToDateChooser3 = new com.toedter.calendar.JDateChooser();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        rpViewResultButton3 = new javax.swing.JButton();
        rpResultScrollPane3 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        rpCurrentSiteInfoLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        rpCurrentSiteTable3 = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        rpCurrentSiteSumLabel3 = new javax.swing.JLabel();
        rpOtherSiteInfoLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        rpOtherSiteTable3 = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        rpOtherSiteSumLabel3 = new javax.swing.JLabel();
        rpCurrentSiteDateLabel3 = new javax.swing.JLabel();
        rpOtherSiteDateLabel3 = new javax.swing.JLabel();
        sysInfoPanel = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        sysUserIDInfoLabel = new javax.swing.JLabel();
        sysRoleInfoLabel = new javax.swing.JLabel();
        sysFullNameInfoLabel = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        mainTabbedPane.setPreferredSize(new java.awt.Dimension(550, 721));
        mainTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                mainTabbedPaneStateChanged(evt);
            }
        });

        tabSystem.setBackground(new java.awt.Color(255, 255, 255));

        systemOptionPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        systemOptionPanel.setPreferredSize(new java.awt.Dimension(500, 110));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        sysSignInButton.setText("Đăng nhập");
        sysSignInButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sysSignInButtonActionPerformed(evt);
            }
        });

        sysLogOutButton.setText("Đăng xuất");
        sysLogOutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sysLogOutButtonActionPerformed(evt);
            }
        });

        sysSignUpButton.setText("Tạo tài khoản");
        sysSignUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sysSignUpButtonActionPerformed(evt);
            }
        });

        jSeparator14.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator15.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout systemOptionPanelLayout = new javax.swing.GroupLayout(systemOptionPanel);
        systemOptionPanel.setLayout(systemOptionPanelLayout);
        systemOptionPanelLayout.setHorizontalGroup(
            systemOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(systemOptionPanelLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(sysSignInButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(sysSignUpButton, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(sysLogOutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        systemOptionPanelLayout.setVerticalGroup(
            systemOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jSeparator14, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(systemOptionPanelLayout.createSequentialGroup()
                .addGroup(systemOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(systemOptionPanelLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(sysSignInButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(systemOptionPanelLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(sysSignUpButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(systemOptionPanelLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(sysLogOutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(31, Short.MAX_VALUE))
            .addComponent(jSeparator15, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        systemFormPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        systemFormPanel1.setPreferredSize(new java.awt.Dimension(550, 500));

        sysLoginButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        sysLoginButton1.setText("Đăng nhập");
        sysLoginButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sysLoginButton1ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Tài khoản:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Mật khẩu:");

        sysUsernameTextField1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        sysBranchComboBox1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Cơ sở:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("ĐĂNG NHẬP");

        javax.swing.GroupLayout systemFormPanel1Layout = new javax.swing.GroupLayout(systemFormPanel1);
        systemFormPanel1.setLayout(systemFormPanel1Layout);
        systemFormPanel1Layout.setHorizontalGroup(
            systemFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(systemFormPanel1Layout.createSequentialGroup()
                .addGroup(systemFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sysLoginButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(systemFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(systemFormPanel1Layout.createSequentialGroup()
                            .addGap(448, 448, 448)
                            .addComponent(jLabel4))
                        .addGroup(systemFormPanel1Layout.createSequentialGroup()
                            .addGap(350, 350, 350)
                            .addComponent(jLabel1)
                            .addGap(64, 64, 64)
                            .addComponent(sysBranchComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(systemFormPanel1Layout.createSequentialGroup()
                            .addGap(350, 350, 350)
                            .addComponent(jLabel2)
                            .addGap(41, 41, 41)
                            .addComponent(sysUsernameTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(systemFormPanel1Layout.createSequentialGroup()
                            .addGap(350, 350, 350)
                            .addComponent(jLabel3)
                            .addGap(43, 43, 43)
                            .addComponent(sysPasswordTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(386, Short.MAX_VALUE))
        );
        systemFormPanel1Layout.setVerticalGroup(
            systemFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(systemFormPanel1Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(jLabel4)
                .addGap(28, 28, 28)
                .addGroup(systemFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(systemFormPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel1))
                    .addComponent(sysBranchComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(systemFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(systemFormPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel2))
                    .addComponent(sysUsernameTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(systemFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(systemFormPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel3))
                    .addComponent(sysPasswordTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addComponent(sysLoginButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(191, Short.MAX_VALUE))
        );

        systemFormPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        sysCreateAccountButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        sysCreateAccountButton2.setText("Tạo tài khoản");
        sysCreateAccountButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sysCreateAccountButton2ActionPerformed(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel44.setText("Tài khoản:");

        jLabel45.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel45.setText("Mật khẩu:");

        sysUsernameTextField2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        sysRoleComboBox2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel46.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel46.setText("Nhóm quyền:");
        jLabel46.setToolTipText("");

        jLabel47.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel47.setText("TẠO TÀI KHOẢN");

        jLabel48.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel48.setText("Nhập lại mật khẩu:");

        sysTeacherIDComboBox2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel49.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel49.setText("Mã giảng viên:");
        jLabel49.setToolTipText("");

        javax.swing.GroupLayout systemFormPanel2Layout = new javax.swing.GroupLayout(systemFormPanel2);
        systemFormPanel2.setLayout(systemFormPanel2Layout);
        systemFormPanel2Layout.setHorizontalGroup(
            systemFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, systemFormPanel2Layout.createSequentialGroup()
                .addGap(442, 442, 442)
                .addComponent(sysCreateAccountButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                .addGap(433, 433, 433))
            .addGroup(systemFormPanel2Layout.createSequentialGroup()
                .addGap(347, 347, 347)
                .addGroup(systemFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(systemFormPanel2Layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(jLabel47))
                    .addGroup(systemFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, systemFormPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel44)
                            .addGap(80, 80, 80)
                            .addComponent(sysUsernameTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, systemFormPanel2Layout.createSequentialGroup()
                            .addGroup(systemFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel45)
                                .addComponent(jLabel48)
                                .addComponent(jLabel46)
                                .addComponent(jLabel49))
                            .addGap(37, 37, 37)
                            .addGroup(systemFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(sysTeacherIDComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(sysReTypePasswordTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(sysPasswordTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(sysRoleComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        systemFormPanel2Layout.setVerticalGroup(
            systemFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(systemFormPanel2Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel47)
                .addGap(48, 48, 48)
                .addGroup(systemFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(sysUsernameTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(systemFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(sysPasswordTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(systemFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48)
                    .addComponent(sysReTypePasswordTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(systemFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sysTeacherIDComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel49))
                .addGap(30, 30, 30)
                .addGroup(systemFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sysRoleComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel46))
                .addGap(40, 40, 40)
                .addComponent(sysCreateAccountButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(85, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout tabSystemLayout = new javax.swing.GroupLayout(tabSystem);
        tabSystem.setLayout(tabSystemLayout);
        tabSystemLayout.setHorizontalGroup(
            tabSystemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(systemOptionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1008, Short.MAX_VALUE)
            .addGroup(tabSystemLayout.createSequentialGroup()
                .addComponent(systemFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1008, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(tabSystemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(systemFormPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tabSystemLayout.setVerticalGroup(
            tabSystemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabSystemLayout.createSequentialGroup()
                .addComponent(systemOptionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(systemFormPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(tabSystemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabSystemLayout.createSequentialGroup()
                    .addGap(143, 143, 143)
                    .addComponent(systemFormPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        mainTabbedPane.addTab("Hệ thống", tabSystem);

        tabCategories.setBackground(new java.awt.Color(255, 255, 255));

        categoriesOptionPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        categoriesOptionPanel.setPreferredSize(new java.awt.Dimension(550, 116));

        jSeparator10.setOrientation(javax.swing.SwingConstants.VERTICAL);

        ctDepartmentButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctDepartmentButton.setText("Khoa");
        ctDepartmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctDepartmentButtonActionPerformed(evt);
            }
        });

        ctClassButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctClassButton.setText("Lớp");
        ctClassButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctClassButtonActionPerformed(evt);
            }
        });

        jSeparator11.setOrientation(javax.swing.SwingConstants.VERTICAL);

        ctSubjectButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctSubjectButton.setText("<html>Môn<br/>học</html>");
        ctSubjectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctSubjectButtonActionPerformed(evt);
            }
        });

        ctTeacherButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctTeacherButton.setText("<html>Giảng<br/>viên</html>");
        ctTeacherButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctTeacherButtonActionPerformed(evt);
            }
        });

        ctStudentButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctStudentButton.setText("<html>Sinh<br/>viên</html>");
        ctStudentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctStudentButtonActionPerformed(evt);
            }
        });

        jSeparator12.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout categoriesOptionPanelLayout = new javax.swing.GroupLayout(categoriesOptionPanel);
        categoriesOptionPanel.setLayout(categoriesOptionPanelLayout);
        categoriesOptionPanelLayout.setHorizontalGroup(
            categoriesOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoriesOptionPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(ctDepartmentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(ctClassButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(ctTeacherButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(ctStudentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(ctSubjectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        categoriesOptionPanelLayout.setVerticalGroup(
            categoriesOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator11, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jSeparator10, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(categoriesOptionPanelLayout.createSequentialGroup()
                .addGroup(categoriesOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(categoriesOptionPanelLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(categoriesOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ctDepartmentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ctClassButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(categoriesOptionPanelLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(categoriesOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ctTeacherButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ctStudentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(categoriesOptionPanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(ctSubjectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(31, Short.MAX_VALUE))
            .addComponent(jSeparator12, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        categoriesFormPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        categoriesFormPanel1.setMaximumSize(new java.awt.Dimension(1048, 485));

        ctDepartmentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã khoa", "Tên khoa", "Mã cơ sở"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(ctDepartmentTable);
        if (ctDepartmentTable.getColumnModel().getColumnCount() > 0) {
            ctDepartmentTable.getColumnModel().getColumn(0).setResizable(false);
            ctDepartmentTable.getColumnModel().getColumn(1).setResizable(false);
            ctDepartmentTable.getColumnModel().getColumn(2).setResizable(false);
            ctDepartmentTable.getColumnModel().getColumn(2).setHeaderValue("Tên");
        }

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel20.setText("Mã khoa");

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel21.setText("Tên khoa");

        ctDepartmentIDTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        ctDepartmentNameTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel22.setText("Mã cơ sở");

        ctDepartmentBranchComboBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        ctAddButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctAddButton1.setText("Thêm");
        ctAddButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctAddButton1ActionPerformed(evt);
            }
        });

        ctEditButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctEditButton1.setText("Hiệu Chỉnh");
        ctEditButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctEditButton1ActionPerformed(evt);
            }
        });

        ctSaveButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctSaveButton1.setText("Ghi");
        ctSaveButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctSaveButton1ActionPerformed(evt);
            }
        });

        ctRemoveButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctRemoveButton1.setText("Xóa");
        ctRemoveButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctRemoveButton1ActionPerformed(evt);
            }
        });

        ctUndoButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctUndoButton1.setText("Phục hồi");
        ctUndoButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctUndoButton1ActionPerformed(evt);
            }
        });

        ctReloadButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctReloadButton1.setText("Làm mới");
        ctReloadButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctReloadButton1ActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel23.setText("Cơ sở:");

        ctBranchComboBox1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout categoriesFormPanel1Layout = new javax.swing.GroupLayout(categoriesFormPanel1);
        categoriesFormPanel1.setLayout(categoriesFormPanel1Layout);
        categoriesFormPanel1Layout.setHorizontalGroup(
            categoriesFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5)
            .addGroup(categoriesFormPanel1Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(categoriesFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22))
                .addGap(37, 37, 37)
                .addGroup(categoriesFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(ctDepartmentIDTextField)
                    .addComponent(ctDepartmentNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                    .addComponent(ctDepartmentBranchComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(categoriesFormPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(categoriesFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(categoriesFormPanel1Layout.createSequentialGroup()
                        .addComponent(ctAddButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, categoriesFormPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel23)
                        .addGap(34, 34, 34)))
                .addGroup(categoriesFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(categoriesFormPanel1Layout.createSequentialGroup()
                        .addComponent(ctEditButton1)
                        .addGap(18, 18, 18)
                        .addComponent(ctSaveButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(ctRemoveButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(ctUndoButton1)
                        .addGap(18, 18, 18)
                        .addComponent(ctReloadButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(403, 403, 403))
                    .addGroup(categoriesFormPanel1Layout.createSequentialGroup()
                        .addComponent(ctBranchComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        categoriesFormPanel1Layout.setVerticalGroup(
            categoriesFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoriesFormPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(categoriesFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ctAddButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctEditButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctSaveButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctRemoveButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctUndoButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctReloadButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(categoriesFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ctBranchComboBox1)
                    .addComponent(jLabel23))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(categoriesFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(ctDepartmentIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(categoriesFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ctDepartmentNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addGap(34, 34, 34)
                .addGroup(categoriesFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(ctDepartmentBranchComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(69, 69, 69))
        );

        categoriesFormPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        categoriesFormPanel2.setPreferredSize(new java.awt.Dimension(1007, 535));

        ctAddButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctAddButton2.setText("Thêm");
        ctAddButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctAddButton2ActionPerformed(evt);
            }
        });

        ctEditButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctEditButton2.setText("Hiệu Chỉnh");
        ctEditButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctEditButton2ActionPerformed(evt);
            }
        });

        ctSaveButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctSaveButton2.setText("Ghi");
        ctSaveButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctSaveButton2ActionPerformed(evt);
            }
        });

        ctRemoveButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctRemoveButton2.setText("Xóa");
        ctRemoveButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctRemoveButton2ActionPerformed(evt);
            }
        });

        ctUndoButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctUndoButton2.setText("Phục hồi");
        ctUndoButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctUndoButton2ActionPerformed(evt);
            }
        });

        ctReloadButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctReloadButton2.setText("Làm mới");
        ctReloadButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctReloadButton2ActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel24.setText("Cơ sở:");

        ctBranchComboBox2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        ctClassTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã lớp", "Tên lớp", "Mã khoa"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(ctClassTable);
        if (ctClassTable.getColumnModel().getColumnCount() > 0) {
            ctClassTable.getColumnModel().getColumn(0).setResizable(false);
            ctClassTable.getColumnModel().getColumn(1).setResizable(false);
            ctClassTable.getColumnModel().getColumn(2).setResizable(false);
            ctClassTable.getColumnModel().getColumn(2).setHeaderValue("Tên");
        }

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel25.setText("Mã lớp");

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel26.setText("Tên lớp");

        ctClassIDTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        ctClassNameTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel27.setText("Mã khoa");

        ctClassDepartmentComboBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout categoriesFormPanel2Layout = new javax.swing.GroupLayout(categoriesFormPanel2);
        categoriesFormPanel2.setLayout(categoriesFormPanel2Layout);
        categoriesFormPanel2Layout.setHorizontalGroup(
            categoriesFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6)
            .addGroup(categoriesFormPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(categoriesFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(categoriesFormPanel2Layout.createSequentialGroup()
                        .addComponent(ctAddButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, categoriesFormPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel24)
                        .addGap(34, 34, 34)))
                .addGroup(categoriesFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(categoriesFormPanel2Layout.createSequentialGroup()
                        .addComponent(ctEditButton2)
                        .addGap(18, 18, 18)
                        .addComponent(ctSaveButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(ctRemoveButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(ctUndoButton2)
                        .addGap(18, 18, 18)
                        .addComponent(ctReloadButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(403, 403, 403))
                    .addGroup(categoriesFormPanel2Layout.createSequentialGroup()
                        .addComponent(ctBranchComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(categoriesFormPanel2Layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addGroup(categoriesFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26)
                    .addComponent(jLabel27))
                .addGap(37, 37, 37)
                .addGroup(categoriesFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(ctClassIDTextField)
                    .addComponent(ctClassNameTextField)
                    .addComponent(ctClassDepartmentComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        categoriesFormPanel2Layout.setVerticalGroup(
            categoriesFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoriesFormPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(categoriesFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ctAddButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctEditButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctSaveButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctRemoveButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctUndoButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctReloadButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(categoriesFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ctBranchComboBox2)
                    .addComponent(jLabel24))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(categoriesFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(ctClassIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(categoriesFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ctClassNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addGap(34, 34, 34)
                .addGroup(categoriesFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(ctClassDepartmentComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(85, 85, 85))
        );

        categoriesFormPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        categoriesFormPanel3.setPreferredSize(new java.awt.Dimension(1007, 535));

        ctAddButton3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctAddButton3.setText("Thêm");
        ctAddButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctAddButton3ActionPerformed(evt);
            }
        });

        ctEditButton3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctEditButton3.setText("Hiệu Chỉnh");
        ctEditButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctEditButton3ActionPerformed(evt);
            }
        });

        ctSaveButton3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctSaveButton3.setText("Ghi");
        ctSaveButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctSaveButton3ActionPerformed(evt);
            }
        });

        ctRemoveButton3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctRemoveButton3.setText("Xóa");
        ctRemoveButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctRemoveButton3ActionPerformed(evt);
            }
        });

        ctUndoButton3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctUndoButton3.setText("Phục hồi");
        ctUndoButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctUndoButton3ActionPerformed(evt);
            }
        });

        ctReloadButton3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctReloadButton3.setText("Làm mới");
        ctReloadButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctReloadButton3ActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel28.setText("Cơ sở:");

        ctBranchComboBox3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        ctTeacherTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã giảng viên", "Họ", "Tên", "Học vị", "Mã khoa"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(ctTeacherTable);
        if (ctTeacherTable.getColumnModel().getColumnCount() > 0) {
            ctTeacherTable.getColumnModel().getColumn(0).setResizable(false);
            ctTeacherTable.getColumnModel().getColumn(1).setResizable(false);
            ctTeacherTable.getColumnModel().getColumn(2).setResizable(false);
            ctTeacherTable.getColumnModel().getColumn(2).setHeaderValue("Tên");
            ctTeacherTable.getColumnModel().getColumn(3).setResizable(false);
            ctTeacherTable.getColumnModel().getColumn(3).setHeaderValue("Ngày sinh");
            ctTeacherTable.getColumnModel().getColumn(4).setResizable(false);
            ctTeacherTable.getColumnModel().getColumn(4).setHeaderValue("Địa chỉ");
        }

        ctTeacherLastNameTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        ctTeacherIDTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel29.setText("Họ:");

        jLabel30.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel30.setText("Tên:");

        ctTeacherFirstNameTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel31.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel31.setText("Mã giáo viên:");

        ctTeacherDegreeTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel32.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel32.setText("Học vị:");

        jLabel33.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel33.setText("Mã khoa:");

        ctTeacherDepartmentComboBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout categoriesFormPanel3Layout = new javax.swing.GroupLayout(categoriesFormPanel3);
        categoriesFormPanel3.setLayout(categoriesFormPanel3Layout);
        categoriesFormPanel3Layout.setHorizontalGroup(
            categoriesFormPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7)
            .addGroup(categoriesFormPanel3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(categoriesFormPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(categoriesFormPanel3Layout.createSequentialGroup()
                        .addComponent(ctAddButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, categoriesFormPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel28)
                        .addGap(34, 34, 34)))
                .addGroup(categoriesFormPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(categoriesFormPanel3Layout.createSequentialGroup()
                        .addComponent(ctEditButton3)
                        .addGap(18, 18, 18)
                        .addComponent(ctSaveButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(ctRemoveButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(ctUndoButton3)
                        .addGap(18, 18, 18)
                        .addComponent(ctReloadButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(403, 403, 403))
                    .addGroup(categoriesFormPanel3Layout.createSequentialGroup()
                        .addComponent(ctBranchComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(categoriesFormPanel3Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(categoriesFormPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(categoriesFormPanel3Layout.createSequentialGroup()
                        .addGroup(categoriesFormPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel31)
                            .addComponent(jLabel29))
                        .addGap(18, 18, 18)
                        .addGroup(categoriesFormPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ctTeacherLastNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(ctTeacherIDTextField)))
                    .addGroup(categoriesFormPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                        .addComponent(ctTeacherFirstNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(134, 134, 134)
                .addGroup(categoriesFormPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel32)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, categoriesFormPanel3Layout.createSequentialGroup()
                        .addGap(89, 89, 89)
                        .addComponent(ctTeacherDegreeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(categoriesFormPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ctTeacherDepartmentComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        categoriesFormPanel3Layout.setVerticalGroup(
            categoriesFormPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoriesFormPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(categoriesFormPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ctAddButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctEditButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctSaveButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctRemoveButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctUndoButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctReloadButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(categoriesFormPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ctBranchComboBox3)
                    .addComponent(jLabel28))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addGroup(categoriesFormPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(categoriesFormPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ctTeacherDegreeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel32))
                    .addGroup(categoriesFormPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel31)
                        .addComponent(ctTeacherIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27)
                .addGroup(categoriesFormPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ctTeacherLastNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29)
                    .addComponent(jLabel33)
                    .addComponent(ctTeacherDepartmentComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(categoriesFormPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ctTeacherFirstNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addGap(108, 108, 108))
        );

        categoriesFormPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        categoriesFormPanel4.setPreferredSize(new java.awt.Dimension(1004, 535));

        ctAddButton4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctAddButton4.setText("Thêm");
        ctAddButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctAddButton4ActionPerformed(evt);
            }
        });

        ctEditButton4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctEditButton4.setText("Hiệu Chỉnh");
        ctEditButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctEditButton4ActionPerformed(evt);
            }
        });

        ctSaveButton4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctSaveButton4.setText("Ghi");
        ctSaveButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctSaveButton4ActionPerformed(evt);
            }
        });

        ctRemoveButton4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctRemoveButton4.setText("Xóa");
        ctRemoveButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctRemoveButton4ActionPerformed(evt);
            }
        });

        ctUndoButton4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctUndoButton4.setText("Phục hồi");
        ctUndoButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctUndoButton4ActionPerformed(evt);
            }
        });

        ctReloadButton4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctReloadButton4.setText("Làm mới");
        ctReloadButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctReloadButton4ActionPerformed(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel34.setText("Cơ sở:");

        ctBranchComboBox4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        ctStudentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã sinh viên", "Họ", "Tên", "Ngày sinh", "Địa chỉ", "Mã lớp"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane8.setViewportView(ctStudentTable);
        if (ctStudentTable.getColumnModel().getColumnCount() > 0) {
            ctStudentTable.getColumnModel().getColumn(0).setResizable(false);
            ctStudentTable.getColumnModel().getColumn(1).setResizable(false);
            ctStudentTable.getColumnModel().getColumn(2).setResizable(false);
            ctStudentTable.getColumnModel().getColumn(2).setHeaderValue("Tên");
            ctStudentTable.getColumnModel().getColumn(3).setResizable(false);
            ctStudentTable.getColumnModel().getColumn(3).setHeaderValue("Ngày sinh");
            ctStudentTable.getColumnModel().getColumn(4).setResizable(false);
            ctStudentTable.getColumnModel().getColumn(4).setHeaderValue("Địa chỉ");
            ctStudentTable.getColumnModel().getColumn(5).setResizable(false);
            ctStudentTable.getColumnModel().getColumn(5).setHeaderValue("Mã lớp");
        }

        jLabel35.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel35.setText("Mã sinh viên:");

        jLabel36.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel36.setText("Họ:");

        jLabel37.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel37.setText("Tên:");

        jLabel38.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel38.setText("Ngày sinh:");

        ctStudentIDTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        ctStudentLastNameTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        ctStudentFirstNameTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        ctStudentBirthDayTextField.setDateFormatString("dd/MM/yyyy");

        jLabel39.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel39.setText("Địa chỉ:");

        ctStudentAddressTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctStudentAddressTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctStudentAddressTextFieldActionPerformed(evt);
            }
        });

        jLabel40.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel40.setText("Mã lớp:");

        ctStudentClassComboBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout categoriesFormPanel4Layout = new javax.swing.GroupLayout(categoriesFormPanel4);
        categoriesFormPanel4.setLayout(categoriesFormPanel4Layout);
        categoriesFormPanel4Layout.setHorizontalGroup(
            categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8)
            .addGroup(categoriesFormPanel4Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(categoriesFormPanel4Layout.createSequentialGroup()
                        .addComponent(ctAddButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, categoriesFormPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel34)
                        .addGap(34, 34, 34)))
                .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(categoriesFormPanel4Layout.createSequentialGroup()
                        .addComponent(ctEditButton4)
                        .addGap(18, 18, 18)
                        .addComponent(ctSaveButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(ctRemoveButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(ctUndoButton4)
                        .addGap(18, 18, 18)
                        .addComponent(ctReloadButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(403, 403, 403))
                    .addGroup(categoriesFormPanel4Layout.createSequentialGroup()
                        .addComponent(ctBranchComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(categoriesFormPanel4Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel35)
                    .addComponent(jLabel36)
                    .addComponent(jLabel37))
                .addGap(14, 14, 14)
                .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ctStudentIDTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                    .addComponent(ctStudentLastNameTextField)
                    .addComponent(ctStudentFirstNameTextField))
                .addGap(127, 127, 127)
                .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(categoriesFormPanel4Layout.createSequentialGroup()
                        .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel39)
                            .addComponent(jLabel38))
                        .addGap(31, 31, 31)
                        .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ctStudentBirthDayTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                            .addComponent(ctStudentAddressTextField)))
                    .addGroup(categoriesFormPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addGap(48, 48, 48)
                        .addComponent(ctStudentClassComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(271, 271, 271))
        );
        categoriesFormPanel4Layout.setVerticalGroup(
            categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoriesFormPanel4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ctAddButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctEditButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctSaveButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctRemoveButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctUndoButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctReloadButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ctBranchComboBox4)
                    .addComponent(jLabel34))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(categoriesFormPanel4Layout.createSequentialGroup()
                        .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel39)
                            .addComponent(ctStudentAddressTextField))
                        .addGap(36, 36, 36)
                        .addComponent(ctStudentBirthDayTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(categoriesFormPanel4Layout.createSequentialGroup()
                        .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ctStudentIDTextField)
                            .addComponent(jLabel35, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(36, 36, 36)
                        .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel36)
                            .addComponent(ctStudentLastNameTextField)
                            .addComponent(jLabel38))))
                .addGap(37, 37, 37)
                .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(ctStudentFirstNameTextField)
                    .addComponent(jLabel40)
                    .addComponent(ctStudentClassComboBox))
                .addGap(96, 96, 96))
        );

        categoriesFormPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        ctAddButton5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctAddButton5.setText("Thêm");
        ctAddButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctAddButton5ActionPerformed(evt);
            }
        });

        ctEditButton5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctEditButton5.setText("Hiệu Chỉnh");
        ctEditButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctEditButton5ActionPerformed(evt);
            }
        });

        ctSaveButton5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctSaveButton5.setText("Ghi");
        ctSaveButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctSaveButton5ActionPerformed(evt);
            }
        });

        ctRemoveButton5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctRemoveButton5.setText("Xóa");
        ctRemoveButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctRemoveButton5ActionPerformed(evt);
            }
        });

        ctUndoButton5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctUndoButton5.setText("Phục hồi");
        ctUndoButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctUndoButton5ActionPerformed(evt);
            }
        });

        ctReloadButton5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctReloadButton5.setText("Làm mới");
        ctReloadButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctReloadButton5ActionPerformed(evt);
            }
        });

        jLabel41.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel41.setText("Cơ sở:");

        ctBranchComboBox5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        ctSubjectTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã môn học", "Tên môn học"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane9.setViewportView(ctSubjectTable);
        if (ctSubjectTable.getColumnModel().getColumnCount() > 0) {
            ctSubjectTable.getColumnModel().getColumn(0).setResizable(false);
            ctSubjectTable.getColumnModel().getColumn(1).setResizable(false);
        }

        jLabel42.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel42.setText("Mã môn học:");

        ctSubjectIDTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel43.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel43.setText("Tên môn học:");

        ctSubjectNameTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout categoriesFormPanel5Layout = new javax.swing.GroupLayout(categoriesFormPanel5);
        categoriesFormPanel5.setLayout(categoriesFormPanel5Layout);
        categoriesFormPanel5Layout.setHorizontalGroup(
            categoriesFormPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane9)
            .addGroup(categoriesFormPanel5Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(categoriesFormPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(categoriesFormPanel5Layout.createSequentialGroup()
                        .addComponent(ctAddButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, categoriesFormPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel41)
                        .addGap(34, 34, 34)))
                .addGroup(categoriesFormPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(categoriesFormPanel5Layout.createSequentialGroup()
                        .addComponent(ctEditButton5)
                        .addGap(18, 18, 18)
                        .addComponent(ctSaveButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(ctRemoveButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(ctUndoButton5)
                        .addGap(18, 18, 18)
                        .addComponent(ctReloadButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(403, 403, 403))
                    .addGroup(categoriesFormPanel5Layout.createSequentialGroup()
                        .addComponent(ctBranchComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(categoriesFormPanel5Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addGroup(categoriesFormPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel42)
                    .addComponent(jLabel43))
                .addGap(18, 18, 18)
                .addGroup(categoriesFormPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ctSubjectIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctSubjectNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        categoriesFormPanel5Layout.setVerticalGroup(
            categoriesFormPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoriesFormPanel5Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(categoriesFormPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ctAddButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctEditButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctSaveButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctRemoveButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctUndoButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctReloadButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(categoriesFormPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ctBranchComboBox5)
                    .addComponent(jLabel41))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addGroup(categoriesFormPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(ctSubjectIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(categoriesFormPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ctSubjectNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43))
                .addGap(144, 144, 144))
        );

        javax.swing.GroupLayout tabCategoriesLayout = new javax.swing.GroupLayout(tabCategories);
        tabCategories.setLayout(tabCategoriesLayout);
        tabCategoriesLayout.setHorizontalGroup(
            tabCategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(categoriesOptionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1005, Short.MAX_VALUE)
            .addComponent(categoriesFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(tabCategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(categoriesFormPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1005, Short.MAX_VALUE))
            .addGroup(tabCategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(tabCategoriesLayout.createSequentialGroup()
                    .addComponent(categoriesFormPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 1005, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
            .addGroup(tabCategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(tabCategoriesLayout.createSequentialGroup()
                    .addComponent(categoriesFormPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 1005, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
            .addGroup(tabCategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(categoriesFormPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tabCategoriesLayout.setVerticalGroup(
            tabCategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabCategoriesLayout.createSequentialGroup()
                .addComponent(categoriesOptionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(categoriesFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(tabCategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabCategoriesLayout.createSequentialGroup()
                    .addGap(122, 122, 122)
                    .addComponent(categoriesFormPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)))
            .addGroup(tabCategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabCategoriesLayout.createSequentialGroup()
                    .addGap(122, 122, 122)
                    .addComponent(categoriesFormPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)))
            .addGroup(tabCategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabCategoriesLayout.createSequentialGroup()
                    .addGap(121, 121, 121)
                    .addComponent(categoriesFormPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)))
            .addGroup(tabCategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabCategoriesLayout.createSequentialGroup()
                    .addGap(120, 120, 120)
                    .addComponent(categoriesFormPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        mainTabbedPane.addTab("Danh mục", tabCategories);

        tabMajor.setBackground(new java.awt.Color(255, 255, 255));

        majorOptionPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jSeparator8.setOrientation(javax.swing.SwingConstants.VERTICAL);

        mjQuestionManagementButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        mjQuestionManagementButton.setText("Soạn câu hỏi");

        mjMarkManagementButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        mjMarkManagementButton.setText("Quản lý điểm sinh viên");

        jSeparator9.setOrientation(javax.swing.SwingConstants.VERTICAL);

        mjTestExamButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        mjTestExamButton.setText("Thi thử");

        jSeparator13.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout majorOptionPanelLayout = new javax.swing.GroupLayout(majorOptionPanel);
        majorOptionPanel.setLayout(majorOptionPanelLayout);
        majorOptionPanelLayout.setHorizontalGroup(
            majorOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(majorOptionPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(mjQuestionManagementButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(mjMarkManagementButton)
                .addGap(41, 41, 41)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(mjTestExamButton, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        majorOptionPanelLayout.setVerticalGroup(
            majorOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
            .addComponent(jSeparator9, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(majorOptionPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(majorOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mjQuestionManagementButton, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mjMarkManagementButton, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mjTestExamButton, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jSeparator13, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 512, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout tabMajorLayout = new javax.swing.GroupLayout(tabMajor);
        tabMajor.setLayout(tabMajorLayout);
        tabMajorLayout.setHorizontalGroup(
            tabMajorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(majorOptionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        tabMajorLayout.setVerticalGroup(
            tabMajorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabMajorLayout.createSequentialGroup()
                .addComponent(majorOptionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mainTabbedPane.addTab("Nghiệp vụ", tabMajor);

        tabReport.setBackground(new java.awt.Color(255, 255, 255));

        reportOptionPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);

        rpRegisterButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        rpRegisterButton.setText("Danh sách đăng ký");
        rpRegisterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rpRegisterButtonActionPerformed(evt);
            }
        });

        rpTranscriptButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        rpTranscriptButton.setText("Bảng điểm lớp");
        rpTranscriptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rpTranscriptButtonActionPerformed(evt);
            }
        });

        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator7.setOrientation(javax.swing.SwingConstants.VERTICAL);

        rpResultButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        rpResultButton.setText("Bảng điểm sinh viên");
        rpResultButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rpResultButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout reportOptionPanelLayout = new javax.swing.GroupLayout(reportOptionPanel);
        reportOptionPanel.setLayout(reportOptionPanelLayout);
        reportOptionPanelLayout.setHorizontalGroup(
            reportOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportOptionPanelLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(rpRegisterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(rpResultButton, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(rpTranscriptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        reportOptionPanelLayout.setVerticalGroup(
            reportOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jSeparator6, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jSeparator7, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, reportOptionPanelLayout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addGroup(reportOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, reportOptionPanelLayout.createSequentialGroup()
                        .addGroup(reportOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rpRegisterButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rpResultButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, reportOptionPanelLayout.createSequentialGroup()
                        .addComponent(rpTranscriptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31))))
        );

        reportFormPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        rpStudentIDTextField1.setMaximumSize(new java.awt.Dimension(166, 2147483647));

        rpSubjectComboBox1.setToolTipText("");

        rpExamTimeComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2" }));

        rpViewResultButton1.setText("Xem kết quả");
        rpViewResultButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rpViewResultButton1ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Mã sinh viên");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Môn học");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Lần thi");

        rpResultPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        rpDataTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(rpDataTable1);
        if (rpDataTable1.getColumnModel().getColumnCount() > 0) {
            rpDataTable1.getColumnModel().getColumn(0).setResizable(false);
            rpDataTable1.getColumnModel().getColumn(0).setPreferredWidth(50);
            rpDataTable1.getColumnModel().getColumn(1).setResizable(false);
            rpDataTable1.getColumnModel().getColumn(1).setPreferredWidth(50);
            rpDataTable1.getColumnModel().getColumn(2).setResizable(false);
            rpDataTable1.getColumnModel().getColumn(2).setPreferredWidth(200);
            rpDataTable1.getColumnModel().getColumn(3).setResizable(false);
            rpDataTable1.getColumnModel().getColumn(3).setPreferredWidth(200);
            rpDataTable1.getColumnModel().getColumn(4).setResizable(false);
            rpDataTable1.getColumnModel().getColumn(4).setPreferredWidth(50);
            rpDataTable1.getColumnModel().getColumn(5).setResizable(false);
            rpDataTable1.getColumnModel().getColumn(5).setPreferredWidth(50);
        }

        rpResultPanel1.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 98, 1020, 310));

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Lớp:");
        rpResultPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(94, 11, -1, -1));

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel16.setText("Họ tên:");
        rpResultPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(94, 46, -1, -1));

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel17.setText("Môn thi:");
        rpResultPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(485, 11, -1, -1));

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel18.setText("Ngày thi:");
        rpResultPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(485, 46, -1, -1));

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel19.setText("Lần thi :");
        rpResultPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(845, 46, -1, -1));

        rpClassLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rpClassLabel1.setText("classLabel");
        rpResultPanel1.add(rpClassLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 11, -1, -1));

        rpNameLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rpNameLabel1.setText("nameLabel");
        rpResultPanel1.add(rpNameLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 46, -1, -1));

        rpSubjectLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rpSubjectLabel1.setText("subjectLabel");
        rpResultPanel1.add(rpSubjectLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(563, 11, -1, -1));

        rpDateLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rpDateLabel1.setText("dateLabel");
        rpResultPanel1.add(rpDateLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(563, 46, -1, -1));

        rpExamTimeLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rpExamTimeLabel1.setText("##");
        rpResultPanel1.add(rpExamTimeLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(913, 46, -1, -1));

        javax.swing.GroupLayout reportFormPanel1Layout = new javax.swing.GroupLayout(reportFormPanel1);
        reportFormPanel1.setLayout(reportFormPanel1Layout);
        reportFormPanel1Layout.setHorizontalGroup(
            reportFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, reportFormPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(rpStudentIDTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(rpSubjectComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(166, 166, 166)
                .addComponent(jLabel7)
                .addGap(26, 26, 26)
                .addComponent(rpExamTimeComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(78, 78, 78))
            .addComponent(rpResultPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(reportFormPanel1Layout.createSequentialGroup()
                .addGap(432, 432, 432)
                .addComponent(rpViewResultButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        reportFormPanel1Layout.setVerticalGroup(
            reportFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportFormPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(reportFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(reportFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(rpStudentIDTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(reportFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(rpSubjectComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)
                        .addComponent(rpExamTimeComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(28, 28, 28)
                .addComponent(rpViewResultButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(rpResultPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        reportFormPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("Lớp");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setText("Môn học");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText("Lần thi");

        rpExamTimeComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2" }));

        rpViewResultButton2.setText("Xem bảng điểm");
        rpViewResultButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rpViewResultButton2ActionPerformed(evt);
            }
        });

        rpDataTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã sinh viên", "Họ", "Tên", "Điểm", "Điểm chữ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(rpDataTable2);
        if (rpDataTable2.getColumnModel().getColumnCount() > 0) {
            rpDataTable2.getColumnModel().getColumn(0).setResizable(false);
            rpDataTable2.getColumnModel().getColumn(1).setResizable(false);
            rpDataTable2.getColumnModel().getColumn(2).setResizable(false);
            rpDataTable2.getColumnModel().getColumn(3).setResizable(false);
            rpDataTable2.getColumnModel().getColumn(3).setPreferredWidth(50);
            rpDataTable2.getColumnModel().getColumn(4).setResizable(false);
            rpDataTable2.getColumnModel().getColumn(4).setPreferredWidth(200);
        }

        javax.swing.GroupLayout reportFormPanel2Layout = new javax.swing.GroupLayout(reportFormPanel2);
        reportFormPanel2.setLayout(reportFormPanel2Layout);
        reportFormPanel2Layout.setHorizontalGroup(
            reportFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(reportFormPanel2Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel10)
                .addGap(38, 38, 38)
                .addComponent(rpSubjectComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(119, 119, 119)
                .addGroup(reportFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(reportFormPanel2Layout.createSequentialGroup()
                        .addComponent(rpViewResultButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(466, 466, 466))
                    .addGroup(reportFormPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(59, 59, 59)
                        .addComponent(rpClassComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 193, Short.MAX_VALUE)
                        .addComponent(jLabel11)
                        .addGap(30, 30, 30)
                        .addComponent(rpExamTimeComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34))))
        );
        reportFormPanel2Layout.setVerticalGroup(
            reportFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportFormPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(reportFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(rpExamTimeComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rpSubjectComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rpClassComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(rpViewResultButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE))
        );

        reportFormPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        rpFromDateChooser3.setDateFormatString("dd/MM/yyyy");

        rpToDateChooser3.setDateFormatString("dd/MM/yyyy");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setText("Từ ngày");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setText("Đến ngày");

        rpViewResultButton3.setText("Xem danh sách đăng ký");
        rpViewResultButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rpViewResultButton3ActionPerformed(evt);
            }
        });

        rpCurrentSiteInfoLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rpCurrentSiteInfoLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rpCurrentSiteInfoLabel3.setText("DANH SÁCH ĐĂNG KÝ THI TRẮC NGHIỆM CƠ SỞ 1");

        rpCurrentSiteTable3.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(rpCurrentSiteTable3);
        if (rpCurrentSiteTable3.getColumnModel().getColumnCount() > 0) {
            rpCurrentSiteTable3.getColumnModel().getColumn(0).setResizable(false);
            rpCurrentSiteTable3.getColumnModel().getColumn(0).setPreferredWidth(50);
            rpCurrentSiteTable3.getColumnModel().getColumn(1).setResizable(false);
            rpCurrentSiteTable3.getColumnModel().getColumn(1).setPreferredWidth(100);
            rpCurrentSiteTable3.getColumnModel().getColumn(2).setResizable(false);
            rpCurrentSiteTable3.getColumnModel().getColumn(2).setPreferredWidth(100);
            rpCurrentSiteTable3.getColumnModel().getColumn(3).setResizable(false);
            rpCurrentSiteTable3.getColumnModel().getColumn(3).setPreferredWidth(150);
            rpCurrentSiteTable3.getColumnModel().getColumn(4).setResizable(false);
            rpCurrentSiteTable3.getColumnModel().getColumn(4).setPreferredWidth(50);
            rpCurrentSiteTable3.getColumnModel().getColumn(5).setResizable(false);
            rpCurrentSiteTable3.getColumnModel().getColumn(5).setPreferredWidth(70);
            rpCurrentSiteTable3.getColumnModel().getColumn(6).setResizable(false);
            rpCurrentSiteTable3.getColumnModel().getColumn(6).setPreferredWidth(50);
            rpCurrentSiteTable3.getColumnModel().getColumn(7).setResizable(false);
        }

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setText("Tổng số lượt đăng ký:");

        rpCurrentSiteSumLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        rpCurrentSiteSumLabel3.setText("##");

        rpOtherSiteInfoLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rpOtherSiteInfoLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rpOtherSiteInfoLabel3.setText("DANH SÁCH ĐĂNG KÝ THI TRẮC NGHIỆM CƠ SỞ 2");

        rpOtherSiteTable3.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(rpOtherSiteTable3);
        if (rpOtherSiteTable3.getColumnModel().getColumnCount() > 0) {
            rpOtherSiteTable3.getColumnModel().getColumn(0).setResizable(false);
            rpOtherSiteTable3.getColumnModel().getColumn(0).setPreferredWidth(50);
            rpOtherSiteTable3.getColumnModel().getColumn(1).setResizable(false);
            rpOtherSiteTable3.getColumnModel().getColumn(1).setPreferredWidth(100);
            rpOtherSiteTable3.getColumnModel().getColumn(2).setResizable(false);
            rpOtherSiteTable3.getColumnModel().getColumn(2).setPreferredWidth(100);
            rpOtherSiteTable3.getColumnModel().getColumn(3).setResizable(false);
            rpOtherSiteTable3.getColumnModel().getColumn(3).setPreferredWidth(150);
            rpOtherSiteTable3.getColumnModel().getColumn(4).setResizable(false);
            rpOtherSiteTable3.getColumnModel().getColumn(4).setPreferredWidth(50);
            rpOtherSiteTable3.getColumnModel().getColumn(5).setResizable(false);
            rpOtherSiteTable3.getColumnModel().getColumn(5).setPreferredWidth(70);
            rpOtherSiteTable3.getColumnModel().getColumn(6).setResizable(false);
            rpOtherSiteTable3.getColumnModel().getColumn(6).setPreferredWidth(50);
            rpOtherSiteTable3.getColumnModel().getColumn(7).setResizable(false);
        }

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel15.setText("Tổng số lượt đăng ký:");

        rpOtherSiteSumLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        rpOtherSiteSumLabel3.setText("##");

        rpCurrentSiteDateLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rpCurrentSiteDateLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rpCurrentSiteDateLabel3.setText("TỪ NGÀY dd/MM/yyyy ĐẾN NGÀY dd/MM/yyyy");

        rpOtherSiteDateLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        rpOtherSiteDateLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rpOtherSiteDateLabel3.setText("TỪ NGÀY dd/MM/yyyy ĐẾN NGÀY dd/MM/yyyy");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(18, 18, 18)
                        .addComponent(rpOtherSiteSumLabel3)
                        .addContainerGap(974, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addGap(18, 18, 18)
                                .addComponent(rpCurrentSiteSumLabel3))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 950, Short.MAX_VALUE)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(333, 333, 333)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(rpCurrentSiteDateLabel3))
                            .addComponent(rpCurrentSiteInfoLabel3)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(350, 350, 350)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(rpOtherSiteDateLabel3))
                            .addComponent(rpOtherSiteInfoLabel3))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(rpCurrentSiteInfoLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rpCurrentSiteDateLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(rpCurrentSiteSumLabel3))
                .addGap(50, 50, 50)
                .addComponent(rpOtherSiteInfoLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rpOtherSiteDateLabel3)
                .addGap(35, 35, 35)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(rpOtherSiteSumLabel3))
                .addGap(29, 29, 29))
        );

        rpResultScrollPane3.setViewportView(jPanel2);

        javax.swing.GroupLayout reportFormPanel3Layout = new javax.swing.GroupLayout(reportFormPanel3);
        reportFormPanel3.setLayout(reportFormPanel3Layout);
        reportFormPanel3Layout.setHorizontalGroup(
            reportFormPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportFormPanel3Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jLabel12)
                .addGap(39, 39, 39)
                .addComponent(rpFromDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel13)
                .addGap(45, 45, 45)
                .addComponent(rpToDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91))
            .addComponent(rpResultScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1020, Short.MAX_VALUE)
            .addGroup(reportFormPanel3Layout.createSequentialGroup()
                .addGap(421, 421, 421)
                .addComponent(rpViewResultButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        reportFormPanel3Layout.setVerticalGroup(
            reportFormPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportFormPanel3Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(reportFormPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel13)
                    .addComponent(rpToDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(rpFromDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addComponent(rpViewResultButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(rpResultScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout tabReportLayout = new javax.swing.GroupLayout(tabReport);
        tabReport.setLayout(tabReportLayout);
        tabReportLayout.setHorizontalGroup(
            tabReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(reportOptionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(reportFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(tabReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(reportFormPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(tabReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(reportFormPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tabReportLayout.setVerticalGroup(
            tabReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabReportLayout.createSequentialGroup()
                .addComponent(reportOptionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(reportFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(tabReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabReportLayout.createSequentialGroup()
                    .addGap(127, 127, 127)
                    .addComponent(reportFormPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(tabReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabReportLayout.createSequentialGroup()
                    .addGap(127, 127, 127)
                    .addComponent(reportFormPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        mainTabbedPane.addTab("Báo cáo", tabReport);

        getContentPane().add(mainTabbedPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1010, 670));

        sysInfoPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        sysInfoPanel.setMaximumSize(new java.awt.Dimension(510, 45));
        sysInfoPanel.setPreferredSize(new java.awt.Dimension(1048, 50));
        sysInfoPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sysInfoPanel.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 0, 10, 50));

        sysUserIDInfoLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        sysUserIDInfoLabel.setText("Mã GV:");
        sysInfoPanel.add(sysUserIDInfoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        sysRoleInfoLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        sysRoleInfoLabel.setText("Nhóm:");
        sysInfoPanel.add(sysRoleInfoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 20, -1, -1));

        sysFullNameInfoLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        sysFullNameInfoLabel.setText("Họ tên:");
        sysInfoPanel.add(sysFullNameInfoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, -1, -1));

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sysInfoPanel.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 0, 10, 50));

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sysInfoPanel.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(362, 0, 10, 50));

        getContentPane().add(sysInfoPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 670, 1010, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sysSignInButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sysSignInButtonActionPerformed
        systemFormPanel1.setVisible(true);
        systemFormPanel2.setVisible(false);
    }//GEN-LAST:event_sysSignInButtonActionPerformed

    private void sysLoginButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sysLoginButton1ActionPerformed
        JDBC_Connection.port = sysBranchComboBox1.getSelectedIndex() == 0 ? "1434" : "1435";
        JDBC_Connection.user = sysUsernameTextField1.getText();
        JDBC_Connection.password = sysPasswordTextField1.getText();
        Connection connector = JDBC_Connection.getConnection();

        if (connector != null) {
            try {
                String sql = "{call dbo.SP_Lay_Thong_Tin_Dang_Nhap(?)}";
                PreparedStatement ps = connector.prepareStatement(sql);
                ps.setString(1, JDBC_Connection.user);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    _role = rs.getString("TENNHOM");
                    sysUserIDInfoLabel.setText("Mã GV: " + rs.getString("USERNAME"));
                    sysFullNameInfoLabel.setText("Họ tên: " + rs.getString("HOTEN"));
                    sysRoleInfoLabel.setText("Nhóm: " + _role);

                    sysUsernameTextField1.setText("");
                    sysPasswordTextField1.setText("");

                    setOtherTabSystemEnable(true);
                    configWithRole(_role);
                    JOptionPane.showMessageDialog(this, "Đăng nhập thành công !");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Không thể lấy thông tin đăng nhập\n" + ex);
            }
        }
    }//GEN-LAST:event_sysLoginButton1ActionPerformed

    private void sysLogOutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sysLogOutButtonActionPerformed
        Object[] option = {"Có", "Không"};
        int choice = JOptionPane.showOptionDialog(this, "Bạn có thật sự muốn đăng xuất ?", "Đăng xuất",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[1]);
        if (choice == JOptionPane.YES_OPTION) {
            sysUserIDInfoLabel.setText("Mã GV:");
            sysFullNameInfoLabel.setText("Họ tên:");
            sysRoleInfoLabel.setText("Nhóm:");

            JDBC_Connection.port = null;
            JDBC_Connection.user = null;
            JDBC_Connection.password = null;
            _role = null;

            setOtherTabSystemEnable(false);
            configWithRole(_role);
        }
    }//GEN-LAST:event_sysLogOutButtonActionPerformed

    private void rpTranscriptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rpTranscriptButtonActionPerformed
        reportFormPanel2.setVisible(true);
        reportFormPanel1.setVisible(false);
        reportFormPanel3.setVisible(false);

        loadSubjectComboBox(rpSubjectComboBox2);
        loadClassComboBox(rpClassComboBox2);
    }//GEN-LAST:event_rpTranscriptButtonActionPerformed

    private void rpViewResultButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rpViewResultButton1ActionPerformed
        String studentID = rpStudentIDTextField1.getText().toUpperCase();
        String subjectID = _listSubject.get(rpSubjectComboBox1.getSelectedIndex()).getMamh();
        int examTime = Integer.valueOf(rpExamTimeComboBox1.getSelectedItem().toString());

        if (checkStudentID(studentID)) {
            Connection connector = JDBC_Connection.getConnection();
            String sql = "{call dbo.SP_Xem_Bang_Diem_Sinh_Vien(?,?,?)}";
            List result = new ArrayList<>();
            try {
                PreparedStatement ps = connector.prepareStatement(sql);
                ps.setString(1, studentID);
                ps.setString(2, subjectID);
                ps.setInt(3, examTime);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    result.add(rs.getString("MASV"));
                    result.add(rs.getString("HOTEN"));
                    result.add(rs.getString("LOP"));
                    result.add(rs.getString("TENMH"));
                    result.add(rs.getString("LAN"));
                    result.add(rs.getDate("NGAYTHI"));
                    result.add(rs.getString("BAITHI"));
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy kết quả bài thi của sinh viên!");
                    return;
                }

                rpResultPanel1.setVisible(true);
                rpClassLabel1.setText(result.get(2).toString());
                rpNameLabel1.setText(result.get(1).toString());
                rpSubjectLabel1.setText(result.get(3).toString());
                rpDateLabel1.setText(new SimpleDateFormat("dd/MM/yyyy").format(result.get(5)));
                rpExamTimeLabel1.setText(result.get(4).toString());

                rpDataTable1.setShowGrid(true);
                rpDataTable1.getColumnModel().getColumn(3).setCellRenderer(new TableCellRenderHelper());
                rpDataTable1.getColumnModel().getColumn(2).setCellRenderer(new TableCellRenderHelper());

                DefaultTableModel model = (DefaultTableModel) rpDataTable1.getModel();
                model.setNumRows(0);

                sql = "{call dbo.SP_Xem_Ket_Qua(?)}";
                int examID = Integer.valueOf(result.get(6).toString());
                try {
                    ps = connector.prepareStatement(sql);
                    ps.setInt(1, examID);

                    rs = ps.executeQuery();
                    while (rs.next()) {
                        Question question = new QuestionDao().getQuestionById(rs.getInt("CAUHOI"));
                        Vector data = new Vector();
                        data.add(rs.getInt("STT"));
                        data.add(question.getCauHoi());
                        data.add(question.getNoiDung());
                        data.add(String.format("A: %s\nB: %s\nC: %s\nD: %s",
                                question.getA(), question.getB(), question.getC(), question.getD()));
                        data.add(question.getDapAn());
                        data.add(rs.getString("DACHON"));
                        model.addRow(data);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }//GEN-LAST:event_rpViewResultButton1ActionPerformed

    private void mainTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mainTabbedPaneStateChanged
        int id = mainTabbedPane.getSelectedIndex();
        switch (id) {
            case 0: {
                systemFormPanel1.setVisible(true);
                systemFormPanel2.setVisible(false);
                break;
            }
            case 1: {
                categoriesFormPanel1.setVisible(false);
                categoriesFormPanel2.setVisible(false);
                categoriesFormPanel3.setVisible(false);
                categoriesFormPanel4.setVisible(false);
                categoriesFormPanel5.setVisible(false);
                break;
            }
            case 3: {
                reportFormPanel1.setVisible(false);
                reportFormPanel2.setVisible(false);
                reportFormPanel3.setVisible(false);
                break;
            }
        }
    }//GEN-LAST:event_mainTabbedPaneStateChanged

    private void rpViewResultButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rpViewResultButton2ActionPerformed
        String subjectID = _listSubject.get(rpSubjectComboBox2.getSelectedIndex()).getMamh();
        String classroomID = _listClassroom.get(rpClassComboBox2.getSelectedIndex()).getMaLop();
        int examTime = Integer.valueOf(rpExamTimeComboBox2.getSelectedItem().toString());

        DefaultTableModel model = (DefaultTableModel) rpDataTable2.getModel();
        model.setNumRows(0);

        Connection connector = JDBC_Connection.getConnection();
        String sql = "{call dbo.SP_Xem_Bang_Diem_Lop(?,?,?)}";

        try {
            PreparedStatement ps = connector.prepareStatement(sql);
            ps.setString(1, subjectID);
            ps.setString(2, classroomID);
            ps.setInt(3, examTime);

            ResultSet rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy kết quả !");
            } else {
                while (rs.next()) {
                    Vector vt = new Vector();
                    Double mark = rs.getDouble("DIEM");
                    vt.add(rs.getString("MASV"));
                    vt.add(rs.getString("HO"));
                    vt.add(rs.getString("TEN"));
                    vt.add(mark);
                    vt.add(convertMarkToString(mark));
                    model.addRow(vt);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_rpViewResultButton2ActionPerformed

    private void rpResultButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rpResultButtonActionPerformed
        reportFormPanel1.setVisible(true);
        reportFormPanel2.setVisible(false);
        reportFormPanel3.setVisible(false);

        loadSubjectComboBox(rpSubjectComboBox1);
        rpResultPanel1.setVisible(false);
    }//GEN-LAST:event_rpResultButtonActionPerformed

    private void rpViewResultButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rpViewResultButton3ActionPerformed
        Date fromDate = rpFromDateChooser3.getDate();
        Date toDate = rpToDateChooser3.getDate();

        if (fromDate == null || toDate == null) {
            return;
        }

        String datePattern = "dd/MM/yyyy";
        String periodText = "\nTỪ NGÀY " + DateHelper.toString(fromDate, datePattern)
                + " ĐẾN NGÀY " + DateHelper.toString(toDate, datePattern);

        rpCurrentSiteDateLabel3.setText(periodText);
        rpOtherSiteDateLabel3.setText(periodText);

        DefaultTableModel currentSiteModel = (DefaultTableModel) rpCurrentSiteTable3.getModel();
        DefaultTableModel otherSiteModel = (DefaultTableModel) rpOtherSiteTable3.getModel();

        currentSiteModel.setNumRows(0);
        otherSiteModel.setNumRows(0);

        String sql = "{call dbo.SP_Get_Register_List(?,?)}";
        Connection connector = JDBC_Connection.getLoginConnection("sa", "123", "1434");
        try {
            PreparedStatement ps = connector.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(fromDate.getTime()));
            ps.setDate(2, new java.sql.Date(toDate.getTime()));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector vt = new Vector();
                String site = rs.getString("MACS");
                vt.add(site.equals("CS1") ? currentSiteModel.getRowCount() + 1 : otherSiteModel.getRowCount() + 1);
                vt.add(rs.getString("TENLOP"));
                vt.add(rs.getString("TENMH"));
                vt.add(rs.getString("TENGV"));
                vt.add(rs.getInt("SOCAUTHI"));
                vt.add(new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate("NGAYTHI")));
                vt.add(rs.getString("DATHI"));

                if (site.equals("CS1")) {
                    currentSiteModel.addRow(vt);
                } else {
                    otherSiteModel.addRow(vt);
                }
            }
            rpCurrentSiteSumLabel3.setText(String.valueOf(currentSiteModel.getRowCount()));
            rpOtherSiteSumLabel3.setText(String.valueOf(otherSiteModel.getRowCount()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_rpViewResultButton3ActionPerformed

    private void rpRegisterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rpRegisterButtonActionPerformed
        reportFormPanel3.setVisible(true);
        reportFormPanel1.setVisible(false);
        reportFormPanel2.setVisible(false);
    }//GEN-LAST:event_rpRegisterButtonActionPerformed

    private void ctDepartmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctDepartmentButtonActionPerformed
        categoriesFormPanel1.setVisible(true);
        categoriesFormPanel2.setVisible(false);
        categoriesFormPanel3.setVisible(false);
        categoriesFormPanel4.setVisible(false);
        categoriesFormPanel5.setVisible(false);

        loadDepartmentTable(ctDepartmentTable);
        loadBranchComboBox(ctBranchComboBox1);
    }//GEN-LAST:event_ctDepartmentButtonActionPerformed

    private void ctClassButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctClassButtonActionPerformed
        categoriesFormPanel2.setVisible(true);
        categoriesFormPanel1.setVisible(false);
        categoriesFormPanel3.setVisible(false);
        categoriesFormPanel4.setVisible(false);
        categoriesFormPanel5.setVisible(false);

        loadClassTable(ctClassTable);
        loadBranchComboBox(ctBranchComboBox2);
    }//GEN-LAST:event_ctClassButtonActionPerformed

    private void ctTeacherButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctTeacherButtonActionPerformed
        categoriesFormPanel3.setVisible(true);
        categoriesFormPanel2.setVisible(false);
        categoriesFormPanel1.setVisible(false);
        categoriesFormPanel4.setVisible(false);
        categoriesFormPanel5.setVisible(false);

        loadTeacherTable(ctTeacherTable);
        loadBranchComboBox(ctBranchComboBox3);
    }//GEN-LAST:event_ctTeacherButtonActionPerformed

    private void ctStudentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctStudentButtonActionPerformed
        categoriesFormPanel4.setVisible(true);
        categoriesFormPanel2.setVisible(false);
        categoriesFormPanel3.setVisible(false);
        categoriesFormPanel1.setVisible(false);
        categoriesFormPanel5.setVisible(false);

        loadStudentTable(ctStudentTable);
        loadBranchComboBox(ctBranchComboBox4);
    }//GEN-LAST:event_ctStudentButtonActionPerformed

    private void ctSubjectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctSubjectButtonActionPerformed
        categoriesFormPanel5.setVisible(true);
        categoriesFormPanel2.setVisible(false);
        categoriesFormPanel3.setVisible(false);
        categoriesFormPanel4.setVisible(false);
        categoriesFormPanel1.setVisible(false);

        loadSubjectTable(ctSubjectTable);
        loadBranchComboBox(ctBranchComboBox5);
    }//GEN-LAST:event_ctSubjectButtonActionPerformed

    private void ctStudentAddressTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctStudentAddressTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctStudentAddressTextFieldActionPerformed

    private void sysSignUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sysSignUpButtonActionPerformed
        systemFormPanel2.setVisible(true);
        systemFormPanel1.setVisible(false);

        loadTeacherIDComboBox(sysTeacherIDComboBox2);

        if (_role.equals("TRUONG")) {
            sysRoleComboBox2.removeAllItems();
            sysRoleComboBox2.addItem("TRUONG");
            sysRoleComboBox2.setEnabled(false);
        } else if (_role.equals("COSO")) {
            sysRoleComboBox2.removeAllItems();
            sysRoleComboBox2.addItem("COSO");
            sysRoleComboBox2.addItem("GIANGVIEN");
            sysRoleComboBox2.setEnabled(true);
        }
    }//GEN-LAST:event_sysSignUpButtonActionPerformed

    private void ctAddButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctAddButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctAddButton1ActionPerformed

    private void ctEditButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctEditButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctEditButton1ActionPerformed

    private void ctSaveButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctSaveButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctSaveButton1ActionPerformed

    private void ctRemoveButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctRemoveButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctRemoveButton1ActionPerformed

    private void ctUndoButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctUndoButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctUndoButton1ActionPerformed

    private void ctReloadButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctReloadButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctReloadButton1ActionPerformed

    private void ctAddButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctAddButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctAddButton2ActionPerformed

    private void ctEditButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctEditButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctEditButton2ActionPerformed

    private void ctSaveButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctSaveButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctSaveButton2ActionPerformed

    private void ctRemoveButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctRemoveButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctRemoveButton2ActionPerformed

    private void ctUndoButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctUndoButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctUndoButton2ActionPerformed

    private void ctReloadButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctReloadButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctReloadButton2ActionPerformed

    private void ctAddButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctAddButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctAddButton3ActionPerformed

    private void ctEditButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctEditButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctEditButton3ActionPerformed

    private void ctSaveButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctSaveButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctSaveButton3ActionPerformed

    private void ctRemoveButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctRemoveButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctRemoveButton3ActionPerformed

    private void ctUndoButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctUndoButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctUndoButton3ActionPerformed

    private void ctReloadButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctReloadButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctReloadButton3ActionPerformed

    private void ctAddButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctAddButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctAddButton4ActionPerformed

    private void ctEditButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctEditButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctEditButton4ActionPerformed

    private void ctSaveButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctSaveButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctSaveButton4ActionPerformed

    private void ctRemoveButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctRemoveButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctRemoveButton4ActionPerformed

    private void ctUndoButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctUndoButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctUndoButton4ActionPerformed

    private void ctReloadButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctReloadButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctReloadButton4ActionPerformed

    private void ctAddButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctAddButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctAddButton5ActionPerformed

    private void ctEditButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctEditButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctEditButton5ActionPerformed

    private void ctSaveButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctSaveButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctSaveButton5ActionPerformed

    private void ctRemoveButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctRemoveButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctRemoveButton5ActionPerformed

    private void ctUndoButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctUndoButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctUndoButton5ActionPerformed

    private void ctReloadButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctReloadButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ctReloadButton5ActionPerformed

    private void sysCreateAccountButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sysCreateAccountButton2ActionPerformed
        String username = sysUsernameTextField2.getText();
        String pass = sysPasswordTextField2.getText();
        String retypePass = sysReTypePasswordTextField2.getText();
        String teacherID = sysTeacherIDComboBox2.getSelectedItem().toString();
        String role = sysRoleComboBox2.getSelectedItem().toString();

        if (username.isEmpty() || pass.isEmpty() || retypePass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ thông tin tài khoản và mật khẩu !");
            return;
        }

        if (!pass.equals(retypePass)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu nhập lại không khớp !");
            return;
        }

        try {
            Connection connector = JDBC_Connection.getConnection();
            String sql = "{Call dbo.SP_Tao_Login(?,?,?,?)}";

            PreparedStatement ps = connector.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, pass);
            ps.setString(3, teacherID);
            ps.setString(4, role);
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Tạo tài khoản thành công !");
            sysUsernameTextField2.setText("");
            sysPasswordTextField2.setText("");
            sysReTypePasswordTextField2.setText("");
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_sysCreateAccountButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel categoriesFormPanel1;
    private javax.swing.JPanel categoriesFormPanel2;
    private javax.swing.JPanel categoriesFormPanel3;
    private javax.swing.JPanel categoriesFormPanel4;
    private javax.swing.JPanel categoriesFormPanel5;
    private javax.swing.JPanel categoriesOptionPanel;
    private javax.swing.JButton ctAddButton1;
    private javax.swing.JButton ctAddButton2;
    private javax.swing.JButton ctAddButton3;
    private javax.swing.JButton ctAddButton4;
    private javax.swing.JButton ctAddButton5;
    private javax.swing.JComboBox<String> ctBranchComboBox1;
    private javax.swing.JComboBox<String> ctBranchComboBox2;
    private javax.swing.JComboBox<String> ctBranchComboBox3;
    private javax.swing.JComboBox<String> ctBranchComboBox4;
    private javax.swing.JComboBox<String> ctBranchComboBox5;
    private javax.swing.JButton ctClassButton;
    private javax.swing.JComboBox ctClassDepartmentComboBox;
    private javax.swing.JTextField ctClassIDTextField;
    private javax.swing.JTextField ctClassNameTextField;
    private javax.swing.JTable ctClassTable;
    private javax.swing.JComboBox ctDepartmentBranchComboBox;
    private javax.swing.JButton ctDepartmentButton;
    private javax.swing.JTextField ctDepartmentIDTextField;
    private javax.swing.JTextField ctDepartmentNameTextField;
    private javax.swing.JTable ctDepartmentTable;
    private javax.swing.JButton ctEditButton1;
    private javax.swing.JButton ctEditButton2;
    private javax.swing.JButton ctEditButton3;
    private javax.swing.JButton ctEditButton4;
    private javax.swing.JButton ctEditButton5;
    private javax.swing.JButton ctReloadButton1;
    private javax.swing.JButton ctReloadButton2;
    private javax.swing.JButton ctReloadButton3;
    private javax.swing.JButton ctReloadButton4;
    private javax.swing.JButton ctReloadButton5;
    private javax.swing.JButton ctRemoveButton1;
    private javax.swing.JButton ctRemoveButton2;
    private javax.swing.JButton ctRemoveButton3;
    private javax.swing.JButton ctRemoveButton4;
    private javax.swing.JButton ctRemoveButton5;
    private javax.swing.JButton ctSaveButton1;
    private javax.swing.JButton ctSaveButton2;
    private javax.swing.JButton ctSaveButton3;
    private javax.swing.JButton ctSaveButton4;
    private javax.swing.JButton ctSaveButton5;
    private javax.swing.JTextField ctStudentAddressTextField;
    private com.toedter.calendar.JDateChooser ctStudentBirthDayTextField;
    private javax.swing.JButton ctStudentButton;
    private javax.swing.JComboBox ctStudentClassComboBox;
    private javax.swing.JTextField ctStudentFirstNameTextField;
    private javax.swing.JTextField ctStudentIDTextField;
    private javax.swing.JTextField ctStudentLastNameTextField;
    private javax.swing.JTable ctStudentTable;
    private javax.swing.JButton ctSubjectButton;
    private javax.swing.JTextField ctSubjectIDTextField;
    private javax.swing.JTextField ctSubjectNameTextField;
    private javax.swing.JTable ctSubjectTable;
    private javax.swing.JButton ctTeacherButton;
    private javax.swing.JTextField ctTeacherDegreeTextField;
    private javax.swing.JComboBox ctTeacherDepartmentComboBox;
    private javax.swing.JTextField ctTeacherFirstNameTextField;
    private javax.swing.JTextField ctTeacherIDTextField;
    private javax.swing.JTextField ctTeacherLastNameTextField;
    private javax.swing.JTable ctTeacherTable;
    private javax.swing.JButton ctUndoButton1;
    private javax.swing.JButton ctUndoButton2;
    private javax.swing.JButton ctUndoButton3;
    private javax.swing.JButton ctUndoButton4;
    private javax.swing.JButton ctUndoButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JPanel majorOptionPanel;
    private javax.swing.JButton mjMarkManagementButton;
    private javax.swing.JButton mjQuestionManagementButton;
    private javax.swing.JButton mjTestExamButton;
    private javax.swing.JPanel reportFormPanel1;
    private javax.swing.JPanel reportFormPanel2;
    private javax.swing.JPanel reportFormPanel3;
    private javax.swing.JPanel reportOptionPanel;
    private javax.swing.JComboBox<String> rpClassComboBox2;
    private javax.swing.JLabel rpClassLabel1;
    private javax.swing.JLabel rpCurrentSiteDateLabel3;
    private javax.swing.JLabel rpCurrentSiteInfoLabel3;
    private javax.swing.JLabel rpCurrentSiteSumLabel3;
    private javax.swing.JTable rpCurrentSiteTable3;
    private javax.swing.JTable rpDataTable1;
    private javax.swing.JTable rpDataTable2;
    private javax.swing.JLabel rpDateLabel1;
    private javax.swing.JComboBox<String> rpExamTimeComboBox1;
    private javax.swing.JComboBox<String> rpExamTimeComboBox2;
    private javax.swing.JLabel rpExamTimeLabel1;
    private com.toedter.calendar.JDateChooser rpFromDateChooser3;
    private javax.swing.JLabel rpNameLabel1;
    private javax.swing.JLabel rpOtherSiteDateLabel3;
    private javax.swing.JLabel rpOtherSiteInfoLabel3;
    private javax.swing.JLabel rpOtherSiteSumLabel3;
    private javax.swing.JTable rpOtherSiteTable3;
    private javax.swing.JButton rpRegisterButton;
    private javax.swing.JButton rpResultButton;
    private javax.swing.JPanel rpResultPanel1;
    private javax.swing.JScrollPane rpResultScrollPane3;
    private javax.swing.JTextField rpStudentIDTextField1;
    private javax.swing.JComboBox<String> rpSubjectComboBox1;
    private javax.swing.JComboBox<String> rpSubjectComboBox2;
    private javax.swing.JLabel rpSubjectLabel1;
    private com.toedter.calendar.JDateChooser rpToDateChooser3;
    private javax.swing.JButton rpTranscriptButton;
    private javax.swing.JButton rpViewResultButton1;
    private javax.swing.JButton rpViewResultButton2;
    private javax.swing.JButton rpViewResultButton3;
    private javax.swing.JComboBox<String> sysBranchComboBox1;
    private javax.swing.JButton sysCreateAccountButton2;
    private javax.swing.JLabel sysFullNameInfoLabel;
    private javax.swing.JPanel sysInfoPanel;
    private javax.swing.JButton sysLogOutButton;
    private javax.swing.JButton sysLoginButton1;
    private javax.swing.JPasswordField sysPasswordTextField1;
    private javax.swing.JPasswordField sysPasswordTextField2;
    private javax.swing.JPasswordField sysReTypePasswordTextField2;
    private javax.swing.JComboBox<String> sysRoleComboBox2;
    private javax.swing.JLabel sysRoleInfoLabel;
    private javax.swing.JButton sysSignInButton;
    private javax.swing.JButton sysSignUpButton;
    private javax.swing.JComboBox<String> sysTeacherIDComboBox2;
    private javax.swing.JLabel sysUserIDInfoLabel;
    private javax.swing.JTextField sysUsernameTextField1;
    private javax.swing.JTextField sysUsernameTextField2;
    private javax.swing.JPanel systemFormPanel1;
    private javax.swing.JPanel systemFormPanel2;
    private javax.swing.JPanel systemOptionPanel;
    private javax.swing.JPanel tabCategories;
    private javax.swing.JPanel tabMajor;
    private javax.swing.JPanel tabReport;
    private javax.swing.JPanel tabSystem;
    // End of variables declaration//GEN-END:variables
}
