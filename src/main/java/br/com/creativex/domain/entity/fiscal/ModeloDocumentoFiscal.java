package br.com.creativex.domain.entity.fiscal;

public enum ModeloDocumentoFiscal {
    NFCE("65"),
    NFE("55"),
    SAT("59");

    private final String codigo;

    ModeloDocumentoFiscal(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
