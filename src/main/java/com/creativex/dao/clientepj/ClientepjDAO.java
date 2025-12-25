package com.creativex.dao.clientepj;

import com.creativex.db.Conexao;
import com.creativex.model.clientepj.Clientepj;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientepjDAO {

    // ================= INSERIR =================
    public void inserir(Clientepj c) throws SQLException {

        String sql = """
            INSERT INTO TABELA_CLIENTES_PJ (
                razao_social, nome_fantasia, cnpj, ie,
                telefone, email,
                endereco, numero, complemento, bairro,
                cidade, uf, cep, limite_credito
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = Conexao.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, c.getRazaoSocial());
            stmt.setString(2, c.getNomeFantasia());
            stmt.setString(3, c.getCnpj());
            stmt.setString(4, c.getIe());
            stmt.setString(5, c.getTelefone());
            stmt.setString(6, c.getEmail());
            stmt.setString(7, c.getEndereco());
            stmt.setString(8, c.getNumero());
            stmt.setString(9, c.getComplemento());
            stmt.setString(10, c.getBairro());
            stmt.setString(11, c.getCidade());
            stmt.setString(12, c.getUf());
            stmt.setString(13, c.getCep());
            stmt.setBigDecimal(14, c.getLimiteCredito());

            stmt.executeUpdate();
        }
    }

    // ================= ATUALIZAR =================
    public void atualizar(Clientepj c) throws SQLException {

        String sql = """
            UPDATE TABELA_CLIENTES_PJ SET
                razao_social = ?, nome_fantasia = ?, cnpj = ?, ie = ?,
				telefone = ?, email = ?,
                endereco = ?, numero = ?, complemento = ?, bairro = ?,
                cidade = ?, uf = ?, cep = ?, limite_credito = ?
            WHERE id = ?
        """;

        try (Connection con = Conexao.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, c.getRazaoSocial());
            stmt.setString(2, c.getNomeFantasia());
            stmt.setString(3, c.getCnpj());
            stmt.setString(4, c.getIe());
            stmt.setString(5, c.getTelefone());
            stmt.setString(6, c.getEmail());
            stmt.setString(7, c.getEndereco());
            stmt.setString(8, c.getNumero());
            stmt.setString(9, c.getComplemento());
            stmt.setString(10, c.getBairro());
            stmt.setString(11, c.getCidade());
            stmt.setString(12, c.getUf());
            stmt.setString(13, c.getCep());
            stmt.setBigDecimal(14, c.getLimiteCredito());
            stmt.setLong(15, c.getId());

            stmt.executeUpdate();
        }
    }

    // ================= BUSCAR POR ID =================
    public Clientepj buscarPorId(long id) throws SQLException {

        String sql = "SELECT * FROM TABELA_CLIENTES_PJ WHERE id = ?";

        try (Connection con = Conexao.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return map(rs);
        }
        return null;
    }

    // ================= BUSCAR POR CNPJ =================
    public Clientepj buscarPorCnpj(String cnpj) throws SQLException {

        String sql = "SELECT * FROM TABELA_CLIENTES_PJ WHERE cnpj = ?";

        try (Connection con = Conexao.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, cnpj);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return map(rs);
        }
        return null;
    }

    // ================= BUSCAR POR RAZÃO SOCIAL =================
    public List<Clientepj> buscarPorRazaoSocial(String nome) throws SQLException {

        List<Clientepj> lista = new ArrayList<>();

        String sql = """
            SELECT * FROM TABELA_CLIENTES_PJ
            WHERE razao_social LIKE ?
            ORDER BY id
        """;

        try (Connection con = Conexao.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) lista.add(map(rs));
        }
        return lista;
    }

    // ================= LISTAR =================
    public List<Clientepj> listarPorIdLimite(int inicio, int limite) throws SQLException {

        List<Clientepj> lista = new ArrayList<>();

        String sql = """
            SELECT * FROM TABELA_CLIENTES_PJ
            ORDER BY id
            LIMIT ?, ?
        """;

        try (Connection con = Conexao.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, inicio - 1);
            stmt.setInt(2, limite);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) lista.add(map(rs));
        }
        return lista;
    }

    // ================= MAP =================
    private Clientepj map(ResultSet rs) throws SQLException {

        Clientepj c = new Clientepj();
        c.setId(rs.getLong("id"));
        c.setRazaoSocial(rs.getString("razao_social"));
        c.setNomeFantasia(rs.getString("nome_fantasia"));
        c.setCnpj(rs.getString("cnpj"));
        c.setIe(rs.getString("ie"));
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
        return c;
    }
}
