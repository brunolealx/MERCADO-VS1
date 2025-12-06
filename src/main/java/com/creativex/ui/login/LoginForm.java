//Bruno Leal
//creativex sistemas

package com.creativex.ui.login;
import com.creativex.dao.usuario.UsuarioDAO;
import com.creativex.ui.MainWindow;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public LoginForm() {
        setTitle("Login - Mercado VS1");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel painel = new JPanel(new GridLayout(4,2,10,10));
        painel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        txtUsuario = new JTextField();
        txtSenha = new JPasswordField();

        JButton btnLogin = new JButton("Entrar");
        JButton btnCadastrar = new JButton("Cadastrar Usuário");

        painel.add(new JLabel("Usuário:"));
        painel.add(txtUsuario);
        painel.add(new JLabel("Senha:"));
        painel.add(txtSenha);

        painel.add(btnLogin);
        painel.add(btnCadastrar);

        add(painel);

        btnLogin.addActionListener(e -> autenticar());
        btnCadastrar.addActionListener(e -> new CadastroUsuarioForm().setVisible(true));
    }

    private void autenticar() {
        String u = txtUsuario.getText();
        String s = new String(txtSenha.getPassword());

        if (usuarioDAO.login(u, s)) {
            JOptionPane.showMessageDialog(this, "Login realizado com sucesso!");
            dispose();
            new MainWindow().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Usuário ou senha incorretos.");
        }
    }
}
