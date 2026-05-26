package br.com.creativex.ui.components;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JacobTextFieldPictureTest {

    @Test
    void testCriarPorPictureSimples() {
        JacobTextField field = JacobTextField.criarPorPicture("X(10)");
        assertEquals(10, getTamanhoMaximo(field));
    }

    @Test
    void testCriarPorPictureRejeitaMisto() {
        assertThrows(IllegalArgumentException.class, () -> {
            JacobTextField.criarPorPicture("X(5)XXX");
        });
    }

    @Test
    void testCriarPorPictureAlfabetica() {
        JacobTextField field = JacobTextField.criarPorPicture("A(20)");
        assertEquals(20, getTamanhoMaximo(field));
    }

    @Test
    void testGetCobolMemoryValue() {
        JacobTextField field = JacobTextField.criarPorPicture("X(5)");
        field.setText("ABC");
        assertEquals("ABC  ", field.getCobolMemoryValue());
    }

    @Test
    void testGetCobolMemoryValueTruncado() {
        JacobTextField field = JacobTextField.criarPorPicture("X(3)");
        // O filtro já impediria isso via UI, mas testamos o método isolado
        field.setText("ABCDE"); 
        assertEquals("ABC", field.getCobolMemoryValue());
    }

    private int getTamanhoMaximo(JacobTextField field) {
        try {
            java.lang.reflect.Field f = JacobTextField.class.getDeclaredField("tamanhoMaximo");
            f.setAccessible(true);
            return (int) f.get(field);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
