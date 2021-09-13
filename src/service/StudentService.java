/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import dao.StudentDao;
import java.util.List;
import model.Student;

/**
 *
 * @author vivau
 */
public class StudentService {
    
    private StudentDao studentDao;

    public StudentService() {
        studentDao = new StudentDao();
    }
    
    public List<Student> getAllStudents() {
        //
        return studentDao.getAllStudents();
    }
    
    public List<Student> getStudentClassroom(String maLop) {
        //
        return studentDao.getStudentClassroom(maLop);
    }
    
    public void addStudent(Student student) {
        studentDao.addStudent(student);
    }
    
    public void deleteStudent(String masv) {
        studentDao.deleteStudent(masv);
    }
    
    public Student getStudentById(String masv) {
        return studentDao.getStudentById(masv);
    }
    
    public void updateStudent(Student student) {
        studentDao.updateStudent(student);
    }
    
}
