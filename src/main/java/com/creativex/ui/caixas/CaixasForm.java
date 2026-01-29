package com.creativex.ui.caixas;

import com.creativex.model.venda.ItemVenda;
import com.creativex.model.venda.Venda;
import com.creativex.model.produto.Produto;
import com.creativex.dao.produto.ProdutoDAO;
import com.creativex.dao.caixa.VendaDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;

public class CaixasForm extends JPanel {
    private JTextField txtCodigoBarras, txtQuantidade, txtTotalVenda;
    private JTable tableCarrinho;
    private DefaultTableModel modelCarrinho;
    private JButton btnFinalizar, btnRemoverItem;

    // Objetos de controle
    private Venda vendaAtual = new Venda();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final VendaDAO vendaDAO = new VendaDAO();
    private final NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public CaixasForm() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        initComponents();
        configurarAtalhos();
    }

    private void initComponents() {
        // --- TOPO: ENTRADA ---
        JPanel pnlTopo = new JPanel(new GridLayout(1, 4, 10, 10));
        txtCodigoBarras = new JTextField();
        txtCodigoBarras.setFont(new Font("SansSerif", Font.BOLD, 18));

        txtQuantidade = new JTextField("1");
        txtQuantidade.setFont(new Font("SansSerif", Font.BOLD, 18));

        pnlTopo.add(new JLabel("CÓDIGO DE BARRAS / ID:"));
        pnlTopo.add(txtCodigoBarras);
        pnlTopo.add(new JLabel("QUANTIDADE:"));
        pnlTopo.add(txtQuantidade);

        // --- CENTRO: TABELA ---
        modelCarrinho = new DefaultTableModel(new String[]{"Item", "Cód", "Descrição", "Qtd", "V. Unit", "Subtotal"}, 0);
        tableCarrinho = new JTable(modelCarrinho);
        tableCarrinho.setRowHeight(30);
        tableCarrinho.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // --- DIREITA: TOTAIS E AÇÕES ---
        JPanel pnlAcoes = new JPanel();
        pnlAcoes.setLayout(new BoxLayout(pnlAcoes, BoxLayout.Y_AXIS));
        pnlAcoes.setPreferredSize(new Dimension(250, 0));

        txtTotalVenda = new JTextField("R$ 0,00");
        txtTotalVenda.setEditable(false);
        txtTotalVenda.setBackground(Color.BLACK);
        txtTotalVenda.setForeground(Color.GREEN);
        txtTotalVenda.setFont(new Font("Monospaced", Font.BOLD, 35));
        txtTotalVenda.setHorizontalAlignment(JTextField.CENTER);

        btnFinalizar = new JButton("FINALIZAR (F12)");
        btnFinalizar.setBackground(new Color(0, 153, 51));
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.setFont(new Font("SansSerif", Font.BOLD, 16));

        btnRemoverItem = new JButton("Remover Item (DEL)");

        pnlAcoes.add(new JLabel("TOTAL DA VENDA:"));
        pnlAcoes.add(txtTotalVenda);
        pnlAcoes.add(Box.createRigidArea(new Dimension(0, 20)));
        pnlAcoes.add(btnFinalizar);
        pnlAcoes.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlAcoes.add(btnRemoverItem);

        add(pnlTopo, BorderLayout.NORTH);
        add(new JScrollPane(tableCarrinho), BorderLayout.CENTER);
        add(pnlAcoes, BorderLayout.EAST);

        configurarEventos();
    }

    private void configurarEventos() {
        txtCodigoBarras.addActionListener(e -> adicionarProdutoPeloCodigo());
        btnFinalizar.addActionListener(e -> finalizarVenda());
        btnRemoverItem.addActionListener(e -> removerItemSelecionado());
    }

    private void adicionarProdutoPeloCodigo() {
        String filtro = txtCodigoBarras.getText().trim();
        BigDecimal qtd;

        try {
            qtd = new BigDecimal(txtQuantidade.getText().replace(",", "."));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida!");
            return;
        }

        if (filtro.isEmpty()) return;

        try {
            Produto p = produtoDAO.buscarPorCodigoOuId(filtro);

            if (p != null) {
                ItemVenda item = new ItemVenda(p.getId(), p.getDescricao(), qtd, p.getPrecoVenda());
                vendaAtual.adicionarItem(item);

                modelCarrinho.addRow(new Object[]{
                        modelCarrinho.getRowCount() + 1,
                        p.getId(),
                        p.getDescricao(),
                        qtd,
                        nf.format(p.getPrecoVenda()),
                        nf.format(item.getSubtotal())
                });

                atualizarExibicaoTotal();
                limparCamposInput();
            } else {
                JOptionPane.showMessageDialog(this, "Produto não encontrado!");
                txtCodigoBarras.selectAll();
                txtCodigoBarras.requestFocus();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao acessar banco: " + ex.getMessage());
        }
    }

    private void finalizarVenda() {
        if (vendaAtual.getItens().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há itens no carrinho!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        FinalizarVendaDialog dialog = new FinalizarVendaDialog((Frame) parentWindow, vendaAtual.getTotalVenda());
        dialog.setVisible(true);

        if (dialog.isVendaConfirmada()) {
            try {
                vendaAtual.setMetodoPagamento(dialog.getMetodoSelecionado());
                vendaAtual.setIdUsuario(1L); // ID fixo temporário

                vendaDAO.finalizarVenda(vendaAtual);

                JOptionPane.showMessageDialog(this, "Venda concluída com sucesso!");
                limparVenda();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro crítico ao gravar venda: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removerItemSelecionado() {
        int row = tableCarrinho.getSelectedRow(); // Pega a linha selecionada pelo mouse

        if (row != -1) {
            // 1. Remove da lista lógica (Model)
            vendaAtual.getItens().remove(row);

            // 2. Remove da tabela visual (UI)
            modelCarrinho.removeRow(row);

            // 3. Recalcula o total
            vendaAtual.calcularTotal();
            atualizarExibicaoTotal();

            // 4. Reorganiza os números da coluna "Item" (1, 2, 3...)
            for (int i = 0; i < modelCarrinho.getRowCount(); i++) {
                modelCarrinho.setValueAt(i + 1, i, 0);
            }

            txtCodigoBarras.requestFocus();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um item na tabela para remover.");
        }
    }

    private void atualizarExibicaoTotal() {
        vendaAtual.calcularTotal(); // Garante que o total está atualizado no objeto
        txtTotalVenda.setText(nf.format(vendaAtual.getTotalVenda()));
    }

    private void limparCamposInput() {
        txtCodigoBarras.setText("");
        txtQuantidade.setText("1");
        txtCodigoBarras.requestFocus();
    }

    private void limparVenda() {
        vendaAtual = new Venda();
        modelCarrinho.setRowCount(0);
        txtTotalVenda.setText("R$ 0,00");
        limparCamposInput();
    }

    private void configurarAtalhos() {
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0), "finalizar");
        this.getActionMap().put("finalizar", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                finalizarVenda();
            }
        });
    }
}