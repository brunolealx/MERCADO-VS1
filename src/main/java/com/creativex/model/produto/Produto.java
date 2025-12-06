//Bruno Leal
//creativex sistemas
package com.creativex.model.produto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Produto {

    private long id;
    private String codigoBarra;
    private String descricao;
    private String marca;
    private String atributos;
    private String unidadeMedida;
    private String categoria;
    private int codGrupo;
    private String grupo;
    private char tipoBalanca;
    private BigDecimal quantidadeEstoque;
    private BigDecimal precoCusto;
    private BigDecimal precoVenda;

    private String ncm;
    private String cest;
    private String cfopPadrao;
    private String unidadeTributavel;
    private String ceanTributavel;

    private String cstIcms;
    private BigDecimal aliquotaIcms;
    private String cstPis;
    private BigDecimal ppis;
    private String cstCofins;
    private BigDecimal pcofins;

    private Timestamp dataCadastro;
    private String loja;

    // Getters e Setters

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getCodigoBarra() { return codigoBarra; }
    public void setCodigoBarra(String codigoBarra) { this.codigoBarra = codigoBarra; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getAtributos() { return atributos; }
    public void setAtributos(String atributos) { this.atributos = atributos; }

    public String getUnidadeMedida() { return unidadeMedida; }
    public void setUnidadeMedida(String unidadeMedida) { this.unidadeMedida = unidadeMedida; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getCodGrupo() { return codGrupo; }
    public void setCodGrupo(int codGrupo) { this.codGrupo = codGrupo; }

    public String getGrupo() { return grupo; }
    public void setGrupo(String grupo) { this.grupo = grupo; }

    public char getTipoBalanca() { return tipoBalanca; }
    public void setTipoBalanca(char tipoBalanca) { this.tipoBalanca = tipoBalanca; }

    public BigDecimal getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(BigDecimal quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }

    public BigDecimal getPrecoCusto() { return precoCusto; }
    public void setPrecoCusto(BigDecimal precoCusto) { this.precoCusto = precoCusto; }

    public BigDecimal getPrecoVenda() { return precoVenda; }
    public void setPrecoVenda(BigDecimal precoVenda) { this.precoVenda = precoVenda; }

    public String getNcm() { return ncm; }
    public void setNcm(String ncm) { this.ncm = ncm; }

    public String getCest() { return cest; }
    public void setCest(String cest) { this.cest = cest; }

    public String getCfopPadrao() { return cfopPadrao; }
    public void setCfopPadrao(String cfopPadrao) { this.cfopPadrao = cfopPadrao; }

    public String getUnidadeTributavel() { return unidadeTributavel; }
    public void setUnidadeTributavel(String unidadeTributavel) { this.unidadeTributavel = unidadeTributavel; }

    public String getCeanTributavel() { return ceanTributavel; }
    public void setCeanTributavel(String ceanTributavel) { this.ceanTributavel = ceanTributavel; }

    public String getCstIcms() { return cstIcms; }
    public void setCstIcms(String cstIcms) { this.cstIcms = cstIcms; }

    public BigDecimal getAliquotaIcms() { return aliquotaIcms; }
    public void setAliquotaIcms(BigDecimal aliquotaIcms) { this.aliquotaIcms = aliquotaIcms; }

    public String getCstPis() { return cstPis; }
    public void setCstPis(String cstPis) { this.cstPis = cstPis; }

    public BigDecimal getPpis() { return ppis; }
    public void setPpis(BigDecimal ppis) { this.ppis = ppis; }

    public String getCstCofins() { return cstCofins; }
    public void setCstCofins(String cstCofins) { this.cstCofins = cstCofins; }

    public BigDecimal getPcofins() { return pcofins; }
    public void setPcofins(BigDecimal pcofins) { this.pcofins = pcofins; }

    public Timestamp getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(Timestamp dataCadastro) { this.dataCadastro = dataCadastro; }

    public String getLoja() { return loja; }
    public void setLoja(String loja) { this.loja = loja; }
}
