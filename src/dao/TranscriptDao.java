/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import helper.DateHelper;
import helper.JDBC_Connection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Transcript;

/**
 *
 * @author vivau
 */
public class TranscriptDao {

    public List<Transcript> getAllTranscripts() {
        List<Transcript> transcripts = new ArrayList<>();
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "SELECT * FROM dbo.[BANGDIEM]";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Transcript transcript = new Transcript();

                transcript.setMasv(rs.getString("MASV"));
                transcript.setMamh(rs.getString("MAMH"));
                transcript.setLan(rs.getInt("LAN"));
                transcript.setNgaythi(DateHelper.toString(rs.getDate("NGAYTHI")));
                transcript.setDiem(rs.getFloat("DIEM"));
                transcript.setBaiThi(rs.getString("BAITHI"));

                transcripts.add(transcript);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TranscriptDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return transcripts;
    }

    public Transcript getTranscriptById(String masv, String mamh, int lan) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "SELECT * FROM dbo.[BANGDIEM] WHERE MASV = ? AND MAMH = ? AND LAN = ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, masv);
            preparedStatement.setString(2, mamh);
            preparedStatement.setInt(3, lan);
            ResultSet rs = preparedStatement.executeQuery();

            rs.next();
            Transcript transcript = new Transcript();
            transcript.setMasv(masv);
            transcript.setMamh(mamh);
            transcript.setLan(lan);
            transcript.setNgaythi(DateHelper.toString(rs.getDate("NGAYTHI")));
            transcript.setDiem(rs.getFloat("DIEM"));
            transcript.setBaiThi(rs.getString("BAITHI"));
            return transcript;
        } catch (SQLException ex) {
            Logger.getLogger(TranscriptDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

//    public void addTranscript(Transcript transcript) {
//        Connection connection = JDBC_Connection.getJDBCConnection();
//        String sql = "INSERT INTO dbo.[BANGDIEM] (MASV, MAMH, LAN, NGAYTHI, DIEM, BAITHI) VALUES (?, ?, ?, ?, ?, ?)";
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setString(1, transcript.getMasv());
//            preparedStatement.setString(2, transcript.getMamh());
//            preparedStatement.setInt(3, transcript.getLan());
//            preparedStatement.setDate(4, transcript.getNgaythi());
//            preparedStatement.setFloat(5, transcript.getDiem());
//            preparedStatement.setString(6, transcript.getBaiThi());
//            int executeUpdate = preparedStatement.executeUpdate();
//        } catch (SQLException ex) {
//            Logger.getLogger(TranscriptDao.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    public void addTranscript(Transcript transcript) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "{CALL dbo.SP_Transcript_Add (?, ?, ?, ?, ?, ?)}";
        try {

            CallableStatement callableStatement = connection.prepareCall(sql);

            callableStatement.setString(1, transcript.getMasv());
            callableStatement.setString(2, transcript.getMamh());
            callableStatement.setInt(3, transcript.getLan());
            callableStatement.setString(4, transcript.getNgaythi());
            callableStatement.setFloat(5, transcript.getDiem());
            callableStatement.setString(6, transcript.getBaiThi());
            int executeUpdate = callableStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TranscriptDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateTranscript(Transcript transcript) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "UPDATE dbo.[BANGDIEM] SET NGAYTHI = ?, DIEM = ?, BAITHI = ? WHERE MASV = ? AND MAMH = ? AND LAN = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, transcript.getNgaythi());
            preparedStatement.setFloat(2, transcript.getDiem());
            preparedStatement.setString(3, transcript.getBaiThi());
            preparedStatement.setString(4, transcript.getMasv());
            preparedStatement.setString(5, transcript.getMamh());
            preparedStatement.setInt(6, transcript.getLan());
            int executeUpdate = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TranscriptDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteTranscript(String masv, String mamh, int lan) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "DELETE FROM dbo.[BANGDIEM] WHERE MASV = ? AND MAMH = ? AND LAN = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, masv);
            preparedStatement.setString(2, mamh);
            preparedStatement.setInt(3, lan);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TranscriptDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int checkTranscript(String masv, String mamh, int lan) {
        Connection connection = JDBC_Connection.getJDBCConnection();
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
        }
        return 0;

    }

    public static void main(String[] args) {
        TranscriptDao transcriptDao = new TranscriptDao();
        System.out.println(transcriptDao.checkTranscript("N18AT011", "CSDL ", 2));

    }

}
