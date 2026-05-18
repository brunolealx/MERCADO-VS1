package br.com.creativex.application.cliente;

import br.com.creativex.domain.entity.cliente.Cliente;
import br.com.creativex.domain.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteUseCasesTest {

    @Mock
    ClienteRepository repo;

    @InjectMocks
    ClienteUseCases useCases;

    @Test
    void save_delegatesToRepository() throws Exception {
        Cliente c = new Cliente();
        when(repo.save(c)).thenReturn(c);

        Cliente out = useCases.save(c);

        assertSame(c, out);
        verify(repo, times(1)).save(c);
    }

    @Test
    void findByName_delegates() throws Exception {
        when(repo.findByName("x")).thenReturn(List.of());
        useCases.findByName("x");
        verify(repo).findByName("x");
    }
}
