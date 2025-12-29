// Bruno Leal
// creativex sistemas

package com.creativex;

import com.creativex.ui.login.LoginForm;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        // 1. Opcional: Deixa o visual com as cores do Windows/Sistema Operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. Inicia o sistema obrigatoriamente pela tela de Login
        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}