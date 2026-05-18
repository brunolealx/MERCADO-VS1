package br.com.creativex.presentation.controller;

import br.com.creativex.application.produto.CreateProdutoUseCase;
import br.com.creativex.domain.entity.produto.Produto;
import br.com.creativex.domain.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoControllerTest {

    @Mock
    CreateProdutoUseCase createProdutoUseCase;

    @Mock
    ProdutoRepository produtoRepository;

    @InjectMocks
    ProdutoController controller;

    @Test
    void buscarPorCodigoOuId_returnsProductByBarcodeFirst() {
        Produto produto = new Produto();
        when(produtoRepository.findByCodigoBarra("789")).thenReturn(Optional.of(produto));

        Produto out = controller.buscarPorCodigoOuId("789");

        assertSame(produto, out);
        verify(produtoRepository, never()).findById(789L);
    }

    @Test
    void buscarPorCodigoOuId_returnsProductByIdWhenBarcodeNotFound() {
        Produto produto = new Produto();
        when(produtoRepository.findByCodigoBarra("15")).thenReturn(Optional.empty());
        when(produtoRepository.findById(15L)).thenReturn(Optional.of(produto));

        Produto out = controller.buscarPorCodigoOuId("15");

        assertSame(produto, out);
    }

    @Test
    void buscarPorCodigoOuId_returnsNullForBlankInput() {
        assertNull(controller.buscarPorCodigoOuId(" "));
    }

    @Test
    void buscarPorCodigoOuId_returnsNullForTextInputNotFound() {
        when(produtoRepository.findByCodigoBarra("abc")).thenReturn(Optional.empty());

        assertNull(controller.buscarPorCodigoOuId("abc"));
        verify(produtoRepository, never()).findById(0L);
    }
}
