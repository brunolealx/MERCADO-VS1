package br.com.creativex.ui.components;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JacobNumericFieldPictureTest {

    @Test
    void testCriarPorPictureSimples() {
        JacobNumericField field = JacobNumericField.criarPorPicture("9(5)V99");
        assertEquals(5.0, getFieldValue(field, "inteiros"));
        assertEquals(2.0, getFieldValue(field, "decimais"));
        assertFalse((boolean) getFieldValue(field, "aceitaSinal"));
    }

    @Test
    void testCriarPorPictureComSinal() {
        JacobNumericField field = JacobNumericField.criarPorPicture("S9(3)V99");
        assertEquals(3.0, getFieldValue(field, "inteiros"));
        assertEquals(2.0, getFieldValue(field, "decimais"));
        assertTrue((boolean) getFieldValue(field, "aceitaSinal"));
    }

    @Test
    void testCriarPorPictureInteiro() {
        JacobNumericField field = JacobNumericField.criarPorPicture("9(10)");
        assertEquals(10.0, getFieldValue(field, "inteiros"));
        assertEquals(0.0, getFieldValue(field, "decimais"));
    }

    @Test
    void testCriarPorPictureSequencial() {
        JacobNumericField field = JacobNumericField.criarPorPicture("999V99");
        assertEquals(3.0, getFieldValue(field, "inteiros"));
        assertEquals(2.0, getFieldValue(field, "decimais"));
    }

    @Test
    void testCriarPorPictureRejeitaMisto() {
        assertThrows(IllegalArgumentException.class, () -> {
            JacobNumericField.criarPorPicture("9(2)99V99");
        });
    }

    @Test
    void testCriarPorPictureValidaHibridoComum() {
        // Formato comum: Compacto no inteiro, fixo no decimal
        JacobNumericField field = JacobNumericField.criarPorPicture("9(5)V99");
        assertEquals(5.0, getFieldValue(field, "inteiros"));
        assertEquals(2.0, getFieldValue(field, "decimais"));
    }

    private Object getFieldValue(JacobNumericField field, String fieldName) {
        try {
            java.lang.reflect.Field f = JacobNumericField.class.getDeclaredField(fieldName);
            f.setAccessible(true);
            Object val = f.get(field);
            if (val instanceof Integer) return ((Integer) val).doubleValue();
            return val;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
