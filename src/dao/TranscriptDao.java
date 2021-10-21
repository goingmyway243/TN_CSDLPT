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
import java.sql.CallableStatement;
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
            Logger.getLogger(TranscriptDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return transcripts;
    }

    public Transcript getTranscriptById(String masv, String mamh, int lan) {
        Connection connection = JDBC_Connection.getJDBCConnection();
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
            Logger.getLogger(TranscriptDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void addTranscript(Transcript transcript) {
        Connection connection = JDBC_Connection.getJDBCConnection();
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
            Logger.getLogger(TranscriptDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateTranscript(Transcript transcript) {
        Connection connection = JDBC_Connection.getJDBCConnection();
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
            Logger.getLogger(TranscriptDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteTranscript(String masv, String mamh, int lan) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "{CALL SP_Transcript_Delete(?, ?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setString(1, masv);
            callableStatement.setString(2, mamh);
            callableStatement.setInt(3, lan);
            callableStatement.executeUpdate();
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
//        List<Transcript> transcripts = transcriptDao.getAllTranscripts();
//        for (Transcript transcript : transcripts) {
//            System.out.println(transcript.toString());
//        }
//
//        Transcript transcript = new Transcript("N18VT010", "HQT  ", 2, 8.5f);
//        transcriptDao.addTranscript(transcript);
//        System.out.println(transcriptDao.getTranscriptById("N18VT010", "HQT  ", 2));
        
        transcriptDao.deleteTranscript("N18VT010", "HQT  ", 2);
    }

}
