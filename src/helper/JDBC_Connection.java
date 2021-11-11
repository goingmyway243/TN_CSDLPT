/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author vivau
 */
public class JDBC_Connection {

    public static String port = "1433";
    public static String user = "sa";
    public static String password = "123";

    public static Connection getPublisherConnection() {
        final String l_user = "sa";
        final String l_password = "123";
        final String url = "jdbc:sqlserver://;databaseName=TN_CSDLPT";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            try {
                System.out.println("Kết nối thành công");
                return DriverManager.getConnection(url, l_user, l_password);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return null;
    }

    public static Connection getConnection() {
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
            JOptionPane.showMessageDialog(null, ex.getMessage());
            return null;
        }
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
            JOptionPane.showMessageDialog(null, ex.getMessage());
            return null;
        }
    }
}
