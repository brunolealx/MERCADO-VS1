//Bruno Leal
//creativex sistemas
package com.creativex.ui;

import com.creativex.ui.produtos.ProdutoForm;
import com.creativex.ui.estoque.EstoqueForm;
import com.creativex.ui.clientes.ClientesForm;
import com.creativex.ui.clientepj.ClientepjForm;
import com.creativex.ui.caixas.CaixaForm;
import com.creativex.ui.ajuda.AjudaForm;
//import com.creativex.ui.listagens.ListagensForm;

import javax.swing.*;
import java.awt.*;

/**
 * Janela principal do sistema.
 * - Área central para carregar módulos (JPanel)
 * - Menu inferior com botões
 */
public class MainWindow extends JFrame {

    private final JPanel painelConteudo;
    private final JPanel bottomMenu;

    public MainWindow() {
        setTitle("📌 MERCADO-VS1 — Sistema de Gestão");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1300, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int response = JOptionPane.showConfirmDialog(MainWindow.this,
                        "Deseja sair do Sistema?", "Confirmação",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        // Painel central onde os módulos serão carregados
        painelConteudo = new JPanel(new BorderLayout());
        add(painelConteudo, BorderLayout.CENTER);

        // Menu inferior
        bottomMenu = criarBottomMenu();
        add(bottomMenu, BorderLayout.SOUTH);
        
        //abrir HomeScreen com o Logo de fundo
        abrirModulo(new HomeScreen());

    }

    /**
     * Cria o painel do menu na parte inferior, com 2 linhas.
     */
    private JPanel criarBottomMenu() {

        // Painel igual ao padrão ProdutoForm
        JPanel menu = new JPanel();
        menu.setBorder(BorderFactory.createTitledBorder("Menu principal"));

        // FlowLayout igual ao ProdutoForm (coloca botões lado a lado automaticamente)
        menu.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 10));

        // Altura maior para suportar 2 linhas no futuro
        menu.setPreferredSize(new Dimension(0, 90));

        // Botões com mesmo estilo do ProdutoForm (simples, sem emojis, sem estilo custom)
        JButton btnProdutos     = new JButton("Produtos");
        JButton btnEstoque      = new JButton("Estoque");
        JButton btnclientes     = new JButton("clientes");
        JButton btnclientepj     = new JButton("clientesPJ");
        JButton btnFornecedores = new JButton("Fornecedores");
        JButton btnCaixas       = new JButton("Caixas");
        JButton btnImpressoras  = new JButton("Impressoras");
        JButton btnAjuda        = new JButton("Ajuda");
        JButton btnSairSistema  = new JButton("Sair do sistema");
        // Adicionar botões (ordem igual à original)
        menu.add(btnProdutos);
        menu.add(btnEstoque);
        menu.add(btnclientes);
        menu.add(btnclientepj);
        menu.add(btnFornecedores);
        menu.add(btnCaixas);
        menu.add(btnImpressoras);
        menu.add(btnAjuda);
        menu.add(btnSairSistema);

        // === AÇÕES ORIGINAIS ===
        btnProdutos.addActionListener(e -> abrirModulo(new ProdutoForm()));
        btnEstoque.addActionListener(e -> abrirModulo(new EstoqueForm()));
        btnclientes.addActionListener(e -> abrirModulo(new ClientesForm()));
        btnclientepj.addActionListener(e -> abrirModulo(new ClientepjForm()));

        btnFornecedores.addActionListener(e -> abrirModulo(new com.creativex.ui.fornecedor.FornecedoresForm()));
        btnCaixas.addActionListener(e -> abrirModulo(new CaixaForm()));
        btnImpressoras.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Módulo Impressoras em desenvolvimento"));
        btnAjuda.addActionListener(e -> abrirModulo(new AjudaForm()));
        btnSairSistema.addActionListener(e -> confirmarSaida());

        return menu;
    }

    private JButton criarBotaoMenu(String texto) {
        JButton b = new JButton(texto);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setPreferredSize(new Dimension(160, 40));
        // aparência leve:
        b.setBackground(new Color(255, 255, 255));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200,200), 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        return b;
    }

    /**
     * Remove o conteúdo atual e adiciona o painel do módulo.
     * O painel passado deve ser um JPanel (cada módulo: ProdutoForm, clientesForm, ...)
     */
    public void abrirModulo(JPanel painel) {
        painelConteudo.removeAll();
        painelConteudo.add(painel, BorderLayout.CENTER);
        painelConteudo.revalidate();
        painelConteudo.repaint();
    }

    /**
     * Saida do sistema.
     *
     */
    public void confirmarSaida() {
        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente sair do sistema?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (resposta == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

}





