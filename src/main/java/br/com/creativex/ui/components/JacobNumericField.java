package br.com.creativex.ui.components;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Biblioteca JacobNumericField
 * Componente Swing reutilizável com comportamento de calculadora COBOL (PIC S9(N)V99).
 */
public class JacobNumericField extends JTextField {

    private final int inteiros;
    private final int decimais;
    private final boolean aceitaSinal;
    private final long limiteMaximo;
    
    // Estado interno do componente
    private boolean isNegativo = false;
    private long valorInterno = 0;

    /**
     * Construtor completo do componente.
     * @param inteiros Quantidade de dígitos inteiros.
     * @param decimais Quantidade de dígitos decimais (0 para inteiros puros).
     * @param aceitaSinal Se true, habilita o comportamento do 'S' (suporte a + e -).
     */
    public JacobNumericField(int inteiros, int decimais, boolean aceitaSinal) {
        // Define o tamanho visual do campo adicionando espaço para o sinal e a vírgula
        super(inteiros + decimais + (decimais > 0 ? 1 : 0) + (aceitaSinal ? 1 : 0) + 2);
        
        this.inteiros = inteiros;
        this.decimais = decimais;
        this.aceitaSinal = aceitaSinal;
        this.limiteMaximo = (long) Math.pow(10, inteiros + decimais);

        // Estilização padrão de terminal corporativo
        this.setFont(new Font("Monospaced", Font.BOLD, 14));
        this.setHorizontalAlignment(JTextField.RIGHT);

        // Intercepta as teclas de sinal (+ e -) antes que cheguem ao documento
        if (aceitaSinal) {
            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyChar() == '-') {
                        isNegativo = true;
                        atualizarTelaViaCodigo();
                        e.consume();
                    } else if (e.getKeyChar() == '+') {
                        isNegativo = false;
                        atualizarTelaViaCodigo();
                        e.consume();
                    }
                }
            });
        }

        // Aplica o filtro atômico de inserção de dados
        PlainDocument doc = (PlainDocument) this.getDocument();
        doc.setDocumentFilter(new CalculadoraFilter());
        
        atualizarTelaViaCodigo();
    }

    public JacobNumericField(int inteiros, int decimais) {
        this(inteiros, decimais, false);
    }

    /**
     * Factory method que cria uma instância baseada em uma Picture COBOL.
     * Exemplos: "9(5)V99", "S9(3)V99", "99999", "S9(10)"
     * 
     * @param picture String representando o formato COBOL
     * @return Uma nova instância de JacobNumericField
     */
    public static JacobNumericField criarPorPicture(String picture) {
        if (picture == null || picture.isEmpty()) {
            throw new IllegalArgumentException("Picture não pode ser nula ou vazia");
        }

        String pic = picture.toUpperCase().trim();

        // Valida padrões numéricos oficiais: S?9(n)V9(n), S?9(n)V99, S?99V99, S?9(n), S?999, etc.
        // Rejeita misturas como 9(3)99
        if (!pic.matches("^S?9\\(\\d+\\)(V(9\\(\\d+\\)|9+))?$|^S?9+(V9+)?$")) {
            throw new IllegalArgumentException("Formato de PICTURE numérica inválido ou misto: " + picture);
        }

        boolean temSinal = pic.startsWith("S");
        if (temSinal) {
            pic = pic.substring(1);
        }

        // Divide a parte inteira da decimal pelo 'V' (ponto decimal implícito do COBOL)
        String[] partes = pic.split("V");
        int ints = extrairTamanho(partes[0]);
        int decs = (partes.length > 1) ? extrairTamanho(partes[1]) : 0;

        return new JacobNumericField(ints, decs, temSinal);
    }

    /**
     * Parser interno para processar repetições como 9(5) ou sequências como 999.
     */
    private static int extrairTamanho(String parte) {
        int total = 0;
        for (int i = 0; i < parte.length(); i++) {
            char c = parte.charAt(i);
            if (c == '9') {
                // Verifica se há repetição entre parênteses: 9(N)
                if (i + 1 < parte.length() && parte.charAt(i + 1) == '(') {
                    int fim = parte.indexOf(')', i + 1);
                    if (fim != -1) {
                        String numStr = parte.substring(i + 2, fim);
                        total += Integer.parseInt(numStr);
                        i = fim; // Pula para o final do parênteses
                    } else {
                        total++;
                    }
                } else {
                    total++;
                }
            }
        }
        return total;
    }

    /**
     * Define o valor do campo via BigDecimal.
     */
    public void setValue(BigDecimal value) {
        if (value == null) {
            valorInterno = 0;
            isNegativo = false;
        } else {
            isNegativo = value.signum() < 0;
            BigDecimal absoluto = value.abs().movePointRight(decimais);
            valorInterno = absoluto.setScale(0, RoundingMode.HALF_UP).longValue();
        }
        atualizarTelaViaCodigo();
    }

    private void atualizarTelaViaCodigo() {
        try {
            PlainDocument doc = (PlainDocument) this.getDocument();
            CalculadoraFilter filter = (CalculadoraFilter) doc.getDocumentFilter();
            if (filter != null) {
                filter.atualizarTela(null);
            }
        } catch (Exception ignored) {}
    }

    private String formatarString(String digitosPuros) {
        String sinalStr = (aceitaSinal) ? (isNegativo ? "-" : "+") : "";
        
        if (decimais == 0) {
            return sinalStr + digitosPuros;
        }
        
        String parteInteira = digitosPuros.substring(0, inteiros);
        String parteDecimal = digitosPuros.substring(inteiros);
        return sinalStr + parteInteira + "," + parteDecimal;
    }

    public double getDoubleValue() {
        long valorBruto = valorInterno;
        if (isNegativo) {
            valorBruto = -valorBruto;
        }
        if (decimais == 0) {
            return (double) valorBruto;
        }
        return valorBruto / Math.pow(10, decimais);
    }

    public BigDecimal getBigDecimalValue() {
        BigDecimal valor = new BigDecimal(valorInterno);
        if (isNegativo) {
            valor = valor.negate();
        }
        if (decimais > 0) {
            valor = valor.divide(BigDecimal.TEN.pow(decimais), decimais, RoundingMode.HALF_UP);
        }
        return valor;
    }

    public String getCobolMemoryValue() {
        String totalDigitos = String.format("%0" + (inteiros + decimais) + "d", valorInterno);
        if (aceitaSinal) {
            return (isNegativo ? "-" : "+") + totalDigitos;
        }
        return totalDigitos;
    }

    private class CalculadoraFilter extends DocumentFilter {
        private final int totalDigitos = inteiros + decimais;

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) 
                throws BadLocationException {
            processarEntrada(fb, string);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
                throws BadLocationException {
            // Se o texto está sendo totalmente substituído ou removido (como em setText("") ou setText("0"))
            if (offset == 0 && length >= fb.getDocument().getLength()) {
                valorInterno = 0;
                isNegativo = false;
                if (text == null || text.isEmpty()) {
                    atualizarTela(fb);
                    return;
                }
            }

            if (text == null || text.isEmpty()) {
                if (length > 0) processarBackspace(fb);
            } else {
                processarEntrada(fb, text);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            if (offset == 0 && length >= fb.getDocument().getLength()) {
                valorInterno = 0;
                isNegativo = false;
                atualizarTela(fb);
            } else {
                processarBackspace(fb);
            }
        }

        private void processarEntrada(FilterBypass fb, String textoInserido) throws BadLocationException {
            String apenasNumeros = textoInserido.replaceAll("[^0-9]", "");
            if (apenasNumeros.isEmpty()) return;

            for (char c : apenasNumeros.toCharArray()) {
                if ((valorInterno * 10) >= limiteMaximo) {
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
                int novoDigito = Character.getNumericValue(c);
                valorInterno = (valorInterno * 10) + novoDigito;
            }
            atualizarTela(fb);
        }

        private void processarBackspace(FilterBypass fb) throws BadLocationException {
            valorInterno = valorInterno / 10;
            if (valorInterno == 0) isNegativo = false;
            atualizarTela(fb);
        }

        public void atualizarTela(FilterBypass fb) throws BadLocationException {
            String mascarado = String.format("%0" + totalDigitos + "d", valorInterno);
            String textoFormatado = formatarString(mascarado);

            if (fb != null) {
                super.replace(fb, 0, fb.getDocument().getLength(), textoFormatado, null);
            } else {
                SwingUtilities.invokeLater(() -> {
                    try {
                        PlainDocument d = (PlainDocument) JacobNumericField.this.getDocument();
                        DocumentFilter original = d.getDocumentFilter();
                        d.setDocumentFilter(null);
                        JacobNumericField.this.setText(textoFormatado);
                        d.setDocumentFilter(original);
                    } catch (Exception ignored) {}
                });
            }
        }
    }
}
