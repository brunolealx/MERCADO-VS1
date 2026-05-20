// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.infrastructure.persistence.repository.estabelecimento;
import br.com.creativex.domain.entity.config.Estabelecimento;
import br.com.creativex.db.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EstabelecimentoDAO {

    public Estabelecimento carregarDados() throws SQLException {
        String sql = "SELECT * FROM tabela_estabelecimento LIMIT 1";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                Estabelecimento est = new Estabelecimento();
                est.setRazaoSocial(rs.getString("razao_social"));
                est.setNomeFantasia(rs.getString("nome_fantasia"));
                est.setCnpj(rs.getString("cnpj"));
                est.setInscricaoEstadual(rs.getString("inscricao_estadual"));
                est.setLogradouro(rs.getString("logradouro"));
                est.setNumero(rs.getString("numero"));
                est.setBairro(rs.getString("bairro"));
                est.setCidade(rs.getString("cidade"));
                est.setEstado(rs.getString("estado"));
                est.setCep(getStringIfExists(rs, "cep"));
                est.setCodigoMunicipioIbge(getStringIfExists(rs, "codigo_municipio_ibge"));
                est.setRegimeTributario(rs.getInt("regime_tributario"));
                est.setAliqIbpt(rs.getBigDecimal("aliq_ibpt"));
                return est;
            }
        }
        return null;
    }

    public void salvar(Estabelecimento est) throws SQLException {
        String sqlCheck = "SELECT COUNT(*) FROM tabela_estabelecimento";
        String sqlInsert = """
            INSERT INTO tabela_estabelecimento 
            (razao_social, nome_fantasia, cnpj, inscricao_estadual, logradouro, numero, bairro, cidade, estado, cep, codigo_municipio_ibge, regime_tributario, aliq_ibpt)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        String sqlUpdate = """
            UPDATE tabela_estabelecimento SET
            razao_social = ?, nome_fantasia = ?, cnpj = ?, inscricao_estadual = ?, logradouro = ?, numero = ?, bairro = ?, cidade = ?, estado = ?, cep = ?, codigo_municipio_ibge = ?, regime_tributario = ?, aliq_ibpt = ?
            """;

        try (Connection conn = Conexao.getConnection()) {
            boolean exists;
            try (PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
                 ResultSet rs = psCheck.executeQuery()) {
                rs.next();
                exists = rs.getInt(1) > 0;
            }

            String sql = exists ? sqlUpdate : sqlInsert;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, est.getRazaoSocial());
                ps.setString(2, est.getNomeFantasia());
                ps.setString(3, est.getCnpj());
                ps.setString(4, est.getInscricaoEstadual());
                ps.setString(5, est.getLogradouro());
                ps.setString(6, est.getNumero());
                ps.setString(7, est.getBairro());
                ps.setString(8, est.getCidade());
                ps.setString(9, est.getEstado());
                ps.setString(10, est.getCep());
                ps.setString(11, est.getCodigoMunicipioIbge());
                ps.setInt(12, est.getRegimeTributario());
                ps.setBigDecimal(13, est.getAliqIbpt());
                ps.executeUpdate();
            }
        }
    }

    private String getStringIfExists(ResultSet rs, String columnName) throws SQLException {
        try {
            return rs.getString(columnName);
        } catch (SQLException e) {
            if ("42703".equals(e.getSQLState())) {
                return null;
            }
            throw e;
        }
    }
}
