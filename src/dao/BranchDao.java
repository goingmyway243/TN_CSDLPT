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
import model.Branch;

/**
 *
 * @author vivau
 */
public class BranchDao {

    public List<Branch> getAllBranchs() {
        List<Branch> branchs = new ArrayList<>();
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "SELECT * FROM dbo.[COSO]";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Branch branch = new Branch();

                branch.setMacs(rs.getString("MACS"));
                branch.setTencs(rs.getString("TENCS"));
                branch.setDiaChi(rs.getString("DIACHI"));

                branchs.add(branch);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BranchDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return branchs;
    }

    public Branch getBranchById(String macs) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "SELECT * FROM dbo.[COSO] WHERE MACS = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, macs);
            ResultSet rs = preparedStatement.executeQuery();

            rs.next();
            Branch branch = new Branch();
            branch.setMacs(macs);
            branch.setTencs(rs.getString("TENCS"));
            branch.setDiaChi(rs.getString("DIACHI"));
            return branch;
        } catch (SQLException ex) {
            Logger.getLogger(BranchDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void addBranch(Branch branch) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "INSERT INTO dbo.[COSO] (MACS, TENCS, DIACHI) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, branch.getMacs());
            preparedStatement.setString(2, branch.getTencs());
            preparedStatement.setString(3, branch.getDiaChi());
            int executeUpdate = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(BranchDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateBranch(Branch branch) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "UPDATE dbo.[COSO] SET TENCS = ?, DIACHI = ? WHERE MACS = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, branch.getTencs());
            preparedStatement.setString(2, branch.getDiaChi());
            preparedStatement.setString(3, branch.getMacs());
            int executeUpdate = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(BranchDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteBranch(String macs) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "DELETE FROM dbo.[COSO] WHERE MACS = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, macs);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(BranchDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
