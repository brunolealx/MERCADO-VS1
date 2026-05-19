package br.com.creativex.domain.entity.fiscal;

import java.time.LocalDateTime;

public class DocumentoFiscal {

    private Long id;
    private Long idVenda;
    private ModeloDocumentoFiscal modelo = ModeloDocumentoFiscal.NFCE;
    private StatusDocumentoFiscal status = StatusDocumentoFiscal.PENDENTE_EMISSAO;
    private AmbienteFiscal ambiente = AmbienteFiscal.HOMOLOGACAO;
    private ModoEmissaoFiscal modoEmissao = ModoEmissaoFiscal.NORMAL;
    private Integer serie;
    private Long numero;
    private String chaveAcesso;
    private String protocoloAutorizacao;
    private String xmlAssinado;
    private String xmlAutorizado;
    private String motivoRejeicao;
    private LocalDateTime dataEmissao;
    private LocalDateTime dataAutorizacao;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdVenda() { return idVenda; }
    public void setIdVenda(Long idVenda) { this.idVenda = idVenda; }
    public ModeloDocumentoFiscal getModelo() { return modelo; }
    public void setModelo(ModeloDocumentoFiscal modelo) { this.modelo = modelo; }
    public StatusDocumentoFiscal getStatus() { return status; }
    public void setStatus(StatusDocumentoFiscal status) { this.status = status; }
    public AmbienteFiscal getAmbiente() { return ambiente; }
    public void setAmbiente(AmbienteFiscal ambiente) { this.ambiente = ambiente; }
    public ModoEmissaoFiscal getModoEmissao() { return modoEmissao; }
    public void setModoEmissao(ModoEmissaoFiscal modoEmissao) { this.modoEmissao = modoEmissao; }
    public Integer getSerie() { return serie; }
    public void setSerie(Integer serie) { this.serie = serie; }
    public Long getNumero() { return numero; }
    public void setNumero(Long numero) { this.numero = numero; }
    public String getChaveAcesso() { return chaveAcesso; }
    public void setChaveAcesso(String chaveAcesso) { this.chaveAcesso = chaveAcesso; }
    public String getProtocoloAutorizacao() { return protocoloAutorizacao; }
    public void setProtocoloAutorizacao(String protocoloAutorizacao) { this.protocoloAutorizacao = protocoloAutorizacao; }
    public String getXmlAssinado() { return xmlAssinado; }
    public void setXmlAssinado(String xmlAssinado) { this.xmlAssinado = xmlAssinado; }
    public String getXmlAutorizado() { return xmlAutorizado; }
    public void setXmlAutorizado(String xmlAutorizado) { this.xmlAutorizado = xmlAutorizado; }
    public String getMotivoRejeicao() { return motivoRejeicao; }
    public void setMotivoRejeicao(String motivoRejeicao) { this.motivoRejeicao = motivoRejeicao; }
    public LocalDateTime getDataEmissao() { return dataEmissao; }
    public void setDataEmissao(LocalDateTime dataEmissao) { this.dataEmissao = dataEmissao; }
    public LocalDateTime getDataAutorizacao() { return dataAutorizacao; }
    public void setDataAutorizacao(LocalDateTime dataAutorizacao) { this.dataAutorizacao = dataAutorizacao; }
}
