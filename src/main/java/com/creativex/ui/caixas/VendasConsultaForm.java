//Bruno Leal
//creativex sistemas
package com.creativex.ui.caixas;
import com.creativex.dao.caixa.VendaDAO;
import com.creativex.db.Conexao;
import com.creativex.util.Sessao;
import com.creativex.ui.HomeScreen;
import com.creativex.ui.MainWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.NumberFormat;
import java.util.Locale;

public class VendasConsultaForm extends JPanel {

    private JTable tabela;
    private DefaultTableModel model;
    private JButton btnCancelar, btnVoltar;
    private MainWindow mainWindow;

    private final NumberFormat nf =
            NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public VendasConsultaForm() {
        setLayout(new BorderLayout(10, 10));
        this.mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(this);
        initComponents();
        carregarVendas();
        configurarEventos();
    }
    
    public VendasConsultaForm(MainWindow mainWindow) {
        setLayout(new BorderLayout(10, 10));
        this.mainWindow = mainWindow;
        initComponents();
        carregarVendas();
        configurarEventos();
    }

    private void initComponents() {
        model = new DefaultTableModel(
                new String[]{"ID", "Data", "Operador", "Total", "Status"}, 0);

        tabela = new JTable(model);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCancelar = new JButton("Cancelar Venda");
        btnVoltar = new JButton("Voltar");
        
        pnlBotoes.add(btnCancelar);
        pnlBotoes.add(btnVoltar);
        
        add(pnlBotoes, BorderLayout.SOUTH);
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
        btnVoltar.addActionListener(e -> voltarParaHome());
    }
    
    private void voltarParaHome() {
        if (mainWindow != null) {
            mainWindow.abrirModulo(new HomeScreen());
        }
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


