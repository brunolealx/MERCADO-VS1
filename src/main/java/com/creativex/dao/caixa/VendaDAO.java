package com.creativex.dao.caixa;

import com.creativex.db.Conexao;
import com.creativex.model.venda.Venda;
import com.creativex.model.venda.ItemVenda;
import java.sql.*;

public class VendaDAO {

    public void finalizarVenda(Venda venda) throws SQLException {
        // SQLs ajustados para usar 'id' conforme seu padrão de banco
        String sqlVenda = "INSERT INTO tabela_vendas (id_usuario, total_venda, metodo_pagamento) VALUES (?, ?, ?)";
        String sqlItem = "INSERT INTO tabela_itens_venda (id_venda, id_produto, quantidade, preco_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        String sqlBaixarEstoque = "UPDATE tabela_produtos SET quantidade_estoque = quantidade_estoque - ? WHERE id = ?";
        String sqlMovimentacao = "INSERT INTO tabela_movimentacoes_estoque (id_produto, tipo, quantidade, motivo) VALUES (?, 'SAIDA', ?, 'VENDA')";

        // Tenta obter a conexão (Verifique se o método é getConnection() ou getConexao() na sua classe Conexao)
        Connection conn = Conexao.getConnection();

        try {
            conn.setAutoCommit(false); // Inicia Transação

            // 1. Grava a Venda
            try (PreparedStatement psVenda = conn.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS)) {
                psVenda.setLong(1, venda.getIdUsuario());
                psVenda.setBigDecimal(2, venda.getTotalVenda());
                psVenda.setString(3, venda.getMetodoPagamento());
                psVenda.executeUpdate();

                try (ResultSet rs = psVenda.getGeneratedKeys()) {
                    if (rs.next()) {
                        long idVendaGerado = rs.getLong(1);

                        // 2. Loop para gravar itens e baixar estoque
                        for (ItemVenda item : venda.getItens()) {
                            // Grava Item da Venda
                            try (PreparedStatement psItem = conn.prepareStatement(sqlItem)) {
                                psItem.setLong(1, idVendaGerado);
                                psItem.setLong(2, item.getIdProduto());
                                psItem.setBigDecimal(3, item.getQuantidade());
                                psItem.setBigDecimal(4, item.getPrecoUnitario());
                                psItem.setBigDecimal(5, item.getSubtotal());
                                psItem.executeUpdate();
                            }

                            // Baixa no Estoque
                            try (PreparedStatement psEstoque = conn.prepareStatement(sqlBaixarEstoque)) {
                                psEstoque.setBigDecimal(1, item.getQuantidade());
                                psEstoque.setLong(2, item.getIdProduto());
                                psEstoque.executeUpdate();
                            }

                            // Registra a Movimentação
                            try (PreparedStatement psMov = conn.prepareStatement(sqlMovimentacao)) {
                                psMov.setLong(1, item.getIdProduto());
                                psMov.setBigDecimal(2, item.getQuantidade());
                                psMov.executeUpdate();
                            }
                        }
                    }
                }
            }

            conn.commit(); // Efetiva todas as operações

        } catch (SQLException e) {
            if (conn != null) conn.rollback(); // Cancela tudo em caso de erro
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
        }
    }
}