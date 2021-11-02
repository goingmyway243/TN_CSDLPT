/*
 * To change this license header, choose License Header     
 */
package dao;

import helper.JDBC_Connection;
import java.sql.Connection;
import java.sql.CallableStatement;
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
        String sql = "{CALL SP_Subject_GetAll}";
        try {
            CallableStatement preparedStatement = connection.prepareCall(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Subject subject = new Subject();

                subject.setMamh(rs.getString("MAMH"));
                subject.setTenmh(rs.getString("TENMH"));

                subjects.add(subject);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return subjects;
    }

    public Subject getSubjectById(String mamh) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "{CALL SP_Subject_GetById(?)}";
        try {
            CallableStatement preparedStatement = connection.prepareCall(sql);
            preparedStatement.setString(1, mamh);
            ResultSet rs = preparedStatement.executeQuery();

            rs.next();
            Subject subject = new Subject();

            subject.setMamh(mamh);
            subject.setTenmh(rs.getString("TENMH"));
            return subject;
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }

    public boolean addSubject(Subject subject) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "{CALL SP_Subject_Add(?, ?)}";
        try {
            CallableStatement preparedStatement = connection.prepareCall(sql);
            preparedStatement.setString(1, subject.getMamh());
            preparedStatement.setString(2, subject.getTenmh());
            int executeUpdate = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean updateSubject(Subject subject) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "{CALL SP_Subject_Update(?, ?)}";
        try {
            CallableStatement preparedStatement = connection.prepareCall(sql);
            preparedStatement.setString(1, subject.getMamh());
            preparedStatement.setString(2, subject.getTenmh());

            int executeUpdate = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean deleteSubject(String mamh) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "{CALL SP_Subject_Delete(?)}";
        try {
            CallableStatement preparedStatement = connection.prepareCall(sql);
            preparedStatement.setString(1, mamh);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public int checkSubject(String maMh) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "{? = CALL SP_Subject_Check(?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);

            callableStatement.registerOutParameter(1, java.sql.Types.INTEGER);
            callableStatement.setString(2, maMh);

            callableStatement.execute();

            return callableStatement.getInt(1);

        } catch (SQLException ex) {
            Logger.getLogger(RegisterDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;

    }

    public static void main(String[] args) {
        SubjectDao subjectDao = new SubjectDao();
        List<Subject> subjects = subjectDao.getAllSubjects();
//        for (Subject subject : subjects) {
//            System.out.println(subject.toString());
//        }

//        System.out.println(subjectDao.getSubjectById("99999"));
        Subject subject = new Subject("99998", "Nguyeenx Phamj Nhatj Minh");
//        subjectDao.addSubject(subject);
//        subjectDao.updateSubject(subject);
    subjectDao.deleteSubject("99999");

    }

}
