// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.infrastructure.persistence.repository.cliente;

import br.com.creativex.db.Conexao;
import br.com.creativex.domain.entity.cliente.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para a tabela unificada de clientes.
 * 
 * Centraliza as operações JDBC para persistência de Clientes PF e PJ.
 * 
 * @author Peracio Dias
 * @version 2.2
 * @since 2026-05-26
 */
public class ClienteDAO {

    public void inserir(Cliente c) throws SQLException {
        String sql = """
            INSERT INTO tabela_clientes 
            (tipo_pessoa, nome_razao_social, nome_fantasia, documento, rg_inscricao_estadual, 
             telefone, email, endereco, numero, complemento, bairro, cidade, uf, cep, limite_credito) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id
        """;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherStatement(c, stmt);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    c.setId(rs.getLong("id"));
                }
            }
        }
    }

    public void atualizar(Cliente c) throws SQLException {
        String sql = """
            UPDATE tabela_clientes SET 
            tipo_pessoa=?, nome_razao_social=?, nome_fantasia=?, documento=?, rg_inscricao_estadual=?, 
            telefone=?, email=?, endereco=?, numero=?, complemento=?, bairro=?, cidade=?, uf=?, 
            cep=?, limite_credito=? 
            WHERE id=?
        """;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherStatement(c, stmt);
            stmt.setLong(16, c.getId());
            int affected = stmt.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Nenhum cliente encontrado com o ID: " + c.getId());
            }
        }
    }

    public void excluir(long id) throws SQLException {
        String sql = "DELETE FROM tabela_clientes WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public Cliente buscarPorDocumento(String documento) throws SQLException {
        String sql = "SELECT * FROM tabela_clientes WHERE documento = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, documento);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public Cliente buscarPorId(long id) throws SQLException {
        String sql = "SELECT * FROM tabela_clientes WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public List<Cliente> buscarPorNomeRazaoSocial(String nome) throws SQLException {
        String sql = "SELECT * FROM tabela_clientes WHERE nome_razao_social LIKE ? OR nome_fantasia LIKE ? ORDER BY nome_razao_social";
        List<Cliente> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String search = "%" + nome + "%";
            stmt.setString(1, search);
            stmt.setString(2, search);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public List<Cliente> listarPorIdLimite(long idInicial, int limite) throws SQLException {
        String sql = "SELECT * FROM tabela_clientes WHERE id >= ? ORDER BY id LIMIT ?";
        List<Cliente> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idInicial);
            stmt.setInt(2, limite);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    private void preencherStatement(Cliente c, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, c.getTipoPessoa());
        stmt.setString(2, c.getNomeRazaoSocial());
        stmt.setString(3, c.getNomeFantasia());
        stmt.setString(4, c.getDocumento());
        stmt.setString(5, c.getRgInscricaoEstadual());
        stmt.setString(6, c.getTelefone());
        stmt.setString(7, c.getEmail());
        stmt.setString(8, c.getEndereco());
        stmt.setString(9, c.getNumero());
        stmt.setString(10, c.getComplemento());
        stmt.setString(11, c.getBairro());
        stmt.setString(12, c.getCidade());
        stmt.setString(13, c.getUf());
        stmt.setString(14, c.getCep());
        stmt.setBigDecimal(15, c.getLimiteCredito());
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getLong("id"));
        c.setTipoPessoa(rs.getString("tipo_pessoa"));
        c.setNomeRazaoSocial(rs.getString("nome_razao_social"));
        c.setNomeFantasia(rs.getString("nome_fantasia"));
        c.setDocumento(rs.getString("documento"));
        c.setRgInscricaoEstadual(rs.getString("rg_inscricao_estadual"));
        c.setTelefone(rs.getString("telefone"));
        c.setEmail(rs.getString("email"));
        c.setEndereco(rs.getString("endereco"));
        c.setNumero(rs.getString("numero"));
        c.setComplemento(rs.getString("complemento"));
        c.setBairro(rs.getString("bairro"));
        c.setCidade(rs.getString("cidade"));
        c.setUf(rs.getString("uf"));
        c.setCep(rs.getString("cep"));
        c.setLimiteCredito(rs.getBigDecimal("limite_credito"));
        c.setDataCadastro(rs.getTimestamp("data_cadastro"));
        return c;
    }
}
