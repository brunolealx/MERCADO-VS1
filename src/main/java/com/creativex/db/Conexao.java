//Bruno Leal
//creativex sistemas

package com.creativex.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static final String URL = "jdbc:mysql://localhost:3306/BCO_DADOS_ESTOQUE?useSSL=false&serverTimezone=UTC";
    private static final String USER = "pera";      // 🔹 altere conforme seu ambiente
    private static final String PASSWORD = "mysqlpera"; // 🔹 altere conforme seu ambiente

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
