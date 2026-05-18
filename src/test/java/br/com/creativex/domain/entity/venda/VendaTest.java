package br.com.creativex.domain.entity.venda;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VendaTest {

    @Test
    void adicionarItem_recalculatesTotalsAndTaxes() {
        Venda venda = new Venda();

        ItemVenda primeiro = new ItemVenda(1L, "Produto 1", BigDecimal.valueOf(2), BigDecimal.valueOf(10));
        primeiro.setDescontoItem(BigDecimal.valueOf(1));
        primeiro.setAliquotaIcms(BigDecimal.TEN);

        ItemVenda segundo = new ItemVenda(2L, "Produto 2", BigDecimal.ONE, BigDecimal.valueOf(5));
        segundo.setPpis(BigDecimal.valueOf(2));
        segundo.setPcofins(BigDecimal.valueOf(3));

        venda.adicionarItem(primeiro);
        venda.adicionarItem(segundo);

        assertEquals(new BigDecimal("25.00"), venda.getTotalBruto());
        assertEquals(new BigDecimal("1.00"), venda.getTotalDesconto());
        assertEquals(new BigDecimal("24.00"), venda.getTotalLiquido());
        assertEquals(new BigDecimal("2.15"), venda.getTotalTributos());
    }

    @Test
    void setItens_recalculatesTotals() {
        Venda venda = new Venda();
        venda.setItens(java.util.List.of(
                new ItemVenda(1L, "Produto", BigDecimal.valueOf(3), BigDecimal.valueOf(2.5))
        ));

        assertEquals(new BigDecimal("7.50"), venda.getTotalBruto());
        assertEquals(new BigDecimal("7.50"), venda.getTotalLiquido());
    }
}
