/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import helper.JDBC_Connection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Classroom;

/**
 *
 * @author vivau
 */
public class ClassroomDao {

    public List<Classroom> getAllClassrooms() {
        List<Classroom> classrooms = new ArrayList<>();
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "SELECT * FROM dbo.[LOP]";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Classroom classroom = new Classroom();

                classroom.setMaLop(rs.getString("MALOP"));
                classroom.setTenLop(rs.getString("TENLOP"));
                classroom.setMakh(rs.getString("MAKH"));

                classrooms.add(classroom);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClassroomDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return classrooms;
    }

    public Classroom getClassroomById(String malop) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "SELECT * FROM dbo.[LOP] WHERE MALOP = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, malop);
            ResultSet rs = preparedStatement.executeQuery();

            rs.next();
            Classroom classroom = new Classroom();
            classroom.setMaLop(malop);
            classroom.setTenLop(rs.getString("TENLOP"));
            classroom.setMakh(rs.getString("MAKH"));
            return classroom;
        } catch (SQLException ex) {
            Logger.getLogger(ClassroomDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void addClassroom(Classroom classroom) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "INSERT INTO dbo.[LOP] (MALOP, TENLOP, MAKH) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, classroom.getMaLop());
            preparedStatement.setString(2, classroom.getTenLop());
            preparedStatement.setString(3, classroom.getMakh());
            int executeUpdate = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ClassroomDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateClassroom(Classroom classroom) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "UPDATE dbo.[LOP] SET TENLOP = ?, MAKH = ? WHERE MALOP = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, classroom.getTenLop());
            preparedStatement.setString(2, classroom.getMakh());
            preparedStatement.setString(3, classroom.getMaLop());
            int executeUpdate = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ClassroomDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteClassroom(String malop) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "DELETE FROM dbo.[LOP] WHERE MALOP = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, malop);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ClassroomDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
