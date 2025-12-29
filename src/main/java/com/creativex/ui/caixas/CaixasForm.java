package com.creativex.ui.caixas;
import com.creativex.ui.MainWindow;
import com.creativex.dao.produto.ProdutoDAO;
import com.creativex.model.produto.Produto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class CaixasForm extends JPanel {
    private JTextField txtCodigoBarra;
    private JTable tabelaItens;
    private DefaultTableModel model;
    private JLabel lblTotal;
    private BigDecimal totalVenda = BigDecimal.ZERO;
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    public CaixasForm() {
        // Define o layout principal como BorderLayout para preencher o espaço do menu
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Margens internas

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // --- Painel Superior: Entrada de Dados ---
        JPanel painelSuperior = new JPanel(new BorderLayout());
        JLabel lblCodigo = new JLabel("Código de Barras (Pressione ENTER para adicionar):");
        lblCodigo.setFont(new Font("SansSerif", Font.BOLD, 14));

        txtCodigoBarra = new JTextField();
        txtCodigoBarra.setFont(new Font("SansSerif", Font.PLAIN, 22));

        painelSuperior.add(lblCodigo, BorderLayout.NORTH);
        painelSuperior.add(txtCodigoBarra, BorderLayout.CENTER);
        add(painelSuperior, BorderLayout.NORTH);

        // --- Painel Central: Tabela de Itens ---
        String[] colunas = {"Item", "Cód. Barras", "Descrição", "Qtd", "V. Unit", "Subtotal"};
        model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaItens = new JTable(model);
        tabelaItens.setRowHeight(25);
        JScrollPane scroll = new JScrollPane(tabelaItens);
        add(scroll, BorderLayout.CENTER);

        // --- Painel Inferior: Total e Finalização ---
        JPanel painelInferior = new JPanel(new BorderLayout());
        lblTotal = new JLabel("TOTAL: R$ 0,00");
      //  lblTotal.setFont(new Font("SansSerif", Font.BOLD, 36));
      //  lblTotal.setForeground(new Color(0, 128, 0));
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 48)); // Fonte bem grande
        lblTotal.setForeground(new Color(0, 100, 0)); // Verde escuro para o preço

        JButton btnCancelar = new JButton("Cancelar Item (F5)");
        JButton btnFechar = new JButton("Fechar Caixa (ESC)");

        JButton btnFinalizar = new JButton("Finalizar Venda (F10)");
        btnFinalizar.setPreferredSize(new Dimension(200, 50));
        btnFinalizar.addActionListener(e -> finalizarVenda());
        painelInferior.add(lblTotal, BorderLayout.EAST);
        painelInferior.add(btnFinalizar, BorderLayout.WEST);

        add(painelInferior, BorderLayout.SOUTH);
        painelInferior.add(btnCancelar);
        painelInferior.add(btnFechar);
        // --- EVENTOS ---
        btnCancelar.addActionListener(e -> cancelarItem());
        btnFechar.addActionListener(e -> fecharCaixa());

// Configurar Atalhos
        configurarAtalho(KeyEvent.VK_F5, "cancelar", e -> cancelarItem());
        configurarAtalho(KeyEvent.VK_ESCAPE, "fechar", e -> fecharCaixa());
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), "finalizar");
        this.getActionMap().put("finalizar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finalizarVenda();
            }

        });
        txtCodigoBarra.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    adicionarProdutoPeloCodigo(txtCodigoBarra.getText());
                }
            }
        });
        //
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), "finalizar");

        this.getActionMap().put("finalizar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finalizarVenda();
            }
        });
    }

    //Configurar Atalho====
    private void configurarAtalho(int tecla, String nomeAcao, java.util.function.Consumer<java.awt.event.ActionEvent> acao) {
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(tecla, 0), nomeAcao);

        this.getActionMap().put(nomeAcao, new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                acao.accept(e);
            }
        });
    }
    //==finaliza venda no caixa===
    private void finalizarVenda() {
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Carrinho vazio!");
            return;
        }

        int op = JOptionPane.showConfirmDialog(this, "Finalizar Venda?", "Confirmação", JOptionPane.YES_NO_OPTION);

        if (op == JOptionPane.YES_OPTION) {
            try {
                for (int i = 0; i < model.getRowCount(); i++) {
                // Agora a coluna 1 é o Código de Barras e a coluna 3 é a Quantidade
                    String cb = model.getValueAt(i, 1).toString();

                    // Garantir que a quantidade seja lida corretamente como BigDecimal
                    Object valorQtd = model.getValueAt(i, 3);
                    BigDecimal qtd = (valorQtd instanceof BigDecimal) ? (BigDecimal) valorQtd : new BigDecimal(valorQtd.toString());

                    Produto p = produtoDAO.buscarPorCodigoBarra(cb);

                        if (p != null) {
                            // Pega o ID do usuário da sessão atual
                            long idUser = com.creativex.util.Sessao.usuarioLogado.getId();

                            // Chama o novo método específico de venda que criamos acima
                            produtoDAO.registrarVenda(p.getId(), qtd, idUser);
                        }
                    }

                JOptionPane.showMessageDialog(this, "Venda realizada com sucesso!");
                limparCarrinho();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao finalizar: " + ex.getMessage());
            }
        }
    }
    //===Cancelar ítem====
    private void cancelarItem() {
        int linhaSelecionada = tabelaItens.getSelectedRow();

        if (linhaSelecionada >= 0) {
            // Recupera o valor do subtotal da linha para subtrair do total geral
            // O subtotal está na última coluna (índice 5 no nosso padrão)
            String valorString = model.getValueAt(linhaSelecionada, 5).toString()
                    .replace("R$", "").replace(".", "").replace(",", ".").trim();
            BigDecimal valorItem = new BigDecimal(valorString);

            // Remove a linha e atualiza o total
            model.removeRow(linhaSelecionada);
            totalVenda = totalVenda.subtract(valorItem);
            lblTotal.setText("TOTAL: " + formatarMoeda(totalVenda));

            // Reorganiza os números dos itens na primeira coluna
            for (int i = 0; i < model.getRowCount(); i++) {
                model.setValueAt(i + 1, i, 0);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um item na tabela para cancelar.");
        }
    }

    //==Fechar caixa==
    private void fecharCaixa() {
        if (model.getRowCount() > 0) {
            int op = JOptionPane.showConfirmDialog(this,
                    "Existe uma venda em curso. Deseja cancelar tudo e fechar o caixa?",
                    "Aviso", JOptionPane.YES_NO_OPTION);
            if (op != JOptionPane.YES_OPTION) return;
        }

        // Retorna para a HomeScreen da MainWindow
        MainWindow main = (MainWindow) SwingUtilities.getWindowAncestor(this);
        main.abrirModulo(new com.creativex.ui.HomeScreen());
    }
    //
    private void limparCarrinho() {
        model.setRowCount(0);
        totalVenda = BigDecimal.ZERO;
        lblTotal.setText("TOTAL: R$ 0,00");
        txtCodigoBarra.requestFocus();
    }
    //
    private void adicionarProdutoPeloCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) return;

        Produto p = produtoDAO.buscarPorCodigoBarra(codigo);

        if (p != null) {
            BigDecimal qtd = BigDecimal.ONE;
            BigDecimal subtotal = p.getPrecoVenda().multiply(qtd);

            // Adiciona na tabela
            Object[] linha = {
                    model.getRowCount() + 1,
                    p.getCodigoBarra(),
                    p.getDescricao(),
                    qtd,
                    formatarMoeda(p.getPrecoVenda()),
                    formatarMoeda(subtotal)
            };
            model.addRow(linha);

            // --- CORREÇÃO 1: ATUALIZAR O TOTAL ---
            totalVenda = totalVenda.add(subtotal); // Acumula o valor
            lblTotal.setText("TOTAL: " + formatarMoeda(totalVenda)); // Atualiza o display visual

            // --- CORREÇÃO 2: LIMPAR E FOCAR O CAMPO ---
            txtCodigoBarra.setText(""); // Limpa o texto anterior
            txtCodigoBarra.requestFocus(); // Garante que o cursor volte para lá

        } else {
            JOptionPane.showMessageDialog(this, "Produto não encontrado: " + codigo);
            txtCodigoBarra.selectAll();
            txtCodigoBarra.requestFocus();
        }
    }

    private String formatarMoeda(BigDecimal valor) {
        return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(valor);
    }
}