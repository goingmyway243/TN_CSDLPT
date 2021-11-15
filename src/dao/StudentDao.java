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

    public static List<Student> getStudentClassroom(String maLop) {
        List<Student> students = new ArrayList<>();
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Student_GetFromClass(?)}";

        try {
            CallableStatement preparedStatement = connection.prepareCall(sql);
            preparedStatement.setString(1, maLop);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Student student = new Student();

                student.setMasv(rs.getString("MASV"));
                student.setHo(rs.getString("HO"));
                student.setTen(rs.getString("TEN"));
                student.setNgaySinh(DateHelper.toString(rs.getDate("NGAYSINH")));
                student.setDiaChi(rs.getString("DIACHI"));
                student.setMaLop(rs.getString("MALOP"));
                student.setMatKhau(rs.getString("MATKHAU"));

                students.add(student);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        return students;
    }

    public static List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL dbo.SP_Student_GetAll}";

        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            ResultSet rs = callableStatement.executeQuery();

            while (rs.next()) {
                Student student = new Student();

                student.setMasv(rs.getString("MASV"));
                student.setHo(rs.getString("HO"));
                student.setTen(rs.getString("TEN"));
                student.setNgaySinh(DateHelper.toString(rs.getDate("NGAYSINH")));
                student.setDiaChi(rs.getString("DIACHI"));
                student.setMaLop(rs.getString("MALOP"));
                student.setMatKhau(rs.getString("MATKHAU"));

                students.add(student);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        return students;
    }

    public static Student getStudentById(String masv) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL dbo.SP_Student_GetById(?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);

            callableStatement.setString(1, masv);
            ResultSet rs = callableStatement.executeQuery();

            rs.next();
            Student student = new Student();
            student.setMasv(masv);
            student.setHo(rs.getString("HO"));
            student.setTen(rs.getString("TEN"));
            student.setNgaySinh(DateHelper.toString(rs.getDate("NGAYSINH")));
            student.setDiaChi(rs.getString("DIACHI"));
            student.setMaLop(rs.getString("MALOP"));
            student.setMatKhau(rs.getString("MATKHAU"));
            return student;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        
    }

    public static boolean addStudent(Student student) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL dbo.SP_Student_Add(?, ?, ?, ?, ?, ?, ?)}";
        try {
            CallableStatement preparedStatement = connection.prepareCall(sql);

            preparedStatement.setString(1, student.getMasv());
            preparedStatement.setString(2, student.getHo());
            preparedStatement.setString(3, student.getTen());
            preparedStatement.setString(4, student.getNgaySinh());
            preparedStatement.setString(5, student.getDiaChi());
            preparedStatement.setString(6, student.getMaLop());
            preparedStatement.setString(7, student.getMatKhau());

            int executeUpdate = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            return false;
        }
        return true;
    }

    public static boolean updateStudent(Student student) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL dbo.SP_Student_Update(?, ?, ?, ?, ?, ?, ?)}";
        try {
            CallableStatement preparedStatement = connection.prepareCall(sql);

            preparedStatement.setString(1, student.getMasv());
            preparedStatement.setString(2, student.getHo());
            preparedStatement.setString(3, student.getTen());
            preparedStatement.setString(4, student.getNgaySinh());
            preparedStatement.setString(5, student.getDiaChi());
            preparedStatement.setString(6, student.getMaLop());
            preparedStatement.setString(7, student.getMatKhau());

            int executeUpdate = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    public static boolean deleteStudent(String masv) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{CALL SP_Student_Delete(?)}";
        try {
            CallableStatement preparedStatement = connection.prepareCall(sql);
            preparedStatement.setString(1, masv);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }
    
    public static int checkStudent(String masv) {
        Connection connection = JDBC_Connection.getConnection();
        String sql = "{? = CALL SP_Student_Check(?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);

            callableStatement.registerOutParameter(1, java.sql.Types.INTEGER);
            callableStatement.setString(2, masv);

            callableStatement.execute();

            return callableStatement.getInt(1);

        } catch (SQLException ex) {
            Logger.getLogger(RegisterDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;

    }

}
