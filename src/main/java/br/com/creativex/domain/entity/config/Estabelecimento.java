// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.domain.entity.config;

import java.math.BigDecimal;

public class Estabelecimento {
    private String razaoSocial;
    private String nomeFantasia;
    private String cnpj;
    private String inscricaoEstadual;
    private String logradouro;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String codigoMunicipioIbge;
    private int regimeTributario;
    private BigDecimal aliqIbpt;

    // Getters e Setters
    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }

    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }
    
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public int getRegimeTributario() { return regimeTributario; }
    public void setRegimeTributario(int regimeTributario) { this.regimeTributario = regimeTributario; }

    public BigDecimal getAliqIbpt() { return aliqIbpt; }
    public void setAliqIbpt(BigDecimal aliqIbpt) { this.aliqIbpt = aliqIbpt; }

// Método SET (para o DAO conseguir gravar o dado vindo do banco)
    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    // Método GET (para o Cupom conseguir ler o dado)
    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public String getCodigoMunicipioIbge() { return codigoMunicipioIbge; }
    public void setCodigoMunicipioIbge(String codigoMunicipioIbge) { this.codigoMunicipioIbge = codigoMunicipioIbge; }
}
