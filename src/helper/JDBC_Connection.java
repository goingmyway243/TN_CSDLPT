/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author vivau
 */
public class JDBC_Connection {

    public static String port = "1433";

    public static Connection getJDBCConnection() {
        final String user = "sa";
        final String password = "123";
        final String url = "jdbc:sqlserver://localhost:" + port + ";databaseName=TN_CSDLPT;user=" + user + ";password=" + password;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            try {
//                System.out.println("Ket noi thanh cong");
                return DriverManager.getConnection(url);
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.toString());
        }
        return null;
    }

    public static Connection getLoginConnection(String user, String password, String port) {
        final String url = "jdbc:sqlserver://localhost:" + port + ";databaseName=TN_CSDLPT;user=" + user + ";password=" + password;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            try {
                System.out.println("Kết nối thành công");
                return DriverManager.getConnection(url);
            } catch (Exception ex) {
                System.out.println(ex);
                JOptionPane.showMessageDialog(null, "Lỗi kết nối tới cơ sở dữ liệu\n"
                        + "Vui lòng xem lại user name và password\n");
                return null;
            }
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
            return null;
        }
    }
}
