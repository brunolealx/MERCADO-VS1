package com.creativex.ui.caixas;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class FinalizarVendaDialog extends JDialog {
    private JComboBox<String> cbPagamento;
    private JButton btnConfirmar, btnCancelar;
    private JLabel lblTotal;
    private boolean vendaConfirmada = false;
    private String metodoSelecionado;

    public FinalizarVendaDialog(Frame parent, BigDecimal total) {
        super(parent, "Finalizar Venda", true);
        setLayout(new BorderLayout(15, 15));
        setSize(350, 250);
        setLocationRelativeTo(parent);

        JPanel pnlInfo = new JPanel(new GridLayout(4, 1, 5, 5));
        pnlInfo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        lblTotal = new JLabel(nf.format(total));
        lblTotal.setFont(new Font("Arial", Font.BOLD, 30));
        lblTotal.setHorizontalAlignment(JLabel.CENTER);
        lblTotal.setForeground(new Color(0, 102, 0));

        cbPagamento = new JComboBox<>(new String[]{"Dinheiro", "Cartão de Crédito", "Cartão de Débito", "PIX"});
        cbPagamento.setFont(new Font("Arial", Font.PLAIN, 18));

        pnlInfo.add(new JLabel("TOTAL A PAGAR:"));
        pnlInfo.add(lblTotal);
        pnlInfo.add(new JLabel("FORMA DE PAGAMENTO:"));
        pnlInfo.add(cbPagamento);

        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnConfirmar = new JButton("Confirmar");
        btnCancelar = new JButton("Cancelar");

        btnConfirmar.addActionListener(e -> {
            vendaConfirmada = true;
            metodoSelecionado = (String) cbPagamento.getSelectedItem();
            dispose();
        });

        btnCancelar.addActionListener(e -> dispose());

        pnlBotoes.add(btnCancelar);
        pnlBotoes.add(btnConfirmar);

        add(pnlInfo, BorderLayout.CENTER);
        add(pnlBotoes, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnConfirmar);
    }

    public boolean isVendaConfirmada() { return vendaConfirmada; }
    public String getMetodoSelecionado() { return metodoSelecionado; }
}