package br.com.creativex.application.fiscal;

import br.com.creativex.domain.entity.config.Estabelecimento;
import br.com.creativex.domain.entity.venda.ItemVenda;
import br.com.creativex.domain.entity.venda.Venda;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NfceXmlGeneratorTest {

    @Test
    void gerar_criaXmlNfceComChaveDeAcesso() {
        Venda venda = new Venda();
        venda.setIdVenda(123L);
        venda.setIdUsuario(1L);
        venda.setMetodoPagamento("DINHEIRO");
        venda.setValorPago(BigDecimal.TEN);

        ItemVenda item = new ItemVenda(1L, "Produto Teste", BigDecimal.ONE, BigDecimal.TEN);
        item.setCodigoBarra("7891234567895");
        item.setNcm("22021000");
        item.setCfop("5102");
        item.setUnidadeComercial("UN");
        item.setUnidadeTributavel("UN");
        item.setCstFiscalMomento("000");
        item.setAliquotaIcms(BigDecimal.valueOf(18));
        venda.adicionarItem(item);

        NfceXmlResult result = new NfceXmlGenerator().gerar(venda, estabelecimento(),
                OffsetDateTime.parse("2026-05-19T10:00:00-03:00"));

        assertEquals(44, result.getChaveAcesso().length());
        assertEquals(1, result.getSerie());
        assertEquals(123L, result.getNumero());
        assertTrue(result.getXml().contains("<mod>65</mod>"));
        assertTrue(result.getXml().contains("<tpAmb>2</tpAmb>"));
        assertFalse(result.getXml().contains("<chaveAcesso>"));
        assertTrue(result.getXml().contains("NFe" + result.getChaveAcesso()));
    }

    private Estabelecimento estabelecimento() {
        Estabelecimento estabelecimento = new Estabelecimento();
        estabelecimento.setRazaoSocial("Mercado Teste");
        estabelecimento.setCnpj("12.345.678/0001-99");
        estabelecimento.setInscricaoEstadual("123456789");
        estabelecimento.setLogradouro("Rua Teste");
        estabelecimento.setNumero("100");
        estabelecimento.setBairro("Centro");
        estabelecimento.setCidade("Sao Paulo");
        estabelecimento.setEstado("SP");
        estabelecimento.setCep("01001000");
        estabelecimento.setCodigoMunicipioIbge("3550308");
        estabelecimento.setRegimeTributario(1);
        return estabelecimento;
    }
}
