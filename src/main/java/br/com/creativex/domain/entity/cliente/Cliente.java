// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.domain.entity.cliente;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Entidade unificada que representa um cliente (Pessoa Física ou Jurídica) no sistema ERP-PDVex.
 * 
 * Encapsula dados de identificação, contato, endereço e informações comerciais.
 * O campo tipoPessoa ('F' ou 'J') distingue entre PF e PJ.
 * 
 * @author Peracio Dias
 * @version 2.0
 * @since 2026-05-26
 */
public class Cliente {

    /** Identificador único do cliente */
    private Long id;
    
    /** Tipo de pessoa: 'F' para Física, 'J' para Jurídica */
    private String tipoPessoa;
    
    /** Nome completo ou Razão Social */
    private String nomeRazaoSocial;
    
    /** Nome Fantasia (opcional para PJ) */
    private String nomeFantasia;
    
    /** Documento de identificação: CPF ou CNPJ */
    private String documento;
    
    /** RG ou Inscrição Estadual */
    private String rgInscricaoEstadual;
    
    /** Telefone de contato */
    private String telefone;
    
    /** Email do cliente */
    private String email;
    
    /** Logradouro do endereço */
    private String endereco;
    
    /** Número do imóvel */
    private String numero;
    
    /** Complemento do endereço */
    private String complemento;
    
    /** Bairro */
    private String bairro;
    
    /** Cidade */
    private String cidade;
    
    /** Unidade federativa (estado) */
    private String uf;
    
    /** Código de endereçamento postal (CEP) */
    private String cep;

    /** Limite de crédito disponível para compras */
    private BigDecimal limiteCredito;
    
    /** Data e hora do cadastro do cliente */
    private Timestamp dataCadastro;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTipoPessoa() { return tipoPessoa; }
    public void setTipoPessoa(String tipoPessoa) { this.tipoPessoa = tipoPessoa; }
    
    public String getNomeRazaoSocial() { return nomeRazaoSocial; }
    public void setNomeRazaoSocial(String nomeRazaoSocial) { this.nomeRazaoSocial = nomeRazaoSocial; }
    
    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }
    
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    
    public String getRgInscricaoEstadual() { return rgInscricaoEstadual; }
    public void setRgInscricaoEstadual(String rgInscricaoEstadual) { this.rgInscricaoEstadual = rgInscricaoEstadual; }
    
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }
    
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    
    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }
    
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    
    public BigDecimal getLimiteCredito() { return limiteCredito; }
    public void setLimiteCredito(BigDecimal limiteCredito) { this.limiteCredito = limiteCredito; }
    
    public Timestamp getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(Timestamp dataCadastro) { this.dataCadastro = dataCadastro; }
    
    /**
     * Verifica se o cliente é uma pessoa jurídica.
     * @return true se for PJ, false se for PF
     */
    public boolean isPessoaJuridica() {
        return "J".equalsIgnoreCase(tipoPessoa);
    }
}
