package br.com.creativex.infrastructure.persistence.repository.caixa;

import br.com.creativex.domain.entity.venda.Venda;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VendaRepositoryJdbcAdapterTest {

    @Mock
    VendaDAO vendaDAO;

    @InjectMocks
    VendaRepositoryJdbcAdapter adapter;

    @Test
    void finalizarVenda_delegatesToDao() throws Exception {
        Venda venda = new Venda();

        adapter.finalizarVenda(venda);

        verify(vendaDAO).finalizarVenda(venda);
    }

    @Test
    void finalizarVenda_wrapsSqlException() throws Exception {
        Venda venda = new Venda();
        doThrow(new SQLException("falha")).when(vendaDAO).finalizarVenda(venda);

        assertThrows(RuntimeException.class, () -> adapter.finalizarVenda(venda));
    }

    @Test
    void cancelarVenda_delegatesToDao() throws Exception {
        adapter.cancelarVenda(10L, 2L);

        verify(vendaDAO).cancelarVenda(10L, 2L);
    }

    @Test
    void cancelarVenda_wrapsSqlException() throws Exception {
        doThrow(new SQLException("falha")).when(vendaDAO).cancelarVenda(10L, 2L);

        assertThrows(RuntimeException.class, () -> adapter.cancelarVenda(10L, 2L));
    }
}
