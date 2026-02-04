package com.creativex.ui.login;

import com.creativex.dao.usuario.UsuarioDAO;
import com.creativex.model.usuario.Usuario;
import com.creativex.ui.MainWindow;
import com.creativex.util.Sessao;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginForm extends JFrame {
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public LoginForm() {
        setTitle("MERCADO-VS1 - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza na tela
        setLayout(new GridLayout(5, 1, 10, 10));


        // Estilização básica
        add(new JLabel("  Login:", SwingConstants.LEFT));
        txtLogin = new JTextField();
        add(txtLogin);

        add(new JLabel("  Senha:", SwingConstants.LEFT));
        txtSenha = new JPasswordField();
        add(txtSenha);

        btnEntrar = new JButton("Entrar");
        add(btnEntrar);

        // Ação do Botão
        btnEntrar.addActionListener(this::efetuarLogin);

        // Permite dar Enter para logar
        getRootPane().setDefaultButton(btnEntrar);
    }

    private void efetuarLogin(ActionEvent e) {
        String login = txtLogin.getText();
        String senha = new String(txtSenha.getPassword());

        if (login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }

        // Chama o DAO que criamos anteriormente
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        Usuario usuario = usuarioDAO.autenticar(
                txtLogin.getText(),
                new String(txtSenha.getPassword())
        );

        if (usuario != null) {
            Sessao.login(usuario);          // 🔥 aqui está o ponto-chave
            new MainWindow().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Login ou senha inválidos.");
        }
    }
    public static void main(String[] args) {
        // Para testar o formulário isoladamente
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
