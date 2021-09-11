/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import dao.TeacherDao;
import java.util.List;
import model.Teacher;

/**
 *
 * @author vivau
 */
public class TeacherService {
    private TeacherDao teacherDao;

    public TeacherService() {
        teacherDao = new TeacherDao();
    }
    
    public List<Teacher> getAllTeachers() {
        //
        return teacherDao.getAllTeachers();
    }
    
    public void addTeacher(Teacher teacher) {
        teacherDao.addTeacher(teacher);
    }
    
    public void deleteTeacher(String magv) {
        teacherDao.deleteTeacher(magv);
    }
    
    public Teacher getTeacherById(String magv) {
        return teacherDao.getTeacherById(magv);
    }
    
    public void updateTeacher(Teacher teacher) {
        teacherDao.updateTeacher(teacher);
    }
}
