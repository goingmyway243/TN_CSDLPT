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
import model.Department;

/**
 *
 * @author vivau
 */
public class DepartmentDao {

    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "SELECT * FROM dbo.[KHOA]";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Department department = new Department();

                department.setMakh(rs.getString("MAKH"));
                department.setTenkh(rs.getString("TENKH"));
                department.setMacs(rs.getString("MACS"));

                departments.add(department);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return departments;
    }

    public Department getDepartmentById(String makh) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "SELECT * FROM dbo.[KHOA] WHERE MAKH = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, makh);
            ResultSet rs = preparedStatement.executeQuery();

            rs.next();
            Department department = new Department();
            department.setMakh(makh);
            department.setTenkh(rs.getString("TENKH"));
            department.setMacs(rs.getString("MACS"));
            return department;
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void addDepartment(Department department) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "INSERT INTO dbo.[KHOA] (MAKH, TENKH, MACS) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, department.getMakh());
            preparedStatement.setString(2, department.getTenkh());
            preparedStatement.setString(3, department.getMacs());
            int executeUpdate = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateDepartment(Department department) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "UPDATE dbo.[KHOA] SET TENKH = ?, MACS = ? WHERE MAKH = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, department.getTenkh());
            preparedStatement.setString(2, department.getMacs());
            preparedStatement.setString(3, department.getMakh());
            int executeUpdate = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteDepartment(String makh) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "DELETE FROM dbo.[KHOA] WHERE MAKH = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, makh);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
