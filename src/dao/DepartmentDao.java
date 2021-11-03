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
import model.Department;

/**
 *
 * @author vivau
 */
public class DepartmentDao {

    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "{CALL SP_Department_GetAll}";

        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            ResultSet rs = callableStatement.executeQuery();
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
        String sql = "{CALL SP_Department_GetById(?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setString(1, makh);
            ResultSet rs = callableStatement.executeQuery();

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
        String sql = "{CALL SP_Department_Add(?, ?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setString(1, department.getMakh());
            callableStatement.setString(2, department.getTenkh());
            callableStatement.setString(3, department.getMacs());
            int executeUpdate = callableStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateDepartment(Department department) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "{CALL SP_Department_Update(?, ?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setString(1, department.getMakh());
            callableStatement.setString(2, department.getTenkh());
            callableStatement.setString(3, department.getMacs());
            int executeUpdate = callableStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteDepartment(String makh) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "{CALL SP_Department_Delete(?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setString(1, makh);
            callableStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        
      
       
    }

}
