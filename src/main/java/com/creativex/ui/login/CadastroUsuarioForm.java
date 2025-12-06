package com.creativex.ui.login;
import com.creativex.dao.usuario.UsuarioDAO;
import javax.swing.*;
import java.awt.*;


public class CadastroUsuarioForm extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public CadastroUsuarioForm() {
        setTitle("Cadastrar Usuário");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4,1,10,10));

        txtUsuario = new JTextField();
        txtSenha = new JPasswordField();
        JButton btnSalvar = new JButton("Salvar");

        add(new JLabel("Usuário:"));
        add(txtUsuario);
        add(new JLabel("Senha:"));
        add(txtSenha);
        add(btnSalvar);

        btnSalvar.addActionListener(e -> salvar());
    }

    private void salvar() {
        String u = txtUsuario.getText();
        String s = new String(txtSenha.getPassword());

        usuarioDAO.cadastrar(u, s);
        JOptionPane.showMessageDialog(this, "Usuário cadastrado!");
        dispose();
    }
}
