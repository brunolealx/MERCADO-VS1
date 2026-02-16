//Bruno Leal
//creativex sistemas
package com.creativex.ui.caixas;
import com.creativex.dao.caixa.VendaDAO;
import com.creativex.db.Conexao;
import com.creativex.util.Sessao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.NumberFormat;
import java.util.Locale;

public class VendasConsultaForm extends JPanel {

    private JTable tabela;
    private DefaultTableModel model;
    private JButton btnCancelar;

    private final NumberFormat nf =
            NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public VendasConsultaForm() {
        setLayout(new BorderLayout(10, 10));

        model = new DefaultTableModel(
                new String[]{"ID", "Data", "Operador", "Total", "Status"}, 0);

        tabela = new JTable(model);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        btnCancelar = new JButton("Cancelar Venda");
        add(btnCancelar, BorderLayout.SOUTH);

        carregarVendas();
        configurarEventos();
    }

    private void carregarVendas() {
        model.setRowCount(0);

        String sql = """
            SELECT v.id_venda, v.data_venda,
                   u.nome,
                   v.total_liquido,
                   v.status
            FROM tabela_vendas v
            JOIN tabela_usuarios u ON v.id_usuario = u.id
            ORDER BY v.data_venda DESC
        """;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getLong("id_venda"),
                        rs.getTimestamp("data_venda"),
                        rs.getString("nome"),
                        nf.format(rs.getBigDecimal("total_liquido")),
                        rs.getString("status")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar vendas: " + e.getMessage());
        }
    }

    private void configurarEventos() {

        btnCancelar.addActionListener(e -> cancelarVenda());
    }

    private void cancelarVenda() {

        int row = tabela.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma venda.");
            return;
        }

        long idVenda = (long) model.getValueAt(row, 0);
        String status = (String) model.getValueAt(row, 4);

        if ("CANCELADA".equals(status)) {
            JOptionPane.showMessageDialog(this,
                    "Venda já está cancelada.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente cancelar a venda " + idVenda + "?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            VendaDAO dao = new VendaDAO();
            dao.cancelarVenda(idVenda,
                    Sessao.getUsuarioLogado().getId());

            JOptionPane.showMessageDialog(this,
                    "Venda cancelada com sucesso.");

            carregarVendas();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao cancelar: " + ex.getMessage());
        }
    }
}


