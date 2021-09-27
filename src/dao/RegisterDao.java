/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import helper.DateHelper;
import helper.JDBC_Connection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Register;

/**
 *
 * @author vivau
 */
public class RegisterDao {

    public List<Register> getAllRegisters() {
        List<Register> registers = new ArrayList<>();
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "SELECT * FROM dbo.[GIAOVIEN_DANGKY]";

        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Register register = new Register();

                register.setMagv(rs.getString("MAGV"));
                register.setMamh(rs.getString("MAMH"));
                register.setMalop(rs.getString("MALOP"));
                register.setTrinhDo(rs.getString("TRINHDO"));
                register.setNgayThi(DateHelper.toString(rs.getDate("NGAYTHI")));
                register.setLan(rs.getInt("LAN"));
                register.setSoCauThi(rs.getInt("SOCAUTHI"));
                register.setThoiGian(rs.getInt("THOIGIAN"));

                registers.add(register);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RegisterDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return registers;
    }

    public Register getRegisterById(String mamh, String malop, int lan) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "SELECT * FROM dbo.[GIAOVIEN_DANGKY] WHERE MAMH = ? AND MALOP = ? AND LAN = ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, mamh);
            preparedStatement.setString(2, malop);
            preparedStatement.setInt(3, lan);
            ResultSet rs = preparedStatement.executeQuery();

            rs.next();
            Register register = new Register();
            register.setMamh(mamh);
            register.setMalop(malop);
            register.setLan(lan);
            register.setMagv(rs.getString("MAGV"));
            register.setTrinhDo(rs.getString("TRINHDO"));
            register.setNgayThi(DateHelper.toString(rs.getDate("NGAYTHI")));
            register.setSoCauThi(rs.getInt("SOCAUTHI"));
            register.setThoiGian(rs.getInt("THOIGIAN"));
            return register;
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        return null;
    }
    

    public void addRegister(Register register) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "INSERT INTO dbo.[GIAOVIEN_DANGKY] (MAGV, MAMH, MALOP, TRINHDO, NGAYTHI, LAN, SOCAUTHI, THOIGIAN) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, register.getMagv());
            preparedStatement.setString(2, register.getMamh());
            preparedStatement.setString(3, register.getMalop());
            preparedStatement.setString(4, register.getTrinhDo());
            preparedStatement.setString(5, register.getNgayThi());
            preparedStatement.setInt(6, register.getLan());
            preparedStatement.setInt(7, register.getSoCauThi());
            preparedStatement.setInt(8, register.getThoiGian());
            int executeUpdate = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RegisterDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateRegister(Register register) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "UPDATE dbo.[GIAOVIEN_DANGKY] SET MAGV = ?, TRINHDO = ?, NGAYTHI = ?, SOCAUTHI = ?, THOIGIAN = ? WHERE MAMH = ? AND MALOP = ? AND LAN = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, register.getMagv());
            preparedStatement.setString(2, register.getTrinhDo());
            preparedStatement.setString(3, register.getNgayThi());
            preparedStatement.setInt(4, register.getSoCauThi());
            preparedStatement.setInt(5, register.getThoiGian());
            preparedStatement.setString(6, register.getMamh());
            preparedStatement.setString(7, register.getMalop());
            preparedStatement.setInt(8, register.getLan());
            int executeUpdate = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RegisterDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteRegister(String mamh, String malop, int lan) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "DELETE FROM dbo.[GIAOVIEN_DANGKY] WHERE MAMH = ? AND MALOP = ? AND LAN = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, mamh);
            preparedStatement.setString(2, malop);
            preparedStatement.setInt(3, lan);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }

}
