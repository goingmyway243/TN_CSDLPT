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
import model.Student;
import model.Teacher;

/**
 *
 * @author vivau
 */
public class TeacherDao {

    public List<Teacher> getAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "SELECT * FROM dbo.[GIAOVIEN]";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Teacher teacher = new Teacher();

                teacher.setMagv(rs.getString("MAGV"));
                teacher.setHo(rs.getString("HO"));
                teacher.setTen(rs.getString("TEN"));
                teacher.setHocVi(rs.getString("HOCVI"));
                teacher.setMakh(rs.getString("MAKH"));

                teachers.add(teacher);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TeacherDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return teachers;
    }

    public Teacher getTeacherById(String magv) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "SELECT * FROM dbo.[GIAOVIEN] WHERE MAGV = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, magv);
            ResultSet rs = preparedStatement.executeQuery();

            rs.next();
            Teacher teacher = new Teacher();
            teacher.setMagv(magv);
            teacher.setHo(rs.getString("HO"));
            teacher.setTen(rs.getString("TEN"));
            teacher.setHocVi(rs.getString("HOCVI"));
            teacher.setMakh(rs.getString("MAKH"));
            return teacher;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public void addTeacher(Teacher teacher) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "INSERT INTO dbo.[GIAOVIEN] (MAGV, HO, TEN, HOCVI, MAKH) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, teacher.getMagv());
            preparedStatement.setString(2, teacher.getHo());
            preparedStatement.setString(3, teacher.getTen());
            preparedStatement.setString(4, teacher.getHocVi());
            preparedStatement.setString(5, teacher.getMakh());
            int executeUpdate = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TeacherDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateTeacher(Teacher teacher) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "UPDATE dbo.[GIAOVIEN] SET HO = ?, TEN = ?, HOCVI = ?, MAKH = ? WHERE MAGV = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, teacher.getHo());
            preparedStatement.setString(2, teacher.getTen());
            preparedStatement.setString(3, teacher.getHocVi());
            preparedStatement.setString(4, teacher.getMakh());
            preparedStatement.setString(5, teacher.getMagv());
            int executeUpdate = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TeacherDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteTeacher(String magv) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "DELETE FROM dbo.[GIAOVIEN] WHERE MAGV = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, magv);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TeacherDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   

}
