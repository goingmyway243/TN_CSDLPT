/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import helper.JDBC_Connection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
        Connection connection = JDBC_Connection.getJDBCConnection();
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
        }
        return questions;
    }

    public List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "SELECT * FROM dbo.[BODE]";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
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
        }
        return questions;
    }

    public Question getQuestionById(int cauhoi) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "SELECT * FROM dbo.[BODE] WHERE CAUHOI = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, cauhoi);
            ResultSet rs = preparedStatement.executeQuery();

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
        }
        return null;
    }

    public void addQuestion(Question question) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "INSERT INTO dbo.[BODE] (CAUHOI, MAMH, TRINHDO, NOIDUNG, A, B, C, D, DAP_AN, MAGV) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, question.getCauHoi());
            preparedStatement.setString(2, question.getMamh());
            preparedStatement.setString(3, question.getTrinhDo());
            preparedStatement.setString(4, question.getNoiDung());
            preparedStatement.setString(5, question.getA());
            preparedStatement.setString(6, question.getB());
            preparedStatement.setString(7, question.getC());
            preparedStatement.setString(8, question.getD());
            preparedStatement.setString(9, question.getDapAn());
            preparedStatement.setString(10, question.getMagv());
            int executeUpdate = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(QuestionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateQuestion(Question question) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "UPDATE dbo.[BODE] SET MAMH = ?, TRINHDO = ?, NOIDUNG = ?, A = ?, B = ?, C = ?, D = ?, DAP_AN = ?, MAGV = ? WHERE CAUHOI = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, question.getMamh());
            preparedStatement.setString(2, question.getTrinhDo());
            preparedStatement.setString(3, question.getNoiDung());
            preparedStatement.setString(4, question.getA());
            preparedStatement.setString(5, question.getB());
            preparedStatement.setString(6, question.getC());
            preparedStatement.setString(7, question.getD());
            preparedStatement.setString(8, question.getDapAn());
            preparedStatement.setString(9, question.getMagv());
            preparedStatement.setInt(10, question.getCauHoi());
            int executeUpdate = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(QuestionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteQuestion(int cauhoi) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "DELETE FROM dbo.[BODE] WHERE CAUHOI = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, cauhoi);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(QuestionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Question> test(int soCau, String mamh, String trinhDo) {
        List<Question> questions = new ArrayList<>();
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "{CALL dbo.SP_Test(?, ?, ?)}";

        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setInt(1, soCau);
            callableStatement.setString(2, mamh);
            callableStatement.setString(3, trinhDo);

            callableStatement.execute();
            callableStatement.getMoreResults();
            ResultSet rs = callableStatement.getResultSet();
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
        }
        return questions;
    }

    public static void main(String[] args) {
        String magv = "AT301   ";
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "{CALL dbo.SP_CHECK(?)}";

        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setString(1, magv);
            boolean rs = callableStatement.execute();
        } catch (SQLException ex) {
//            Logger.getLogger(QuestionDao.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());

        }

    }

}
