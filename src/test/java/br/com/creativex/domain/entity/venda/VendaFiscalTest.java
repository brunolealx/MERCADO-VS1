package br.com.creativex.domain.entity.venda;

import br.com.creativex.domain.entity.fiscal.StatusDocumentoFiscal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VendaFiscalTest {

    @Test
    void novaVenda_iniciaComEmissaoFiscalPendente() {
        Venda venda = new Venda();

        assertEquals(StatusDocumentoFiscal.PENDENTE_EMISSAO, venda.getStatusFiscal());
    }
}
