package com.creativex.ui.clientes;

import com.creativex.dao.cliente.ClienteDAO;
import com.creativex.model.clientepj.Clientepj;
import com.creativex.ui.HomeScreen;
import com.creativex.ui.MainWindow;
import com.creativex.model.cliente.Cliente;

//imports para formatação de campos
import javax.swing.text.MaskFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.JFormattedTextField;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ClientesForm extends JPanel {

    // Campos
    private JTextField txtId, txtNome, txtRgIe, txtEmail;

    private JFormattedTextField txtCpf;
    private JFormattedTextField txtTelefone;
    private JFormattedTextField txtCep;
    private JFormattedTextField txtLimiteCredito;

    private JTextField txtEndereco, txtNumero, txtBairro, txtCidade, txtUf;
    // Botões
    private JButton btnNovo, btnSalvar, btnAtualizar, btnBuscar, btnListar, btnVoltar;

    // Tabela
    private JTable table;
    private DefaultTableModel model;

    // DAO
    private final ClienteDAO dao;

    public ClientesForm() {
        dao = new ClienteDAO();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        initComponents();
        bindEvents();
    }

    // ================== UI ==================
    private void initComponents() {

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Dados do Cliente", criarPainelCliente());
        add(abas, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{
                "ID", "Nome", "CPF/CNPJ", "Telefone", "Cidade"
        }, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));

        btnNovo = new JButton("Novo");
        btnSalvar = new JButton("Salvar");
        btnAtualizar = new JButton("Atualizar");
        btnBuscar = new JButton("Buscar");
        btnListar = new JButton("Listar");

        btnVoltar = new JButton("Voltar");

        pnlBotoes.add(btnNovo);
        pnlBotoes.add(btnSalvar);
        pnlBotoes.add(btnAtualizar);
        pnlBotoes.add(btnBuscar);
        pnlBotoes.add(btnListar);
        pnlBotoes.add(btnVoltar);

        add(pnlBotoes, BorderLayout.SOUTH);

        btnSalvar.setVisible(false);
        btnAtualizar.setEnabled(false);
    }

    private JPanel criarPainelCliente() {

        JPanel p = new JPanel(new GridLayout(13, 1, 6, 6));
        p.setBorder(BorderFactory.createTitledBorder("Cadastro"));

        txtId = new JTextField();       addCampo(p, "ID", txtId);
        txtId.setEditable(false);

        txtNome = new JTextField();     addCampo(p, "Nome*", txtNome);
        txtRgIe = new JTextField();     addCampo(p, "RG / IE", txtRgIe);

        txtCpf = new JFormattedTextField();   addCampo(p, "CPF / CNPJ*", txtCpf);
        txtCpf.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                aplicarMascaraCpf();
            }
        });

        txtTelefone = new JFormattedTextField(criarMascara("(##) #####-####"));
        addCampo(p, "Telefone", txtTelefone);
        txtEmail = new JTextField();    addCampo(p, "Email", txtEmail);
        txtEndereco = new JTextField(); addCampo(p, "Endereço", txtEndereco);
        txtNumero = new JTextField();   addCampo(p, "Número", txtNumero);
        txtBairro = new JTextField();   addCampo(p, "Bairro", txtBairro);
        txtCidade = new JTextField();   addCampo(p, "Cidade*", txtCidade);
        txtCep = new JFormattedTextField(criarMascara("#####-###")); addCampo(p, "CEP", txtCep);
        txtUf = new JTextField();       addCampo(p, "UF", txtUf);
        NumberFormat moeda = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));
        txtLimiteCredito = new JFormattedTextField(moeda);
        txtLimiteCredito.setValue(BigDecimal.ZERO);

        addCampo(p, "Limite Crédito (R$)", txtLimiteCredito);

        return p;
    }

    private void addCampo(JPanel p, String label, JTextField campo) {
        p.add(new JLabel(label));
        p.add(campo);
    }

    // ================== EVENTOS ==================
    private void bindEvents() {

        btnNovo.addActionListener(e -> modoNovo());
        btnSalvar.addActionListener(e -> salvar());
        btnAtualizar.addActionListener(e -> atualizar());
        btnBuscar.addActionListener(e -> buscar());
        btnListar.addActionListener(e -> listar());
        btnVoltar.addActionListener(e -> voltar());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    carregarDaTabela(row);
                    modoEdicao();
                }
            }
        });
    }

    // ================== AÇÕES ==================
    private void modoNovo() {
        limparCampos();
        txtId.setText("");
        btnSalvar.setVisible(true);
        txtNome.requestFocus();
    }

    private void modoEdicao() {
        btnSalvar.setVisible(false);
        btnAtualizar.setEnabled(true);
    }

    private void salvar() {

        if (!validar()) return;

        try {
            Cliente c = criarCliente();
            dao.inserir(c);

            JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!");
            listar();
            modoEdicao();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage());
        }
    }

    private void atualizar() {

        if (txtId.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente.");
            return;
        }

        try {
            Cliente c = criarCliente();
            c.setId(Long.parseLong(txtId.getText()));
            dao.atualizar(c);

            JOptionPane.showMessageDialog(this, "Cliente atualizado!");
            listar();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + e.getMessage());
        }
    }
//=======Buscar======
    private void buscar() {

        String filtro = JOptionPane.showInputDialog(this, "Digite ID, Nome ou CPF/CNPJ:");
        if (filtro == null || filtro.isBlank()) return;
        String somenteNumeros = filtro.replaceAll("\\D", "");
        try {
            model.setRowCount(0);
            if (somenteNumeros.matches("\\d+") && somenteNumeros.length() <= 9) {
                Cliente c = dao.buscarPorId(Long.parseLong(somenteNumeros));
                if (c != null) {
                    preencherCampos(c);
                    modoEdicao(); //  habilita atualização
                } else {
                    JOptionPane.showMessageDialog(this, "Fornecedor não encontrado.");
                }
                return;
            }

            List<Cliente> lista = dao.buscarPorFiltro(filtro);
            model.setRowCount(0);

            for (Cliente c : lista) {
                model.addRow(new Object[]{
                        c.getId(), c.getNome(), c.getCpf(),
                        c.getTelefone(), c.getCidade()
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro na busca: " + e.getMessage());
        }
    }

    private void listar() {
        try {
            model.setRowCount(0);
            List<Cliente> lista = dao.listarPorIdLimite(1, 10);
            for (Cliente c : lista) {
                model.addRow(new Object[]{
                        c.getId(), c.getNome(), c.getCpf(),
                        c.getTelefone(), c.getCidade()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao listar.");
        }
    }

    // ================== UTIL ==================
    private MaskFormatter criarMascara(String mascara) {
        try {
            MaskFormatter mf = new MaskFormatter(mascara);
            mf.setPlaceholderCharacter('_');
            mf.setValueContainsLiteralCharacters(false);
            return mf;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    //===
    private void aplicarMascaraCpf() {
        String valor = txtCpf.getText().replaceAll("\\D", "");

        try {
            if (valor.length() <= 11) {
                txtCpf.setFormatterFactory(
                        new DefaultFormatterFactory(criarMascara("###.###.###-##"))
                );
            } else {
                txtCpf.setFormatterFactory(
                        new DefaultFormatterFactory(criarMascara("##.###.###/####-##"))
                );
            }
            txtCpf.setValue(valor);
        } catch (Exception ignored) {}
    }
//===
// CRIAR OS FORMATADORES (MÁSCARAS)
//===
    private Cliente criarCliente() {

        Cliente c = new Cliente();
        c.setNome(txtNome.getText());
        c.setCpf(txtCpf.getText().replaceAll("\\D", ""));
        c.setRg(txtRgIe.getText());
        c.setTelefone(txtTelefone.getText().replaceAll("\\D", ""));
        c.setEmail(txtEmail.getText());
        c.setEndereco(txtEndereco.getText());
        c.setNumero(txtNumero.getText());
        c.setBairro(txtBairro.getText());
        c.setCidade(txtCidade.getText());
        c.setUf(txtUf.getText());
        c.setCep(txtCep.getText().replaceAll("\\D", ""));
        c.setLimiteCredito(parseBig(txtLimiteCredito.getText()));
        return c;
    }

    private void preencherCampos(Cliente c) {

        txtId.setText(String.valueOf(c.getId()));
        txtNome.setText(c.getNome());
        txtCpf.setText(c.getCpf());
        aplicarMascaraCpf();

        txtRgIe.setText(c.getRg());
        txtTelefone.setText(c.getTelefone());
        txtEmail.setText(c.getEmail());
        txtEndereco.setText(c.getEndereco());
        txtNumero.setText(c.getNumero());
        txtBairro.setText(c.getBairro());
        txtCidade.setText(c.getCidade());
        txtUf.setText(c.getUf());
        txtCep.setText(c.getCep());
        txtLimiteCredito.setText(String.valueOf(c.getLimiteCredito()));
    }

    private void carregarDaTabela(int row) {
        txtId.setText(String.valueOf(model.getValueAt(row, 0)));
        txtNome.setText(String.valueOf(model.getValueAt(row, 1)));
        txtCpf.setText(String.valueOf(model.getValueAt(row, 2)));
        txtTelefone.setText(String.valueOf(model.getValueAt(row, 3)));
        txtCidade.setText(String.valueOf(model.getValueAt(row, 4)));
    }

    private boolean validar() {

        if (txtNome.getText().isBlank()) {
           JOptionPane.showMessageDialog(this, "Nome é obrigatório.");
           txtNome.requestFocus();
            return false;
        }

        if (txtCidade.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Cidade é obrigatória.");
            return false;
        }

        return true;
    }

    private BigDecimal parseBig(String valor) {
        try {
            valor = valor.replace("R$", "")
                    .replace(".", "")
                    .replace(",", ".")
                    .trim();
            return new BigDecimal(valor);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private void limparCampos() {
        for (Component c : getComponents()) limparRec(c);
    }

    private void limparRec(Component c) {
        if (c instanceof JTextField tf) tf.setText("");
        if (c instanceof Container ct)
            for (Component cc : ct.getComponents()) limparRec(cc);
    }

    private void voltar() {
        MainWindow mw = getMainWindow();
        if (mw != null) mw.abrirModulo(new HomeScreen());
    }

    private MainWindow getMainWindow() {
        Container p = getParent();
        while (p != null) {
            if (p instanceof MainWindow mw) return mw;
            p = p.getParent();
        }
        return null;
    }
}
