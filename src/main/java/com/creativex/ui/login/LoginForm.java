package com.creativex.ui.login;

import com.creativex.dao.usuario.UsuarioDAO;
import com.creativex.model.usuario.Usuario;
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
        Usuario usuario = usuarioDAO.autenticar(login, senha);

        if (usuario != null) {
            // // Guarda o utilizador na sessão global
            com.creativex.util.Sessao.usuarioLogado = usuario;

            JOptionPane.showMessageDialog(this, "Bem-vindo, " + usuario.getNome() + "!");

            // Fecha o Login e abre o Menu Principal
            this.dispose();
            new com.creativex.ui.MainWindow().setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Para testar o formulário isoladamente
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
