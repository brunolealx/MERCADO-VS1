package com.creativex.model.venda;

import java.math.BigDecimal;

public class ItemVenda {

    private Long idProduto;
    private String nomeProduto;

    private BigDecimal quantidade = BigDecimal.ZERO;
    private BigDecimal precoUnitario = BigDecimal.ZERO;
    private BigDecimal descontoItem = BigDecimal.ZERO;
    private BigDecimal subtotal = BigDecimal.ZERO;

    // Snapshots (histórico)
    private BigDecimal precoCustoMomento;
    private String cstFiscalMomento;

    public ItemVenda() {}

    public ItemVenda(Long idProduto, String nomeProduto,
                     BigDecimal quantidade, BigDecimal precoUnitario) {
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        calcularSubtotal();
    }

    // =========================
    // REGRA DE NEGÓCIO
    // =========================
    public void calcularSubtotal() {
        this.subtotal = quantidade
                .multiply(precoUnitario)
                .subtract(descontoItem);
    }

    // =========================
    // GETTERS E SETTERS
    // =========================
    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
        calcularSubtotal();
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
        calcularSubtotal();
    }

    public BigDecimal getDescontoItem() {
        return descontoItem;
    }

    public void setDescontoItem(BigDecimal descontoItem) {
        this.descontoItem = descontoItem;
        calcularSubtotal();
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getPrecoCustoMomento() {
        return precoCustoMomento;
    }

    public void setPrecoCustoMomento(BigDecimal precoCustoMomento) {
        this.precoCustoMomento = precoCustoMomento;
    }

    public String getCstFiscalMomento() {
        return cstFiscalMomento;
    }

    public void setCstFiscalMomento(String cstFiscalMomento) {
        this.cstFiscalMomento = cstFiscalMomento;
    }
}
