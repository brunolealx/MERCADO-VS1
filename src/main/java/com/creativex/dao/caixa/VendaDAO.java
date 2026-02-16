package com.creativex.dao.caixa;

import com.creativex.db.Conexao;
import com.creativex.model.venda.Venda;
import com.creativex.model.venda.ItemVenda;

import java.math.BigDecimal;
import java.sql.*;

public class VendaDAO {

    public void finalizarVenda(Venda venda) throws SQLException {

        String sqlVenda = """
            INSERT INTO tabela_vendas
            (id_usuario, id_cliente,
             total_bruto, total_desconto, total_liquido,
             metodo_pagamento, valor_pago, troco)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id_venda
        """;

        String sqlItem = """
            INSERT INTO tabela_itens_venda
            (id_venda, id_produto, quantidade, preco_unitario,
             desconto_item, subtotal,
             preco_custo_momento, cst_fiscal_momento)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        String sqlSaldoAtual =
                "SELECT quantidade_estoque FROM tabela_produtos WHERE id = ? FOR UPDATE";

        String sqlBaixaEstoque =
                "UPDATE tabela_produtos SET quantidade_estoque = quantidade_estoque - ? WHERE id = ?";

        String sqlMov = """
            INSERT INTO tabela_movimentacoes_estoque
            (id_produto, tipo, quantidade,
             saldo_anterior, saldo_posterior,
             motivo, id_usuario, id_venda_origem)
            VALUES (?, 'SAIDA', ?, ?, ?, 'VENDA', ?, ?)
        """;

        Connection conn = Conexao.getConnection();

        try {
            conn.setAutoCommit(false);

            // 1️⃣ Grava VENDA
            long idVenda;
            try (PreparedStatement ps = conn.prepareStatement(sqlVenda)) {
                ps.setLong(1, venda.getIdUsuario());
                ps.setObject(2, venda.getIdCliente());
                ps.setBigDecimal(3, venda.getTotalBruto());
                ps.setBigDecimal(4, venda.getTotalDesconto());
                ps.setBigDecimal(5, venda.getTotalLiquido());
                ps.setString(6, venda.getMetodoPagamento());
                ps.setBigDecimal(7, venda.getValorPago());
                ps.setBigDecimal(8, venda.getTroco());

                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new SQLException("Erro ao gerar ID da venda.");
                }
                idVenda = rs.getLong("id_venda");
            }

            // 2️⃣ Itens + estoque + movimentação
            for (ItemVenda item : venda.getItens()) {

                BigDecimal saldoAnterior;

                // trava o produto (Postgres)
                try (PreparedStatement ps = conn.prepareStatement(sqlSaldoAtual)) {
                    ps.setLong(1, item.getIdProduto());
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    saldoAnterior = rs.getBigDecimal("quantidade_estoque");
                }

                BigDecimal saldoPosterior =
                        saldoAnterior.subtract(item.getQuantidade());

                // Item da venda
                try (PreparedStatement ps = conn.prepareStatement(sqlItem)) {
                    ps.setLong(1, idVenda);
                    ps.setLong(2, item.getIdProduto());
                    ps.setBigDecimal(3, item.getQuantidade());
                    ps.setBigDecimal(4, item.getPrecoUnitario());
                    ps.setBigDecimal(5, item.getDescontoItem());
                    ps.setBigDecimal(6, item.getSubtotal());
                    ps.setBigDecimal(7, item.getPrecoCustoMomento());
                    ps.setString(8, item.getCstFiscalMomento());
                    ps.executeUpdate();
                }

                // Baixa estoque
                try (PreparedStatement ps = conn.prepareStatement(sqlBaixaEstoque)) {
                    ps.setBigDecimal(1, item.getQuantidade());
                    ps.setLong(2, item.getIdProduto());
                    ps.executeUpdate();
                }

                // Movimentação
                try (PreparedStatement ps = conn.prepareStatement(sqlMov)) {
                    ps.setLong(1, item.getIdProduto());
                    ps.setBigDecimal(2, item.getQuantidade());
                    ps.setBigDecimal(3, saldoAnterior);
                    ps.setBigDecimal(4, saldoPosterior);
                    ps.setLong(5, venda.getIdUsuario());
                    ps.setLong(6, idVenda);
                    ps.executeUpdate();
                }
            }

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    //
    // Cancelamento de venda
    //
    public void cancelarVenda(long idVenda, long idUsuario) throws SQLException {

        String sqlBuscaItens = """
        SELECT id_produto, quantidade
        FROM tabela_itens_venda
        WHERE id_venda = ?
    """;

        String sqlAtualizaStatus = """
        UPDATE tabela_vendas
        SET status = 'CANCELADA'
        WHERE id_venda = ?
    """;

        String sqlSaldoAtual =
                "SELECT quantidade_estoque FROM tabela_produtos WHERE id = ? FOR UPDATE";

        String sqlAtualizaEstoque =
                "UPDATE tabela_produtos SET quantidade_estoque = quantidade_estoque + ? WHERE id = ?";

        String sqlMov = """
        INSERT INTO tabela_movimentacoes_estoque
        (id_produto, tipo, quantidade,
         saldo_anterior, saldo_posterior,
         motivo, id_usuario, id_venda_origem)
        VALUES (?, 'ENTRADA', ?, ?, ?, 'ESTORNO VENDA', ?, ?)
    """;

        Connection conn = Conexao.getConnection();

        try {
            conn.setAutoCommit(false);

            // 1️⃣ Atualiza status
            try (PreparedStatement ps = conn.prepareStatement(sqlAtualizaStatus)) {
                ps.setLong(1, idVenda);
                ps.executeUpdate();
            }

            // 2️⃣ Busca itens da venda
            try (PreparedStatement psItens = conn.prepareStatement(sqlBuscaItens)) {
                psItens.setLong(1, idVenda);

                ResultSet rsItens = psItens.executeQuery();

                while (rsItens.next()) {

                    long idProduto = rsItens.getLong("id_produto");
                    BigDecimal qtd = rsItens.getBigDecimal("quantidade");

                    BigDecimal saldoAnterior;

                    // 🔒 Lock do produto
                    try (PreparedStatement psSaldo = conn.prepareStatement(sqlSaldoAtual)) {
                        psSaldo.setLong(1, idProduto);
                        ResultSet rs = psSaldo.executeQuery();
                        rs.next();
                        saldoAnterior = rs.getBigDecimal("quantidade_estoque");
                    }

                    BigDecimal saldoPosterior = saldoAnterior.add(qtd);

                    // 3️⃣ Devolve estoque
                    try (PreparedStatement psEstoque = conn.prepareStatement(sqlAtualizaEstoque)) {
                        psEstoque.setBigDecimal(1, qtd);
                        psEstoque.setLong(2, idProduto);
                        psEstoque.executeUpdate();
                    }

                    // 4️⃣ Registra movimentação reversa
                    try (PreparedStatement psMov = conn.prepareStatement(sqlMov)) {
                        psMov.setLong(1, idProduto);
                        psMov.setBigDecimal(2, qtd);
                        psMov.setBigDecimal(3, saldoAnterior);
                        psMov.setBigDecimal(4, saldoPosterior);
                        psMov.setLong(5, idUsuario);
                        psMov.setLong(6, idVenda);
                        psMov.executeUpdate();
                    }
                }
            }

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }


}
