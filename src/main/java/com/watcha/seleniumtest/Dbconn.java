package com.watcha.seleniumtest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Dbconn {
    private static Connection conn;

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        String url = "jdbc:mysql://127.0.0.1/watcha?useSSL=false";
        String uid = "root";
        String upw = "12345678";

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(url, uid, upw);
        return conn;
    }
}
