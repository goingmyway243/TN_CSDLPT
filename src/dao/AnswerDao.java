/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import helper.JDBC_Connection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Answer;

/**
 *
 * @author vivau
 */
public class AnswerDao {
    
    public List<Answer> getAllAnswers() {
        List<Answer> answers = new ArrayList<>();
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Answer_GetAll}";

        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                Answer answer = new Answer();

                answer.setBaiThi(rs.getInt("BAITHI"));
                answer.setStt(rs.getInt("STT"));
                answer.setCauHoi(rs.getInt("CAUHOI"));
                answer.setDaChon(rs.getString("DACHON"));

                answers.add(answer);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AnswerDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return answers;
    }

    public Answer getAnswerById(int baiThi, int stt) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Answer_GetById(?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setInt(1, baiThi);
            callableStatement.setInt(2, stt);
            ResultSet rs = callableStatement.executeQuery();

            rs.next();
            Answer answer = new Answer();
            answer.setBaiThi(baiThi);
            answer.setStt(stt);
            answer.setCauHoi(rs.getInt("CAUHOI"));
            answer.setDaChon(rs.getString("DACHON"));
            return answer;
        } catch (SQLException ex) {
            Logger.getLogger(AnswerDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }

    public boolean addAnswer(Answer answer) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL dbo.SP_Answer_Add (?, ?, ?, ?)}";
        try {

            CallableStatement callableStatement = connection.prepareCall(sql);

            callableStatement.setInt(1, answer.getBaiThi());
            callableStatement.setInt(2, answer.getStt());
            callableStatement.setInt(3, answer.getCauHoi());
            callableStatement.setString(4, answer.getDaChon());
            int executeUpdate = callableStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AnswerDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean updateAnswer(Answer answer) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Answer_Update(?, ?, ?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setInt(1, answer.getBaiThi());
            callableStatement.setInt(2, answer.getStt());
            callableStatement.setInt(3, answer.getCauHoi());
            callableStatement.setString(4, answer.getDaChon());
            int executeUpdate = callableStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AnswerDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean deleteAnswer(int baiThi, int stt) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Answer_Delete(?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setInt(1, baiThi);
            callableStatement.setInt(2, stt);
            callableStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AnswerDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public static void main(String[] args) {
        
        AnswerDao answerDao = new AnswerDao();
        
//        List<Answer> answers = answerDao.getAllAnswers();
//        for (Answer answer : answers) {
//            System.out.println(answer.toString());
//        }
        
        answerDao.deleteAnswer(1, 7);

    }
    
}
