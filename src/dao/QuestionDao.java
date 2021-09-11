/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

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

    public List<Question> getAllExams() {
        List<Question> exams = new ArrayList<>();
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "SELECT * FROM dbo.[BODE]";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Question exam = new Question();

                exam.setCauHoi(rs.getInt("CAUHOI"));
                exam.setMamh(rs.getString("MAMH"));
                exam.setTrinhDo(rs.getString("TRINHDO"));
                exam.setNoiDung(rs.getString("NOIDUNG"));
                exam.setA(rs.getString("A"));
                exam.setB(rs.getString("B"));
                exam.setC(rs.getString("C"));
                exam.setD(rs.getString("D"));
                exam.setDapAn(rs.getString("DAP_AN"));
                exam.setMagv(rs.getString("MAGV"));

                exams.add(exam);
            }
        } catch (SQLException ex) {
            Logger.getLogger(QuestionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return exams;
    }

    public Question getExamById(int cauhoi) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "SELECT * FROM dbo.[BODE] WHERE CAUHOI = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, cauhoi);
            ResultSet rs = preparedStatement.executeQuery();

            rs.next();
            Question exam = new Question();
            exam.setCauHoi(cauhoi);
            exam.setMamh(rs.getString("MAMH"));
            exam.setTrinhDo(rs.getString("TRINHDO"));
            exam.setNoiDung(rs.getString("NOIDUNG"));
            exam.setA(rs.getString("A"));
            exam.setB(rs.getString("B"));
            exam.setC(rs.getString("C"));
            exam.setD(rs.getString("D"));
            exam.setDapAn(rs.getString("DAP_AN"));
            exam.setMagv(rs.getString("MAGV"));
            
            return exam;
        } catch (SQLException ex) {
            Logger.getLogger(QuestionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void addExam(Question exam) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "INSERT INTO dbo.[BODE] (CAUHOI, MAMH, TRINHDO, NOIDUNG, A, B, C, D, DAP_AN, MAGV) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, exam.getCauHoi());
            preparedStatement.setString(2, exam.getMamh());
            preparedStatement.setString(3, exam.getTrinhDo());
            preparedStatement.setString(4, exam.getNoiDung());
            preparedStatement.setString(5, exam.getA());
            preparedStatement.setString(6, exam.getB());
            preparedStatement.setString(7, exam.getC());
            preparedStatement.setString(8, exam.getD());
            preparedStatement.setString(9, exam.getDapAn());
            preparedStatement.setString(10, exam.getMagv());
            int executeUpdate = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(QuestionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateExam(Question exam) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "UPDATE dbo.[BODE] SET MAMH = ?, TRINHDO = ?, NOIDUNG = ?, A = ?, B = ?, C = ?, D = ?, DAP_AN = ?, MAGV = ? WHERE CAUHOI = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, exam.getMamh());
            preparedStatement.setString(2, exam.getTrinhDo());
            preparedStatement.setString(3, exam.getNoiDung());
            preparedStatement.setString(4, exam.getA());
            preparedStatement.setString(5, exam.getB());
            preparedStatement.setString(6, exam.getC());
            preparedStatement.setString(7, exam.getD());
            preparedStatement.setString(8, exam.getDapAn());
            preparedStatement.setString(9, exam.getMagv());
            preparedStatement.setInt(10, exam.getCauHoi());
            int executeUpdate = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(QuestionDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteExam(int cauhoi) {
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

}
