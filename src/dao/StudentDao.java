/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import helper.JDBC_Connection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Student;

/**
 *
 * @author vivau
 */
public class StudentDao {
    
    public List<Student> getStudentClassroom(String maLop) {
        List<Student> students = new ArrayList<>();
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "SELECT * FROM dbo.[SINHVIEN] WHERE MALOP = " + "'" + maLop + "'";
//        System.out.println(sql);System.exit(0);
        
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Student student = new Student();
                
                student.setMasv(rs.getString("MASV"));
                student.setHo(rs.getString("HO"));
                student.setTen(rs.getString("TEN"));
                student.setNgaySinh(rs.getDate("NGAYSINH"));
                student.setDiaChi(rs.getString("DIACHI"));
                student.setMaLop(rs.getString("MALOP"));
                student.setMatKhau(rs.getString("MATKHAU"));
                
                students.add(student);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return students;
    }
    
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "SELECT * FROM dbo.[SINHVIEN]";
        
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Student student = new Student();
                
                student.setMasv(rs.getString("MASV"));
                student.setHo(rs.getString("HO"));
                student.setTen(rs.getString("TEN"));
                student.setNgaySinh(rs.getDate("NGAYSINH"));
                student.setDiaChi(rs.getString("DIACHI"));
                student.setMaLop(rs.getString("MALOP"));
                student.setMatKhau(rs.getString("MATKHAU"));
                
                students.add(student);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return students;
    }
    
    public Student getStudentById(String masv) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "SELECT * FROM dbo.[SINHVIEN] WHERE MASV = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, masv);
            ResultSet rs = preparedStatement.executeQuery();
            
            rs.next();
            Student student = new Student();
            student.setMasv(masv);
            student.setHo(rs.getString("HO"));
            student.setTen(rs.getString("TEN"));
            student.setNgaySinh(rs.getDate("NGAYSINH"));
            student.setDiaChi(rs.getString("DIACHI"));
            student.setMaLop(rs.getString("MALOP"));
            student.setMatKhau(rs.getString("MATKHAU"));
            return student;
        } catch (SQLException ex) {
            Logger.getLogger(StudentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void addStudent(Student student) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "INSERT INTO dbo.[SINHVIEN] (MASV, HO, TEN, NGAYSINH, DIACHI, MALOP, MATKHAU) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, student.getMasv());
            preparedStatement.setString(2, student.getHo());
            preparedStatement.setString(3, student.getTen());
            preparedStatement.setDate(4, (Date) student.getNgaySinh());
            preparedStatement.setString(5, student.getDiaChi());
            preparedStatement.setString(6, student.getMaLop());
            preparedStatement.setString(7, student.getMatKhau());
            int executeUpdate = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(StudentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateStudent(Student student) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "UPDATE dbo.[SINHVIEN] SET HO = ?, TEN = ?, NGAYSINH = ?, DIACHI = ?, MALOP = ?, MATKHAU = ? WHERE MASV = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, student.getHo());
            preparedStatement.setString(2, student.getTen());
            preparedStatement.setDate(3, (Date) student.getNgaySinh());
            preparedStatement.setString(4, student.getDiaChi());
            preparedStatement.setString(5, student.getMaLop());
            preparedStatement.setString(6, student.getMatKhau());
            preparedStatement.setString(7, student.getMasv());
            int executeUpdate = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(StudentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteStudent(String masv) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "DELETE FROM dbo.[SINHVIEN] WHERE MASV = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, masv);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(StudentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
}
