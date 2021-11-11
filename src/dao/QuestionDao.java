/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import helper.JDBC_Connection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Question;

/**
 *
 * @author vivau
 */
public class QuestionDao {

    public List<Question> getExam(int soCau, String mamh, String trinhDo) {
        List<Question> questions = new ArrayList<>();
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL dbo.SP_TaoDeThi(?, ?, ?)}";

        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setInt(1, soCau);
            callableStatement.setString(2, mamh);
            callableStatement.setString(3, trinhDo);
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                Question question = new Question();

                question.setCauHoi(rs.getInt("CAUHOI"));
                question.setMamh(rs.getString("MAMH"));
                question.setTrinhDo(rs.getString("TRINHDO"));
                question.setNoiDung(rs.getString("NOIDUNG"));
                question.setA(rs.getString("A"));
                question.setB(rs.getString("B"));
                question.setC(rs.getString("C"));
                question.setD(rs.getString("D"));
                question.setDapAn(rs.getString("DAP_AN"));
                question.setMagv(rs.getString("MAGV"));

                questions.add(question);
            }
        } catch (SQLException ex) {
            Logger.getLogger(QuestionDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return questions;
    }

    public List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Question_GetAll}";

        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                Question question = new Question();

                question.setCauHoi(rs.getInt("CAUHOI"));
                question.setMamh(rs.getString("MAMH"));
                question.setTrinhDo(rs.getString("TRINHDO"));
                question.setNoiDung(rs.getString("NOIDUNG"));
                question.setA(rs.getString("A"));
                question.setB(rs.getString("B"));
                question.setC(rs.getString("C"));
                question.setD(rs.getString("D"));
                question.setDapAn(rs.getString("DAP_AN"));
                question.setMagv(rs.getString("MAGV"));

                questions.add(question);
            }
        } catch (SQLException ex) {
            Logger.getLogger(QuestionDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return questions;
    }

    public Question getQuestionById(int cauhoi) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Question_GetById(?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setInt(1, cauhoi);
            ResultSet rs = callableStatement.executeQuery();

            rs.next();
            Question question = new Question();
            question.setCauHoi(cauhoi);
            question.setMamh(rs.getString("MAMH"));
            question.setTrinhDo(rs.getString("TRINHDO"));
            question.setNoiDung(rs.getString("NOIDUNG"));
            question.setA(rs.getString("A"));
            question.setB(rs.getString("B"));
            question.setC(rs.getString("C"));
            question.setD(rs.getString("D"));
            question.setDapAn(rs.getString("DAP_AN"));
            question.setMagv(rs.getString("MAGV"));

            return question;
        } catch (SQLException ex) {
            Logger.getLogger(QuestionDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public boolean addQuestion(Question question) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Question_Add(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setInt(1, question.getCauHoi());
            callableStatement.setString(2, question.getMamh());
            callableStatement.setString(3, question.getTrinhDo());
            callableStatement.setString(4, question.getNoiDung());
            callableStatement.setString(5, question.getA());
            callableStatement.setString(6, question.getB());
            callableStatement.setString(7, question.getC());
            callableStatement.setString(8, question.getD());
            callableStatement.setString(9, question.getDapAn());
            callableStatement.setString(10, question.getMagv());
            int executeUpdate = callableStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(QuestionDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean updateQuestion(Question question) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Question_Update(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setInt(1, question.getCauHoi());
            callableStatement.setString(2, question.getMamh());
            callableStatement.setString(3, question.getTrinhDo());
            callableStatement.setString(4, question.getNoiDung());
            callableStatement.setString(5, question.getA());
            callableStatement.setString(6, question.getB());
            callableStatement.setString(7, question.getC());
            callableStatement.setString(8, question.getD());
            callableStatement.setString(9, question.getDapAn());
            callableStatement.setString(10, question.getMagv());
            int executeUpdate = callableStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(QuestionDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean deleteQuestion(int cauhoi) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Question_Delete(?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setInt(1, cauhoi);
            callableStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(QuestionDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public int checkQuestion(int Cauhoi) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{? = CALL SP_Question_Check(?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);

            callableStatement.registerOutParameter(1, java.sql.Types.INTEGER);
            callableStatement.setInt(2, Cauhoi);

            callableStatement.execute();

            return callableStatement.getInt(1);

        } catch (SQLException ex) {
            Logger.getLogger(RegisterDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;

    }

    public static void main(String[] args) {


        
    }

}
