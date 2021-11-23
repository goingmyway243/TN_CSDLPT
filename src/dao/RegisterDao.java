/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import helper.DateHelper;
import helper.JDBC_Connection;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Register;
import view.MainFrame;

/**
 *
 * @author vivau
 */
public class RegisterDao {

    public static List<Register> getAllRegisters() {
        List<Register> registers = new ArrayList<>();
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Register_GetAll}";

        try {
            CallableStatement preCallableStatement = connection.prepareCall(sql);
            ResultSet rs = preCallableStatement.executeQuery();
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
            System.out.println(ex.getMessage());
            MainFrame.message = ex.getMessage();
            return null;
        }
        return registers;
    }

    public static Register getRegisterById(String mamh, String malop, int lan) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Register_GetById(?, ?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setString(1, mamh);
            callableStatement.setString(2, malop);
            callableStatement.setInt(3, lan);
            ResultSet rs = callableStatement.executeQuery();

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
            System.out.println(ex.getMessage());
            MainFrame.message = ex.getMessage();
            return null;
        }
        
    }

    public static boolean addRegister(Register register) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Register_Add(?, ?, ?, ?, ?, ?, ?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setString(1, register.getMagv());
            callableStatement.setString(2, register.getMamh());
            callableStatement.setString(3, register.getMalop());
            callableStatement.setString(4, register.getTrinhDo());
            callableStatement.setString(5, register.getNgayThi());
            callableStatement.setInt(6, register.getLan());
            callableStatement.setInt(7, register.getSoCauThi());
            callableStatement.setInt(8, register.getThoiGian());
            int executeUpdate = callableStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            MainFrame.message = ex.getMessage();
            return false;
        }
        return true;
    }

    public static boolean updateRegister(Register register) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Register_Update(?, ?, ?, ?, ?, ?, ?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setString(1, register.getMagv());
            callableStatement.setString(2, register.getMamh());
            callableStatement.setString(3, register.getMalop());
            callableStatement.setString(4, register.getTrinhDo());
            callableStatement.setString(5, register.getNgayThi());
            callableStatement.setInt(6, register.getLan());
            callableStatement.setInt(7, register.getSoCauThi());
            callableStatement.setInt(8, register.getThoiGian());
            int executeUpdate = callableStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            MainFrame.message = ex.getMessage();
            return false;
        }
        return true;
    }

    public static boolean deleteRegister(String mamh, String malop, int lan) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Register_Delete(?, ?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setString(1, mamh);
            callableStatement.setString(2, malop);
            callableStatement.setInt(3, lan);
            callableStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            MainFrame.message = ex.getMessage();
            return false;
        }
        return true;
    }

    public static int checkRegister(String mamh, String maLop, int lan) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{? = CALL SP_Register_Check(?, ?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);

            callableStatement.registerOutParameter(1, java.sql.Types.INTEGER);

            callableStatement.setString(2, mamh);
            callableStatement.setString(3, maLop);
            callableStatement.setInt(4, lan);

            callableStatement.execute();

            return callableStatement.getInt(1);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            MainFrame.message = ex.getMessage();
        }
        return 0;

    }
}
