package com.creativex.dao.cliente;

import com.creativex.db.Conexao;
import com.creativex.model.cliente.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    // ==============================
    // INSERIR
    // ==============================
    public void inserir(Cliente c) throws SQLException {

        String sql = """
            INSERT INTO TABELA_CLIENTES
            (nome, cpf, rg, telefone, email,
             endereco, numero, bairro, cidade, uf, cep,
             limite_credito)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherStatement(c, stmt);
            stmt.executeUpdate();
        }
    }

    // ==============================
    // ATUALIZAR
    // ==============================
    public void atualizar(Cliente c) throws SQLException {

        String sql = """
            UPDATE TABELA_CLIENTES SET
                nome = ?, cpf = ?, rg = ?, telefone = ?, email = ?,
                endereco = ?, numero = ?, bairro = ?, cidade = ?, uf = ?, cep = ?,
                limite_credito = ?
            WHERE id = ?
        """;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherStatement(c, stmt);
            stmt.setLong(13, c.getId());
            stmt.executeUpdate();
        }
    }

    // ==============================
    // BUSCAR POR ID
    // ==============================
    public Cliente buscarPorId(long id) throws SQLException {

        String sql = "SELECT * FROM TABELA_CLIENTES WHERE id = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return mapear(rs);
        }

        return null;
    }

    // ==============================
    // BUSCA INTELIGENTE (NOME / CPF / CNPJ)
    // ==============================
    public List<Cliente> buscarPorFiltro(String filtro) throws SQLException {

        String sql = """
            SELECT *
            FROM TABELA_CLIENTES
            WHERE nome LIKE ?
               OR cpf LIKE ?
            ORDER BY nome
            LIMIT 50
        """;

        List<Cliente> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String like = "%" + filtro + "%";
            stmt.setString(1, like);
            stmt.setString(2, like);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }

        return lista;
    }

    // ==============================
    // LISTAR COM LIMITE (ERP)
    // ==============================
    public List<Cliente> listarPorIdLimite(long idInicial, int limite) throws SQLException {

        String sql = """
            SELECT *
            FROM TABELA_CLIENTES
            WHERE id >= ?
            ORDER BY id
            LIMIT ?
        """;

        List<Cliente> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idInicial);
            stmt.setInt(2, limite);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }

        return lista;
    }

    // ==============================
    // MÉTODOS AUXILIARES
    // ==============================

    private void preencherStatement(Cliente c, PreparedStatement stmt) throws SQLException {

        stmt.setString(1, c.getNome());
        stmt.setString(2, c.getCpf());
        stmt.setString(3, c.getRg());
        stmt.setString(4, c.getTelefone());
        stmt.setString(5, c.getEmail());
        stmt.setString(6, c.getEndereco());
        stmt.setString(7, c.getNumero());
        stmt.setString(8, c.getBairro());
        stmt.setString(9, c.getCidade());
        stmt.setString(10, c.getUf());
        stmt.setString(11, c.getCep());
        stmt.setBigDecimal(12, c.getLimiteCredito());
    }

    private Cliente mapear(ResultSet rs) throws SQLException {

        Cliente c = new Cliente();
        c.setId(rs.getLong("id"));
        c.setNome(rs.getString("nome"));
        c.setCpf(rs.getString("cpf"));
        c.setRg(rs.getString("rg"));
        c.setTelefone(rs.getString("telefone"));
        c.setEmail(rs.getString("email"));
        c.setEndereco(rs.getString("endereco"));
        c.setNumero(rs.getString("numero"));
        c.setBairro(rs.getString("bairro"));
        c.setCidade(rs.getString("cidade"));
        c.setUf(rs.getString("uf"));
        c.setCep(rs.getString("cep"));
        c.setLimiteCredito(rs.getBigDecimal("limite_credito"));
        c.setDataCadastro(rs.getTimestamp("data_cadastro"));
        return c;
    }
}
