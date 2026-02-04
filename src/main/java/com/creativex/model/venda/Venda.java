package com.creativex.model.venda;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Venda {

    private Long idVenda;
    private Long idUsuario;
    private Long idCliente;

    private BigDecimal totalBruto = BigDecimal.ZERO;
    private BigDecimal totalDesconto = BigDecimal.ZERO;
    private BigDecimal totalLiquido = BigDecimal.ZERO;

    private String status = "CONCLUIDA";
    private String metodoPagamento;
    private BigDecimal valorPago;
    private BigDecimal troco;

    private List<ItemVenda> itens = new ArrayList<>();

    // =========================
    // REGRAS DE NEGÓCIO
    // =========================
    public void adicionarItem(ItemVenda item) {
        itens.add(item);
        recalcularTotais();
    }

    public void recalcularTotais() {
        totalBruto = itens.stream()
                .map(ItemVenda::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalLiquido = totalBruto.subtract(totalDesconto);
    }

    // =========================
    // GETTERS E SETTERS
    // =========================
    public Long getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(Long idVenda) {
        this.idVenda = idVenda;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public BigDecimal getTotalBruto() {
        return totalBruto;
    }

    public void setTotalBruto(BigDecimal totalBruto) {
        this.totalBruto = totalBruto;
    }

    public BigDecimal getTotalDesconto() {
        return totalDesconto;
    }

    public void setTotalDesconto(BigDecimal totalDesconto) {
        this.totalDesconto = totalDesconto;
    }

    public BigDecimal getTotalLiquido() {
        return totalLiquido;
    }

    public void setTotalLiquido(BigDecimal totalLiquido) {
        this.totalLiquido = totalLiquido;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public BigDecimal getTroco() {
        return troco;
    }

    public void setTroco(BigDecimal troco) {
        this.troco = troco;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
        recalcularTotais();
    }
}
