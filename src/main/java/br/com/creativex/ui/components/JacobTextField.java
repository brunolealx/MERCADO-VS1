package br.com.creativex.ui.components;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Biblioteca JacobTextField v1
 * Componente Swing focado em campos de texto COBOL (PIC A e PIC X).
 */
public class JacobTextField extends JTextField {

    public enum TipoTexto { ALFABETICO, ALFANUMERICO }

    private final TipoTexto tipo;
    private final int tamanhoMaximo;

    /**
     * Construtor Privado - Força o uso da fábrica estática
     */
    private JacobTextField(TipoTexto tipo, int tamanhoMaximo) {
        super(tamanhoMaximo + 2); // Define o tamanho visual do campo
        this.tipo = tipo;
        this.tamanhoMaximo = tamanhoMaximo;
        
        this.setFont(new Font("Monospaced", Font.BOLD, 18));
        this.setHorizontalAlignment(JTextField.LEFT);

        // Comportamento de terminal: seleciona tudo ao ganhar foco
        this.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                selectAll();
            }
        });
        
        // Aplica o filtro de validação de digitação
        ((PlainDocument) this.getDocument()).setDocumentFilter(new TextoCobolFilter());
    }

    /**
     * Fábrica estática que interpreta máscaras textuais do COBOL.
     * Suporta formatos como "A(30)", "AAAAA", "X(15)", "X(10)XX"
     */
    public static JacobTextField criarPorPicture(String picture) {
        if (picture == null || picture.isEmpty()) {
            throw new IllegalArgumentException("Picture não pode ser nula ou vazia");
        }
        
        String pic = picture.toUpperCase().trim();

        // Valida se segue um padrão puro: X(n), A(n), XXXX ou AAAA
        // Rejeita misturas como X(10)XX
        if (!pic.matches("^A\\(\\d+\\)$|^A+$|^X\\(\\d+\\)$|^X+$")) {
            throw new IllegalArgumentException("Formato de PICTURE de texto inválido ou misto: " + picture);
        }
        
        // Identifica o tipo pelo primeiro caractere (A ou X)
        TipoTexto tipoDefinido = pic.startsWith("A") ? TipoTexto.ALFABETICO : TipoTexto.ALFANUMERICO;
        
        // Soma todos os tamanhos
        int tamanho = extrairTamanhoTexto(pic);
        
        return new JacobTextField(tipoDefinido, tamanho);
    }

    /**
     * Parser iterativo para somar tamanhos de PIC A ou PIC X.
     */
    private static int extrairTamanhoTexto(String parte) {
        int total = 0;
        for (int i = 0; i < parte.length(); i++) {
            char c = parte.charAt(i);
            if (c == 'A' || c == 'X') {
                if (i + 1 < parte.length() && parte.charAt(i + 1) == '(') {
                    int fim = parte.indexOf(')', i + 1);
                    if (fim != -1) {
                        String numStr = parte.substring(i + 2, fim);
                        total += Integer.parseInt(numStr);
                        i = fim;
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
     * Retorna o valor formatado para armazenamento (Preenchido com espaços à direita).
     * Garante que o dado tenha exatamente o tamanhoMaximo.
     */
    public String getCobolMemoryValue() {
        String texto = getText();
        if (texto.length() >= tamanhoMaximo) {
            return texto.substring(0, tamanhoMaximo);
        }
        return String.format("%-" + tamanhoMaximo + "s", texto);
    }

    /**
     * Filtro interno que age diretamente na digitação ou colagem (Ctrl+V)
     */
    private class TextoCobolFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            processarTexto(fb, offset, 0, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            processarTexto(fb, offset, length, text, attrs);
        }

        private void processarTexto(FilterBypass fb, int offset, int length, String textoInserido, AttributeSet attrs) throws BadLocationException {
            if (textoInserido == null) return;

            String textoFiltrado;
            if (tipo == TipoTexto.ALFABETICO) {
                // PIC A: Mantém apenas letras (incluindo acentuações básicas) e espaços
                textoFiltrado = textoInserido.replaceAll("[^a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ ]", "");
            } else {
                // PIC X: Alfanumérico puro, aceita tudo, monitora apenas o tamanho
                textoFiltrado = textoInserido;
            }

            // Força todas as letras para Maiúsculas
            textoFiltrado = textoFiltrado.toUpperCase();

            // Validação de estouro de tamanho (Teto Máximo do Buffer)
            int comprimentoAtual = fb.getDocument().getLength();
            int futuroComprimento = comprimentoAtual - length + textoFiltrado.length();

            if (futuroComprimento <= tamanhoMaximo) {
                super.replace(fb, offset, length, textoFiltrado, attrs);
            } else {
                // Trunca o texto para caber exatamente no limite e emite um alerta sonoro
                int quantoCabe = tamanhoMaximo - (comprimentoAtual - length);
                if (quantoCabe > 0) {
                    super.replace(fb, offset, length, textoFiltrado.substring(0, quantoCabe), attrs);
                }
                Toolkit.getDefaultToolkit().beep(); // Avisa o operador que estourou o limite do campo
            }
        }
    }
}
