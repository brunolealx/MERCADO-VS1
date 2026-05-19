package br.com.creativex.application.fiscal;

import br.com.creativex.domain.entity.config.Estabelecimento;
import br.com.creativex.domain.entity.venda.ItemVenda;
import br.com.creativex.domain.entity.venda.Venda;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class NfceXmlGenerator {

    private static final String NFE_NAMESPACE = "http://www.portalfiscal.inf.br/nfe";
    private static final int MODELO_NFCE = 65;
    private static final int SERIE_PADRAO = 1;
    private static final Map<String, String> CODIGO_UF = Map.ofEntries(
            Map.entry("RO", "11"), Map.entry("AC", "12"), Map.entry("AM", "13"),
            Map.entry("RR", "14"), Map.entry("PA", "15"), Map.entry("AP", "16"),
            Map.entry("TO", "17"), Map.entry("MA", "21"), Map.entry("PI", "22"),
            Map.entry("CE", "23"), Map.entry("RN", "24"), Map.entry("PB", "25"),
            Map.entry("PE", "26"), Map.entry("AL", "27"), Map.entry("SE", "28"),
            Map.entry("BA", "29"), Map.entry("MG", "31"), Map.entry("ES", "32"),
            Map.entry("RJ", "33"), Map.entry("SP", "35"), Map.entry("PR", "41"),
            Map.entry("SC", "42"), Map.entry("RS", "43"), Map.entry("MS", "50"),
            Map.entry("MT", "51"), Map.entry("GO", "52"), Map.entry("DF", "53")
    );

    public NfceXmlResult gerar(Venda venda, Estabelecimento estabelecimento, OffsetDateTime emissao) {
        if (venda == null || venda.getIdVenda() == null) {
            throw new IllegalArgumentException("Venda precisa estar gravada para gerar XML NFC-e.");
        }
        if (estabelecimento == null) {
            throw new IllegalArgumentException("Estabelecimento não configurado.");
        }

        int serie = SERIE_PADRAO;
        long numero = venda.getIdVenda();
        String uf = texto(estabelecimento.getEstado(), "SP").toUpperCase();
        String cUf = CODIGO_UF.getOrDefault(uf, "35");
        String cnpj = somenteDigitos(estabelecimento.getCnpj());
        String cNf = String.format("%08d", Math.abs(numero % 100_000_000));
        String chave = gerarChaveAcesso(cUf, emissao, cnpj, serie, numero, cNf);

        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element nfe = doc.createElementNS(NFE_NAMESPACE, "NFe");
            doc.appendChild(nfe);

            Element infNFe = append(doc, nfe, "infNFe");
            infNFe.setAttribute("Id", "NFe" + chave);
            infNFe.setAttribute("versao", "4.00");

            appendIde(doc, infNFe, venda, estabelecimento, emissao, cUf, cNf, serie, numero);
            appendEmit(doc, infNFe, estabelecimento);
            appendDest(doc, infNFe, venda);
            appendItens(doc, infNFe, venda);
            appendTotal(doc, infNFe, venda);
            appendTransp(doc, infNFe);
            appendPag(doc, infNFe, venda);
            appendInfAdic(doc, infNFe);

            return new NfceXmlResult(chave, serie, numero, toXml(doc));
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao gerar XML NFC-e.", e);
        }
    }

    private void appendIde(Document doc, Element infNFe, Venda venda, Estabelecimento estabelecimento,
                           OffsetDateTime emissao, String cUf, String cNf, int serie, long numero) {
        Element ide = append(doc, infNFe, "ide");
        text(doc, ide, "cUF", cUf);
        text(doc, ide, "cNF", cNf);
        text(doc, ide, "natOp", "VENDA");
        text(doc, ide, "mod", String.valueOf(MODELO_NFCE));
        text(doc, ide, "serie", String.valueOf(serie));
        text(doc, ide, "nNF", String.valueOf(numero));
        text(doc, ide, "dhEmi", emissao.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        text(doc, ide, "tpNF", "1");
        text(doc, ide, "idDest", "1");
        text(doc, ide, "cMunFG", texto(estabelecimento.getCodigoMunicipioIbge(), "3550308"));
        text(doc, ide, "tpImp", "4");
        text(doc, ide, "tpEmis", "1");
        text(doc, ide, "cDV", gerarDigitoVerificador(gerarBaseChave(cUf, emissao, somenteDigitos(estabelecimento.getCnpj()), SERIE_PADRAO, venda.getIdVenda(), cNf)));
        text(doc, ide, "tpAmb", "2");
        text(doc, ide, "finNFe", "1");
        text(doc, ide, "indFinal", "1");
        text(doc, ide, "indPres", "1");
        text(doc, ide, "procEmi", "0");
        text(doc, ide, "verProc", "PDVEX-VS1");
    }

    private void appendEmit(Document doc, Element infNFe, Estabelecimento estabelecimento) {
        Element emit = append(doc, infNFe, "emit");
        text(doc, emit, "CNPJ", somenteDigitos(estabelecimento.getCnpj()));
        text(doc, emit, "xNome", texto(estabelecimento.getRazaoSocial(), "EMITENTE NAO CONFIGURADO"));

        Element enderEmit = append(doc, emit, "enderEmit");
        text(doc, enderEmit, "xLgr", texto(estabelecimento.getLogradouro(), "NAO INFORMADO"));
        text(doc, enderEmit, "nro", texto(estabelecimento.getNumero(), "S/N"));
        text(doc, enderEmit, "xBairro", texto(estabelecimento.getBairro(), "CENTRO"));
        text(doc, enderEmit, "cMun", texto(estabelecimento.getCodigoMunicipioIbge(), "3550308"));
        text(doc, enderEmit, "xMun", texto(estabelecimento.getCidade(), "SAO PAULO"));
        text(doc, enderEmit, "UF", texto(estabelecimento.getEstado(), "SP").toUpperCase());
        text(doc, enderEmit, "CEP", somenteDigitos(texto(estabelecimento.getCep(), "01001000")));
        text(doc, enderEmit, "cPais", "1058");
        text(doc, enderEmit, "xPais", "BRASIL");

        text(doc, emit, "IE", somenteDigitos(estabelecimento.getInscricaoEstadual()));
        text(doc, emit, "CRT", String.valueOf(estabelecimento.getRegimeTributario() > 0 ? estabelecimento.getRegimeTributario() : 1));
    }

    private void appendDest(Document doc, Element infNFe, Venda venda) {
        if (venda.getCpfAvulso() == null || venda.getCpfAvulso().isBlank()) {
            return;
        }
        Element dest = append(doc, infNFe, "dest");
        text(doc, dest, "CPF", somenteDigitos(venda.getCpfAvulso()));
        text(doc, dest, "indIEDest", "9");
    }

    private void appendItens(Document doc, Element infNFe, Venda venda) {
        int nItem = 1;
        for (ItemVenda item : venda.getItens()) {
            Element det = append(doc, infNFe, "det");
            det.setAttribute("nItem", String.valueOf(nItem++));

            Element prod = append(doc, det, "prod");
            text(doc, prod, "cProd", String.valueOf(item.getIdProduto()));
            text(doc, prod, "cEAN", codigoBarras(item.getCodigoBarra()));
            text(doc, prod, "xProd", texto(item.getNomeProduto(), "PRODUTO"));
            text(doc, prod, "NCM", texto(item.getNcm(), "00000000"));
            if (item.getCest() != null && !item.getCest().isBlank()) {
                text(doc, prod, "CEST", somenteDigitos(item.getCest()));
            }
            text(doc, prod, "CFOP", texto(item.getCfop(), "5102"));
            text(doc, prod, "uCom", texto(item.getUnidadeComercial(), "UN"));
            text(doc, prod, "qCom", decimal(item.getQuantidade(), 4));
            text(doc, prod, "vUnCom", decimal(item.getPrecoUnitario(), 10));
            text(doc, prod, "vProd", decimal(item.getSubtotal(), 2));
            text(doc, prod, "cEANTrib", codigoBarras(item.getCeanTributavel()));
            text(doc, prod, "uTrib", texto(item.getUnidadeTributavel(), texto(item.getUnidadeComercial(), "UN")));
            text(doc, prod, "qTrib", decimal(item.getQuantidade(), 4));
            text(doc, prod, "vUnTrib", decimal(item.getPrecoUnitario(), 10));
            text(doc, prod, "indTot", "1");

            appendImposto(doc, det, item);
        }
    }

    private void appendImposto(Document doc, Element det, ItemVenda item) {
        Element imposto = append(doc, det, "imposto");
        text(doc, imposto, "vTotTrib", decimal(item.getTotalTributos(), 2));

        Element icms = append(doc, imposto, "ICMS");
        String cst = texto(item.getCstFiscalMomento(), "000");
        if ("040".equals(cst) || "041".equals(cst)) {
            Element icms40 = append(doc, icms, "ICMS40");
            text(doc, icms40, "orig", "0");
            text(doc, icms40, "CST", cst);
        } else {
            Element icms00 = append(doc, icms, "ICMS00");
            text(doc, icms00, "orig", "0");
            text(doc, icms00, "CST", "000".equals(cst) ? "00" : cst.substring(Math.max(0, cst.length() - 2)));
            text(doc, icms00, "modBC", "3");
            text(doc, icms00, "vBC", decimal(item.getSubtotal(), 2));
            text(doc, icms00, "pICMS", decimal(item.getAliquotaIcms(), 2));
            text(doc, icms00, "vICMS", decimal(item.getValorIcms(), 2));
        }

        appendPis(doc, imposto, item);
        appendCofins(doc, imposto, item);
    }

    private void appendPis(Document doc, Element imposto, ItemVenda item) {
        Element pis = append(doc, imposto, "PIS");
        String cst = texto(item.getCstPis(), "07");
        if ("01".equals(cst) || "02".equals(cst)) {
            Element pisAliq = append(doc, pis, "PISAliq");
            text(doc, pisAliq, "CST", cst);
            text(doc, pisAliq, "vBC", decimal(item.getSubtotal(), 2));
            text(doc, pisAliq, "pPIS", decimal(item.getPpis(), 2));
            text(doc, pisAliq, "vPIS", decimal(item.getValorPis(), 2));
        } else {
            Element pisNt = append(doc, pis, "PISNT");
            text(doc, pisNt, "CST", cst);
        }
    }

    private void appendCofins(Document doc, Element imposto, ItemVenda item) {
        Element cofins = append(doc, imposto, "COFINS");
        String cst = texto(item.getCstCofins(), "07");
        if ("01".equals(cst) || "02".equals(cst)) {
            Element cofinsAliq = append(doc, cofins, "COFINSAliq");
            text(doc, cofinsAliq, "CST", cst);
            text(doc, cofinsAliq, "vBC", decimal(item.getSubtotal(), 2));
            text(doc, cofinsAliq, "pCOFINS", decimal(item.getPcofins(), 2));
            text(doc, cofinsAliq, "vCOFINS", decimal(item.getValorCofins(), 2));
        } else {
            Element cofinsNt = append(doc, cofins, "COFINSNT");
            text(doc, cofinsNt, "CST", cst);
        }
    }

    private void appendTotal(Document doc, Element infNFe, Venda venda) {
        Element total = append(doc, infNFe, "total");
        Element icmsTot = append(doc, total, "ICMSTot");
        BigDecimal totalIcms = venda.getItens().stream().map(ItemVenda::getValorIcms).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPis = venda.getItens().stream().map(ItemVenda::getValorPis).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCofins = venda.getItens().stream().map(ItemVenda::getValorCofins).reduce(BigDecimal.ZERO, BigDecimal::add);

        text(doc, icmsTot, "vBC", decimal(venda.getTotalLiquido(), 2));
        text(doc, icmsTot, "vICMS", decimal(totalIcms, 2));
        text(doc, icmsTot, "vICMSDeson", "0.00");
        text(doc, icmsTot, "vFCP", "0.00");
        text(doc, icmsTot, "vBCST", "0.00");
        text(doc, icmsTot, "vST", "0.00");
        text(doc, icmsTot, "vFCPST", "0.00");
        text(doc, icmsTot, "vFCPSTRet", "0.00");
        text(doc, icmsTot, "vProd", decimal(venda.getTotalLiquido(), 2));
        text(doc, icmsTot, "vFrete", "0.00");
        text(doc, icmsTot, "vSeg", "0.00");
        text(doc, icmsTot, "vDesc", decimal(venda.getTotalDesconto(), 2));
        text(doc, icmsTot, "vII", "0.00");
        text(doc, icmsTot, "vIPI", "0.00");
        text(doc, icmsTot, "vIPIDevol", "0.00");
        text(doc, icmsTot, "vPIS", decimal(totalPis, 2));
        text(doc, icmsTot, "vCOFINS", decimal(totalCofins, 2));
        text(doc, icmsTot, "vOutro", "0.00");
        text(doc, icmsTot, "vNF", decimal(venda.getTotalLiquido(), 2));
        text(doc, icmsTot, "vTotTrib", decimal(venda.getTotalTributos(), 2));
    }

    private void appendTransp(Document doc, Element infNFe) {
        Element transp = append(doc, infNFe, "transp");
        text(doc, transp, "modFrete", "9");
    }

    private void appendPag(Document doc, Element infNFe, Venda venda) {
        Element pag = append(doc, infNFe, "pag");
        Element detPag = append(doc, pag, "detPag");
        text(doc, detPag, "indPag", "0");
        text(doc, detPag, "tPag", mapFormaPagamento(venda.getMetodoPagamento()));
        text(doc, detPag, "vPag", decimal(venda.getValorPago(), 2));
        if (venda.getTroco() != null && venda.getTroco().compareTo(BigDecimal.ZERO) > 0) {
            text(doc, pag, "vTroco", decimal(venda.getTroco(), 2));
        }
    }

    private void appendInfAdic(Document doc, Element infNFe) {
        Element infAdic = append(doc, infNFe, "infAdic");
        text(doc, infAdic, "infCpl", "XML NFC-e gerado localmente pelo PDVEX-VS1. Documento ainda nao assinado nem autorizado pela SEFAZ.");
    }

    private String gerarChaveAcesso(String cUf, OffsetDateTime emissao, String cnpj, int serie, long numero, String cNf) {
        String base = gerarBaseChave(cUf, emissao, cnpj, serie, numero, cNf);
        return base + gerarDigitoVerificador(base);
    }

    private String gerarBaseChave(String cUf, OffsetDateTime emissao, String cnpj, int serie, long numero, String cNf) {
        return cUf
                + emissao.format(DateTimeFormatter.ofPattern("yyMM"))
                + String.format("%014d", Long.parseLong(cnpj.isBlank() ? "0" : cnpj))
                + MODELO_NFCE
                + String.format("%03d", serie)
                + String.format("%09d", numero)
                + "1"
                + cNf;
    }

    private String gerarDigitoVerificador(String base) {
        int peso = 2;
        int soma = 0;
        for (int i = base.length() - 1; i >= 0; i--) {
            soma += Character.digit(base.charAt(i), 10) * peso;
            peso = peso == 9 ? 2 : peso + 1;
        }
        int resto = soma % 11;
        int dv = 11 - resto;
        return String.valueOf(dv >= 10 ? 0 : dv);
    }

    private String mapFormaPagamento(String metodo) {
        if (metodo == null) {
            return "99";
        }
        String normalizado = Normalizer.normalize(metodo, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toUpperCase();
        if (normalizado.contains("DINHEIRO")) {
            return "01";
        }
        if (normalizado.contains("CREDITO")) {
            return "03";
        }
        if (normalizado.contains("DEBITO")) {
            return "04";
        }
        if (normalizado.contains("PIX")) {
            return "17";
        }
        return "99";
    }

    private Element append(Document doc, Element parent, String name) {
        Element element = doc.createElement(name);
        parent.appendChild(element);
        return element;
    }

    private void text(Document doc, Element parent, String name, String value) {
        Element element = append(doc, parent, name);
        element.setTextContent(value == null ? "" : value);
    }

    private String toXml(Document doc) throws Exception {
        var transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        return writer.toString();
    }

    private String texto(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }

    private String somenteDigitos(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }

    private String codigoBarras(String value) {
        String digitos = somenteDigitos(value);
        return digitos.length() == 8 || digitos.length() == 12 || digitos.length() == 13 || digitos.length() == 14
                ? digitos
                : "SEM GTIN";
    }

    private String decimal(BigDecimal value, int scale) {
        return (value == null ? BigDecimal.ZERO : value).setScale(scale, RoundingMode.HALF_UP).toPlainString();
    }
}
