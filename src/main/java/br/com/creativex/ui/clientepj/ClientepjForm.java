// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

// Peracio Dias
//creativex sistemas
package br.com.creativex.ui.clientepj;

import br.com.creativex.domain.entity.cliente.Cliente;
import br.com.creativex.presentation.controller.ClienteController;
import br.com.creativex.ui.HomeScreen;
import br.com.creativex.ui.MainWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

/**
 * Formulário de Cadastro de Clientes PJ (Pessoa Jurídica) utilizando a entidade unificada.
 * 
 * @author Peracio Dias
 * @version 2.0
 * @since 2026-05-26
 */
public class ClientepjForm extends JPanel {

    private JTextField txtId, txtRazaoSocial, txtNomeFantasia, txtIe, txtEmail;
    private JFormattedTextField txtCnpj, txtTelefone, txtCep, txtLimiteCredito;
    private JTextField txtEndereco, txtNumero, txtComplemento, txtBairro, txtCidade, txtUf;

    private JButton btnNovo, btnSalvar, btnAtualizar, btnBuscar, btnListar, btnVoltar;

    private JTable table;
    private DefaultTableModel model;

    private final ClienteController controller = br.com.creativex.config.AppFactory.clienteController();

    public ClientepjForm() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        initComponents();
        bindEvents();
    }

    private void initComponents() {

        model = new DefaultTableModel(new String[]{
                "ID", "Razão Social", "CNPJ", "Telefone", "Cidade"
        }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);

        add(criarPainel(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(criarPainelBotoes(), BorderLayout.SOUTH);

        btnSalvar.setVisible(false);
        btnAtualizar.setEnabled(false);
    }

    private JPanel criarPainel() {

        JPanel p = new JPanel(new GridLayout(18, 1, 6, 6));
        p.setBorder(BorderFactory.createTitledBorder("Cadastro de Clientes PJ"));

        txtId = createFormattedField("####################");
        txtId.setEnabled(false);
        addCampo(p, "ID", txtId);
        
        txtRazaoSocial = new JTextField(); addCampo(p, "Razão Social*", txtRazaoSocial);
        txtNomeFantasia = new JTextField(); addCampo(p, "Nome Fantasia", txtNomeFantasia);
        
        txtIe = new JTextField();
        applyNumericFilter(txtIe);
        addCampo(p, "Inscrição Estadual", txtIe);
        
        txtCnpj = new JFormattedTextField(criarMascara("##.###.###/####-##"));
        addCampo(p, "CNPJ*", txtCnpj);

        txtTelefone = new JFormattedTextField(criarMascara("(##) #####-####"));
        addCampo(p, "Telefone", txtTelefone);

        txtEmail = new JTextField(); addCampo(p, "Email", txtEmail);
        txtEndereco = new JTextField(); addCampo(p, "Endereço", txtEndereco);
        
        txtNumero = new JTextField();
        applyNumericFilter(txtNumero);
        addCampo(p, "Número", txtNumero);
        
        txtComplemento = new JTextField(); addCampo(p, "Complemento", txtComplemento);
        txtBairro = new JTextField(); addCampo(p, "Bairro", txtBairro);
        txtCidade = new JTextField(); addCampo(p, "Cidade*", txtCidade);
        txtUf = new JTextField(); addCampo(p, "UF", txtUf);
        
        txtCep = new JFormattedTextField(criarMascara("#####-###"));
        addCampo(p, "CEP", txtCep);
        
        txtLimiteCredito = new JFormattedTextField();
        txtLimiteCredito.setValue(BigDecimal.ZERO);
        applyNumericFilter(txtLimiteCredito);
        addCampo(p, "Limite Crédito R$", txtLimiteCredito);

        return p;
    }

    private JFormattedTextField createFormattedField(String mask) {
        try {
            MaskFormatter mf = new MaskFormatter(mask);
            mf.setPlaceholderCharacter(' ');
            mf.setValueContainsLiteralCharacters(false);
            return new JFormattedTextField(mf);
        } catch (ParseException e) {
            return new JFormattedTextField();
        }
    }

    private void applyNumericFilter(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null && string.matches("[0-9.,]*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null && text.matches("[0-9.,]*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }

    private JPanel criarPainelBotoes() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnNovo = new JButton("Novo");
        btnSalvar = new JButton("Salvar");
        btnAtualizar = new JButton("Atualizar");
        btnBuscar = new JButton("Buscar");
        btnListar = new JButton("Listar/Id");
        btnVoltar = new JButton("Voltar");
        p.add(btnNovo); p.add(btnSalvar); p.add(btnAtualizar);
        p.add(btnBuscar); p.add(btnListar); p.add(btnVoltar);
        return p;
    }
    private void addCampo(JPanel p, String l, JTextField c) {
        p.add(new JLabel(l));
        p.add(c);
    }

    private MaskFormatter criarMascara(String m) {
        try {
            MaskFormatter mf = new MaskFormatter(m);
            mf.setPlaceholderCharacter('_');
            mf.setValueContainsLiteralCharacters(true);
            return mf;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    // ================= EVENTOS =================
    private void bindEvents() {
        btnNovo.addActionListener(e -> modoNovo());
        btnSalvar.addActionListener(e -> salvar());
        btnAtualizar.addActionListener(e -> atualizar());
        btnBuscar.addActionListener(e -> buscar());
        btnListar.addActionListener(e -> listar());
        btnVoltar.addActionListener(e -> voltar());

        txtCnpj.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String cnpj = somenteNumeros(txtCnpj.getText());
                txtCnpj.setForeground(validarCNPJ(cnpj) ? new Color(0, 120, 0) : Color.RED);
            }
        });
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    long id = (long) model.getValueAt(row, 0);
                    carregar(id);
                }
            }
        });
    }
    
    private void carregar(long id) {
        try {
            Cliente c = controller.findById(id);
            if (c != null) {
                preencherCampos(c);
                modoEdicao();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar: " + e.getMessage());
        }
    }

    // ================= AÇÕES =================
    private void modoNovo() {
        limparCampos();
        txtId.setText("");
        btnSalvar.setVisible(true);
        btnAtualizar.setEnabled(false);
        txtRazaoSocial.requestFocus();
    }

    private void modoEdicao() {
        btnSalvar.setVisible(false);
        btnAtualizar.setEnabled(true);
    }

    private void salvar() {
        if (!validar()) return;
        try {
            controller.save(criar());
            JOptionPane.showMessageDialog(this, "Cliente PJ cadastrado!");
            listar();
            modoNovo();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage());
        }
    }

    private void atualizar() {
        if (txtId.getText().trim().isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para atualizar.");
            return;
        }
        try {
            Cliente c = criar();
            c.setId(Long.parseLong(txtId.getText().trim()));
            controller.save(c);
            JOptionPane.showMessageDialog(this, "Cliente PJ atualizado!");
            listar();
            modoNovo();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + e.getMessage());
        }
    }

private String somenteNumeros(String s) { return s == null ? "" : s.replaceAll("\\D", ""); }

private boolean validarCNPJ(String cnpj) {
    if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) return false;
    try {
        int[] peso1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] peso2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int soma1 = 0;
        for (int i = 0; i < 12; i++) {
            soma1 += Integer.parseInt(cnpj.substring(i, i + 1)) * peso1[i];
        }
        int r1 = soma1 % 11;
        r1 = (r1 < 2) ? 0 : 11 - r1;
        int soma2 = 0;
        for (int i = 0; i < 12; i++) {
            soma2 += Integer.parseInt(cnpj.substring(i, i + 1)) * peso2[i];
        }
        soma2 += r1 * peso2[12];
        int r2 = soma2 % 11;
        r2 = (r2 < 2) ? 0 : 11 - r2;
        return cnpj.endsWith("" + r1 + r2);
    } catch (Exception e) { return false; }
}

    private void buscar() {
        String filtro = JOptionPane.showInputDialog(this, "Digite ID, Razão Social ou CNPJ:");
        if (filtro == null || filtro.isBlank()) return;
        String numFiltrado = somenteNumeros(filtro);
        try {
            model.setRowCount(0);
            if (numFiltrado.matches("\\d+") && numFiltrado.length() <= 6) {
                Cliente c = controller.findById(Long.parseLong(numFiltrado));
                if (c != null) {
                    adicionarNaGrade(c);
                    carregar(c.getId());
                } else {
                    JOptionPane.showMessageDialog(this, "Cliente não encontrado.");
                }
            } else if (numFiltrado.length() == 14) {
                Cliente c = controller.findByDocumento(numFiltrado);
                if (c != null) {
                    adicionarNaGrade(c);
                    carregar(c.getId());
                } else {
                    JOptionPane.showMessageDialog(this, "Cliente não encontrado.");
                }
            } else {
                List<Cliente> lista = controller.findByNomeRazaoSocial(filtro);
                if (lista.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nenhum cliente encontrado.");
                } else {
                    for (Cliente c : lista) adicionarNaGrade(c);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro na busca: " + e.getMessage());
        }
    }

    private void adicionarNaGrade(Cliente c) {
        model.addRow(new Object[]{
                c.getId(), c.getNomeRazaoSocial(), c.getDocumento(),
                c.getTelefone(), c.getCidade()
        });
    }

    private void listar() {
        try {
            model.setRowCount(0);
            List<Cliente> lista = controller.listByIdLimit(1, 100);
            for (Cliente c : lista) {
                if (c.isPessoaJuridica()) {
                    adicionarNaGrade(c);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private Cliente criar() {
        Cliente c = new Cliente();
        c.setTipoPessoa("J");
        c.setNomeRazaoSocial(txtRazaoSocial.getText());
        c.setNomeFantasia(txtNomeFantasia.getText());
        c.setDocumento(somenteNumeros(txtCnpj.getText()));
        c.setRgInscricaoEstadual(txtIe.getText());
        c.setTelefone(somenteNumeros(txtTelefone.getText()));
        c.setEmail(txtEmail.getText());
        c.setEndereco(txtEndereco.getText());
        c.setNumero(txtNumero.getText());
        c.setComplemento(txtComplemento.getText());
        c.setBairro(txtBairro.getText());
        c.setCidade(txtCidade.getText());
        c.setUf(txtUf.getText());
        c.setCep(somenteNumeros(txtCep.getText()));
        c.setLimiteCredito(parseBig(txtLimiteCredito.getText()));
        return c;
    }

    private void preencherCampos(Cliente c) {
        txtId.setText(String.valueOf(c.getId()));
        txtRazaoSocial.setText(c.getNomeRazaoSocial());
        txtNomeFantasia.setText(c.getNomeFantasia());
        txtIe.setText(c.getRgInscricaoEstadual());
        txtCnpj.setText(c.getDocumento());
        txtTelefone.setText(c.getTelefone());
        txtEmail.setText(c.getEmail());
        txtEndereco.setText(c.getEndereco());
        txtNumero.setText(c.getNumero());
        txtComplemento.setText(c.getComplemento());
        txtBairro.setText(c.getBairro());
        txtCidade.setText(c.getCidade());
        txtUf.setText(c.getUf());
        txtCep.setText(c.getCep());
        txtLimiteCredito.setValue(c.getLimiteCredito());
    }

    private boolean validar() {
        if (txtRazaoSocial.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Razão Social é obrigatória.");
            return false;
        }
        if (txtCidade.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Cidade é obrigatória.");
            return false;
        }
        if (!validarCNPJ(somenteNumeros(txtCnpj.getText()))) {
            JOptionPane.showMessageDialog(this, "CNPJ inválido.");
            return false;
        }
        return true;
    }

    private BigDecimal parseBig(String v) {
        try {
            return new BigDecimal(v.trim().replace(",", "."));
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private void limparCampos() {
        for (Component c : getComponents()) limparRec(c);
    }

    private void limparRec(Component c) {
        if (c instanceof JFormattedTextField ft) {
            ft.setValue(null);
        } else if (c instanceof JTextField tf) {
            tf.setText("");
        }
        if (c instanceof Container ct) {
            for (Component cc : ct.getComponents()) {
                limparRec(cc);
            }
        }
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
