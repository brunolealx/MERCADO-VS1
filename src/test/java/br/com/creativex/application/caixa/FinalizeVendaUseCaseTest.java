package br.com.creativex.application.caixa;

import br.com.creativex.domain.entity.produto.Produto;
import br.com.creativex.domain.entity.venda.ItemVenda;
import br.com.creativex.domain.entity.venda.Venda;
import br.com.creativex.domain.repository.ProdutoRepository;
import br.com.creativex.domain.repository.VendaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinalizeVendaUseCaseTest {

    @Mock
    VendaRepository vendaRepository;

    @Mock
    ProdutoRepository produtoRepository;

    @InjectMocks
    FinalizeVendaUseCase useCase;

    @Test
    void execute_finalizesValidSale() {
        Venda venda = vendaValida();
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoComEstoque("100")));

        useCase.execute(venda);

        verify(vendaRepository).finalizarVenda(venda);
    }

    @Test
    void execute_rejectsEmptySale() {
        Venda venda = vendaValida();
        venda.getItens().clear();

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(venda));
        verify(vendaRepository, never()).finalizarVenda(venda);
    }

    @Test
    void execute_rejectsMissingUser() {
        Venda venda = vendaValida();
        venda.setIdUsuario(null);

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(venda));
        verify(vendaRepository, never()).finalizarVenda(venda);
    }

    @Test
    void execute_rejectsPaymentLowerThanTotal() {
        Venda venda = vendaValida();
        venda.setValorPago(BigDecimal.ONE);
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoComEstoque("100")));

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(venda));
        verify(vendaRepository, never()).finalizarVenda(venda);
    }

    @Test
    void execute_rejectsInvalidItemQuantity() {
        Venda venda = vendaValida();
        venda.getItens().get(0).setQuantidade(BigDecimal.ZERO);

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(venda));
        verify(vendaRepository, never()).finalizarVenda(venda);
    }

    @Test
    void execute_rejectsMissingProduct() {
        Venda venda = vendaValida();
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(venda));
        verify(vendaRepository, never()).finalizarVenda(venda);
    }

    @Test
    void execute_rejectsInsufficientStock() {
        Venda venda = vendaValida();
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoComEstoque("0.5")));

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(venda));
        verify(vendaRepository, never()).finalizarVenda(venda);
    }

    @Test
    void execute_rejectsInsufficientAggregatedStockForRepeatedProduct() {
        Venda venda = vendaValida();
        venda.adicionarItem(new ItemVenda(1L, "Produto", BigDecimal.ONE, BigDecimal.TEN));
        venda.setValorPago(BigDecimal.valueOf(20));
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoComEstoque("1.5")));

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(venda));
        verify(vendaRepository, never()).finalizarVenda(venda);
    }

    private Venda vendaValida() {
        Venda venda = new Venda();
        venda.setIdUsuario(1L);
        venda.setMetodoPagamento("DINHEIRO");
        venda.setValorPago(BigDecimal.TEN);
        venda.setTroco(BigDecimal.ZERO);
        venda.adicionarItem(new ItemVenda(1L, "Produto", BigDecimal.ONE, BigDecimal.TEN));
        return venda;
    }

    private Produto produtoComEstoque(String quantidade) {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setQuantidadeEstoque(new BigDecimal(quantidade));
        return produto;
    }
}
