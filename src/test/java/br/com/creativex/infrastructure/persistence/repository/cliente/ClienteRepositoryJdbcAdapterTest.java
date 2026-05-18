package br.com.creativex.infrastructure.persistence.repository.cliente;

import br.com.creativex.domain.entity.cliente.Cliente;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteRepositoryJdbcAdapterTest {

    @Mock
    ClienteDAO dao;

    @InjectMocks
    ClienteRepositoryJdbcAdapter adapter;

    @Test
    void save_callsInserirWhenIdNull() throws Exception {
        Cliente c = new Cliente();
        c.setId(null);

        adapter.save(c);

        verify(dao).inserir(c);
    }

    @Test
    void save_callsAtualizarWhenIdPresent() throws Exception {
        Cliente c = new Cliente();
        c.setId(1L);

        adapter.save(c);

        verify(dao).atualizar(c);
    }

    @Test
    void findById_delegates() throws Exception {
        Cliente c = new Cliente();
        when(dao.buscarPorId(1L)).thenReturn(c);
        Cliente out = adapter.findById(1L);
        assertSame(c, out);
        verify(dao).buscarPorId(1L);
    }
}
