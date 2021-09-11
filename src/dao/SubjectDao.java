/*
 * To change this license header, choose License Header     
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
import model.Subject;

/**
 *
 * @author vivau
 */
public class SubjectDao {

    public List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "SELECT * FROM dbo.[MONHOC]";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Subject subject = new Subject();

                subject.setMamh(rs.getString("MAMH"));
                subject.setTenmh(rs.getString("TENMH"));

                subjects.add(subject);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return subjects;
    }

    public Subject getSubjectById(String mamh) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "SELECT * FROM dbo.[MONHOC] WHERE MAMH = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, mamh);
            ResultSet rs = preparedStatement.executeQuery();

            rs.next();
            Subject subject = new Subject();
            subject.setMamh(mamh);
            subject.setTenmh(rs.getString("TENMH"));
            return subject;
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void addSubject(Subject subject) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "INSERT INTO dbo.[MONHOC] (MAMH, TENMH) VALUES (?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, subject.getMamh());
            preparedStatement.setString(2, subject.getTenmh());
            int executeUpdate = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateSubject(Subject subject) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "UPDATE dbo.[MONHOC] SET TENMH = ? WHERE MAMH = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, subject.getTenmh());
            preparedStatement.setString(2, subject.getMamh());
            int executeUpdate = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteSubject(String mamh) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "DELETE FROM dbo.[MONHOC] WHERE MAMH = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, mamh);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
