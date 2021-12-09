/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import dao.BranchDao;
import dao.ClassroomDao;
import dao.DepartmentDao;
import dao.QuestionDao;
import dao.RegisterDao;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TeacherDao;
import helper.DateHelper;
import helper.JDBC_Connection;
import helper.PasswordCellRenderHelper;
import helper.TableCellRenderHelper;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import model.Branch;
import model.Classroom;
import model.Department;
import model.Question;
import model.Register;
import model.Student;
import model.Subject;
import model.Teacher;
import undo.UndoClassroom;
import undo.UndoDepartment;
import undo.UndoQuestion;
import undo.UndoRegister;
import undo.UndoStudent;
import undo.UndoSubject;
import undo.UndoTeacher;

/**
 *
 * @author vivau
 */
public class MainFrame extends javax.swing.JFrame {
    
    public static String message;
    
    private List<Subject> _listSubject;
    private List<Classroom> _listClassroom;
    private List<Department> _listDepartment;
    private List<Teacher> _listTeacher;
    private List<Student> _listStudent;
    private List<Question> _listQuestion;
    private List<Register> _listRegister;
    
    private String _userName;
    private String _pass;
    private String _role;
    private String _userID;
    private int _branchIndex;
    
    private int _addDepartmentCount;
    private int _addClassCount;
    private int _addTeacherCount;
    private int _addStudentCount;
    private int _addSubjectCount;
    private int _addQuestionCount;
    private int _addRegisterCount;
    
    private Stack<UndoDepartment> _undoDepart;
    private Stack<UndoClassroom> _undoClass;
    private Stack<UndoStudent> _undoStudent;
    private Stack<UndoTeacher> _undoTeacher;
    private Stack<UndoSubject> _undoSubject;
    private Stack<UndoQuestion> _undoQuestion;
    private Stack<UndoRegister> _undoRegister;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        
        _undoDepart = new Stack<>();
        _undoClass = new Stack<>();
        _undoStudent = new Stack<>();
        _undoTeacher = new Stack<>();
        _undoSubject = new Stack<>();
        _undoQuestion = new Stack<>();
        _undoRegister = new Stack<>();
        
        cleanup();
        loadBranchComboBox(sysBranchComboBox1, 0);
        
        configWithRole(_role);
        setOtherTabSystemEnable(false);
        configRpResultScrollPane3();
        
        initTableFilter(ctDepartmentTable, ctSearchTextField1, ctSearchComboBox1);
        initTableFilter(ctClassTable, ctSearchTextField2, ctSearchComboBox2);
        initTableFilter(ctTeacherTable, ctSearchTextField3, ctSearchComboBox3);
        initTableFilter(ctStudentTable, ctSearchTextField4, ctSearchComboBox4);
        initTableFilter(ctSubjectTable, ctSearchTextField5, ctSearchComboBox5);
        initTableFilter(mjQuestionTable, mjSearchTextField1, mjSearchComboBox1);
        initTableFilter(mjRegisterTable, mjSearchTextField2, mjSearchComboBox2);
    }
    
    private void cleanup() {
        _addDepartmentCount = 0;
        _addClassCount = 0;
        _addTeacherCount = 0;
        _addStudentCount = 0;
        _addSubjectCount = 0;
        _addQuestionCount = 0;
        _addRegisterCount = 0;
        
        _undoDepart.clear();
        _undoClass.clear();
        _undoStudent.clear();
        _undoTeacher.clear();
        _undoSubject.clear();
        _undoQuestion.clear();
        _undoRegister.clear();
    }
    
    private void initTableFilter(JTable table, JTextField textField, JComboBox comboBox) {
        TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(rowSorter);
        
        comboBox.removeAllItems();
        comboBox.addItem("Toàn bộ");
        for (int i = 0; i < table.getColumnCount(); i++) {
            String columnName = table.getColumnName(i);
            if (!columnName.equalsIgnoreCase("Mật khẩu")) {
                comboBox.addItem(columnName);
            }
        }
        
        textField.getDocument().addDocumentListener(new DocumentListener() {
            
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = textField.getText();
                
                if (text.trim().isEmpty()) {
                    rowSorter.setRowFilter(null);
                } else {
                    if (comboBox.getSelectedItem().toString().equals("Toàn bộ")) {
                        rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    } else {
                        int column = comboBox.getSelectedIndex() - 1;
                        rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, column));
                    }
                }
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = textField.getText();
                
                if (text.trim().isEmpty()) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
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
        
        if (role.equals("GIANGVIEN") || role.equals("TRUONG")) {
            boolean isSchool = role.equals("TRUONG");
            sysSignUpButton.setEnabled(isSchool);
            ctBranchComboBox1.setEnabled(isSchool);
            ctBranchComboBox2.setEnabled(isSchool);
            ctBranchComboBox3.setEnabled(isSchool);
            ctBranchComboBox4.setEnabled(isSchool);
            ctBranchComboBox5.setEnabled(isSchool);
            
            mjAddButton1.setEnabled(!isSchool);
            mjEditButton1.setEnabled(!isSchool);
            mjRemoveButton1.setEnabled(!isSchool);
            mjSaveButton1.setEnabled(!isSchool);
            mjUndoButton1.setEnabled(!isSchool);
            mjAddButton2.setEnabled(!isSchool);
            mjEditButton2.setEnabled(!isSchool);
            mjRemoveButton2.setEnabled(!isSchool);
            mjSaveButton2.setEnabled(!isSchool);
            mjUndoButton2.setEnabled(!isSchool);
            
            ctStudentPassLabel.setVisible(isSchool);
            ctStudentPassTextField.setVisible(isSchool);
            ctStudentTable.getColumnModel().getColumn(6).setCellRenderer(isSchool ? new DefaultTableCellRenderer() : new PasswordCellRenderHelper());
            
            ctAddButton1.setEnabled(false);
            ctAddButton2.setEnabled(false);
            ctAddButton3.setEnabled(false);
            ctAddButton4.setEnabled(false);
            ctAddButton5.setEnabled(false);
            ctEditButton1.setEnabled(false);
            ctEditButton2.setEnabled(false);
            ctEditButton3.setEnabled(false);
            ctEditButton4.setEnabled(false);
            ctEditButton5.setEnabled(false);
            ctRemoveButton1.setEnabled(false);
            ctRemoveButton2.setEnabled(false);
            ctRemoveButton3.setEnabled(false);
            ctRemoveButton4.setEnabled(false);
            ctRemoveButton5.setEnabled(false);
            ctSaveButton1.setEnabled(false);
            ctSaveButton2.setEnabled(false);
            ctSaveButton3.setEnabled(false);
            ctSaveButton4.setEnabled(false);
            ctSaveButton5.setEnabled(false);
            ctUndoButton1.setEnabled(false);
            ctUndoButton2.setEnabled(false);
            ctUndoButton3.setEnabled(false);
            ctUndoButton4.setEnabled(false);
            ctUndoButton5.setEnabled(false);
        } else {
            sysSignUpButton.setEnabled(true);
            ctStudentPassLabel.setVisible(true);
            ctStudentPassTextField.setVisible(true);
            ctStudentTable.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer());
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
        model.setRowCount(0);
        for (Department depart : _listDepartment) {
            model.addRow(depart.toArray());
        }
    }
    
    private void loadClassTable(JTable table) {
        _listClassroom = ClassroomDao.getAllClassrooms();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (Classroom classroom : _listClassroom) {
            model.addRow(classroom.toArray());
        }
    }
    
    private void loadTeacherTable(JTable table) {
        _listTeacher = TeacherDao.getAllTeachers();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (Teacher teacher : _listTeacher) {
            model.addRow(teacher.toArray());
        }
    }
    
    private void loadStudentTable(JTable table) {
        _listStudent = StudentDao.getAllStudents();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (Student student : _listStudent) {
            Date date = DateHelper.toDate(student.getNgaySinh());
            student.setNgaySinh(DateHelper.toString2(date));
            model.addRow(student.toArray());
        }
    }
    
    private void loadSubjectTable(JTable table) {
        _listSubject = SubjectDao.getAllSubjects();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (Subject subject : _listSubject) {
            model.addRow(subject.toArray());
        }
    }
    
    private void loadQuestionTable(JTable table) {
        _listQuestion = QuestionDao.getAllQuestions();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (Question question : _listQuestion) {
            model.addRow(question.toArray());
        }
    }
    
    private void loadRegisterTable(JTable table) {
        _listRegister = RegisterDao.getAllRegisters();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (Register register : _listRegister) {
            model.addRow(register.toArray());
        }
    }
    
    private void loadBranchComboBox(JComboBox comboBox, int selectedIndex) {
        Connection connector = JDBC_Connection.getPublisherConnection();
        String sql = "SELECT * FROM Get_Subcribers";
        comboBox.removeAllItems();
        
        try {
            PreparedStatement ps = connector.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                comboBox.addItem(rs.getString("TENCN"));
            }
            comboBox.setSelectedIndex(selectedIndex);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
    }
    
    private void loadSubjectComboBox(JComboBox comboBox, boolean isID) {
        _listSubject = SubjectDao.getAllSubjects();
        comboBox.removeAllItems();
        for (Subject subject : _listSubject) {
            if (isID) {
                comboBox.addItem(subject.getMamh());
            } else {
                comboBox.addItem(subject.getTenmh());
            }
        }
    }
    
    private void loadClassComboBox(JComboBox comboBox, boolean isID) {
        _listClassroom = ClassroomDao.getAllClassrooms();
        comboBox.removeAllItems();
        for (Classroom classroom : _listClassroom) {
            if (isID) {
                comboBox.addItem(classroom.getMaLop());
            } else {
                comboBox.addItem(classroom.getTenLop());
            }
        }
    }
    
    private void loadTeacherIDComboBox(JComboBox comboBox, boolean inBranchOnly) {
        _listTeacher = TeacherDao.getAllTeachers();
        for (Teacher teacher : _listTeacher) {
            if (inBranchOnly) {
                if (TeacherDao.isTeacherInBranch(teacher.getMagv())) {
                    comboBox.addItem(teacher.getMagv());
                }
            } else {
                comboBox.addItem(teacher.getMagv());
            }
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

    //==============================Depart
    private void setDepartInput(Department depart) {
        if (depart == null) {
            ctDepartmentIDTextField.setText("");
            ctDepartmentNameTextField.setText("");
            ctDepartmentBranchComboBox.setSelectedIndex(0);
        } else {
            ctDepartmentIDTextField.setText(depart.getMakh());
            ctDepartmentNameTextField.setText(depart.getTenkh());
            ctDepartmentBranchComboBox.setSelectedItem(depart.getMacs());
        }
    }
    
    private Department getDepartInput() {
        Department depart = new Department();
        depart.setMakh(ctDepartmentIDTextField.getText().trim());
        depart.setTenkh(ctDepartmentNameTextField.getText().trim());
        depart.setMacs(ctDepartmentBranchComboBox.getSelectedItem().toString());
        return depart;
    }
    
    private boolean checkDepartment(Department depart, boolean isEdit) {
        if (depart == null) {
            return false;
        }
        
        boolean check = true;
        String str = "";

        //1
        if (depart.getMakh().isEmpty()) {
            str += "Không được bỏ trống Mã khoa\n";
            check = false;
        } else if (depart.getMakh().matches("\\w{1,8}") == false) {
            str += "Mã khoa: Tối đa 8 chữ cái không dấu hoặc số\n";
            check = false;
        } else if (!isEdit) {
            if (DepartmentDao.getDepartmentById(depart.getMakh()) != null) {
                str += "Mã khoa đã tồn tại\n";
                check = false;
            } else {
                for (int i = _addDepartmentCount; i > 0; i--) {
                    if (depart.getMakh().equals(ctDepartmentTable.getValueAt(ctDepartmentTable.getRowCount() - 1 - i, 0).toString())) {
                        str += "Mã khoa đã tồn tại\n";
                        check = false;
                        break;
                    }
                }
            }
        }

        //2
        if (depart.getTenkh().isEmpty()) {
            str += "Không được bỏ trống Tên khoa\n";
            check = false;
        } else if (depart.getTenkh().matches(".{1,40}") == false) {
            str += "Tên khoa: Tối đa 40 kí tự\n";
            check = false;
        }
        
        if (check == false) {
            JOptionPane.showMessageDialog(this, str, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        
        return check;
    }
    
    private void loadDepartBranchComboBox() {
        ctDepartmentBranchComboBox.removeAllItems();
        List<Branch> list = BranchDao.getAllBranchs();
        ctDepartmentBranchComboBox.addItem(list.get(0).getMacs());
    }
    
    private void setDepartComponentsEnable(boolean enable) {
        ctDepartmentIDTextField.setEnabled(enable);
        ctDepartmentNameTextField.setEnabled(enable);
        ctDepartmentBranchComboBox.setEnabled(enable);
    }

    //======================================ClassRoom
    private void setClassroomInput(Classroom c) {
        if (c == null) {
            ctClassIDTextField.setText("");
            ctClassNameTextField.setText("");
            ctClassDepartmentComboBox.setSelectedIndex(0);
        } else {
            ctClassIDTextField.setText(c.getMaLop());
            ctClassNameTextField.setText(c.getTenLop());
            ctClassDepartmentComboBox.setSelectedItem(c.getMakh());
        }
    }
    
    private Classroom getClassroomInput() {
        Classroom c = new Classroom();
        
        c.setMaLop(ctClassIDTextField.getText().trim());
        c.setTenLop(ctClassNameTextField.getText().trim());
        c.setMakh(ctClassDepartmentComboBox.getSelectedItem().toString());
        return c;
    }
    
    private boolean checkClassroom(Classroom classroom, boolean isEdit) {
        if (classroom == null) {
            return false;
        }
        
        boolean check = true;
        String str = "";
        //1
        if (classroom.getMaLop().isEmpty()) {
            str += "Không được bỏ trống Mã lớp\n";
            check = false;
        } else if (classroom.getMaLop().matches("\\w{1,8}") == false) {
            str += "Mã lớp: Tối đa 8 chữ cái không dấu hoặc số\n";
            check = false;
        } else if (!isEdit) {
            if (ClassroomDao.checkClassroom(classroom.getMaLop()) != 0) {
                str += "Mã lớp đã tồn tại\n";
                check = false;
            } else {
                for (int i = _addClassCount; i > 0; i--) {
                    if (classroom.getMaLop().equals(ctClassTable.getValueAt(ctClassTable.getRowCount() - 1 - i, 0).toString())) {
                        str += "Mã lớp đã tồn tại\n";
                        check = false;
                        break;
                    }
                }
            }
        }
        //2
        if (classroom.getTenLop().isEmpty()) {
            str += "Không được bỏ trống Tên lớp\n";
            check = false;
        } else if (classroom.getTenLop().matches(".{1,40}") == false) {
            str += "Tên lớp: Tối đa 40 kí tự\n";
            check = false;
            
        }
        
        if (check == false) {
            JOptionPane.showMessageDialog(this, str, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        
        return check;
    }
    
    private void loadClassDepartComboBox() {
        List<Department> departs = DepartmentDao.getAllDepartments();
        
        for (Department depart : departs) {
            ctClassDepartmentComboBox.addItem(depart.getMakh());
        }
    }
    
    private void setClassComponentsEnable(boolean enable) {
        ctClassIDTextField.setEnabled(enable);
        ctClassNameTextField.setEnabled(enable);
        ctClassDepartmentComboBox.setEnabled(enable);
    }

    //====================================Teacher
    private void setTeacherInput(Teacher t) {
        if (t == null) {
            ctTeacherIDTextField.setText("");
            ctTeacherLastNameTextField.setText("");
            ctTeacherFirstNameTextField.setText("");
            ctTeacherDepartmentComboBox.setSelectedIndex(0);
            ctTeacherDegreeTextField.setText("");
        } else {
            boolean pass = false;
            ctTeacherIDTextField.setText(t.getMagv());
            ctTeacherLastNameTextField.setText(t.getHo());
            ctTeacherFirstNameTextField.setText(t.getTen());
            for (int i = 0; i < ctTeacherDepartmentComboBox.getItemCount(); i++) {
                if (t.getMakh().equals(ctTeacherDepartmentComboBox.getItemAt(i))) {
                    ctTeacherDepartmentComboBox.setSelectedIndex(i);
                    pass = true;
                }
            }
            if (!pass) {
                ctEditButton3.setEnabled(false);
                ctRemoveButton3.setEnabled(false);
            }
            ctTeacherDegreeTextField.setText(t.getHocVi());
        }
    }
    
    private Teacher getTeacherInput() {
        Teacher teacher = new Teacher();
        
        teacher.setMagv(ctTeacherIDTextField.getText().trim());
        teacher.setHo(ctTeacherLastNameTextField.getText().trim());
        teacher.setTen(ctTeacherFirstNameTextField.getText().trim());
        teacher.setMakh(ctTeacherDepartmentComboBox.getSelectedItem().toString());
        teacher.setHocVi(ctTeacherDegreeTextField.getText().trim());
        return teacher;
    }
    
    private void loadTeacherDepartCbx() {
        _listDepartment = DepartmentDao.getAllDepartments();
        ctTeacherDepartmentComboBox.removeAllItems();
        
        for (Department depart : _listDepartment) {
            ctTeacherDepartmentComboBox.addItem(depart.getMakh());
        }
    }
    
    private boolean checkTeacher(Teacher teacher, boolean isEdit) {
        if (teacher == null) {
            return false;
        }
        
        boolean check = true;
        String reTiengViet = "[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s]";
        String str = "";
        //1
        if (teacher.getMagv().isEmpty()) {
            str += "Không được bỏ trống Mã giáo viên\n";
            check = false;
        } else if (teacher.getMagv().matches("\\w{1,8}") == false) {
            str += "Mã giáo viên: Tối đa 8 chữ cái không dấu hoặc số\n";
            check = false;
        } else if (!isEdit) {
            if (TeacherDao.getTeacherById(teacher.getMagv()) != null) {
                str += "Mã giáo viên đã tồn tại\n";
                check = false;
            } else {
                for (int i = _addTeacherCount; i > 0; i--) {
                    if (teacher.getMagv().equals(ctTeacherTable.getValueAt(ctTeacherTable.getRowCount() - 1 - i, 0).toString())) {
                        str += "\"Mã giáo viên đã tồn tại\n";
                        check = false;
                        break;
                    }
                }
            }
        }
        //2

        if (teacher.getHo().isEmpty()) {
            str += "Không được bỏ trống Họ\n";
            check = false;
        } else if (teacher.getHo().matches(reTiengViet + "+") == false) {
            str += "Họ: Vui lòng sử dụng chữ cái Tiếng Việt\n";
            check = false;
        } else if (teacher.getHo().matches(reTiengViet + "{1,40}") == false) {
            str += "Họ: Tối đa 40 kí tự\n";
            check = false;
        }
        //3

        if (teacher.getTen().isEmpty()) {
            str += "Không được bỏ trống Tên\n";
            check = false;
        } else if (teacher.getTen().matches(reTiengViet + "+") == false) {
            str += "Tên: Vui lòng sử dụng chữ cái Tiếng Việt\n";
            check = false;
        } else if (teacher.getTen().matches(reTiengViet + "{1,40}") == false) {
            str += "Tên: Tối đa 40 kí tự\n";
            check = false;
        }
        //4

        if (teacher.getHocVi().isEmpty()) {
            str += "Không được bỏ trống Học vị\n";
            check = false;
        } else if (teacher.getHocVi().matches(".{1,40}") == false) {
            str += "Học vị: Tối đa 40 kí tự\n";
            check = false;
        }
        
        if (check == false) {
            JOptionPane.showMessageDialog(this, str, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        
        return check;
    }
    
    private void setTeacherComponentsEnable(boolean enable) {
        ctTeacherIDTextField.setEnabled(enable);
        ctTeacherLastNameTextField.setEnabled(enable);
        ctTeacherFirstNameTextField.setEnabled(enable);
        ctTeacherDegreeTextField.setEnabled(enable);
        ctTeacherDepartmentComboBox.setEnabled(enable);
    }

    //================================Student
    private void setStudentInput(Student s) {
        if (s == null) {
            ctStudentIDTextField.setText("");
            ctStudentLastNameTextField.setText("");
            ctStudentFirstNameTextField.setText("");
            ctStudentAddressTextField.setText("");
            ctStudentPassTextField.setText("");
            ctStudentBirthDayDateChooser.setDate(null);
            ctStudentClassComboBox.setSelectedIndex(0);
        } else {
            ctStudentIDTextField.setText(s.getMasv());
            ctStudentLastNameTextField.setText(s.getHo());
            ctStudentFirstNameTextField.setText(s.getTen());
            ctStudentAddressTextField.setText(s.getDiaChi());
            ctStudentPassTextField.setText(s.getMatKhau());
            ctStudentBirthDayDateChooser.setDate(DateHelper.toDate(s.getNgaySinh()));
            ctStudentClassComboBox.setSelectedItem(s.getMaLop());
        }
    }
    
    private Student getStudentInput() {
        Student student = new Student();
        
        student.setMasv(ctStudentIDTextField.getText().trim());
        student.setHo(ctStudentLastNameTextField.getText().trim());
        student.setTen(ctStudentFirstNameTextField.getText().trim());
        student.setDiaChi(ctStudentAddressTextField.getText().trim());
        student.setMatKhau(ctStudentPassTextField.getText().trim());
        student.setMaLop(ctStudentClassComboBox.getSelectedItem().toString());
        student.setNgaySinh(DateHelper.toString(ctStudentBirthDayDateChooser.getDate()));
        return student;
    }
    
    private void loadStudentClassCbx() {
        _listClassroom = ClassroomDao.getAllClassrooms();
        ctStudentClassComboBox.removeAllItems();
        for (Classroom classroom : _listClassroom) {
            ctStudentClassComboBox.addItem(classroom.getMaLop());
        }
    }
    
    private boolean checkStudent(Student student, boolean isEdit) {
        if (student == null) {
            return false;
        }
        //
        boolean check = true;
        String reTiengViet = "[aAàÀảẢãÃáÁạẠăĂằẰẳẲẵẴắẮặẶâÂầẦẩẨẫẪấẤậẬbBcCdDđĐeEèÈẻẺẽẼéÉẹẸêÊềỀểỂễỄếẾệỆfFgGhHiIìÌỉỈĩĨíÍịỊjJkKlLmMnNoOòÒỏỎõÕóÓọỌôÔồỒổỔỗỖốỐộỘơƠờỜởỞỡỠớỚợỢpPqQrRsStTuUùÙủỦũŨúÚụỤưƯừỪửỬữỮứỨựỰvVwWxXyYỳỲỷỶỹỸýÝỵỴzZ\\s]";
        String str = "";
        //1
        if (student.getMasv().isEmpty()) {
            str += "Không được bỏ trống Mã sinh viên\n";
            check = false;
        } else if (student.getMasv().matches("^[a-zA-Z]{1}[\\d]{2}[a-zA-Z]{2}[\\d]{3}$") == false) {
            str += "Sai định dạng Mã sinh viên (Mã sinh viên có dạng: N18CN001)\n";
            check = false;
        } else if (!isEdit) {
            if (StudentDao.checkStudent(student.getMasv()) != 0) {
                str += "Mã sinh viên đã tồn tại\n";
                check = false;
            } else {
                for (int i = _addStudentCount; i > 0; i--) {
                    if (student.getMasv().equals(ctStudentTable.getValueAt(ctStudentTable.getRowCount() - 1 - i, 0).toString())) {
                        str += "Mã sinh viên đã tồn tại\n";
                        check = false;
                        break;
                    }
                }
            }
        }
        //2
        if (student.getHo().isEmpty()) {
            str += "Không được bỏ trống Họ\n";
            check = false;
        } else if (student.getHo().matches(reTiengViet + "+") == false) {
            str += "Họ: Chỉ sử dụng bảng chữ cái Tiếng Việt\n";
            check = false;
        } else if (student.getHo().matches(reTiengViet + "{1,40}") == false) {
            str += "Họ: Tối đa 40 kí tự\n";
            check = false;
        }
        //3
        if (student.getTen().isEmpty()) {
            str += "Không được bỏ trống Tên\n";
            check = false;
        } else if (student.getTen().matches(reTiengViet + "+") == false) {
            str += "Tên: Chỉ sử dụng bảng chữ cái Tiếng Việt\n";
            check = false;
        } else if (student.getTen().matches(reTiengViet + "{1,10}") == false) {
            str += "Tên: Tối đa 10 kí tự\n";
            check = false;
        }
        //4
        if (student.getDiaChi().isEmpty()) {
            str += "Không được bỏ trống Địa chỉ\n";
            check = false;
        } else if (student.getDiaChi().matches(".{1,40}") == false) {
            str += "Địa chỉ: Tối đa 40 kí tự\n";
            check = false;
        }
        //5
        if (student.getMatKhau().isEmpty()) {
            str += "Không được bỏ trống Mật khẩu\n";
            check = false;
        } else if (student.getMatKhau().matches("[a-zA-Z0-9]{1,20}") == false) {
            str += "Mật khẩu: Tối đa 20 kí tự và không có kí tự đặc biệt\n";
            check = false;
        }
        //6
        if (ctStudentBirthDayDateChooser.getDate() == null) {
            str += "Không được bỏ trống Ngày sinh\n";
            check = false;
        }
        
        if (check == false) {
            JOptionPane.showMessageDialog(this, str, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        
        return check;
    }
    
    private void setStudentComponentsEnable(boolean enable) {
        ctStudentIDTextField.setEnabled(enable);
        ctStudentLastNameTextField.setEnabled(enable);
        ctStudentFirstNameTextField.setEnabled(enable);
        ctStudentAddressTextField.setEnabled(enable);
        ctStudentPassTextField.setEnabled(enable);
        ctStudentBirthDayDateChooser.setEnabled(enable);
        ctStudentClassComboBox.setEnabled(enable);
    }

    //=======================================Subject
    private void setSubjectInput(Subject s) {
        if (s == null) {
            ctSubjectIDTextField.setText("");
            ctSubjectNameTextField.setText("");
        } else {
            ctSubjectIDTextField.setText(s.getMamh());
            ctSubjectNameTextField.setText(s.getTenmh());
        }
    }
    
    private Subject getSubjectInput() {
        Subject subject = new Subject();
        
        subject.setMamh(ctSubjectIDTextField.getText().trim());
        subject.setTenmh(ctSubjectNameTextField.getText().trim());
        return subject;
    }
    
    private boolean checkSubject(Subject subject, boolean isEdit) {
        if (subject == null) {
            return false;
        }
        
        boolean check = true;
        String str = "";

        //1
        if (subject.getMamh().isEmpty()) {
            str += "Không được bỏ trống Mã môn học\n";
            check = false;
        } else if (subject.getMamh().matches("\\w{1,5}") == false) {
            str += "Mã môn học: Tối đa 5 chữ cái không dấu hoặc số\n";
            check = false;
        } else if (!isEdit) {
            if (SubjectDao.getSubjectById(subject.getMamh()) != null) {
                str += "Mã môn học này đã tồn tại\n";
                check = false;
            } else {
                for (int i = _addSubjectCount; i > 0; i--) {
                    if (subject.getMamh().equals(ctSubjectTable.getValueAt(ctSubjectTable.getRowCount() - 1 - i, 0).toString())) {
                        str += "Mã môn học này đã tồn tại\n";
                        check = false;
                        break;
                    }
                }
            }
        }

        //2
        if (subject.getTenmh().isEmpty()) {
            str += "Không được bỏ trống Tên môn học\n";
            check = false;
        } else if (subject.getTenmh().matches(".{1,40}") == false) {
            str += "Tối đa 40 kí tự\n";
            check = false;
            
        }
        
        if (check == false) {
            JOptionPane.showMessageDialog(this, str, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        
        return check;
    }
    
    private void setSubjectComponentsEnable(boolean enable) {
        ctSubjectIDTextField.setEnabled(enable);
        ctSubjectNameTextField.setEnabled(enable);
    }

    //================================Question
    private void setQuestionInput(Question q) {
        if (q == null) {
            mjQuestionIDTextField.setText("");
            mjQuestionContentTextArea.setText("");
            mjQuestionATextField.setText("");
            mjQuestionBTextField.setText("");
            mjQuestionCTextField.setText("");
            mjQuestionDTextField.setText("");
            mjQuestionSubjectIDComboBox.setSelectedIndex(0);
            mjQuestionAnswerComboBox.setSelectedIndex(0);
            mjQuestionLevelComboBox.setSelectedIndex(0);
        } else {
            mjQuestionIDTextField.setText(String.valueOf(q.getCauHoi()));
            mjQuestionContentTextArea.setText(q.getNoiDung());
            mjQuestionATextField.setText(q.getA());
            mjQuestionBTextField.setText(q.getB());
            mjQuestionCTextField.setText(q.getC());
            mjQuestionDTextField.setText(q.getD());
            mjQuestionSubjectIDComboBox.setSelectedItem(q.getMamh());
            mjQuestionAnswerComboBox.setSelectedItem(q.getDapAn());
            mjQuestionLevelComboBox.setSelectedItem(q.getTrinhDo());
        }
    }
    
    private Question getQuestionInput() {
        Question question = new Question();
        
        question.setCauHoi(Integer.valueOf(mjQuestionIDTextField.getText()));
        question.setMamh(mjQuestionSubjectIDComboBox.getSelectedItem().toString());
        question.setTrinhDo(mjQuestionLevelComboBox.getSelectedItem().toString());
        question.setNoiDung(mjQuestionContentTextArea.getText());
        question.setA(mjQuestionATextField.getText());
        question.setB(mjQuestionBTextField.getText());
        question.setC(mjQuestionCTextField.getText());
        question.setD(mjQuestionDTextField.getText());
        question.setDapAn(mjQuestionAnswerComboBox.getSelectedItem().toString());
        question.setMagv(_userID);
        
        return question;
    }
    
    private boolean checkQuestion(Question question, boolean isEdit) {
        if (question == null) {
            return false;
        }
        
        boolean check = true;
        String str = "";
        
        if (!isEdit) {
            if (QuestionDao.getQuestionById(question.getCauHoi()) != null) {
                str += "Mã câu hỏi này đã tồn tại, bấm [làm mới] để cập nhật lại mã câu hỏi\n";
                check = false;
            }
        }
        //1
        if (question.getNoiDung().isEmpty()) {
            str += "Không được để trống Nội dung\n";
            check = false;
        }
        //2
        if (question.getA().isEmpty()) {
            str += "Không được để trống Lựa chọn A\n";
            check = false;
        }
        //3
        if (question.getB().isEmpty()) {
            str += "Không được để trống Lựa chọn B\n";
            check = false;
        }
        //4
        if (question.getC().isEmpty()) {
            str += "Không được để trống Lựa chọn C\n";
            check = false;
        }
        //5
        if (question.getD().isEmpty()) {
            str += "Không được để trống Lựa chọn D\n";
            check = false;
        }
        
        if (check == false) {
            JOptionPane.showMessageDialog(this, str, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        
        return check;
    }
    
    private void setQuestionComponentsEnable(boolean enable) {
        mjQuestionIDTextField.setEnabled(enable);
        mjQuestionContentTextArea.setEnabled(enable);
        mjQuestionATextField.setEnabled(enable);
        mjQuestionBTextField.setEnabled(enable);
        mjQuestionCTextField.setEnabled(enable);
        mjQuestionDTextField.setEnabled(enable);
        mjQuestionSubjectIDComboBox.setEnabled(enable);
        mjQuestionAnswerComboBox.setEnabled(enable);
        mjQuestionLevelComboBox.setEnabled(enable);
    }

    //================================Register
    private void setRegisterInput(Register r) {
        if (r == null) {
            mjRegisterClassIDComboBox.setSelectedIndex(0);
            mjRegisterSubjectIDComboBox.setSelectedIndex(0);
            mjRegisterExamDateChooser.setDate(null);
            mjRegisterExamTimeComboBox.setSelectedIndex(0);
            mjRegisterExamLevelComboBox.setSelectedIndex(0);
            mjRegisterExamTotalTimeTextField.setText("");
            mjRegisterQuestionCountTextField.setText("");
            
            mjRegisterClassIDComboBox.setEnabled(true);
            mjRegisterSubjectIDComboBox.setEnabled(true);
            mjRegisterExamTimeComboBox.setEnabled(true);
        } else {
            mjRegisterClassIDComboBox.setSelectedItem(r.getMalop());
            mjRegisterSubjectIDComboBox.setSelectedItem(r.getMamh());
            mjRegisterExamDateChooser.setDate(DateHelper.toDate2(r.getNgayThi()));
            mjRegisterExamTimeComboBox.setSelectedItem(r.getLan());
            mjRegisterExamLevelComboBox.setSelectedItem(r.getTrinhDo());
            mjRegisterExamTotalTimeTextField.setText(String.valueOf(r.getThoiGian()));
            mjRegisterQuestionCountTextField.setText(String.valueOf(r.getSoCauThi()));
            
            mjRegisterClassIDComboBox.setEnabled(false);
            mjRegisterSubjectIDComboBox.setEnabled(false);
            mjRegisterExamTimeComboBox.setEnabled(false);
        }
    }
    
    private Register getRegisterInput() {
        Register register = new Register();
        register.setMagv(_userID);
        register.setMalop(mjRegisterClassIDComboBox.getSelectedItem().toString());
        register.setMamh(mjRegisterSubjectIDComboBox.getSelectedItem().toString());
        register.setNgayThi(DateHelper.toString(mjRegisterExamDateChooser.getDate()));
        register.setLan(Integer.valueOf(mjRegisterExamTimeComboBox.getSelectedItem().toString()));
        register.setTrinhDo(mjRegisterExamLevelComboBox.getSelectedItem().toString());
        register.setSoCauThi(Integer.valueOf(mjRegisterQuestionCountTextField.getText().isEmpty() ? "0" : mjRegisterQuestionCountTextField.getText()));
        register.setThoiGian(Integer.valueOf(mjRegisterExamTotalTimeTextField.getText().isEmpty() ? "0" : mjRegisterExamTotalTimeTextField.getText()));
        
        return register;
    }
    
    private boolean checkRegister(Register register, boolean isEdit) {
        if (register == null) {
            return false;
        }
        
        boolean check = true;
        String str = "";

        //1
        if (!isEdit) {
            if (RegisterDao.getRegisterById(register.getMamh(), register.getMalop(), register.getLan()) != null) {
                str += "Đã có giảng viên đăng ký môn thi cho lớp với số lần hiện tại !\n";
                check = false;
            } else {
                for (int i = _addRegisterCount; i > 0; i--) {
                    if (register.getMamh().equals(mjRegisterTable.getValueAt(mjRegisterTable.getRowCount() - 1 - i, 1).toString())
                            && register.getMalop().equals(mjRegisterTable.getValueAt(mjRegisterTable.getRowCount() - 1 - i, 2).toString())
                            && String.valueOf(register.getLan()).equals(mjRegisterTable.getValueAt(mjRegisterTable.getRowCount() - 1 - i, 5).toString())) {
                        str += "Đã có giảng viên đăng ký môn thi cho lớp với số lần hiện tại !\n";
                        check = false;
                        break;
                    }
                }
            }
        }
        //2
        if (mjRegisterQuestionCountTextField.getText().isEmpty()) {
            str += "Không được bỏ trống Số câu thi\n";
            check = false;
        } else if (register.getSoCauThi() > 100 || register.getSoCauThi() < 10) {
            str += "Số câu thi phải từ 10 đến 100 câu\n";
            check = false;
        }
        //3
        if (mjRegisterExamDateChooser.getDate() == null) {
            str += "Không được để trống Ngày thi\n";
            check = false;
        } else if (DateHelper.toDate(register.getNgayThi()).compareTo(DateHelper.now()) < 0 && !isEdit) {
            str += "Chỉ được đăng ký ngày thi trong tương lai\n";
            check = false;
        }
        //4
        if (mjRegisterExamTotalTimeTextField.getText().isEmpty()) {
            str += "Không được bỏ trống Thời gian thi\n";
            check = false;
        } else if (register.getThoiGian() < 15 || register.getThoiGian() > 60) {
            str += "Thời gian thi phải từ 15 đến 60 phút\n";
            check = false;
        }
        //5
        if (QuestionDao.getExam(register.getSoCauThi(), register.getMamh(), register.getTrinhDo()) == null) {
            str += "Không đủ số câu trong bộ đề để đăng ký\n";
            check = false;
        }
        
        if (check == false) {
            JOptionPane.showMessageDialog(this, str, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        
        return check;
    }
    
    private void setRegisterComponentsEnable(boolean enable) {
        mjRegisterClassIDComboBox.setEnabled(enable);
        mjRegisterSubjectIDComboBox.setEnabled(enable);
        mjRegisterExamDateChooser.setEnabled(enable);
        mjRegisterExamTimeComboBox.setEnabled(enable);
        mjRegisterExamLevelComboBox.setEnabled(enable);
        mjRegisterExamTotalTimeTextField.setEnabled(enable);
        mjRegisterQuestionCountTextField.setEnabled(enable);
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
        ctSearchTextField1 = new javax.swing.JTextField();
        ctSearchComboBox1 = new javax.swing.JComboBox<>();
        jLabel60 = new javax.swing.JLabel();
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
        ctSearchComboBox2 = new javax.swing.JComboBox<>();
        ctSearchTextField2 = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
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
        ctSearchComboBox3 = new javax.swing.JComboBox<>();
        ctSearchTextField3 = new javax.swing.JTextField();
        jLabel62 = new javax.swing.JLabel();
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
        ctStudentBirthDayDateChooser = new com.toedter.calendar.JDateChooser();
        jLabel39 = new javax.swing.JLabel();
        ctStudentAddressTextField = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        ctStudentClassComboBox = new javax.swing.JComboBox();
        ctSearchComboBox4 = new javax.swing.JComboBox<>();
        ctSearchTextField4 = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        ctStudentPassTextField = new javax.swing.JTextField();
        ctStudentPassLabel = new javax.swing.JLabel();
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
        ctSearchComboBox5 = new javax.swing.JComboBox<>();
        ctSearchTextField5 = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        tabMajor = new javax.swing.JPanel();
        majorOptionPanel = new javax.swing.JPanel();
        jSeparator8 = new javax.swing.JSeparator();
        mjQuestionManagementButton = new javax.swing.JButton();
        jSeparator9 = new javax.swing.JSeparator();
        mjRegisterButton = new javax.swing.JButton();
        mjTestExamButton = new javax.swing.JButton();
        majorQuestionPanel = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        mjQuestionIDTextField = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        mjQuestionSubjectIDComboBox = new javax.swing.JComboBox();
        jLabel52 = new javax.swing.JLabel();
        mjQuestionLevelComboBox = new javax.swing.JComboBox();
        jScrollPane10 = new javax.swing.JScrollPane();
        mjQuestionContentTextArea = new javax.swing.JTextArea();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        mjQuestionATextField = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        mjQuestionBTextField = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        mjQuestionCTextField = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        mjQuestionDTextField = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        mjQuestionAnswerComboBox = new javax.swing.JComboBox();
        jScrollPane11 = new javax.swing.JScrollPane();
        mjQuestionTable = new javax.swing.JTable();
        mjAddButton1 = new javax.swing.JButton();
        mjEditButton1 = new javax.swing.JButton();
        mjSaveButton1 = new javax.swing.JButton();
        mjRemoveButton1 = new javax.swing.JButton();
        mjUndoButton1 = new javax.swing.JButton();
        mjReloadButton1 = new javax.swing.JButton();
        jLabel59 = new javax.swing.JLabel();
        mjSearchTextField1 = new javax.swing.JTextField();
        mjSearchComboBox1 = new javax.swing.JComboBox<>();
        majorRegisterPanel = new javax.swing.JPanel();
        mjAddButton2 = new javax.swing.JButton();
        mjEditButton2 = new javax.swing.JButton();
        mjSaveButton2 = new javax.swing.JButton();
        mjRemoveButton2 = new javax.swing.JButton();
        mjUndoButton2 = new javax.swing.JButton();
        mjReloadButton2 = new javax.swing.JButton();
        jLabel65 = new javax.swing.JLabel();
        mjSearchTextField2 = new javax.swing.JTextField();
        mjSearchComboBox2 = new javax.swing.JComboBox<>();
        jScrollPane12 = new javax.swing.JScrollPane();
        mjRegisterTable = new javax.swing.JTable();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        mjRegisterSubjectIDComboBox = new javax.swing.JComboBox<>();
        mjRegisterClassIDComboBox = new javax.swing.JComboBox<>();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        mjRegisterExamTimeComboBox = new javax.swing.JComboBox<>();
        mjRegisterExamLevelComboBox = new javax.swing.JComboBox<>();
        mjRegisterExamTotalTimeTextField = new javax.swing.JTextField();
        mjRegisterQuestionCountTextField = new javax.swing.JTextField();
        mjRegisterExamDateChooser = new com.toedter.calendar.JDateChooser();
        jLabel73 = new javax.swing.JLabel();
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
        rpResultPane3 = new javax.swing.JPanel();
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

        sysPasswordTextField1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

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
                .addGap(448, 448, 448)
                .addComponent(jLabel4))
            .addGroup(systemFormPanel1Layout.createSequentialGroup()
                .addGap(350, 350, 350)
                .addComponent(jLabel1)
                .addGap(64, 64, 64)
                .addComponent(sysBranchComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(systemFormPanel1Layout.createSequentialGroup()
                .addGap(520, 520, 520)
                .addComponent(sysLoginButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(systemFormPanel1Layout.createSequentialGroup()
                .addGap(350, 350, 350)
                .addGroup(systemFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(systemFormPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(44, 44, 44)
                        .addComponent(sysPasswordTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(systemFormPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(41, 41, 41)
                        .addComponent(sysUsernameTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        systemFormPanel1Layout.setVerticalGroup(
            systemFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(systemFormPanel1Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(jLabel4)
                .addGap(28, 28, 28)
                .addGroup(systemFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sysBranchComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(26, 26, 26)
                .addGroup(systemFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(systemFormPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel2))
                    .addComponent(sysUsernameTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(systemFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sysPasswordTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(52, 52, 52)
                .addComponent(sysLoginButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addComponent(sysCreateAccountButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
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
                .addContainerGap(136, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
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

        ctDepartmentTable.setAutoCreateRowSorter(true);
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
        ctDepartmentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ctDepartmentTableMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(ctDepartmentTable);
        if (ctDepartmentTable.getColumnModel().getColumnCount() > 0) {
            ctDepartmentTable.getColumnModel().getColumn(0).setResizable(false);
            ctDepartmentTable.getColumnModel().getColumn(1).setResizable(false);
            ctDepartmentTable.getColumnModel().getColumn(2).setResizable(false);
        }

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel20.setText("Mã khoa:");

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel21.setText("Tên khoa:");

        ctDepartmentIDTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctDepartmentIDTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ctDepartmentIDTextFieldKeyTyped(evt);
            }
        });

        ctDepartmentNameTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel22.setText("Mã cơ sở:");

        ctDepartmentBranchComboBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        ctAddButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctAddButton1.setText("Thêm");
        ctAddButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctAddButton1ActionPerformed(evt);
            }
        });

        ctEditButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctEditButton1.setText("Sửa");
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
        ctBranchComboBox1.setEnabled(false);
        ctBranchComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ctBranchComboBox1ItemStateChanged(evt);
            }
        });

        ctSearchTextField1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        ctSearchComboBox1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel60.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel60.setText("Tìm kiếm:");

        javax.swing.GroupLayout categoriesFormPanel1Layout = new javax.swing.GroupLayout(categoriesFormPanel1);
        categoriesFormPanel1.setLayout(categoriesFormPanel1Layout);
        categoriesFormPanel1Layout.setHorizontalGroup(
            categoriesFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5)
            .addGroup(categoriesFormPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(ctAddButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(ctEditButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ctSaveButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ctRemoveButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ctUndoButton1)
                .addGap(18, 18, 18)
                .addComponent(ctReloadButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(386, 386, 386))
            .addGroup(categoriesFormPanel1Layout.createSequentialGroup()
                .addGroup(categoriesFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                            .addComponent(ctDepartmentBranchComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(categoriesFormPanel1Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                        .addGap(34, 34, 34)
                        .addComponent(ctBranchComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(77, 77, 77)))
                .addGap(131, 131, 131)
                .addComponent(jLabel60)
                .addGap(18, 18, 18)
                .addComponent(ctSearchTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ctSearchComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(132, 132, 132))
        );
        categoriesFormPanel1Layout.setVerticalGroup(
            categoriesFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoriesFormPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(categoriesFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ctAddButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctEditButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctUndoButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctReloadButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctSaveButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctRemoveButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(categoriesFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(categoriesFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel60)
                        .addComponent(ctSearchTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(ctSearchComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(categoriesFormPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ctBranchComboBox1)
                        .addComponent(jLabel23)))
                .addGap(29, 29, 29)
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
        ctEditButton2.setText("Sửa");
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
        ctBranchComboBox2.setEnabled(false);
        ctBranchComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ctBranchComboBox2ItemStateChanged(evt);
            }
        });

        ctClassTable.setAutoCreateRowSorter(true);
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
        ctClassTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ctClassTableMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(ctClassTable);
        if (ctClassTable.getColumnModel().getColumnCount() > 0) {
            ctClassTable.getColumnModel().getColumn(0).setResizable(false);
            ctClassTable.getColumnModel().getColumn(1).setResizable(false);
            ctClassTable.getColumnModel().getColumn(2).setResizable(false);
        }

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel25.setText("Mã lớp:");

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel26.setText("Tên lớp:");

        ctClassIDTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctClassIDTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ctClassIDTextFieldKeyTyped(evt);
            }
        });

        ctClassNameTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel27.setText("Mã khoa:");

        ctClassDepartmentComboBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        ctSearchComboBox2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        ctSearchTextField2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel61.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel61.setText("Tìm kiếm:");

        javax.swing.GroupLayout categoriesFormPanel2Layout = new javax.swing.GroupLayout(categoriesFormPanel2);
        categoriesFormPanel2.setLayout(categoriesFormPanel2Layout);
        categoriesFormPanel2Layout.setHorizontalGroup(
            categoriesFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6)
            .addGroup(categoriesFormPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(ctAddButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(ctEditButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ctSaveButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ctRemoveButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ctUndoButton2)
                .addGap(18, 18, 18)
                .addComponent(ctReloadButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(383, Short.MAX_VALUE))
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
            .addGroup(categoriesFormPanel2Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jLabel24)
                .addGap(34, 34, 34)
                .addComponent(ctBranchComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel61)
                .addGap(18, 18, 18)
                .addComponent(ctSearchTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ctSearchComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(125, 125, 125))
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
                .addGap(28, 28, 28)
                .addGroup(categoriesFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(categoriesFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel61)
                        .addComponent(ctSearchTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(ctSearchComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(categoriesFormPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ctBranchComboBox2)
                        .addComponent(jLabel24)))
                .addGap(31, 31, 31)
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
        ctEditButton3.setText("Sửa");
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
        ctBranchComboBox3.setEnabled(false);
        ctBranchComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ctBranchComboBox3ItemStateChanged(evt);
            }
        });

        ctTeacherTable.setAutoCreateRowSorter(true);
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
        ctTeacherTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ctTeacherTableMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(ctTeacherTable);
        if (ctTeacherTable.getColumnModel().getColumnCount() > 0) {
            ctTeacherTable.getColumnModel().getColumn(0).setResizable(false);
            ctTeacherTable.getColumnModel().getColumn(1).setResizable(false);
            ctTeacherTable.getColumnModel().getColumn(2).setResizable(false);
            ctTeacherTable.getColumnModel().getColumn(3).setResizable(false);
            ctTeacherTable.getColumnModel().getColumn(4).setResizable(false);
        }

        ctTeacherLastNameTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        ctTeacherIDTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctTeacherIDTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ctTeacherIDTextFieldKeyTyped(evt);
            }
        });

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

        ctSearchComboBox3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        ctSearchTextField3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel62.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel62.setText("Tìm kiếm:");

        javax.swing.GroupLayout categoriesFormPanel3Layout = new javax.swing.GroupLayout(categoriesFormPanel3);
        categoriesFormPanel3.setLayout(categoriesFormPanel3Layout);
        categoriesFormPanel3Layout.setHorizontalGroup(
            categoriesFormPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7)
            .addGroup(categoriesFormPanel3Layout.createSequentialGroup()
                .addGroup(categoriesFormPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                                .addComponent(ctTeacherDepartmentComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(categoriesFormPanel3Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(ctAddButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ctEditButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ctSaveButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ctRemoveButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ctUndoButton3)
                        .addGap(18, 18, 18)
                        .addComponent(ctReloadButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(253, Short.MAX_VALUE))
            .addGroup(categoriesFormPanel3Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(jLabel28)
                .addGap(34, 34, 34)
                .addComponent(ctBranchComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 207, Short.MAX_VALUE)
                .addComponent(jLabel62)
                .addGap(18, 18, 18)
                .addComponent(ctSearchTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ctSearchComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(124, 124, 124))
        );
        categoriesFormPanel3Layout.setVerticalGroup(
            categoriesFormPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoriesFormPanel3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(categoriesFormPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ctAddButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctEditButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctSaveButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctRemoveButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctReloadButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctUndoButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(categoriesFormPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(categoriesFormPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel62)
                        .addComponent(ctSearchTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(ctSearchComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(categoriesFormPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ctBranchComboBox3)
                        .addComponent(jLabel28)))
                .addGap(31, 31, 31)
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
        ctEditButton4.setText("Sửa");
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
        ctBranchComboBox4.setEnabled(false);
        ctBranchComboBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ctBranchComboBox4ItemStateChanged(evt);
            }
        });

        ctStudentTable.setAutoCreateRowSorter(true);
        ctStudentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã sinh viên", "Họ", "Tên", "Ngày sinh", "Địa chỉ", "Mã lớp", "Mật khẩu"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ctStudentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ctStudentTableMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(ctStudentTable);
        if (ctStudentTable.getColumnModel().getColumnCount() > 0) {
            ctStudentTable.getColumnModel().getColumn(0).setResizable(false);
            ctStudentTable.getColumnModel().getColumn(1).setResizable(false);
            ctStudentTable.getColumnModel().getColumn(2).setResizable(false);
            ctStudentTable.getColumnModel().getColumn(3).setResizable(false);
            ctStudentTable.getColumnModel().getColumn(4).setResizable(false);
            ctStudentTable.getColumnModel().getColumn(5).setResizable(false);
            ctStudentTable.getColumnModel().getColumn(6).setResizable(false);
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
        ctStudentIDTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ctStudentIDTextFieldKeyTyped(evt);
            }
        });

        ctStudentLastNameTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        ctStudentFirstNameTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        ctStudentBirthDayDateChooser.setDateFormatString("dd/MM/yyyy");
        ctStudentBirthDayDateChooser.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel39.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel39.setText("Địa chỉ:");

        ctStudentAddressTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel40.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel40.setText("Mã lớp:");

        ctStudentClassComboBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        ctSearchComboBox4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        ctSearchTextField4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel63.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel63.setText("Tìm kiếm:");

        ctStudentPassTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        ctStudentPassLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ctStudentPassLabel.setText("Mật khẩu:");

        javax.swing.GroupLayout categoriesFormPanel4Layout = new javax.swing.GroupLayout(categoriesFormPanel4);
        categoriesFormPanel4.setLayout(categoriesFormPanel4Layout);
        categoriesFormPanel4Layout.setHorizontalGroup(
            categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoriesFormPanel4Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(jLabel34)
                .addGap(34, 34, 34)
                .addComponent(ctBranchComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel63)
                .addGap(18, 18, 18)
                .addComponent(ctSearchTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ctSearchComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(134, 134, 134))
            .addGroup(categoriesFormPanel4Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(categoriesFormPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addGap(18, 18, 18)
                        .addComponent(ctStudentIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(categoriesFormPanel4Layout.createSequentialGroup()
                        .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel37)
                            .addComponent(jLabel36))
                        .addGap(62, 62, 62)
                        .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ctStudentFirstNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(categoriesFormPanel4Layout.createSequentialGroup()
                                .addComponent(ctStudentLastNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(83, 83, 83)
                                .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel38)
                                    .addComponent(jLabel39)
                                    .addComponent(jLabel40))
                                .addGap(18, 18, 18)
                                .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(ctStudentBirthDayDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(ctStudentClassComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(ctStudentAddressTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ctStudentPassLabel)
                .addGap(18, 18, 18)
                .addComponent(ctStudentPassTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
            .addGroup(categoriesFormPanel4Layout.createSequentialGroup()
                .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(categoriesFormPanel4Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(ctAddButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ctEditButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ctSaveButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ctRemoveButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ctUndoButton4)
                        .addGap(18, 18, 18)
                        .addComponent(ctReloadButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 1002, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        categoriesFormPanel4Layout.setVerticalGroup(
            categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoriesFormPanel4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ctAddButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctEditButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctSaveButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctRemoveButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctUndoButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctReloadButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(categoriesFormPanel4Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel34))
                    .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel63)
                        .addComponent(ctSearchTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(ctSearchComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(ctBranchComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, categoriesFormPanel4Layout.createSequentialGroup()
                        .addGap(219, 219, 219)
                        .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ctStudentAddressTextField)
                            .addComponent(jLabel39)
                            .addComponent(ctStudentPassTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ctStudentPassLabel)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, categoriesFormPanel4Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel35)
                            .addComponent(ctStudentIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(35, 35, 35)
                .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel36)
                        .addComponent(ctStudentLastNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel38))
                    .addComponent(ctStudentBirthDayDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(37, 37, 37)
                .addGroup(categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ctStudentClassComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                        .addComponent(jLabel40))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, categoriesFormPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel37)
                        .addComponent(ctStudentFirstNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(69, 69, 69))
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
        ctEditButton5.setText("Sửa");
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
        ctBranchComboBox5.setEnabled(false);
        ctBranchComboBox5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ctBranchComboBox5ItemStateChanged(evt);
            }
        });

        ctSubjectTable.setAutoCreateRowSorter(true);
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
        ctSubjectTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ctSubjectTableMouseClicked(evt);
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
        ctSubjectIDTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ctSubjectIDTextFieldKeyTyped(evt);
            }
        });

        jLabel43.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel43.setText("Tên môn học:");

        ctSubjectNameTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        ctSearchComboBox5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        ctSearchTextField5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel64.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel64.setText("Tìm kiếm:");

        javax.swing.GroupLayout categoriesFormPanel5Layout = new javax.swing.GroupLayout(categoriesFormPanel5);
        categoriesFormPanel5.setLayout(categoriesFormPanel5Layout);
        categoriesFormPanel5Layout.setHorizontalGroup(
            categoriesFormPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane9)
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
            .addGroup(categoriesFormPanel5Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(ctAddButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(ctEditButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ctSaveButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ctRemoveButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ctUndoButton5)
                .addGap(18, 18, 18)
                .addComponent(ctReloadButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(376, 376, 376))
            .addGroup(categoriesFormPanel5Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jLabel41)
                .addGap(34, 34, 34)
                .addComponent(ctBranchComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel64)
                .addGap(18, 18, 18)
                .addComponent(ctSearchTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ctSearchComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(126, 126, 126))
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
                .addGap(27, 27, 27)
                .addGroup(categoriesFormPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(categoriesFormPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel64)
                        .addComponent(ctSearchTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(ctSearchComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(categoriesFormPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ctBranchComboBox5)
                        .addComponent(jLabel41)))
                .addGap(31, 31, 31)
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
            .addComponent(categoriesOptionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1012, Short.MAX_VALUE)
            .addComponent(categoriesFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(tabCategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(categoriesFormPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1012, Short.MAX_VALUE))
            .addGroup(tabCategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(tabCategoriesLayout.createSequentialGroup()
                    .addComponent(categoriesFormPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 1012, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
            .addGroup(tabCategoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(tabCategoriesLayout.createSequentialGroup()
                    .addComponent(categoriesFormPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 1012, Short.MAX_VALUE)
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
        mjQuestionManagementButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mjQuestionManagementButtonActionPerformed(evt);
            }
        });

        jSeparator9.setOrientation(javax.swing.SwingConstants.VERTICAL);

        mjRegisterButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        mjRegisterButton.setText("Đăng ký thi");
        mjRegisterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mjRegisterButtonActionPerformed(evt);
            }
        });

        mjTestExamButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        mjTestExamButton.setText("Thi thử");

        javax.swing.GroupLayout majorOptionPanelLayout = new javax.swing.GroupLayout(majorOptionPanel);
        majorOptionPanel.setLayout(majorOptionPanelLayout);
        majorOptionPanelLayout.setHorizontalGroup(
            majorOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(majorOptionPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(mjQuestionManagementButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(mjRegisterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(mjTestExamButton, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        majorOptionPanelLayout.setVerticalGroup(
            majorOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
            .addComponent(jSeparator9, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(majorOptionPanelLayout.createSequentialGroup()
                .addGroup(majorOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(majorOptionPanelLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(mjQuestionManagementButton, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(majorOptionPanelLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(mjRegisterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(majorOptionPanelLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(mjTestExamButton, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        majorQuestionPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jLabel50.setText("Câu hỏi số:");

        mjQuestionIDTextField.setEditable(false);

        jLabel51.setText("Mã môn học:");

        jLabel52.setText("Trình độ:");

        mjQuestionLevelComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "A", "B", "C" }));

        mjQuestionContentTextArea.setColumns(20);
        mjQuestionContentTextArea.setLineWrap(true);
        mjQuestionContentTextArea.setRows(5);
        mjQuestionContentTextArea.setWrapStyleWord(true);
        jScrollPane10.setViewportView(mjQuestionContentTextArea);

        jLabel53.setText("Nội dung:");

        jLabel54.setText("Lựa chọn A:");

        jLabel55.setText("Lựa chọn B:");

        jLabel56.setText("Lựa chọn C:");

        jLabel57.setText("Lựa chọn D:");

        jLabel58.setText("Đáp án đúng:");

        mjQuestionAnswerComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "A", "B", "C", "D" }));

        mjQuestionTable.setAutoCreateRowSorter(true);
        mjQuestionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Câu hỏi", "Mã môn học", "Trình độ", "Nội dung", "A", "B", "C", "D", "Đáp án", "Mã giáo viên"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        mjQuestionTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mjQuestionTableMouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(mjQuestionTable);
        if (mjQuestionTable.getColumnModel().getColumnCount() > 0) {
            mjQuestionTable.getColumnModel().getColumn(0).setResizable(false);
            mjQuestionTable.getColumnModel().getColumn(0).setHeaderValue("Câu hỏi");
            mjQuestionTable.getColumnModel().getColumn(1).setResizable(false);
            mjQuestionTable.getColumnModel().getColumn(1).setHeaderValue("Mã môn học");
            mjQuestionTable.getColumnModel().getColumn(2).setResizable(false);
            mjQuestionTable.getColumnModel().getColumn(2).setHeaderValue("Trình độ");
            mjQuestionTable.getColumnModel().getColumn(3).setResizable(false);
            mjQuestionTable.getColumnModel().getColumn(3).setHeaderValue("Nội dung");
            mjQuestionTable.getColumnModel().getColumn(4).setResizable(false);
            mjQuestionTable.getColumnModel().getColumn(4).setHeaderValue("A");
            mjQuestionTable.getColumnModel().getColumn(5).setResizable(false);
            mjQuestionTable.getColumnModel().getColumn(5).setHeaderValue("B");
            mjQuestionTable.getColumnModel().getColumn(6).setResizable(false);
            mjQuestionTable.getColumnModel().getColumn(6).setHeaderValue("C");
            mjQuestionTable.getColumnModel().getColumn(7).setResizable(false);
            mjQuestionTable.getColumnModel().getColumn(7).setHeaderValue("D");
            mjQuestionTable.getColumnModel().getColumn(8).setResizable(false);
            mjQuestionTable.getColumnModel().getColumn(8).setHeaderValue("Đáp án");
            mjQuestionTable.getColumnModel().getColumn(9).setResizable(false);
            mjQuestionTable.getColumnModel().getColumn(9).setHeaderValue("Mã giáo viên");
        }

        mjAddButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        mjAddButton1.setText("Thêm");
        mjAddButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mjAddButton1ActionPerformed(evt);
            }
        });

        mjEditButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        mjEditButton1.setText("Sửa");
        mjEditButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mjEditButton1ActionPerformed(evt);
            }
        });

        mjSaveButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        mjSaveButton1.setText("Ghi");
        mjSaveButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mjSaveButton1ActionPerformed(evt);
            }
        });

        mjRemoveButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        mjRemoveButton1.setText("Xóa");
        mjRemoveButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mjRemoveButton1ActionPerformed(evt);
            }
        });

        mjUndoButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        mjUndoButton1.setText("Phục hồi");
        mjUndoButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mjUndoButton1ActionPerformed(evt);
            }
        });

        mjReloadButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        mjReloadButton1.setText("Làm mới");
        mjReloadButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mjReloadButton1ActionPerformed(evt);
            }
        });

        jLabel59.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel59.setText("Tìm kiếm:");

        mjSearchTextField1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        mjSearchComboBox1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        javax.swing.GroupLayout majorQuestionPanelLayout = new javax.swing.GroupLayout(majorQuestionPanel);
        majorQuestionPanel.setLayout(majorQuestionPanelLayout);
        majorQuestionPanelLayout.setHorizontalGroup(
            majorQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, majorQuestionPanelLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(majorQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(majorQuestionPanelLayout.createSequentialGroup()
                        .addGroup(majorQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel51)
                            .addComponent(jLabel52))
                        .addGap(27, 27, 27)
                        .addGroup(majorQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mjQuestionLevelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mjQuestionSubjectIDComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, majorQuestionPanelLayout.createSequentialGroup()
                        .addComponent(jLabel50)
                        .addGap(34, 34, 34)
                        .addComponent(mjQuestionIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(39, 39, 39)
                .addGroup(majorQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel58)
                    .addComponent(jLabel53))
                .addGap(22, 22, 22)
                .addGroup(majorQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(majorQuestionPanelLayout.createSequentialGroup()
                        .addComponent(mjQuestionAnswerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(majorQuestionPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                        .addGap(53, 53, 53)))
                .addGroup(majorQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel54)
                    .addComponent(jLabel55)
                    .addComponent(jLabel56)
                    .addComponent(jLabel57))
                .addGap(30, 30, 30)
                .addGroup(majorQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(mjQuestionBTextField)
                    .addComponent(mjQuestionATextField)
                    .addComponent(mjQuestionCTextField)
                    .addComponent(mjQuestionDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(71, 71, 71))
            .addComponent(jScrollPane11)
            .addGroup(majorQuestionPanelLayout.createSequentialGroup()
                .addGroup(majorQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(majorQuestionPanelLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(mjAddButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(mjEditButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(mjSaveButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(mjRemoveButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(mjUndoButton1)
                        .addGap(18, 18, 18)
                        .addComponent(mjReloadButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(majorQuestionPanelLayout.createSequentialGroup()
                        .addGap(97, 97, 97)
                        .addComponent(jLabel59)
                        .addGap(18, 18, 18)
                        .addComponent(mjSearchTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(mjSearchComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        majorQuestionPanelLayout.setVerticalGroup(
            majorQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, majorQuestionPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(majorQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mjAddButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mjEditButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mjSaveButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mjRemoveButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mjReloadButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mjUndoButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(majorQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel59)
                    .addComponent(mjSearchTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mjSearchComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                .addGap(31, 31, 31)
                .addGroup(majorQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(majorQuestionPanelLayout.createSequentialGroup()
                        .addGroup(majorQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(mjQuestionIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel50))
                        .addGap(32, 32, 32)
                        .addGroup(majorQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel51)
                            .addComponent(mjQuestionSubjectIDComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(majorQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel52)
                            .addComponent(mjQuestionLevelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(majorQuestionPanelLayout.createSequentialGroup()
                        .addGroup(majorQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(mjQuestionATextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel54))
                        .addGap(18, 18, 18)
                        .addGroup(majorQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel55)
                            .addComponent(mjQuestionBTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(majorQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel56)
                            .addComponent(mjQuestionCTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(majorQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(mjQuestionDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel57)))
                    .addGroup(majorQuestionPanelLayout.createSequentialGroup()
                        .addGroup(majorQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel53)
                            .addComponent(jScrollPane10))
                        .addGap(18, 18, 18)
                        .addGroup(majorQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel58)
                            .addComponent(mjQuestionAnswerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(41, 41, 41))
        );

        majorRegisterPanel.setPreferredSize(new java.awt.Dimension(1005, 514));
        majorRegisterPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        mjAddButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        mjAddButton2.setText("Thêm");
        mjAddButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mjAddButton2ActionPerformed(evt);
            }
        });
        majorRegisterPanel.add(mjAddButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 29, 81, 33));

        mjEditButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        mjEditButton2.setText("Sửa");
        mjEditButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mjEditButton2ActionPerformed(evt);
            }
        });
        majorRegisterPanel.add(mjEditButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 29, 85, 33));

        mjSaveButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        mjSaveButton2.setText("Ghi");
        mjSaveButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mjSaveButton2ActionPerformed(evt);
            }
        });
        majorRegisterPanel.add(mjSaveButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 29, 85, 33));

        mjRemoveButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        mjRemoveButton2.setText("Xóa");
        mjRemoveButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mjRemoveButton2ActionPerformed(evt);
            }
        });
        majorRegisterPanel.add(mjRemoveButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(338, 29, 85, 33));

        mjUndoButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        mjUndoButton2.setText("Phục hồi");
        mjUndoButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mjUndoButton2ActionPerformed(evt);
            }
        });
        majorRegisterPanel.add(mjUndoButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(441, 29, -1, 33));

        mjReloadButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        mjReloadButton2.setText("Làm mới");
        mjReloadButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mjReloadButton2ActionPerformed(evt);
            }
        });
        majorRegisterPanel.add(mjReloadButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(538, 29, 85, 33));

        jLabel65.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel65.setText("Tìm kiếm:");
        majorRegisterPanel.add(jLabel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(96, 83, -1, -1));

        mjSearchTextField2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        majorRegisterPanel.add(mjSearchTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(167, 80, 200, -1));

        mjSearchComboBox2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        majorRegisterPanel.add(mjSearchComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(385, 80, 80, -1));

        mjRegisterTable.setAutoCreateRowSorter(true);
        mjRegisterTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã giảng viên", "Mã môn học", "Mã lớp", "Trình độ", "Ngày thi", "Lần thi", "Số câu thi", "Thời gian (phút)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        mjRegisterTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mjRegisterTableMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(mjRegisterTable);
        if (mjRegisterTable.getColumnModel().getColumnCount() > 0) {
            mjRegisterTable.getColumnModel().getColumn(0).setResizable(false);
            mjRegisterTable.getColumnModel().getColumn(1).setResizable(false);
            mjRegisterTable.getColumnModel().getColumn(2).setResizable(false);
            mjRegisterTable.getColumnModel().getColumn(3).setResizable(false);
            mjRegisterTable.getColumnModel().getColumn(3).setPreferredWidth(30);
            mjRegisterTable.getColumnModel().getColumn(4).setResizable(false);
            mjRegisterTable.getColumnModel().getColumn(5).setResizable(false);
            mjRegisterTable.getColumnModel().getColumn(5).setPreferredWidth(30);
            mjRegisterTable.getColumnModel().getColumn(6).setResizable(false);
            mjRegisterTable.getColumnModel().getColumn(6).setPreferredWidth(30);
            mjRegisterTable.getColumnModel().getColumn(7).setResizable(false);
            mjRegisterTable.getColumnModel().getColumn(7).setPreferredWidth(30);
        }

        majorRegisterPanel.add(jScrollPane12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 127, 1005, 193));

        jLabel66.setText("Mã môn học:");
        majorRegisterPanel.add(jLabel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(53, 355, -1, -1));

        jLabel67.setText("Mã lớp:");
        majorRegisterPanel.add(jLabel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(53, 399, -1, -1));

        majorRegisterPanel.add(mjRegisterSubjectIDComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 352, 201, -1));

        majorRegisterPanel.add(mjRegisterClassIDComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 396, 201, -1));

        jLabel68.setText("Ngày thi:");
        majorRegisterPanel.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 450, -1, -1));

        jLabel69.setText("Trình độ:");
        majorRegisterPanel.add(jLabel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(413, 355, -1, -1));

        jLabel70.setText("Lần thi:");
        majorRegisterPanel.add(jLabel70, new org.netbeans.lib.awtextra.AbsoluteConstraints(413, 399, -1, -1));

        jLabel71.setText("Số câu thi:");
        majorRegisterPanel.add(jLabel71, new org.netbeans.lib.awtextra.AbsoluteConstraints(688, 399, -1, -1));

        mjRegisterExamTimeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2" }));
        majorRegisterPanel.add(mjRegisterExamTimeComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(474, 396, 70, -1));

        mjRegisterExamLevelComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A", "B", "C" }));
        majorRegisterPanel.add(mjRegisterExamLevelComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(474, 352, 70, -1));

        mjRegisterExamTotalTimeTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                mjRegisterExamTotalTimeTextFieldKeyTyped(evt);
            }
        });
        majorRegisterPanel.add(mjRegisterExamTotalTimeTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(768, 352, 80, -1));

        mjRegisterQuestionCountTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                mjRegisterQuestionCountTextFieldKeyTyped(evt);
            }
        });
        majorRegisterPanel.add(mjRegisterQuestionCountTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(768, 396, 80, -1));

        mjRegisterExamDateChooser.setDateFormatString("dd/MM/yyyy");
        mjRegisterExamDateChooser.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        majorRegisterPanel.add(mjRegisterExamDateChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 440, 210, 30));

        jLabel73.setText("Thời gian thi:");
        majorRegisterPanel.add(jLabel73, new org.netbeans.lib.awtextra.AbsoluteConstraints(688, 355, -1, -1));

        javax.swing.GroupLayout tabMajorLayout = new javax.swing.GroupLayout(tabMajor);
        tabMajor.setLayout(tabMajorLayout);
        tabMajorLayout.setHorizontalGroup(
            tabMajorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(majorOptionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(majorQuestionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(tabMajorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(majorRegisterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tabMajorLayout.setVerticalGroup(
            tabMajorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabMajorLayout.createSequentialGroup()
                .addComponent(majorOptionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(majorQuestionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(tabMajorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabMajorLayout.createSequentialGroup()
                    .addGap(129, 129, 129)
                    .addComponent(majorRegisterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
        rpStudentIDTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                rpStudentIDTextField1KeyTyped(evt);
            }
        });

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

        javax.swing.GroupLayout rpResultPane3Layout = new javax.swing.GroupLayout(rpResultPane3);
        rpResultPane3.setLayout(rpResultPane3Layout);
        rpResultPane3Layout.setHorizontalGroup(
            rpResultPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rpResultPane3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(rpResultPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(rpResultPane3Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(18, 18, 18)
                        .addComponent(rpOtherSiteSumLabel3)
                        .addContainerGap(974, Short.MAX_VALUE))
                    .addGroup(rpResultPane3Layout.createSequentialGroup()
                        .addGroup(rpResultPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(rpResultPane3Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addGap(18, 18, 18)
                                .addComponent(rpCurrentSiteSumLabel3))
                            .addGroup(rpResultPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 950, Short.MAX_VALUE)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(rpResultPane3Layout.createSequentialGroup()
                .addGroup(rpResultPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(rpResultPane3Layout.createSequentialGroup()
                        .addGap(333, 333, 333)
                        .addGroup(rpResultPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(rpResultPane3Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(rpCurrentSiteDateLabel3))
                            .addComponent(rpCurrentSiteInfoLabel3)))
                    .addGroup(rpResultPane3Layout.createSequentialGroup()
                        .addGap(350, 350, 350)
                        .addGroup(rpResultPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(rpResultPane3Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(rpOtherSiteDateLabel3))
                            .addComponent(rpOtherSiteInfoLabel3))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        rpResultPane3Layout.setVerticalGroup(
            rpResultPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rpResultPane3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(rpCurrentSiteInfoLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rpCurrentSiteDateLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(rpResultPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(rpCurrentSiteSumLabel3))
                .addGap(50, 50, 50)
                .addComponent(rpOtherSiteInfoLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rpOtherSiteDateLabel3)
                .addGap(35, 35, 35)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(rpResultPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(rpOtherSiteSumLabel3))
                .addGap(29, 29, 29))
        );

        rpResultScrollPane3.setViewportView(rpResultPane3);

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
        _userName = JDBC_Connection.user;
        _pass = JDBC_Connection.password;
        Connection connector = JDBC_Connection.getConnection();
        
        if (connector != null) {
            try {
                String sql = "{call dbo.SP_Lay_Thong_Tin_Dang_Nhap(?)}";
                PreparedStatement ps = connector.prepareStatement(sql);
                ps.setString(1, JDBC_Connection.user);
                
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    _role = rs.getString("TENNHOM");
                    _userID = rs.getString("USERNAME");
                    _branchIndex = sysBranchComboBox1.getSelectedIndex();
                    
                    sysUserIDInfoLabel.setText("Mã GV: " + _userID);
                    sysFullNameInfoLabel.setText("Họ tên: " + rs.getString("HOTEN"));
                    sysRoleInfoLabel.setText("Nhóm: " + _role);
                    
                    sysUsernameTextField1.setText("");
                    sysPasswordTextField1.setText("");
                    
                    cleanup();
                    setOtherTabSystemEnable(true);
                    configWithRole(_role);
                    JOptionPane.showMessageDialog(this, "Đăng nhập thành công !");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Không được thể lấy thông tin đăng nhập\n" + ex);
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
            
            cleanup();
            setOtherTabSystemEnable(false);
            configWithRole(_role);
            
            sysSignInButtonActionPerformed(evt);
        }
    }//GEN-LAST:event_sysLogOutButtonActionPerformed

    private void rpTranscriptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rpTranscriptButtonActionPerformed
        reportFormPanel2.setVisible(true);
        reportFormPanel1.setVisible(false);
        reportFormPanel3.setVisible(false);
        
        loadSubjectComboBox(rpSubjectComboBox2, false);
        loadClassComboBox(rpClassComboBox2, false);
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
                    JOptionPane.showMessageDialog(this, "Không được tìm thấy kết quả bài thi của sinh viên!");
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
            case 2: {
                majorQuestionPanel.setVisible(false);
                majorRegisterPanel.setVisible(false);
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
                JOptionPane.showMessageDialog(this, "Không được tìm thấy kết quả !");
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
        
        loadSubjectComboBox(rpSubjectComboBox1, false);
        rpResultPanel1.setVisible(false);
    }//GEN-LAST:event_rpResultButtonActionPerformed

    private void rpViewResultButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rpViewResultButton3ActionPerformed
        Date fromDate = rpFromDateChooser3.getDate();
        Date toDate = rpToDateChooser3.getDate();
        
        if (fromDate == null || toDate == null) {
            return;
        }
        
        rpResultPane3.setVisible(true);
        
        String titleText = "DANH SÁCH ĐĂNG KÝ THI TRẮC NGHIỆM CƠ SỞ ";
        String periodText = "\nTỪ NGÀY " + DateHelper.toString2(fromDate)
                + " ĐẾN NGÀY " + DateHelper.toString2(toDate);
        
        DefaultTableModel currentSiteModel = (DefaultTableModel) rpCurrentSiteTable3.getModel();
        DefaultTableModel otherSiteModel = (DefaultTableModel) rpOtherSiteTable3.getModel();
        
        currentSiteModel.setNumRows(0);
        otherSiteModel.setNumRows(0);
        
        String sql = "{call dbo.SP_Get_Register_List(?,?)}";
        Connection connector = JDBC_Connection.getConnection();
        String currentSite = BranchDao.getAllBranchs().get(0).getMacs();
        
        try {
            PreparedStatement ps = connector.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(fromDate.getTime()));
            ps.setDate(2, new java.sql.Date(toDate.getTime()));
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector vt = new Vector();
                String site = rs.getString("MACS");
                vt.add(site.equals(currentSite) ? currentSiteModel.getRowCount() + 1 : otherSiteModel.getRowCount() + 1);
                vt.add(rs.getString("TENLOP"));
                vt.add(rs.getString("TENMH"));
                vt.add(rs.getString("TENGV"));
                vt.add(rs.getInt("SOCAUTHI"));
                vt.add(new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate("NGAYTHI")));
                vt.add(rs.getString("DATHI"));
                
                if (site.equals(currentSite)) {
                    currentSiteModel.addRow(vt);
                } else {
                    otherSiteModel.addRow(vt);
                }
            }
            
            rpCurrentSiteInfoLabel3.setText(titleText + (currentSite.equals("CS1") ? "1" : "2"));
            rpCurrentSiteSumLabel3.setText(String.valueOf(currentSiteModel.getRowCount()));
            rpCurrentSiteDateLabel3.setText(periodText);
            
            rpOtherSiteInfoLabel3.setText(titleText + (currentSite.equals("CS1") ? "2" : "1"));
            rpOtherSiteSumLabel3.setText(String.valueOf(otherSiteModel.getRowCount()));
            rpOtherSiteDateLabel3.setText(periodText);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_rpViewResultButton3ActionPerformed

    private void rpRegisterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rpRegisterButtonActionPerformed
        reportFormPanel3.setVisible(true);
        reportFormPanel1.setVisible(false);
        reportFormPanel2.setVisible(false);
        
        rpResultPane3.setVisible(false);
    }//GEN-LAST:event_rpRegisterButtonActionPerformed

    private void ctDepartmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctDepartmentButtonActionPerformed
        categoriesFormPanel1.setVisible(true);
        categoriesFormPanel2.setVisible(false);
        categoriesFormPanel3.setVisible(false);
        categoriesFormPanel4.setVisible(false);
        categoriesFormPanel5.setVisible(false);
        
        loadDepartmentTable(ctDepartmentTable);
        loadBranchComboBox(ctBranchComboBox1, _branchIndex);
        loadDepartBranchComboBox();
        setDepartInput(null);
        setDepartComponentsEnable(false);
    }//GEN-LAST:event_ctDepartmentButtonActionPerformed

    private void ctClassButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctClassButtonActionPerformed
        categoriesFormPanel2.setVisible(true);
        categoriesFormPanel1.setVisible(false);
        categoriesFormPanel3.setVisible(false);
        categoriesFormPanel4.setVisible(false);
        categoriesFormPanel5.setVisible(false);
        
        loadClassTable(ctClassTable);
        loadBranchComboBox(ctBranchComboBox2, _branchIndex);
        loadClassDepartComboBox();
        setClassroomInput(null);
        setClassComponentsEnable(false);
    }//GEN-LAST:event_ctClassButtonActionPerformed

    private void ctTeacherButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctTeacherButtonActionPerformed
        categoriesFormPanel3.setVisible(true);
        categoriesFormPanel2.setVisible(false);
        categoriesFormPanel1.setVisible(false);
        categoriesFormPanel4.setVisible(false);
        categoriesFormPanel5.setVisible(false);
        
        loadTeacherTable(ctTeacherTable);
        loadBranchComboBox(ctBranchComboBox3, _branchIndex);
        loadTeacherDepartCbx();
        setTeacherInput(null);
        setTeacherComponentsEnable(false);
    }//GEN-LAST:event_ctTeacherButtonActionPerformed

    private void ctStudentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctStudentButtonActionPerformed
        categoriesFormPanel4.setVisible(true);
        categoriesFormPanel2.setVisible(false);
        categoriesFormPanel3.setVisible(false);
        categoriesFormPanel1.setVisible(false);
        categoriesFormPanel5.setVisible(false);
        
        loadStudentTable(ctStudentTable);
        loadBranchComboBox(ctBranchComboBox4, _branchIndex);
        loadStudentClassCbx();
        setStudentInput(null);
        setStudentComponentsEnable(false);
    }//GEN-LAST:event_ctStudentButtonActionPerformed

    private void ctSubjectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctSubjectButtonActionPerformed
        categoriesFormPanel5.setVisible(true);
        categoriesFormPanel2.setVisible(false);
        categoriesFormPanel3.setVisible(false);
        categoriesFormPanel4.setVisible(false);
        categoriesFormPanel1.setVisible(false);
        
        loadSubjectTable(ctSubjectTable);
        loadBranchComboBox(ctBranchComboBox5, _branchIndex);
        setSubjectInput(null);
        setSubjectComponentsEnable(false);
    }//GEN-LAST:event_ctSubjectButtonActionPerformed

    private void sysSignUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sysSignUpButtonActionPerformed
        systemFormPanel2.setVisible(true);
        systemFormPanel1.setVisible(false);
        
        loadTeacherIDComboBox(sysTeacherIDComboBox2, true);
        
        if (_role.equals("TRUONG")) {
            sysRoleComboBox2.removeAllItems();
            sysRoleComboBox2.addItem("TRUONG");
        } else if (_role.equals("COSO")) {
            sysRoleComboBox2.removeAllItems();
            sysRoleComboBox2.addItem("COSO");
            sysRoleComboBox2.addItem("GIANGVIEN");
        }
    }//GEN-LAST:event_sysSignUpButtonActionPerformed

    private void ctAddButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctAddButton1ActionPerformed
        int index = ctDepartmentTable.getRowCount() - 1;
        boolean pass = ctDepartmentTable.getValueAt(index, 0) != null;
        DefaultTableModel model = (DefaultTableModel) ctDepartmentTable.getModel();
        
        if (pass) {
            model.addRow(new Object[]{null, null, null});
            index = ctDepartmentTable.getRowCount() - 1;
            ctDepartmentTable.changeSelection(index, 0, false, false);
            ctDepartmentTableMouseClicked(null);
        } else {
            Department depart = getDepartInput();
            if (checkDepartment(depart, false)) {
                model.removeRow(index);
                model.addRow(depart.toArray());
                model.addRow(new Object[]{null, null, null});
                index = ctDepartmentTable.getRowCount() - 1;
                ctDepartmentTable.changeSelection(index, 0, false, false);
                ctDepartmentTableMouseClicked(null);
                
                _addDepartmentCount++;
            }
        }
    }//GEN-LAST:event_ctAddButton1ActionPerformed

    private void ctEditButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctEditButton1ActionPerformed
        int selectedRow = ctDepartmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khoa muốn sửa", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            Department depart = getDepartInput();
            UndoDepartment undo = new UndoDepartment(2, DepartmentDao.getDepartmentById(depart.getMakh()));
            if (checkDepartment(depart, true)) {
                if (DepartmentDao.updateDepartment(depart)) {
                    _undoDepart.push(undo);
                    JOptionPane.showMessageDialog(this, "Hiệu chỉnh thành công");
                    loadDepartmentTable(ctDepartmentTable);
                } else {
                    JOptionPane.showMessageDialog(this, "Hiệu chỉnh thất bại!\n" + message, "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_ctEditButton1ActionPerformed

    private void ctSaveButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctSaveButton1ActionPerformed
        int rowCount = ctDepartmentTable.getRowCount() - 1;
        boolean check = true;
        for (int i = _addDepartmentCount; i > 0; i--) {
            Department depart = new Department();
            int index = rowCount - i;
            depart.setMakh(ctDepartmentTable.getValueAt(index, 0).toString());
            depart.setTenkh(ctDepartmentTable.getValueAt(index, 1).toString());
            depart.setMacs(ctDepartmentTable.getValueAt(index, 2).toString());
            UndoDepartment undo = new UndoDepartment(1, depart);
            if (!DepartmentDao.addDepartment(depart)) {
                check = false;
                break;
            }
            _undoDepart.push(undo);
        }
        
        if (check) {
            JOptionPane.showMessageDialog(this, "Thêm thành công !");
            setDepartInput(null);
            _addDepartmentCount = 0;
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại !", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        
        ctReloadButton1ActionPerformed(evt);
    }//GEN-LAST:event_ctSaveButton1ActionPerformed

    private void ctRemoveButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctRemoveButton1ActionPerformed
        int selectedRow = ctDepartmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khoa muốn xoá", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            Object[] option = {"Có", "Không"};
            int confirm = JOptionPane.showOptionDialog(this, "Bạn có thật sự muốn xóa ?", "Xóa",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[1]);
            if (confirm == JOptionPane.YES_OPTION) {
                String departID = ctDepartmentTable.getValueAt(selectedRow, 0).toString();
                Department depart = DepartmentDao.getDepartmentById(departID);
                if (depart == null) {
                    _addDepartmentCount--;
                    ((DefaultTableModel) ctDepartmentTable.getModel()).removeRow(selectedRow);
                } else {
                    UndoDepartment undo = new UndoDepartment(3, depart);
                    if (DepartmentDao.deleteDepartment(departID)) {
                        _undoDepart.push(undo);
                        JOptionPane.showMessageDialog(this, "Xóa thành công");
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa thất bại!\n" + message, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                    loadDepartmentTable(ctDepartmentTable);
                }
                setDepartInput(null);
            }
        }
    }//GEN-LAST:event_ctRemoveButton1ActionPerformed

    private void ctUndoButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctUndoButton1ActionPerformed
        if (_undoDepart.isEmpty()) {
            return;
        }
        
        UndoDepartment undo = _undoDepart.pop();
        boolean pass = false;
        String mode = "";
        switch (undo.getMode()) {
            case 1: {
                if (DepartmentDao.deleteDepartment(undo.getDepart().getMakh())) {
                    pass = true;
                    mode = "thêm";
                }
                break;
            }
            case 2: {
                if (DepartmentDao.updateDepartment(undo.getDepart())) {
                    pass = true;
                    mode = "sửa";
                }
                break;
            }
            case 3: {
                if (DepartmentDao.addDepartment(undo.getDepart())) {
                    pass = true;
                    mode = "xóa";
                }
                break;
            }
        }
        
        if (pass) {
            JOptionPane.showMessageDialog(this, "Hoàn tác " + mode + " thành công với mã khoa "
                    + undo.getDepart().getMakh() + " !");
            loadDepartmentTable(ctDepartmentTable);
            setDepartInput(null);
        } else {
            JOptionPane.showMessageDialog(this, "Hoàn tác thất bại với mã khoa " + undo.getDepart().getMakh() + " !", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_ctUndoButton1ActionPerformed

    private void ctReloadButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctReloadButton1ActionPerformed
        loadDepartmentTable(ctDepartmentTable);
        setDepartComponentsEnable(false);
    }//GEN-LAST:event_ctReloadButton1ActionPerformed

    private void ctAddButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctAddButton2ActionPerformed
        int index = ctClassTable.getRowCount() - 1;
        boolean pass = ctClassTable.getValueAt(index, 0) != null;
        DefaultTableModel model = (DefaultTableModel) ctClassTable.getModel();
        
        if (pass) {
            model.addRow(new Object[]{null, null, null});
            index = ctClassTable.getRowCount() - 1;
            ctClassTable.changeSelection(index, 0, false, false);
            ctClassTableMouseClicked(null);
        } else {
            Classroom classroom = getClassroomInput();
            if (checkClassroom(classroom, false)) {
                model.removeRow(index);
                model.addRow(classroom.toArray());
                model.addRow(new Object[]{null, null, null});
                index = ctClassTable.getRowCount() - 1;
                ctClassTable.changeSelection(index, 0, false, false);
                ctClassTableMouseClicked(null);
                
                _addClassCount++;
            }
        }
    }//GEN-LAST:event_ctAddButton2ActionPerformed

    private void ctEditButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctEditButton2ActionPerformed
        int selectedRow = ctClassTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lớp muốn sửa", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            Classroom classroom = getClassroomInput();
            UndoClassroom undo = new UndoClassroom(2, ClassroomDao.getClassroomById(classroom.getMaLop()));
            if (checkClassroom(classroom, true)) {
                if (ClassroomDao.updateClassroom(classroom)) {
                    _undoClass.push(undo);
                    JOptionPane.showMessageDialog(this, "Hiệu chỉnh thành công");
                    loadClassTable(ctClassTable);
                } else {
                    JOptionPane.showMessageDialog(this, "Hiệu chỉnh thất bại!\n" + message, "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_ctEditButton2ActionPerformed

    private void ctSaveButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctSaveButton2ActionPerformed
        int rowCount = ctClassTable.getRowCount() - 1;
        boolean check = true;
        for (int i = _addClassCount; i > 0; i--) {
            Classroom classroom = new Classroom();
            int index = rowCount - i;
            classroom.setMaLop(ctClassTable.getValueAt(index, 0).toString());
            classroom.setTenLop(ctClassTable.getValueAt(index, 1).toString());
            classroom.setMakh(ctClassTable.getValueAt(index, 2).toString());
            UndoClassroom undo = new UndoClassroom(1, classroom);
            
            if (!ClassroomDao.addClassroom(classroom)) {
                check = false;
                break;
            }
            
            _undoClass.push(undo);
        }
        
        if (check) {
            JOptionPane.showMessageDialog(this, "Thêm thành công !");
            setClassroomInput(null);
            _addClassCount = 0;
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại !", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        
        ctReloadButton2ActionPerformed(evt);
    }//GEN-LAST:event_ctSaveButton2ActionPerformed

    private void ctRemoveButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctRemoveButton2ActionPerformed
        int selectedRow = ctClassTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lớp muốn xoá", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            Object[] option = {"Có", "Không"};
            int confirm = JOptionPane.showOptionDialog(this, "Bạn có thật sự muốn xóa?", "Xóa",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[1]);
            if (confirm == JOptionPane.YES_OPTION) {
                String classID = ctClassTable.getValueAt(selectedRow, 0).toString();
                Classroom classroom = ClassroomDao.getClassroomById(classID);
                if (classroom == null) {
                    _addClassCount--;
                    ((DefaultTableModel) ctClassTable.getModel()).removeRow(selectedRow);
                } else {
                    UndoClassroom undo = new UndoClassroom(3, classroom);
                    if (ClassroomDao.deleteClassroom(classID)) {
                        _undoClass.push(undo);
                        JOptionPane.showMessageDialog(this, "Xóa thành công");
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa thất bại!\n" + message, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                    loadClassTable(ctClassTable);
                }
                setClassroomInput(null);
            }
        }
    }//GEN-LAST:event_ctRemoveButton2ActionPerformed

    private void ctUndoButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctUndoButton2ActionPerformed
        if (_undoClass.isEmpty()) {
            return;
        }
        
        UndoClassroom undo = _undoClass.pop();
        boolean pass = false;
        String mode = "";
        switch (undo.getMode()) {
            case 1: {
                if (ClassroomDao.deleteClassroom(undo.getClassroom().getMaLop())) {
                    pass = true;
                    mode = "thêm";
                }
                break;
            }
            case 2: {
                if (ClassroomDao.updateClassroom(undo.getClassroom())) {
                    pass = true;
                    mode = "sửa";
                }
                break;
            }
            case 3: {
                if (ClassroomDao.addClassroom(undo.getClassroom())) {
                    pass = true;
                    mode = "xóa";
                }
                break;
            }
        }
        
        if (pass) {
            JOptionPane.showMessageDialog(this, "Hoàn tác " + mode + " thành công với mã lớp "
                    + undo.getClassroom().getMaLop() + " !");
            loadClassTable(ctClassTable);
            setClassroomInput(null);
        } else {
            JOptionPane.showMessageDialog(this, "Hoàn tác thất bại với mã lớp "
                    + undo.getClassroom().getMaLop() + " !", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_ctUndoButton2ActionPerformed

    private void ctReloadButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctReloadButton2ActionPerformed
        loadClassTable(ctClassTable);
        setClassComponentsEnable(false);
    }//GEN-LAST:event_ctReloadButton2ActionPerformed

    private void ctAddButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctAddButton3ActionPerformed
        int index = ctTeacherTable.getRowCount() - 1;
        boolean pass = ctTeacherTable.getValueAt(index, 0) != null;
        DefaultTableModel model = (DefaultTableModel) ctTeacherTable.getModel();
        
        if (pass) {
            model.addRow(new Object[]{null, null, null, null, null});
            index = ctTeacherTable.getRowCount() - 1;
            ctTeacherTable.changeSelection(index, 0, false, false);
            ctTeacherTableMouseClicked(null);
        } else {
            Teacher teacher = getTeacherInput();
            if (checkTeacher(teacher, false)) {
                model.removeRow(index);
                model.addRow(teacher.toArray());
                model.addRow(new Object[]{null, null, null, null, null});
                index = ctTeacherTable.getRowCount() - 1;
                ctTeacherTable.changeSelection(index, 0, false, false);
                ctTeacherTableMouseClicked(null);
                
                _addTeacherCount++;
            }
        }
    }//GEN-LAST:event_ctAddButton3ActionPerformed

    private void ctEditButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctEditButton3ActionPerformed
        int selectedRow = ctTeacherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn giảng viên muốn sửa", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            Teacher teacher = getTeacherInput();
            UndoTeacher undo = new UndoTeacher(2, TeacherDao.getTeacherById(teacher.getMagv()));
            if (checkTeacher(teacher, true)) {
                if (TeacherDao.updateTeacher(teacher)) {
                    _undoTeacher.push(undo);
                    JOptionPane.showMessageDialog(this, "Hiệu chỉnh thành công");
                    loadTeacherTable(ctTeacherTable);
                } else {
                    JOptionPane.showMessageDialog(this, "Hiệu chỉnh thất bại!\n" + message, "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_ctEditButton3ActionPerformed

    private void ctSaveButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctSaveButton3ActionPerformed
        int rowCount = ctTeacherTable.getRowCount() - 1;
        boolean check = true;
        for (int i = _addTeacherCount; i > 0; i--) {
            Teacher teacher = new Teacher();
            int index = rowCount - i;
            teacher.setMagv(ctTeacherTable.getValueAt(index, 0).toString());
            teacher.setHo(ctTeacherTable.getValueAt(index, 1).toString());
            teacher.setTen(ctTeacherTable.getValueAt(index, 2).toString());
            teacher.setHocVi(ctTeacherTable.getValueAt(index, 3).toString());
            teacher.setMakh(ctTeacherTable.getValueAt(index, 4).toString());
            UndoTeacher undo = new UndoTeacher(1, teacher);
            if (!TeacherDao.addTeacher(teacher)) {
                check = false;
                break;
            }
            _undoTeacher.push(undo);
        }
        
        if (check) {
            JOptionPane.showMessageDialog(this, "Thêm thành công !");
            setTeacherInput(null);
            _addTeacherCount = 0;
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại !", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        
        ctReloadButton3ActionPerformed(evt);
    }//GEN-LAST:event_ctSaveButton3ActionPerformed

    private void ctRemoveButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctRemoveButton3ActionPerformed
        int selectedRow = ctTeacherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn giảng viên muốn xoá", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            Object[] option = {"Có", "Không"};
            int confirm = JOptionPane.showOptionDialog(this, "Bạn có thật sự muốn xóa?", "Xóa",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[1]);
            if (confirm == JOptionPane.YES_OPTION) {
                String teacherID = ctTeacherTable.getValueAt(selectedRow, 0).toString();
                Teacher teacher = TeacherDao.getTeacherById(teacherID);
                if (teacher == null) {
                    _addTeacherCount--;
                    ((DefaultTableModel) ctTeacherTable.getModel()).removeRow(selectedRow);
                } else {
                    UndoTeacher undo = new UndoTeacher(3, teacher);
                    if (TeacherDao.deleteTeacher(teacherID)) {
                        _undoTeacher.push(undo);
                        JOptionPane.showMessageDialog(this, "Xóa thành công");
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa thất bại!\n" + message, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                    loadTeacherTable(ctTeacherTable);
                }
                setTeacherInput(null);
            }
        }
    }//GEN-LAST:event_ctRemoveButton3ActionPerformed

    private void ctUndoButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctUndoButton3ActionPerformed
        if (_undoTeacher.isEmpty()) {
            return;
        }
        
        UndoTeacher undo = _undoTeacher.pop();
        boolean pass = false;
        String mode = "";
        switch (undo.getMode()) {
            case 1: {
                if (TeacherDao.deleteTeacher(undo.getTeacher().getMagv())) {
                    pass = true;
                    mode = "thêm";
                }
                break;
            }
            case 2: {
                if (TeacherDao.updateTeacher(undo.getTeacher())) {
                    pass = true;
                    mode = "sửa";
                }
                break;
            }
            case 3: {
                if (TeacherDao.addTeacher(undo.getTeacher())) {
                    pass = true;
                    mode = "xóa";
                }
                break;
            }
        }
        
        if (pass) {
            JOptionPane.showMessageDialog(this, "Hoàn tác " + mode + " thành công với mã giáo viên "
                    + undo.getTeacher().getMagv() + " !");
            loadTeacherTable(ctTeacherTable);
            setTeacherInput(null);
        } else {
            JOptionPane.showMessageDialog(this, "Hoàn tác thất bại với mã giáo viên "
                    + undo.getTeacher().getMagv() + " !", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_ctUndoButton3ActionPerformed

    private void ctReloadButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctReloadButton3ActionPerformed
        loadTeacherTable(ctTeacherTable);
        setTeacherComponentsEnable(false);
    }//GEN-LAST:event_ctReloadButton3ActionPerformed

    private void ctAddButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctAddButton4ActionPerformed
        int index = ctStudentTable.getRowCount() - 1;
        boolean pass = ctStudentTable.getValueAt(index, 0) != null;
        DefaultTableModel model = (DefaultTableModel) ctStudentTable.getModel();
        
        if (pass) {
            model.addRow(new Object[]{null, null, null, null, null, null});
            index = ctStudentTable.getRowCount() - 1;
            ctStudentTable.changeSelection(index, 0, false, false);
            ctStudentTableMouseClicked(null);
        } else {
            Student student = getStudentInput();
            if (checkStudent(student, false)) {
                model.removeRow(index);
                model.addRow(student.toArray());
                model.addRow(new Object[]{null, null, null, null, null, null});
                index = ctStudentTable.getRowCount() - 1;
                ctStudentTable.changeSelection(index, 0, false, false);
                ctStudentTableMouseClicked(null);
                
                _addStudentCount++;
            }
        }
    }//GEN-LAST:event_ctAddButton4ActionPerformed

    private void ctEditButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctEditButton4ActionPerformed
        int selectedRow = ctStudentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sinh viên muốn sửa", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            Student student = getStudentInput();
            UndoStudent undo = new UndoStudent(2, StudentDao.getStudentById(student.getMasv()));
            if (checkStudent(student, true)) {
                if (StudentDao.updateStudent(student)) {
                    _undoStudent.push(undo);
                    JOptionPane.showMessageDialog(this, "Hiệu chỉnh thành công");
                    loadStudentTable(ctStudentTable);
                } else {
                    JOptionPane.showMessageDialog(this, "Hiệu chỉnh thất bại!\n" + message, "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_ctEditButton4ActionPerformed

    private void ctSaveButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctSaveButton4ActionPerformed
        int rowCount = ctStudentTable.getRowCount() - 1;
        boolean check = true;
        for (int i = _addStudentCount; i > 0; i--) {
            Student student = new Student();
            int index = rowCount - i;
            student.setMasv(ctStudentTable.getValueAt(index, 0).toString());
            student.setHo(ctStudentTable.getValueAt(index, 1).toString());
            student.setTen(ctStudentTable.getValueAt(index, 2).toString());
            student.setNgaySinh(DateHelper.toString(DateHelper.toDate(ctStudentTable.getValueAt(index, 3).toString())));
            student.setDiaChi(ctStudentTable.getValueAt(index, 4).toString());
            student.setMaLop(ctStudentTable.getValueAt(index, 5).toString());
            student.setMatKhau(ctStudentTable.getValueAt(index, 6).toString());
            UndoStudent undo = new UndoStudent(1, student);
            if (!StudentDao.addStudent(student)) {
                check = false;
                break;
            }
            _undoStudent.push(undo);
        }
        
        if (check) {
            JOptionPane.showMessageDialog(this, "Thêm thành công !");
            setStudentInput(null);
            _addStudentCount = 0;
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại !", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        
        ctReloadButton4ActionPerformed(evt);
    }//GEN-LAST:event_ctSaveButton4ActionPerformed

    private void ctRemoveButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctRemoveButton4ActionPerformed
        int selectedRow = ctStudentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sinh viên muốn xoá", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            Object[] option = {"Có", "Không"};
            int confirm = JOptionPane.showOptionDialog(this, "Bạn có thật sự muốn xóa?", "Xóa",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[1]);
            if (confirm == JOptionPane.YES_OPTION) {
                String studentID = ctStudentTable.getValueAt(selectedRow, 0).toString();
                Student student = StudentDao.getStudentById(studentID);
                if (student == null) {
                    _addStudentCount--;
                    ((DefaultTableModel) ctStudentTable.getModel()).removeRow(selectedRow);
                } else {
                    UndoStudent undo = new UndoStudent(3, student);
                    if (StudentDao.deleteStudent(studentID)) {
                        _undoStudent.push(undo);
                        JOptionPane.showMessageDialog(this, "Xóa thành công");
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa thất bại!\n" + message, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                    loadStudentTable(ctStudentTable);
                }
                setStudentInput(null);
            }
        }
    }//GEN-LAST:event_ctRemoveButton4ActionPerformed

    private void ctUndoButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctUndoButton4ActionPerformed
        if (_undoStudent.isEmpty()) {
            return;
        }
        
        UndoStudent undo = _undoStudent.pop();
        boolean pass = false;
        String mode = "";
        switch (undo.getMode()) {
            case 1: {
                if (StudentDao.deleteStudent(undo.getStudent().getMasv())) {
                    pass = true;
                    mode = "thêm";
                }
                break;
            }
            case 2: {
                if (StudentDao.updateStudent(undo.getStudent())) {
                    pass = true;
                    mode = "sửa";
                }
                break;
            }
            case 3: {
                if (StudentDao.addStudent(undo.getStudent())) {
                    pass = true;
                    mode = "xóa";
                }
                break;
            }
        }
        
        if (pass) {
            JOptionPane.showMessageDialog(this, "Hoàn tác " + mode + " thành công với mã sinh viên "
                    + undo.getStudent().getMasv() + " !");
            loadStudentTable(ctStudentTable);
            setStudentInput(null);
        } else {
            JOptionPane.showMessageDialog(this, "Hoàn tác thất bại với mã sinh viên "
                    + undo.getStudent().getMasv() + " !", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_ctUndoButton4ActionPerformed

    private void ctReloadButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctReloadButton4ActionPerformed
        loadStudentTable(ctStudentTable);
        setStudentComponentsEnable(false);
    }//GEN-LAST:event_ctReloadButton4ActionPerformed

    private void ctAddButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctAddButton5ActionPerformed
        int index = ctSubjectTable.getRowCount() - 1;
        boolean pass = ctSubjectTable.getValueAt(index, 0) != null;
        DefaultTableModel model = (DefaultTableModel) ctSubjectTable.getModel();
        
        if (pass) {
            model.addRow(new Object[]{null, null});
            index = ctSubjectTable.getRowCount() - 1;
            ctSubjectTable.changeSelection(index, 0, false, false);
            ctSubjectTableMouseClicked(null);
        } else {
            Subject subject = getSubjectInput();
            if (checkSubject(subject, false)) {
                model.removeRow(index);
                model.addRow(subject.toArray());
                model.addRow(new Object[]{null, null});
                index = ctSubjectTable.getRowCount() - 1;
                ctSubjectTable.changeSelection(index, 0, false, false);
                ctSubjectTableMouseClicked(null);
                
                _addSubjectCount++;
            }
        }
    }//GEN-LAST:event_ctAddButton5ActionPerformed

    private void ctEditButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctEditButton5ActionPerformed
        int selectedRow = ctSubjectTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn môn học muốn sửa", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            Subject subject = getSubjectInput();
            UndoSubject undo = new UndoSubject(2, SubjectDao.getSubjectById(subject.getMamh()));
            if (checkSubject(subject, true)) {
                if (SubjectDao.updateSubject(subject)) {
                    _undoSubject.push(undo);
                    JOptionPane.showMessageDialog(this, "Hiệu chỉnh thành công");
                    loadSubjectTable(ctSubjectTable);
                } else {
                    JOptionPane.showMessageDialog(this, "Hiệu chỉnh thất bại!\n" + message, "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_ctEditButton5ActionPerformed

    private void ctSaveButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctSaveButton5ActionPerformed
        int rowCount = ctSubjectTable.getRowCount() - 1;
        boolean check = true;
        for (int i = _addSubjectCount; i > 0; i--) {
            Subject subject = new Subject();
            int index = rowCount - i;
            subject.setMamh(ctSubjectTable.getValueAt(index, 0).toString());
            subject.setTenmh(ctSubjectTable.getValueAt(index, 1).toString());
            UndoSubject undo = new UndoSubject(1, subject);
            
            if (!SubjectDao.addSubject(subject)) {
                check = false;
                break;
            }
            _undoSubject.push(undo);
        }
        
        if (check) {
            JOptionPane.showMessageDialog(this, "Thêm thành công !");
            setSubjectInput(null);
            _addSubjectCount = 0;
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại !", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        
        ctReloadButton5ActionPerformed(evt);
    }//GEN-LAST:event_ctSaveButton5ActionPerformed

    private void ctRemoveButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctRemoveButton5ActionPerformed
        int selectedRow = ctSubjectTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn môn học muốn xoá", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            Object[] option = {"Có", "Không"};
            int confirm = JOptionPane.showOptionDialog(this, "Bạn có thật sự muốn xóa?", "Xóa",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[1]);
            if (confirm == JOptionPane.YES_OPTION) {
                String subjectID = ctSubjectTable.getValueAt(selectedRow, 0).toString();
                Subject subject = SubjectDao.getSubjectById(subjectID);
                if (subject == null) {
                    _addSubjectCount--;
                    ((DefaultTableModel) ctSubjectTable.getModel()).removeRow(selectedRow);
                } else {
                    UndoSubject undo = new UndoSubject(3, subject);
                    if (SubjectDao.deleteSubject(subjectID)) {
                        _undoSubject.push(undo);
                        JOptionPane.showMessageDialog(this, "Xóa thành công");
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa thất bại!\n" + message, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                    loadSubjectTable(ctSubjectTable);
                }
                setSubjectInput(null);
            }
        }
    }//GEN-LAST:event_ctRemoveButton5ActionPerformed

    private void ctUndoButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctUndoButton5ActionPerformed
        if (_undoSubject.isEmpty()) {
            return;
        }
        
        UndoSubject undo = _undoSubject.pop();
        boolean pass = false;
        String mode = "";
        switch (undo.getMode()) {
            case 1: {
                if (SubjectDao.deleteSubject(undo.getSubject().getMamh())) {
                    pass = true;
                    mode = "thêm";
                }
                break;
            }
            case 2: {
                if (SubjectDao.updateSubject(undo.getSubject())) {
                    pass = true;
                    mode = "sửa";
                }
                break;
            }
            case 3: {
                if (SubjectDao.addSubject(undo.getSubject())) {
                    pass = true;
                    mode = "xóa";
                }
                break;
            }
        }
        
        if (pass) {
            JOptionPane.showMessageDialog(this, "Hoàn tác " + mode + " thành công với mã môn "
                    + undo.getSubject().getMamh() + " !");
            loadSubjectTable(ctSubjectTable);
            setSubjectInput(null);
        } else {
            JOptionPane.showMessageDialog(this, "Hoàn tác thất bại với mã môn "
                    + undo.getSubject().getMamh() + " !", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_ctUndoButton5ActionPerformed

    private void ctReloadButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctReloadButton5ActionPerformed
        loadSubjectTable(ctSubjectTable);
        setSubjectComponentsEnable(false);
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
            String sql = "{? = Call dbo.SP_Tao_Login(?,?,?,?)}";
            
            CallableStatement cs = connector.prepareCall(sql);
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.setString(2, username);
            cs.setString(3, pass);
            cs.setString(4, teacherID);
            cs.setString(5, role);
            cs.execute();
            
            int result = cs.getInt(1);
            switch (result) {
                case 0:
                    JOptionPane.showMessageDialog(this, "Tạo tài khoản thành công !");
                    sysUsernameTextField2.setText("");
                    sysPasswordTextField2.setText("");
                    sysReTypePasswordTextField2.setText("");
                    break;
                case 1:
                    JOptionPane.showMessageDialog(this, "Tài khoản đã tồn tại !", "Tạo tài khoản thất bại", JOptionPane.ERROR_MESSAGE);
                    break;
                case 2:
                    JOptionPane.showMessageDialog(this, "Mã giảng viên đã được sử dụng !", "Tạo tài khoản thất bại", JOptionPane.ERROR_MESSAGE);
                    break;
                default:
                    break;
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_sysCreateAccountButton2ActionPerformed

    private void ctDepartmentTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctDepartmentTableMouseClicked
        int index = ctDepartmentTable.getSelectedRow();
        Object departmentID = ctDepartmentTable.getValueAt(index, 0);
        boolean isEmpty = departmentID == null;
        Department department = isEmpty ? null : DepartmentDao.getDepartmentById(departmentID.toString());
        
        setDepartComponentsEnable(true);
        if (_role.equals("COSO")) {
            ctDepartmentIDTextField.setEditable(isEmpty);
            ctEditButton2.setEnabled(!isEmpty);
            ctRemoveButton2.setEnabled(!isEmpty);
        }
        
        setDepartInput(department);
    }//GEN-LAST:event_ctDepartmentTableMouseClicked

    private void ctClassTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctClassTableMouseClicked
        int index = ctClassTable.getSelectedRow();
        Object classID = ctClassTable.getValueAt(index, 0);
        boolean isEmpty = classID == null;
        Classroom classroom = isEmpty ? null : ClassroomDao.getClassroomById(classID.toString());
        
        setClassComponentsEnable(true);
        if (_role.equals("COSO")) {
            ctClassIDTextField.setEditable(isEmpty);
            ctEditButton2.setEnabled(!isEmpty);
            ctRemoveButton2.setEnabled(!isEmpty);
        }
        
        setClassroomInput(classroom);
    }//GEN-LAST:event_ctClassTableMouseClicked

    private void ctTeacherTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctTeacherTableMouseClicked
        int index = ctTeacherTable.getSelectedRow();
        Object teacherID = ctTeacherTable.getValueAt(index, 0);
        boolean isEmpty = teacherID == null;
        Teacher teacher = isEmpty ? null : TeacherDao.getTeacherById(teacherID.toString());
        
        setTeacherComponentsEnable(true);
        if (_role.equals("COSO")) {
            ctTeacherIDTextField.setEditable(isEmpty);
            ctEditButton3.setEnabled(!isEmpty);
            ctRemoveButton3.setEnabled(!isEmpty);
        }
        
        setTeacherInput(teacher);
    }//GEN-LAST:event_ctTeacherTableMouseClicked

    private void ctStudentTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctStudentTableMouseClicked
        int index = ctStudentTable.getSelectedRow();
        Object studentID = ctStudentTable.getValueAt(index, 0);
        boolean isEmpty = studentID == null;
        Student student = isEmpty ? null : StudentDao.getStudentById(studentID.toString());
        
        setStudentComponentsEnable(true);
        if (_role.equals("COSO")) {
            ctStudentIDTextField.setEditable(isEmpty);
            ctEditButton4.setEnabled(!isEmpty);
            ctRemoveButton4.setEnabled(!isEmpty);
        }
        
        setStudentInput(student);
    }//GEN-LAST:event_ctStudentTableMouseClicked

    private void ctSubjectTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ctSubjectTableMouseClicked
        int index = ctSubjectTable.getSelectedRow();
        Object subjectID = ctSubjectTable.getValueAt(index, 0);
        boolean isEmpty = subjectID == null;
        Subject subject = isEmpty ? null : SubjectDao.getSubjectById(subjectID.toString());
        
        setSubjectComponentsEnable(true);
        if (_role.equals("COSO")) {
            ctSubjectIDTextField.setEditable(isEmpty);
            ctEditButton5.setEnabled(!isEmpty);
            ctRemoveButton5.setEnabled(!isEmpty);
        }
        
        setSubjectInput(subject);
    }//GEN-LAST:event_ctSubjectTableMouseClicked

    private void ctStudentIDTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ctStudentIDTextFieldKeyTyped
        char keyChar = evt.getKeyChar();
        if (Character.isLowerCase(keyChar)) {
            evt.setKeyChar(Character.toUpperCase(keyChar));
        }
    }//GEN-LAST:event_ctStudentIDTextFieldKeyTyped

    private void ctSubjectIDTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ctSubjectIDTextFieldKeyTyped
        char keyChar = evt.getKeyChar();
        if (Character.isLowerCase(keyChar)) {
            evt.setKeyChar(Character.toUpperCase(keyChar));
        }
    }//GEN-LAST:event_ctSubjectIDTextFieldKeyTyped

    private void ctTeacherIDTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ctTeacherIDTextFieldKeyTyped
        char keyChar = evt.getKeyChar();
        if (Character.isLowerCase(keyChar)) {
            evt.setKeyChar(Character.toUpperCase(keyChar));
        }
    }//GEN-LAST:event_ctTeacherIDTextFieldKeyTyped

    private void ctClassIDTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ctClassIDTextFieldKeyTyped
        char keyChar = evt.getKeyChar();
        if (Character.isLowerCase(keyChar)) {
            evt.setKeyChar(Character.toUpperCase(keyChar));
        }
    }//GEN-LAST:event_ctClassIDTextFieldKeyTyped

    private void ctDepartmentIDTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ctDepartmentIDTextFieldKeyTyped
        char keyChar = evt.getKeyChar();
        if (Character.isLowerCase(keyChar)) {
            evt.setKeyChar(Character.toUpperCase(keyChar));
        }
    }//GEN-LAST:event_ctDepartmentIDTextFieldKeyTyped

    private void mjQuestionManagementButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mjQuestionManagementButtonActionPerformed
        majorQuestionPanel.setVisible(true);
        majorRegisterPanel.setVisible(false);
        
        loadQuestionTable(mjQuestionTable);
        loadSubjectComboBox(mjQuestionSubjectIDComboBox, true);
        setQuestionComponentsEnable(false);
    }//GEN-LAST:event_mjQuestionManagementButtonActionPerformed

    private void mjAddButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mjAddButton1ActionPerformed
        int index = mjQuestionTable.getRowCount() - 1;
        boolean pass = mjQuestionTable.getValueAt(index, 0) != null;
        DefaultTableModel model = (DefaultTableModel) mjQuestionTable.getModel();
        
        if (pass) {
            model.addRow(new Object[]{null, null});
            index = mjQuestionTable.getRowCount() - 1;
            mjQuestionTable.changeSelection(index, 0, false, false);
            mjQuestionTableMouseClicked(null);
            _listQuestion = QuestionDao.getAllQuestions();
            mjQuestionIDTextField.setText(String.valueOf(_listQuestion.get(_listQuestion.size() - 1).getCauHoi() + 1 + _addQuestionCount));
        } else {
            Question question = getQuestionInput();
            if (checkQuestion(question, false)) {
                model.removeRow(index);
                model.addRow(question.toArray());
                model.addRow(new Object[]{null, null});
                index = mjQuestionTable.getRowCount() - 1;
                mjQuestionTable.changeSelection(index, 0, false, false);
                mjQuestionTableMouseClicked(null);
                _addQuestionCount++;
                _listQuestion = QuestionDao.getAllQuestions();
                mjQuestionIDTextField.setText(String.valueOf(_listQuestion.get(_listQuestion.size() - 1).getCauHoi() + 1 + _addQuestionCount));
            }
        }
    }//GEN-LAST:event_mjAddButton1ActionPerformed

    private void mjEditButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mjEditButton1ActionPerformed
        int selectedRow = mjQuestionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn câu hỏi muốn sửa", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            Question question = getQuestionInput();
            UndoQuestion undo = new UndoQuestion(2, QuestionDao.getQuestionById(question.getCauHoi()));
            if (checkQuestion(question, true)) {
                if (QuestionDao.updateQuestion(question)) {
                    _undoQuestion.push(undo);
                    JOptionPane.showMessageDialog(this, "Hiệu chỉnh thành công");
                    loadQuestionTable(mjQuestionTable);
                } else {
                    JOptionPane.showMessageDialog(this, "Hiệu chỉnh thất bại!\n" + message, "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_mjEditButton1ActionPerformed

    private void mjSaveButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mjSaveButton1ActionPerformed
        int rowCount = mjQuestionTable.getRowCount() - 1;
        boolean check = true;
        for (int i = _addQuestionCount; i > 0; i--) {
            Question question = new Question();
            int index = rowCount - i;
            question.setCauHoi(Integer.valueOf(mjQuestionTable.getValueAt(index, 0).toString()));
            question.setMamh(mjQuestionTable.getValueAt(index, 1).toString());
            question.setTrinhDo(mjQuestionTable.getValueAt(index, 2).toString());
            question.setNoiDung(mjQuestionTable.getValueAt(index, 3).toString());
            question.setA(mjQuestionTable.getValueAt(index, 4).toString());
            question.setB(mjQuestionTable.getValueAt(index, 5).toString());
            question.setC(mjQuestionTable.getValueAt(index, 6).toString());
            question.setD(mjQuestionTable.getValueAt(index, 7).toString());
            question.setDapAn(mjQuestionTable.getValueAt(index, 8).toString());
            question.setMagv(mjQuestionTable.getValueAt(index, 9).toString());
            UndoQuestion undo = new UndoQuestion(1, question);
            
            if (!QuestionDao.addQuestion(question)) {
                check = false;
                break;
            }
            _undoQuestion.push(undo);
        }
        
        if (check) {
            JOptionPane.showMessageDialog(this, "Thêm thành công !");
            setQuestionInput(null);
            _addQuestionCount = 0;
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại !", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        
        mjReloadButton1ActionPerformed(evt);
    }//GEN-LAST:event_mjSaveButton1ActionPerformed

    private void mjRemoveButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mjRemoveButton1ActionPerformed
        int selectedRow = mjQuestionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn câu hỏi muốn xoá", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            Object[] option = {"Có", "Không"};
            int confirm = JOptionPane.showOptionDialog(this, "Bạn có thật sự muốn xóa?", "Xóa",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[1]);
            if (confirm == JOptionPane.YES_OPTION) {
                int questionID = Integer.valueOf(mjQuestionTable.getValueAt(selectedRow, 0).toString());
                Question question = QuestionDao.getQuestionById(questionID);
                if (question == null) {
                    _addQuestionCount--;
                    ((DefaultTableModel) mjQuestionTable.getModel()).removeRow(selectedRow);
                } else {
                    UndoQuestion undo = new UndoQuestion(3, question);
                    if (QuestionDao.deleteQuestion(questionID)) {
                        _undoQuestion.push(undo);
                        JOptionPane.showMessageDialog(this, "Xóa thành công");
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa thất bại!\n" + message, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                    loadQuestionTable(mjQuestionTable);
                }
                setQuestionInput(null);
            }
        }
    }//GEN-LAST:event_mjRemoveButton1ActionPerformed

    private void mjUndoButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mjUndoButton1ActionPerformed
        if (_undoQuestion.isEmpty()) {
            return;
        }
        
        UndoQuestion undo = _undoQuestion.pop();
        boolean pass = false;
        String mode = "";
        switch (undo.getMode()) {
            case 1: {
                if (QuestionDao.deleteQuestion(undo.getQuestion().getCauHoi())) {
                    pass = true;
                    mode = "thêm";
                }
                break;
            }
            case 2: {
                if (QuestionDao.updateQuestion(undo.getQuestion())) {
                    pass = true;
                    mode = "sửa";
                }
                break;
            }
            case 3: {
                if (QuestionDao.addQuestion(undo.getQuestion())) {
                    pass = true;
                    mode = "xóa";
                }
                break;
            }
        }
        
        if (pass) {
            JOptionPane.showMessageDialog(this, "Hoàn tác " + mode + " thành công với câu hỏi số "
                    + undo.getQuestion().getCauHoi() + " !");
            loadQuestionTable(mjQuestionTable);
            setQuestionInput(null);
        } else {
            JOptionPane.showMessageDialog(this, "Hoàn tác thất bại với câu hỏi số "
                    + undo.getQuestion().getCauHoi() + " !", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mjUndoButton1ActionPerformed

    private void mjReloadButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mjReloadButton1ActionPerformed
        if (_addQuestionCount == 0) {
            loadQuestionTable(mjQuestionTable);
            setQuestionComponentsEnable(false);
        } else {
            _listQuestion = QuestionDao.getAllQuestions();
            mjQuestionIDTextField.setText(String.valueOf(_listQuestion.get(_listQuestion.size() - 1).getCauHoi() + 1 + _addQuestionCount));
        }
    }//GEN-LAST:event_mjReloadButton1ActionPerformed

    private void mjQuestionTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mjQuestionTableMouseClicked
        int index = mjQuestionTable.getSelectedRow();
        Object questionID = mjQuestionTable.getValueAt(index, 0);
        boolean isEmpty = questionID == null;
        Question question = isEmpty ? null : QuestionDao.getQuestionById(Integer.valueOf(questionID.toString()));
        
        setQuestionComponentsEnable(true);
        if (_role.equals("COSO") || _role.equals("GIANGVIEN")) {
            mjEditButton1.setEnabled(!isEmpty);
            mjRemoveButton1.setEnabled(!isEmpty);
            
            if (_role.equals("COSO") && !isEmpty) {
                boolean pass = TeacherDao.isTeacherInBranch(mjQuestionTable.getValueAt(index, 9).toString());
                mjEditButton1.setEnabled(pass);
                mjRemoveButton1.setEnabled(pass);
            } else if (_role.equals("GIANGVIEN") && !isEmpty) {
                boolean pass = mjQuestionTable.getValueAt(index, 9).toString().equals(_userID);
                mjEditButton1.setEnabled(pass);
                mjRemoveButton1.setEnabled(pass);
            }
        }
        
        setQuestionInput(question);
        if (isEmpty && index == mjQuestionTable.getRowCount() - 1) {
            _listQuestion = QuestionDao.getAllQuestions();
            mjQuestionIDTextField.setText(String.valueOf(_listQuestion.get(_listQuestion.size() - 1).getCauHoi() + 1 + _addQuestionCount));
        }
    }//GEN-LAST:event_mjQuestionTableMouseClicked

    private void ctBranchComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ctBranchComboBox1ItemStateChanged
        int index = ctBranchComboBox1.getSelectedIndex();
        JDBC_Connection.port = index == 0 ? "1434" : "1435";
        JDBC_Connection.user = index == _branchIndex ? _userName : JDBC_Connection.htknUser;
        JDBC_Connection.password = index == _branchIndex ? _pass : JDBC_Connection.htknPass;
        
        loadDepartBranchComboBox();
        loadDepartmentTable(ctDepartmentTable);
    }//GEN-LAST:event_ctBranchComboBox1ItemStateChanged

    private void ctBranchComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ctBranchComboBox2ItemStateChanged
        int index = ctBranchComboBox2.getSelectedIndex();
        JDBC_Connection.port = index == 0 ? "1434" : "1435";
        JDBC_Connection.user = index == _branchIndex ? _userName : JDBC_Connection.htknUser;
        JDBC_Connection.password = index == _branchIndex ? _pass : JDBC_Connection.htknPass;
        
        loadClassTable(ctClassTable);
    }//GEN-LAST:event_ctBranchComboBox2ItemStateChanged

    private void ctBranchComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ctBranchComboBox3ItemStateChanged
        int index = ctBranchComboBox3.getSelectedIndex();
        JDBC_Connection.port = index == 0 ? "1434" : "1435";
        JDBC_Connection.user = index == _branchIndex ? _userName : JDBC_Connection.htknUser;
        JDBC_Connection.password = index == _branchIndex ? _pass : JDBC_Connection.htknPass;
        
        loadTeacherDepartCbx();
        loadTeacherTable(ctTeacherTable);
    }//GEN-LAST:event_ctBranchComboBox3ItemStateChanged

    private void ctBranchComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ctBranchComboBox4ItemStateChanged
        int index = ctBranchComboBox4.getSelectedIndex();
        JDBC_Connection.port = index == 0 ? "1434" : "1435";
        JDBC_Connection.user = index == _branchIndex ? _userName : JDBC_Connection.htknUser;
        JDBC_Connection.password = index == _branchIndex ? _pass : JDBC_Connection.htknPass;
        
        loadStudentClassCbx();
        loadStudentTable(ctStudentTable);
    }//GEN-LAST:event_ctBranchComboBox4ItemStateChanged

    private void ctBranchComboBox5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ctBranchComboBox5ItemStateChanged
        int index = ctBranchComboBox5.getSelectedIndex();
        JDBC_Connection.port = index == 0 ? "1434" : "1435";
        JDBC_Connection.user = index == _branchIndex ? _userName : JDBC_Connection.htknUser;
        JDBC_Connection.password = index == _branchIndex ? _pass : JDBC_Connection.htknPass;
        
        loadSubjectTable(ctSubjectTable);
    }//GEN-LAST:event_ctBranchComboBox5ItemStateChanged

    private void rpStudentIDTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rpStudentIDTextField1KeyTyped
        char keyChar = evt.getKeyChar();
        if (Character.isLowerCase(keyChar)) {
            evt.setKeyChar(Character.toUpperCase(keyChar));
        }
    }//GEN-LAST:event_rpStudentIDTextField1KeyTyped

    private void mjRegisterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mjRegisterButtonActionPerformed
        majorQuestionPanel.setVisible(false);
        majorRegisterPanel.setVisible(true);
        
        loadClassComboBox(mjRegisterClassIDComboBox, true);
        loadSubjectComboBox(mjRegisterSubjectIDComboBox, true);
        setRegisterComponentsEnable(false);
        loadRegisterTable(mjRegisterTable);
    }//GEN-LAST:event_mjRegisterButtonActionPerformed

    private void mjAddButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mjAddButton2ActionPerformed
        int index = mjRegisterTable.getRowCount() - 1;
        boolean pass = mjRegisterTable.getValueAt(index, 0) != null;
        DefaultTableModel model = (DefaultTableModel) mjRegisterTable.getModel();
        
        if (pass) {
            model.addRow(new Object[]{null, null, null, null, null, null, null, null});
            index = mjRegisterTable.getRowCount() - 1;
            mjRegisterTable.changeSelection(index, 0, false, false);
            mjRegisterTableMouseClicked(null);
        } else {
            Register register = getRegisterInput();
            if (checkRegister(register, false)) {
                model.removeRow(index);
                model.addRow(register.toArray());
                model.addRow(new Object[]{null, null, null, null, null, null, null, null});
                index = mjRegisterTable.getRowCount() - 1;
                mjRegisterTable.changeSelection(index, 0, false, false);
                mjRegisterTableMouseClicked(null);
                _addRegisterCount++;
            }
        }
    }//GEN-LAST:event_mjAddButton2ActionPerformed

    private void mjEditButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mjEditButton2ActionPerformed
        int selectedRow = mjRegisterTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn môn thi muốn sửa", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            Register register = getRegisterInput();
            UndoRegister undo = new UndoRegister(2, RegisterDao.getRegisterById(register.getMamh(), register.getMalop(), register.getLan()));
            if (checkRegister(register, true)) {
                if (RegisterDao.updateRegister(register)) {
                    _undoRegister.push(undo);
                    JOptionPane.showMessageDialog(this, "Hiệu chỉnh thành công");
                    loadRegisterTable(mjRegisterTable);
                } else {
                    JOptionPane.showMessageDialog(this, "Hiệu chỉnh thất bại!\n" + message, "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_mjEditButton2ActionPerformed

    private void mjSaveButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mjSaveButton2ActionPerformed
        int rowCount = mjRegisterTable.getRowCount() - 1;
        boolean check = true;
        for (int i = _addRegisterCount; i > 0; i--) {
            Register register = new Register();
            int index = rowCount - i;
            register.setMagv(mjRegisterTable.getValueAt(index, 0).toString());
            register.setMamh(mjRegisterTable.getValueAt(index, 1).toString());
            register.setMalop(mjRegisterTable.getValueAt(index, 2).toString());
            register.setTrinhDo(mjRegisterTable.getValueAt(index, 3).toString());
            register.setNgayThi(DateHelper.toString(DateHelper.toDate(mjRegisterTable.getValueAt(index, 4).toString())));
            register.setLan(Integer.valueOf(mjRegisterTable.getValueAt(index, 5).toString()));
            register.setSoCauThi(Integer.valueOf(mjRegisterTable.getValueAt(index, 6).toString()));
            register.setThoiGian(Integer.valueOf(mjRegisterTable.getValueAt(index, 7).toString()));
            UndoRegister undo = new UndoRegister(1, register);
            
            if (!RegisterDao.addRegister(register)) {
                check = false;
                break;
            }
            _undoRegister.push(undo);
        }
        
        if (check) {
            JOptionPane.showMessageDialog(this, "Thêm thành công !");
            setRegisterInput(null);
            _addRegisterCount = 0;
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại !", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        
        mjReloadButton2ActionPerformed(evt);
    }//GEN-LAST:event_mjSaveButton2ActionPerformed

    private void mjRemoveButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mjRemoveButton2ActionPerformed
        int selectedRow = mjRegisterTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn môn thi muốn xoá", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            Object[] option = {"Có", "Không"};
            int confirm = JOptionPane.showOptionDialog(this, "Bạn có thật sự muốn xóa?", "Xóa",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[1]);
            if (confirm == JOptionPane.YES_OPTION) {
                String subjectID = mjRegisterTable.getValueAt(selectedRow, 1).toString();
                String classID = mjRegisterTable.getValueAt(selectedRow, 2).toString();
                int examTime = Integer.valueOf(mjRegisterTable.getValueAt(selectedRow, 5).toString());
                Register register = RegisterDao.getRegisterById(subjectID, classID, examTime);
                if (register == null) {
                    _addRegisterCount--;
                    ((DefaultTableModel) mjRegisterTable.getModel()).removeRow(selectedRow);
                } else {
                    UndoRegister undo = new UndoRegister(3, register);
                    if (RegisterDao.deleteRegister(subjectID, classID, examTime)) {
                        _undoRegister.push(undo);
                        JOptionPane.showMessageDialog(this, "Xóa thành công");
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa thất bại!\n" + message, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                    loadRegisterTable(mjRegisterTable);
                }
                setRegisterInput(null);
            }
        }
    }//GEN-LAST:event_mjRemoveButton2ActionPerformed

    private void mjUndoButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mjUndoButton2ActionPerformed
        if (_undoRegister.isEmpty()) {
            return;
        }
        
        UndoRegister undo = _undoRegister.pop();
        boolean pass = false;
        String mode = "";
        switch (undo.getMode()) {
            case 1: {
                if (RegisterDao.deleteRegister(undo.getRegister().getMamh(), undo.getRegister().getMalop(), undo.getRegister().getLan())) {
                    pass = true;
                    mode = "thêm";
                }
                break;
            }
            case 2: {
                if (RegisterDao.updateRegister(undo.getRegister())) {
                    pass = true;
                    mode = "sửa";
                }
                break;
            }
            case 3: {
                if (RegisterDao.addRegister(undo.getRegister())) {
                    pass = true;
                    mode = "xóa";
                }
                break;
            }
        }
        
        if (pass) {
            JOptionPane.showMessageDialog(this, "Hoàn tác " + mode + " thành công với môn thi có"
                    + "\nMã môn: " + undo.getRegister().getMamh()
                    + "\nMã lớp: " + undo.getRegister().getMalop()
                    + "\nLần thi: " + undo.getRegister().getLan() + " !");
            loadRegisterTable(mjRegisterTable);
            setRegisterInput(null);
        } else {
            JOptionPane.showMessageDialog(this, "Hoàn tác thất bại với môn thi có"
                    + "\nMã môn: " + undo.getRegister().getMamh()
                    + "\nMã lớp: " + undo.getRegister().getMalop()
                    + "\nLần thi: " + undo.getRegister().getLan() + " !", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_mjUndoButton2ActionPerformed

    private void mjReloadButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mjReloadButton2ActionPerformed
        loadRegisterTable(mjRegisterTable);
        setRegisterComponentsEnable(false);
    }//GEN-LAST:event_mjReloadButton2ActionPerformed

    private void mjRegisterTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mjRegisterTableMouseClicked
        int index = mjRegisterTable.getSelectedRow();
        
        Object subjectID = mjRegisterTable.getValueAt(index, 1);
        Object classID = mjRegisterTable.getValueAt(index, 2);
        Object examTime = mjRegisterTable.getValueAt(index, 5);
        
        boolean isEmpty = subjectID == null;
        Register register = isEmpty ? null : RegisterDao.getRegisterById(subjectID.toString(), classID.toString(), Integer.valueOf(examTime.toString()));
        
        setRegisterComponentsEnable(true);
        if (_role.equals("COSO") || _role.equals("GIANGVIEN")) {
            mjEditButton2.setEnabled(!isEmpty);
            mjRemoveButton2.setEnabled(!isEmpty);
            
            if (_role.equals("GIANGVIEN") && !isEmpty) {
                boolean pass = mjRegisterTable.getValueAt(index, 0).toString().equals(_userID);
                mjEditButton2.setEnabled(pass);
                mjRemoveButton2.setEnabled(pass);
            }
        }
        
        setRegisterInput(register);
    }//GEN-LAST:event_mjRegisterTableMouseClicked

    private void mjRegisterExamTotalTimeTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mjRegisterExamTotalTimeTextFieldKeyTyped
        char c = evt.getKeyChar();
        if (!((c >= '0') && (c <= '9')) || mjRegisterExamTotalTimeTextField.getText().length() >= 2) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_mjRegisterExamTotalTimeTextFieldKeyTyped

    private void mjRegisterQuestionCountTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mjRegisterQuestionCountTextFieldKeyTyped
        char c = evt.getKeyChar();
        if (!((c >= '0') && (c <= '9')) || mjRegisterQuestionCountTextField.getText().length() >= 3) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_mjRegisterQuestionCountTextFieldKeyTyped

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
            java.util.logging.Logger.getLogger(MainFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setLocationRelativeTo(null);
                mainFrame.setVisible(true);
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
    private javax.swing.JComboBox<String> ctSearchComboBox1;
    private javax.swing.JComboBox<String> ctSearchComboBox2;
    private javax.swing.JComboBox<String> ctSearchComboBox3;
    private javax.swing.JComboBox<String> ctSearchComboBox4;
    private javax.swing.JComboBox<String> ctSearchComboBox5;
    private javax.swing.JTextField ctSearchTextField1;
    private javax.swing.JTextField ctSearchTextField2;
    private javax.swing.JTextField ctSearchTextField3;
    private javax.swing.JTextField ctSearchTextField4;
    private javax.swing.JTextField ctSearchTextField5;
    private javax.swing.JTextField ctStudentAddressTextField;
    private com.toedter.calendar.JDateChooser ctStudentBirthDayDateChooser;
    private javax.swing.JButton ctStudentButton;
    private javax.swing.JComboBox ctStudentClassComboBox;
    private javax.swing.JTextField ctStudentFirstNameTextField;
    private javax.swing.JTextField ctStudentIDTextField;
    private javax.swing.JTextField ctStudentLastNameTextField;
    private javax.swing.JLabel ctStudentPassLabel;
    private javax.swing.JTextField ctStudentPassTextField;
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
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
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
    private javax.swing.JPanel majorQuestionPanel;
    private javax.swing.JPanel majorRegisterPanel;
    private javax.swing.JButton mjAddButton1;
    private javax.swing.JButton mjAddButton2;
    private javax.swing.JButton mjEditButton1;
    private javax.swing.JButton mjEditButton2;
    private javax.swing.JTextField mjQuestionATextField;
    private javax.swing.JComboBox mjQuestionAnswerComboBox;
    private javax.swing.JTextField mjQuestionBTextField;
    private javax.swing.JTextField mjQuestionCTextField;
    private javax.swing.JTextArea mjQuestionContentTextArea;
    private javax.swing.JTextField mjQuestionDTextField;
    private javax.swing.JTextField mjQuestionIDTextField;
    private javax.swing.JComboBox mjQuestionLevelComboBox;
    private javax.swing.JButton mjQuestionManagementButton;
    private javax.swing.JComboBox mjQuestionSubjectIDComboBox;
    private javax.swing.JTable mjQuestionTable;
    private javax.swing.JButton mjRegisterButton;
    private javax.swing.JComboBox<String> mjRegisterClassIDComboBox;
    private com.toedter.calendar.JDateChooser mjRegisterExamDateChooser;
    private javax.swing.JComboBox<String> mjRegisterExamLevelComboBox;
    private javax.swing.JComboBox<String> mjRegisterExamTimeComboBox;
    private javax.swing.JTextField mjRegisterExamTotalTimeTextField;
    private javax.swing.JTextField mjRegisterQuestionCountTextField;
    private javax.swing.JComboBox<String> mjRegisterSubjectIDComboBox;
    private javax.swing.JTable mjRegisterTable;
    private javax.swing.JButton mjReloadButton1;
    private javax.swing.JButton mjReloadButton2;
    private javax.swing.JButton mjRemoveButton1;
    private javax.swing.JButton mjRemoveButton2;
    private javax.swing.JButton mjSaveButton1;
    private javax.swing.JButton mjSaveButton2;
    private javax.swing.JComboBox<String> mjSearchComboBox1;
    private javax.swing.JComboBox<String> mjSearchComboBox2;
    private javax.swing.JTextField mjSearchTextField1;
    private javax.swing.JTextField mjSearchTextField2;
    private javax.swing.JButton mjTestExamButton;
    private javax.swing.JButton mjUndoButton1;
    private javax.swing.JButton mjUndoButton2;
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
    private javax.swing.JPanel rpResultPane3;
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
