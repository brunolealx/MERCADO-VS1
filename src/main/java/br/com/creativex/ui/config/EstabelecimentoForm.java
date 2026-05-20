package br.com.creativex.ui.config;

import br.com.creativex.domain.entity.config.Estabelecimento;
import br.com.creativex.infrastructure.persistence.repository.estabelecimento.EstabelecimentoDAO;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;

public class EstabelecimentoForm extends JDialog {

    private JTextField txtRazaoSocial = new JTextField(30);
    private JTextField txtNomeFantasia = new JTextField(30);
    private JTextField txtCnpj = new JTextField(18);
    private JTextField txtIE = new JTextField(15);
    private JTextField txtLogradouro = new JTextField(30);
    private JTextField txtNumero = new JTextField(10);
    private JTextField txtBairro = new JTextField(20);
    private JTextField txtCidade = new JTextField(20);
    private JTextField txtEstado = new JTextField(2);
    private JTextField txtCep = new JTextField(8);
    private JTextField txtIbge = new JTextField(7);
    private JComboBox<Integer> comboRegime = new JComboBox<>(new Integer[]{1, 2, 3});
    private JTextField txtAliqIbpt = new JTextField(10);

    private EstabelecimentoDAO dao = new EstabelecimentoDAO();

    public EstabelecimentoForm(Frame owner) {
        super(owner, "Dados do Estabelecimento", true);
        setLayout(new BorderLayout());

        JPanel panelFields = new JPanel(new GridLayout(0, 2, 5, 5));
        panelFields.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelFields.add(new JLabel("Razão Social:"));
        panelFields.add(txtRazaoSocial);
        panelFields.add(new JLabel("Nome Fantasia:"));
        panelFields.add(txtNomeFantasia);
        panelFields.add(new JLabel("CNPJ:"));
        panelFields.add(txtCnpj);
        panelFields.add(new JLabel("Insc. Estadual:"));
        panelFields.add(txtIE);
        panelFields.add(new JLabel("Logradouro:"));
        panelFields.add(txtLogradouro);
        panelFields.add(new JLabel("Número:"));
        panelFields.add(txtNumero);
        panelFields.add(new JLabel("Bairro:"));
        panelFields.add(txtBairro);
        panelFields.add(new JLabel("Cidade:"));
        panelFields.add(txtCidade);
        panelFields.add(new JLabel("Estado (UF):"));
        panelFields.add(txtEstado);
        panelFields.add(new JLabel("CEP:"));
        panelFields.add(txtCep);
        panelFields.add(new JLabel("Cód. IBGE Município:"));
        panelFields.add(txtIbge);
        panelFields.add(new JLabel("Regime Tributário (1-SN, 3-Normal):"));
        panelFields.add(comboRegime);
        panelFields.add(new JLabel("Aliq. IBPT (%):"));
        panelFields.add(txtAliqIbpt);

        add(panelFields, BorderLayout.CENTER);

        JPanel panelButtons = new JPanel();
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvar());
        panelButtons.add(btnSalvar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        panelButtons.add(btnCancelar);

        add(panelButtons, BorderLayout.SOUTH);

        carregar();
        pack();
        setLocationRelativeTo(owner);
    }

    private void carregar() {
        try {
            Estabelecimento est = dao.carregarDados();
            if (est != null) {
                txtRazaoSocial.setText(est.getRazaoSocial());
                txtCnpj.setText(est.getCnpj());
                txtIE.setText(est.getInscricaoEstadual());
                txtLogradouro.setText(est.getLogradouro());
                txtNumero.setText(est.getNumero());
                txtBairro.setText(est.getBairro());
                txtCidade.setText(est.getCidade());
                txtEstado.setText(est.getEstado());
                txtCep.setText(est.getCep());
                txtIbge.setText(est.getCodigoMunicipioIbge());
                comboRegime.setSelectedItem(est.getRegimeTributario());
                txtAliqIbpt.setText(est.getAliqIbpt() != null ? est.getAliqIbpt().toString() : "0.00");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage());
        }
    }

    private void salvar() {
        try {
            Estabelecimento est = new Estabelecimento();
            est.setRazaoSocial(txtRazaoSocial.getText());
            est.setCnpj(txtCnpj.getText());
            est.setInscricaoEstadual(txtIE.getText());
            est.setLogradouro(txtLogradouro.getText());
            est.setNumero(txtNumero.getText());
            est.setBairro(txtBairro.getText());
            est.setCidade(txtCidade.getText());
            est.setEstado(txtEstado.getText());
            est.setCep(txtCep.getText());
            est.setCodigoMunicipioIbge(txtIbge.getText());
            est.setRegimeTributario((Integer) comboRegime.getSelectedItem());
            est.setAliqIbpt(new BigDecimal(txtAliqIbpt.getText().replace(",", ".")));

            dao.salvar(est);
            JOptionPane.showMessageDialog(this, "Dados salvos com sucesso!");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar dados: " + e.getMessage());
        }
    }
}
