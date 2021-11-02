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
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Classroom;

/**
 *
 * @author vivau
 */
public class ClassroomDao {

    public List<Classroom> getAllClassrooms() {
        List<Classroom> classrooms = new ArrayList<>();
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "{CALL SP_Classroom_GetAll}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()) {
                Classroom classroom = new Classroom();

                classroom.setMaLop(rs.getString("MALOP"));
                classroom.setTenLop(rs.getString("TENLOP"));
                classroom.setMakh(rs.getString("MAKH"));

                classrooms.add(classroom);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClassroomDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return classrooms;
    }

    public Classroom getClassroomById(String malop) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "{CALL SP_Classroom_GetById(?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setString(1, malop);
            ResultSet rs = callableStatement.executeQuery();

            rs.next();
            Classroom classroom = new Classroom();
            classroom.setMaLop(malop);
            classroom.setTenLop(rs.getString("TENLOP"));
            classroom.setMakh(rs.getString("MAKH"));
            return classroom;
        } catch (SQLException ex) {
            Logger.getLogger(ClassroomDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public boolean addClassroom(Classroom classroom) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "{CALL SP_Classroom_Add(?, ?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setString(1, classroom.getMaLop());
            callableStatement.setString(2, classroom.getTenLop());
            callableStatement.setString(3, classroom.getMakh());
            int executeUpdate = callableStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ClassroomDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean updateClassroom(Classroom classroom) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "{CALL SP_Classroom_Update(?, ?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setString(1, classroom.getMaLop());
            callableStatement.setString(2, classroom.getTenLop());
            callableStatement.setString(3, classroom.getMakh());
            int executeUpdate = callableStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ClassroomDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean deleteClassroom(String malop) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "{CALL SP_Classroom_Delete(?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setString(1, malop);
            callableStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ClassroomDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public int checkClassroom(String malop) {
        Connection connection = JDBC_Connection.getJDBCConnection();
        String sql = "{? = CALL SP_Classroom_Check(?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);

            callableStatement.registerOutParameter(1, java.sql.Types.INTEGER);
            callableStatement.setString(2, malop);

            callableStatement.execute();

            return callableStatement.getInt(1);

        } catch (SQLException ex) {
            Logger.getLogger(RegisterDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;

    }

    public static void main(String[] args) {
        ClassroomDao classroomDao = new ClassroomDao();
//        List<Classroom> classrooms = classroomDao.getAllClassrooms();
//        for (Classroom classroom : classrooms) {
//            System.out.println(classroom.toString());
//        }

        if (classroomDao.checkClassroom("D18CQMR2") == 1) {
            System.out.println("Ton tai!");
        } else {
            System.out.println("Khong ton tai!");
        }
    }

}
