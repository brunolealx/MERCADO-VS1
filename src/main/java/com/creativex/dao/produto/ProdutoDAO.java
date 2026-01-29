//Bruno Leal
//creativex sistemas


package com.creativex.dao.produto;
import com.creativex.db.Conexao;
import com.creativex.model.produto.Produto;
import java.sql.*;
import java.math.BigDecimal;
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
     * Retorna o último produto cadastrado (com maior “ID”).
     */
    public Produto buscarUltimo() throws SQLException {
        String sql = "SELECT * FROM tabela_produtos ORDER BY id DESC LIMIT 1";
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
        INSERT INTO tabela_produtos (
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
     * Atualiza um produto existente com base no "ID".
     *
     * @param p Produto contendo os novos dados.
     */
    // Atualizar (não atualiza “id”; WHERE “id” = ? é o último parâmetro)
    public void atualizar(Produto p) throws SQLException {
        String sql = """
        UPDATE tabela_produtos SET
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
     * Busca um produto por “ID”.
     *
     * @param id Identificador do produto.
     * @return Produto encontrado ou null se inexistente.
     */
    public Produto buscarPorId(long id) throws SQLException {
        String sql = "SELECT * FROM tabela_produtos WHERE id = ?";
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

    //Busca por Codigo de barras  ou id usado no CaixasForm
    public Produto buscarPorCodigoOuId(String filtro) throws SQLException {
        // Removida a coluna 'ativo' que não existe no seu banco
        String sql = "SELECT id, descricao, preco_venda FROM tabela_produtos " +
                "WHERE id = ? OR codigo_barra = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            long idLong = 0;
            // Se o que foi digitado for apenas números, tentamos converter para Long para o ID
            if (filtro.matches("\\d+")) {
                try {
                    idLong = Long.parseLong(filtro);
                } catch (NumberFormatException e) {
                    idLong = 0;
                }
            }

            ps.setLong(1, idLong);     // Primeiro ? -> id
            ps.setString(2, filtro);   // Segundo ? -> codigo_barra

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Produto p = new Produto();
                    p.setId(rs.getLong("id"));
                    p.setDescricao(rs.getString("descricao"));
                    p.setPrecoVenda(rs.getBigDecimal("preco_venda"));
                    return p;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL detalhado: " + e.getMessage());
            throw e;
        }
        return null;
    }
    //
    /**
     * Busca produtos por parte da descrição.
     *
     * @param nome parcial a ser pesquisado.
     * @return Lista de produtos encontrados.
     */
    public Produto buscarPorNome(String nome) throws SQLException {
       
        String sql = "SELECT * FROM tabela_produtos WHERE descricao LIKE ?";
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
    
    /**
     * Registrar Entrada no estoque
     *Recomendação: Use apenas o movimentarEstoque em todo o sistema.
     * Para uma entrada de mercadoria,
     *você chamaria: dao.movimentarEstoque(id, qtd,
     *  "ENTRADA", "Compra de Fornecedor");
     * @param 
     * @return
     */

    public boolean registrarEntradaEstoque(long idProduto, BigDecimal qtdEntrada, BigDecimal novoPrecoCusto) {
        String sql = "UPDATE tabela_produtos SET quantidade_estoque = quantidade_estoque + ?, preco_custo = ? WHERE id = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, qtdEntrada);
            stmt.setBigDecimal(2, novoPrecoCusto);
            stmt.setLong(3, idProduto);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar estoque: " + e.getMessage());
            return false;
        }
    }

    // usando com.creativex.util.Sessao e Conexao.getConnection()
    /**
     * MÉTODO 1: REGISTRAR VENDA (Saída de Estoque)
     * Este método é exclusivo para o PDV (Caixa).
     * Ele garante que a quantidade seja SUBTRAÍDA da tabela principal.
     */
    public boolean registrarVenda(long idProduto, BigDecimal qtd, long idUsuario) {
        // Usamos o operador de subtração (-) no SQL para garantir a baixa
        String sqlUpdate = "UPDATE tabela_produtos SET quantidade_estoque = quantidade_estoque - ? WHERE id = ?";
        String sqlInsert = "INSERT INTO TABELA_MOVIMENTACOES_ESTOQUE (id_produto, tipo, quantidade, motivo, id_usuario) VALUES (?, 'SAIDA', ?, 'Venda PDV', ?)";

        Connection conn = null;
        try {
            conn = Conexao.getConnection();
            conn.setAutoCommit(false); // Inicia transação

            // 1. Atualiza a Tabela de Produtos (Subtraindo)
            try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                stmtUpdate.setBigDecimal(1, qtd);
                stmtUpdate.setLong(2, idProduto);
                stmtUpdate.executeUpdate();
            }

            // 2. Insere o Histórico de Saída
            try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
                stmtInsert.setLong(1, idProduto);
                stmtInsert.setBigDecimal(2, qtd);
                stmtInsert.setLong(3, idUsuario);
                stmtInsert.executeUpdate();
            }

            conn.commit(); // Salva as duas operações
            return true;
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println("Erro ao registrar venda: " + e.getMessage());
            return false;
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    /**
     * MÉTODO 2: REGISTRAR ENTRADA (Lançamento de Estoque)
     * Este método será usado no futuro módulo de Compras/Estoque.
     * Ele garante que a quantidade seja SOMADA à tabela principal.
     */
    public boolean registrarEntradaManual(long idProduto, BigDecimal qtd, String motivo, long idUsuario) {
        // Usamos o operador de soma (+) no SQL
        String sqlUpdate = "UPDATE tabela_produtos SET quantidade_estoque = quantidade_estoque + ? WHERE id = ?";
        String sqlInsert = "INSERT INTO TABELA_MOVIMENTACOES_ESTOQUE (id_produto, tipo, quantidade, motivo, id_usuario) VALUES (?, 'ENTRADA', ?, ?, ?)";

        Connection conn = null;
        try {
            conn = Conexao.getConnection();
            conn.setAutoCommit(false);

            // 1. Atualiza a Tabela de Produtos (Somando)
            try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                stmtUpdate.setBigDecimal(1, qtd);
                stmtUpdate.setLong(2, idProduto);
                stmtUpdate.executeUpdate();
            }

            // 2. Insere o Histórico de Entrada
            try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
                stmtInsert.setLong(1, idProduto);
                stmtInsert.setBigDecimal(2, qtd);
                stmtInsert.setString(3, motivo);
                stmtInsert.setLong(4, idUsuario);
                stmtInsert.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println("Erro ao registrar entrada: " + e.getMessage());
            return false;
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
//======================================================================
public Produto buscarPorCodigoBarra(String codigoBarra) {
    String sql = "SELECT * FROM tabela_produtos WHERE codigo_barra = ?";

    try (Connection con = Conexao.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, codigoBarra);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return mapear(rs);
        }

    } catch (SQLException e) {
        throw new RuntimeException("Erro ao buscar produto por código de barras", e);
    }

    return null;
}
//==========================================
    /**
     * Exclui um produto pelo ID.
     */
    public void excluir(long id) throws SQLException {
        String sql = "DELETE FROM tabela_produtos WHERE id = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    // ==============================
    // LISTAR por ID , 10 LINHAS
    // ==============================
    public List<Produto> listarPorIdLimite(long id, int limite) throws SQLException {
        String sql = "SELECT * FROM tabela_produtos WHERE id >= ? ORDER BY id ASC LIMIT ?";
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
