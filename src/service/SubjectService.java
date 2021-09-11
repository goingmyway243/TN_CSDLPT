/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import dao.SubjectDao;
import java.util.List;
import model.Subject;

/**
 *
 * @author vivau
 */
public class SubjectService {
    
    private SubjectDao subjectDao;

    public SubjectService() {
        subjectDao = new SubjectDao();
    }
    
    public List<Subject> getAllSubjects() {
        //
        return subjectDao.getAllSubjects();
    }
    
    public void addSubject(Subject subject) {
        subjectDao.addSubject(subject);
    }
    
    public void deleteSubject(String mamh) {
        subjectDao.deleteSubject(mamh);
    }
    
    public Subject getSubjectById(String mamh) {
        return subjectDao.getSubjectById(mamh);
    }
    
    public void updateSubject(Subject subject) {
        subjectDao.updateSubject(subject);
    }
}
