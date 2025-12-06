//Bruno Leal
//creativex sistemas

package com.creativex.dao.produto;
import com.creativex.db.Conexao;
import com.creativex.model.produto.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Classe responsável por todas as operações de acesso ao banco de dados
 * relacionadas ao Produto. Implementa o padrão DAO, garantindo separação
 * entre regras de negócio e persistência.
 * Contém métodos para inserir, atualizar, buscar e listar produtos.
 */
public class ProdutoDAO {
    /**
     * Retorna o último produto cadastrado (com maior ID).
     */
    public Produto buscarUltimo() throws SQLException {
        String sql = "SELECT * FROM TABELA_PRODUTOS ORDER BY id DESC LIMIT 1";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return mapear(rs);
            }
        }
        return null;
    }

    /**
     * Insere um novo produto no banco de dados.
     *
     * @param p Objeto contendo todos os dados que serão persistidos.
     */
    public void inserir(Produto p) throws SQLException {

        String sql = """
        INSERT INTO TABELA_PRODUTOS (
            codigo_barra, descricao, marca, atributos, unidade_medida, categoria,
            cod_grupo, grupo, tipo_balanca, quantidade_estoque, preco_custo, preco_venda,
            ncm, cest, cfop_padrao, unidade_tributavel, cean_tributavel,
            cst_icms, aliquota_icms, cst_pis, ppis, cst_cofins, pcofins,
            loja
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getCodigoBarra());
            stmt.setString(2, p.getDescricao());
            stmt.setString(3, p.getMarca());
            stmt.setString(4, p.getAtributos());
            stmt.setString(5, p.getUnidadeMedida());
            stmt.setString(6, p.getCategoria());
            stmt.setInt(7, p.getCodGrupo());
            stmt.setString(8, p.getGrupo());
            stmt.setString(9, p.getTipoBalanca() == 0 ? null : String.valueOf(p.getTipoBalanca()));
            stmt.setBigDecimal(10, p.getQuantidadeEstoque());
            stmt.setBigDecimal(11, p.getPrecoCusto());
            stmt.setBigDecimal(12, p.getPrecoVenda());
            stmt.setString(13, p.getNcm());
            stmt.setString(14, p.getCest());
            stmt.setString(15, p.getCfopPadrao());
            stmt.setString(16, p.getUnidadeTributavel());
            stmt.setString(17, p.getCeanTributavel());
            stmt.setString(18, p.getCstIcms());
            stmt.setBigDecimal(19, p.getAliquotaIcms());
            stmt.setString(20, p.getCstPis());
            stmt.setBigDecimal(21, p.getPpis());
            stmt.setString(22, p.getCstCofins());
            stmt.setBigDecimal(23, p.getPcofins());
            stmt.setString(24, p.getLoja());

            stmt.executeUpdate();
        }
    }
    /**
     * Atualiza um produto existente com base no ID.
     *
     * @param p Produto contendo os novos dados.
     */
    // Atualizar (não atualiza id; WHERE id = ? é o último parâmetro)
    public void atualizar(Produto p) throws SQLException {
        String sql = """
        UPDATE TABELA_PRODUTOS SET
            codigo_barra = ?, descricao = ?, marca = ?, atributos = ?, unidade_medida = ?,
            categoria = ?, cod_grupo = ?, grupo = ?, tipo_balanca = ?,
            quantidade_estoque = ?, preco_custo = ?, preco_venda = ?,
            ncm = ?, cest = ?, cfop_padrao = ?, unidade_tributavel = ?, cean_tributavel = ?,
            cst_icms = ?, aliquota_icms = ?, cst_pis = ?, ppis = ?,
            cst_cofins = ?, pcofins = ?, loja = ?
        WHERE id = ?
    """;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getCodigoBarra());
            stmt.setString(2, p.getDescricao());
            stmt.setString(3, p.getMarca());
            stmt.setString(4, p.getAtributos());
            stmt.setString(5, p.getUnidadeMedida());
            stmt.setString(6, p.getCategoria());
            stmt.setInt(7, p.getCodGrupo());
            stmt.setString(8, p.getGrupo());
            stmt.setString(9, p.getTipoBalanca() == 0 ? null : String.valueOf(p.getTipoBalanca()));
            stmt.setBigDecimal(10, p.getQuantidadeEstoque());
            stmt.setBigDecimal(11, p.getPrecoCusto());
            stmt.setBigDecimal(12, p.getPrecoVenda());
            stmt.setString(13, p.getNcm());
            stmt.setString(14, p.getCest());
            stmt.setString(15, p.getCfopPadrao());
            stmt.setString(16, p.getUnidadeTributavel());
            stmt.setString(17, p.getCeanTributavel());
            stmt.setString(18, p.getCstIcms());
            stmt.setBigDecimal(19, p.getAliquotaIcms());
            stmt.setString(20, p.getCstPis());
            stmt.setBigDecimal(21, p.getPpis());
            stmt.setString(22, p.getCstCofins());
            stmt.setBigDecimal(23, p.getPcofins());
            stmt.setString(24, p.getLoja());

            stmt.setLong(25, p.getId()); // WHERE id = ?

            stmt.executeUpdate();
        }
    }

    /**
     * Busca um produto por ID.
     *
     * @param id Identificador do produto.
     * @return Produto encontrado ou null se inexistente.
     */
    public Produto buscarPorId(long id) throws SQLException {
        String sql = "SELECT * FROM TABELA_PRODUTOS WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    /**
     * Busca produtos por parte da descrição.
     *
     * @param nome parcial a ser pesquisado.
     * @return Lista de produtos encontrados.
     */
    public Produto buscarPorNome(String nome) throws SQLException {
       
        String sql = "SELECT * FROM TABELA_PRODUTOS WHERE descricao LIKE ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    // ==============================
    // LISTAR por ID , 10 LINHAS
    // ==============================
    public List<Produto> listarPorIdLimite(long id, int limite) throws SQLException {
        String sql = "SELECT * FROM TABELA_PRODUTOS WHERE id >= ? ORDER BY id ASC LIMIT ?";
        List<Produto> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.setInt(2, limite);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }

        return lista;
    }
    // ==============================
    // CONVERSOR DE RESULTSET → PRODUTO
    // ==============================
    private Produto mapear(ResultSet rs) throws SQLException {
        Produto p = new Produto();
        p.setId(rs.getLong("id"));
        p.setCodigoBarra(rs.getString("codigo_barra"));
        p.setDescricao(rs.getString("descricao"));
        p.setMarca(rs.getString("marca"));
        p.setAtributos(rs.getString("atributos"));
        p.setUnidadeMedida(rs.getString("unidade_medida"));
        p.setCategoria(rs.getString("categoria"));
        p.setCodGrupo(rs.getInt("cod_grupo"));
        p.setGrupo(rs.getString("grupo"));
        // Trata char (pode ser null)
        String tipoBalanca = rs.getString("tipo_balanca");

        if (tipoBalanca != null && !tipoBalanca.isEmpty()) {
            p.setTipoBalanca(tipoBalanca.charAt(0));
        }

        p.setQuantidadeEstoque(rs.getBigDecimal("quantidade_estoque"));
        p.setPrecoCusto(rs.getBigDecimal("preco_custo"));
        p.setPrecoVenda(rs.getBigDecimal("preco_venda"));

        p.setNcm(rs.getString("ncm"));
        p.setCest(rs.getString("cest"));
        p.setCfopPadrao(rs.getString("cfop_padrao"));
        p.setUnidadeTributavel(rs.getString("unidade_tributavel"));
        p.setCeanTributavel(rs.getString("cean_tributavel"));

        p.setCstIcms(rs.getString("cst_icms"));
        p.setAliquotaIcms(rs.getBigDecimal("aliquota_icms"));

        p.setCstPis(rs.getString("cst_pis"));
        p.setPpis(rs.getBigDecimal("ppis"));

        p.setCstCofins(rs.getString("cst_cofins"));
        p.setPcofins(rs.getBigDecimal("pcofins"));
        p.setDataCadastro(rs.getTimestamp("data_cadastro"));
        p.setLoja(rs.getString("loja"));
        return p;
    }
}
