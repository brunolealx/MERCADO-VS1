package br.com.creativex.application.fiscal;

public class NfceXmlResult {

    private final String chaveAcesso;
    private final int serie;
    private final long numero;
    private final String xml;

    public NfceXmlResult(String chaveAcesso, int serie, long numero, String xml) {
        this.chaveAcesso = chaveAcesso;
        this.serie = serie;
        this.numero = numero;
        this.xml = xml;
    }

    public String getChaveAcesso() {
        return chaveAcesso;
    }

    public int getSerie() {
        return serie;
    }

    public long getNumero() {
        return numero;
    }

    public String getXml() {
        return xml;
    }
}
