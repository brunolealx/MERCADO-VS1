//Bruno Leal
//creativex sistemas

package com.creativex.ui;

import javax.swing.*;
import java.awt.*;

public class HomeScreen extends JPanel {

    public HomeScreen() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // Painel central
        JPanel centro = new JPanel();
        centro.setBackground(new Color(245, 245, 245));
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        ImageIcon icon = null;
        try {
            icon = new ImageIcon(getClass().getResource("/img/logo.png"));
        } catch (Exception e) {
            System.out.println("Logo não encontrada!");
        }

        JLabel lblLogo = new JLabel(icon);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // TÍTULO
        JLabel lblTitulo = new JLabel("MERCADO VS1");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(new Color(60, 60, 60));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // SUBTÍTULO
        JLabel lblSub = new JLabel("Sistema de Gestão Comercial");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblSub.setForeground(new Color(90, 90, 90));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        centro.add(lblLogo);
        centro.add(Box.createRigidArea(new Dimension(0, 20)));
        centro.add(lblTitulo);
        centro.add(Box.createRigidArea(new Dimension(0, 10)));
        centro.add(lblSub);

        add(centro, BorderLayout.CENTER);
    }
}
