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
import model.Branch;
import view.MainFrame;

/**
 *
 * @author vivau
 */
public class BranchDao {

    public static List<Branch> getAllBranchs() {
        List<Branch> branchs = new ArrayList<>();
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Branch_GetAll}";

        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                Branch branch = new Branch();

                branch.setMacs(rs.getString("MACS"));
                branch.setTencs(rs.getString("TENCS"));
                branch.setDiaChi(rs.getString("DIACHI"));

                branchs.add(branch);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            MainFrame.message = ex.getMessage();
            return null;
        }
        return branchs;
    }

    public static Branch getBranchById(String macs) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Branch_GetById(?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setString(1, macs);
            ResultSet rs = callableStatement.executeQuery();

            rs.next();
            Branch branch = new Branch();
            branch.setMacs(macs);
            branch.setTencs(rs.getString("TENCS"));
            branch.setDiaChi(rs.getString("DIACHI"));
            return branch;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            MainFrame.message = ex.getMessage();
            return null;
        }
    }

    public static boolean addBranch(Branch branch) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Branch_Add(?, ?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setString(1, branch.getMacs());
            callableStatement.setString(2, branch.getTencs());
            callableStatement.setString(3, branch.getDiaChi());
            int executeUpdate = callableStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            MainFrame.message = ex.getMessage();
            return false;
        }
        return true;
    }

    public static boolean updateBranch(Branch branch) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Branch_Update(?, ? , ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setString(1, branch.getMacs());
            callableStatement.setString(2, branch.getTencs());
            callableStatement.setString(3, branch.getDiaChi());
            int executeUpdate = callableStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            MainFrame.message = ex.getMessage();
            return false;
        }
        return true;
    }

    public static boolean deleteBranch(String macs) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Branch_Delete(?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setString(1, macs);
            callableStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            MainFrame.message = ex.getMessage();
            return false;
        }
        return true;
    }
    
}
