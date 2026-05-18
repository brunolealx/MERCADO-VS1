package br.com.creativex.presentation.controller;

import br.com.creativex.application.caixa.FinalizeVendaUseCase;
import br.com.creativex.domain.entity.venda.Venda;
import br.com.creativex.domain.repository.VendaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CaixaControllerTest {

    @Mock
    FinalizeVendaUseCase finalizeVendaUseCase;

    @Mock
    VendaRepository vendaRepository;

    @InjectMocks
    CaixaController controller;

    @Test
    void finalizarVenda_delegatesToUseCase() {
        Venda venda = new Venda();

        controller.finalizarVenda(venda);

        verify(finalizeVendaUseCase).execute(venda);
    }

    @Test
    void cancelarVenda_delegatesToRepository() {
        controller.cancelarVenda(10L, 2L);

        verify(vendaRepository).cancelarVenda(10L, 2L);
    }

    @Test
    void buscarVendaPorId_returnsRepositorySaleWithItems() {
        Venda venda = new Venda();
        when(vendaRepository.buscarPorIdComItens(10L)).thenReturn(venda);

        Venda out = controller.buscarVendaPorId(10L);

        assertSame(venda, out);
        verify(vendaRepository).buscarPorIdComItens(10L);
    }
}
