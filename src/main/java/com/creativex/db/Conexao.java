//Bruno Leal
//creativex sistemas

package com.creativex.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    // 1. Alterado de 'mysql' para 'postgresql'
    // 2. Alterada a porta de '3306' para '5432'
    // 3. Removidos parâmetros específicos do MySQL (?useSSL...)
    private static final String URL = "jdbc:postgresql://localhost:5432/bco_dados_mercado";
    private static final String USER = "pera";
    private static final String PASSWORD = "postboot"; // 🔹 Coloque a senha do usuário 'pera' definida no Postgres

    public static Connection getConnection() throws SQLException {
        try {
            // Garante que o driver do PostgreSQL esteja carregado (opcional em versões recentes do JDBC)
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL não encontrado!", e);
        }
    }
}
