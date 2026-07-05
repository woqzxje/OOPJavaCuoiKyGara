package com.mycompany.quanlygara.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ManhQuynh
 */
public class DBConnection {
    private static final String URL="jdbc:mysql://localhost:3306/quangara";
    private static final String USER="root";
    private static final String PASSWORD="";
    //phuong thuc ket noi, pt Connection thuoc ve sql ko dc go ten khac
    public static Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
