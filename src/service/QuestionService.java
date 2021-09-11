/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import dao.QuestionDao;
import java.util.List;
import model.Question;

/**
 *
 * @author vivau
 */
public class QuestionService {
    
    private QuestionDao examDao;

    public QuestionService() {
        examDao = new QuestionDao();
    }
    
    public List<Question> getAllExams() {
        //
        return examDao.getAllExams();
    }
    
    public void addExam(Question exam) {
        examDao.addExam(exam);
    }
    
    public void deleteExam(int cauhoi) {
        examDao.deleteExam(cauhoi);
    }
    
    public Question getExamById(int cauhoi) {
        return examDao.getExamById(cauhoi);
    }
    
    public void updateExam(Question exam) {
        examDao.updateExam(exam);
    }
    
    
    
}
