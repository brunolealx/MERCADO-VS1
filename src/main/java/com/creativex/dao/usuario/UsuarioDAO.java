package com.creativex.dao.usuario;

import com.creativex.db.Conexao;
import com.creativex.model.usuario.Usuario;
import java.sql.*;

public class UsuarioDAO {

    // 1. Cadastro de Usuário usando TABELA_USUARIOS
    public boolean salvar(Usuario usuario) {
        String sql = "INSERT INTO TABELA_USUARIOS (nome, login, senha, perfil, ativo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection(); //
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getLogin());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getPerfil());
            stmt.setBoolean(5, true);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. Autenticação usando TABELA_USUARIOS
    public Usuario autenticar(String login, String senha) {
        String sql = "SELECT * FROM TABELA_USUARIOS WHERE login = ? AND senha = ? AND ativo = TRUE";

        try (Connection conn = Conexao.getConnection(); //
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario user = new Usuario();
                user.setId(rs.getLong("id"));
                user.setNome(rs.getString("nome"));
                user.setPerfil(rs.getString("perfil"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 3. Verificação de existência
    public boolean existeLogin(String login) {
        String sql = "SELECT COUNT(*) FROM TABELA_USUARIOS WHERE login = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}