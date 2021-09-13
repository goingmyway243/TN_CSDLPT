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

    public static Connection getJDBCConnection() {
        final String user = "sa";
        final String password = "123";
        final String url = "jdbc:sqlserver://localhost:1433;databaseName=TN_CSDLPT;user=" + user + ";password=" + password;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            try {
                return DriverManager.getConnection(url);
                        } catch (SQLException ex) {
                Logger.getLogger(JDBC_Connection.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JDBC_Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
