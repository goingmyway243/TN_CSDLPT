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

/**
 *
 * @author vivau
 */
public class JDBC_Connection {

    public static String port = "1434";

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

}
