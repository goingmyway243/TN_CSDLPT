/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import model.Teacher;

/**
 *
 * @author vivau
 */
public class TeacherDao {
    
    public static List<Teacher> getAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL dbo.SP_Teacher_GetAll}";
        
        try {
            CallableStatement preparedStatement = connection.prepareCall(sql);
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
            System.out.println(ex.getMessage());
            return null;
        }
        return teachers;
    }
    
    public static Teacher getTeacherById(String magv) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Teacher_GetById(?)}";
        try {
            CallableStatement preparedStatement = connection.prepareCall(sql);
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
            return null;
        }
        
    }
    
    public static boolean addTeacher(Teacher teacher) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Teacher_Add(?, ?, ?, ?, ?)}";
        try {
            CallableStatement preparedStatement = connection.prepareCall(sql);
            preparedStatement.setString(1, teacher.getMagv());
            preparedStatement.setString(2, teacher.getHo());
            preparedStatement.setString(3, teacher.getTen());
            preparedStatement.setString(4, teacher.getHocVi());
            preparedStatement.setString(5, teacher.getMakh());
            int executeUpdate = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }
    
    public static boolean updateTeacher(Teacher teacher) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Teacher_Update(?, ?, ?, ?, ?)}";
        try {
            CallableStatement preparedStatement = connection.prepareCall(sql);
            preparedStatement.setString(1, teacher.getMagv());
            preparedStatement.setString(2, teacher.getHo());
            preparedStatement.setString(3, teacher.getTen());
            preparedStatement.setString(4, teacher.getHocVi());
            preparedStatement.setString(5, teacher.getMakh());
            int executeUpdate = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }
    
    public static boolean deleteTeacher(String magv) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CAll SP_Teacher_Delete(?)}";
        try {
            CallableStatement preparedStatement = connection.prepareCall(sql);
            preparedStatement.setString(1, magv);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }
    
    public static int checkTeacher(String magv) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{? = CALL SP_Teacher_Check(?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);

            callableStatement.registerOutParameter(1, java.sql.Types.INTEGER);
            callableStatement.setString(2, magv);

            callableStatement.execute();

            return callableStatement.getInt(1);

        } catch (SQLException ex) {
            Logger.getLogger(RegisterDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;

    }
   
    
}
