package com.creativex.model.venda;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Venda {
    private Long idVenda;
    private Long idUsuario; // ID do operador que está logado
    private BigDecimal totalVenda;
    private String metodoPagamento;
    private List<ItemVenda> itens;

    public Venda() {
        this.itens = new ArrayList<>();
        this.totalVenda = BigDecimal.ZERO;
    }

    // Método utilitário para adicionar item e já atualizar o total
    public void adicionarItem(ItemVenda item) {
        this.itens.add(item);
        calcularTotal();
    }

    public void calcularTotal() {
        this.totalVenda = itens.stream()
                .map(ItemVenda::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Getters e Setters
    public Long getIdVenda() { return idVenda; }
    public void setIdVenda(Long idVenda) { this.idVenda = idVenda; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public BigDecimal getTotalVenda() { return totalVenda; }
    public void setTotalVenda(BigDecimal totalVenda) { this.totalVenda = totalVenda; }

    public String getMetodoPagamento() { return metodoPagamento; }
    public void setMetodoPagamento(String metodoPagamento) { this.metodoPagamento = metodoPagamento; }

    public List<ItemVenda> getItens() { return itens; }
    public void setItens(List<ItemVenda> itens) { this.itens = itens; }
}
