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
import model.Transcript;
import view.MainFrame;

/**
 *
 * @author vivau
 */
public class TranscriptDao {

    public static List<Transcript> getAllTranscripts() {
        List<Transcript> transcripts = new ArrayList<>();
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Transcript_GetAll}";

        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                Transcript transcript = new Transcript();

                transcript.setMasv(rs.getString("MASV"));
                transcript.setMamh(rs.getString("MAMH"));
                transcript.setLan(rs.getInt("LAN"));
                transcript.setNgaythi(DateHelper.toString(rs.getDate("NGAYTHI")));
                transcript.setDiem(rs.getFloat("DIEM"));
                transcript.setBaiThi(rs.getInt("BAITHI"));

                transcripts.add(transcript);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            MainFrame.message = ex.getMessage();
            return null;
        }
        return transcripts;
    }

    public static Transcript getTranscriptById(String masv, String mamh, int lan) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Transcript_GetById(?, ?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setString(1, masv);
            callableStatement.setString(2, mamh);
            callableStatement.setInt(3, lan);
            ResultSet rs = callableStatement.executeQuery();

            rs.next();
            Transcript transcript = new Transcript();
            transcript.setMasv(masv);
            transcript.setMamh(mamh);
            transcript.setLan(lan);
            transcript.setNgaythi(DateHelper.toString(rs.getDate("NGAYTHI")));
            transcript.setDiem(rs.getFloat("DIEM"));
            transcript.setBaiThi(rs.getInt("BAITHI"));
            return transcript;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            MainFrame.message = ex.getMessage();
            return null;
        }
        
    }

    public static boolean addTranscript(Transcript transcript) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL dbo.SP_Transcript_Add (?, ?, ?, ?, ?)}";
        try {

            CallableStatement callableStatement = connection.prepareCall(sql);

            callableStatement.setString(1, transcript.getMasv());
            callableStatement.setString(2, transcript.getMamh());
            callableStatement.setInt(3, transcript.getLan());
            callableStatement.setString(4, transcript.getNgaythi());
            callableStatement.setFloat(5, transcript.getDiem());
            int executeUpdate = callableStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            MainFrame.message = ex.getMessage();
            return false;
        }
        return true;
    }

    public static boolean updateTranscript(Transcript transcript) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Transcript_Update(?, ?, ?, ?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setString(1, transcript.getMasv());
            callableStatement.setString(2, transcript.getMamh());
            callableStatement.setInt(3, transcript.getLan());
            callableStatement.setString(4, transcript.getNgaythi());
            callableStatement.setFloat(5, transcript.getDiem());
            int executeUpdate = callableStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            MainFrame.message = ex.getMessage();
            return false;
        }
        return true;
    }

    public static boolean deleteTranscript(String masv, String mamh, int lan) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Transcript_Delete(?, ?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setString(1, masv);
            callableStatement.setString(2, mamh);
            callableStatement.setInt(3, lan);
            callableStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            MainFrame.message = ex.getMessage();
            return false;
        }
        return true;
    }

    public static int checkTranscript(String masv, String mamh, int lan) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{? = CALL dbo.SP_KiemTraBangDiem(?, ?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);

            callableStatement.registerOutParameter(1, java.sql.Types.INTEGER);

            callableStatement.setString(2, masv);
            callableStatement.setString(3, mamh);
            callableStatement.setInt(4, lan);

            callableStatement.execute();

            return callableStatement.getInt(1);

        } catch (SQLException ex) {
            Logger.getLogger(RegisterDao.class.getName()).log(Level.SEVERE, null, ex);
            MainFrame.message = ex.getMessage();
        }
        return 0;

    }

}
