package br.com.zup.proposta.proposta.analise;

import br.com.zup.proposta.proposta.Proposta;
import br.com.zup.proposta.proposta.StatusProposta;
import feign.FeignException;

public class AnaliseRequest {
    private String documento;
    private String nome;
    private String idProposta;

    public AnaliseRequest(String documento, String nome, String idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.idProposta = idProposta;
    }

    public static AnaliseRequest build(Proposta proposta) {
        return new AnaliseRequest(proposta.getDocumento(), proposta.getNome(), proposta.getId());
    }

    public static void solicitar(Proposta proposta, AnaliseClient analiseClient) {
        AnaliseRequest request = AnaliseRequest.build(proposta);
        try {
            analiseClient.solicitarAnalise(request);
            proposta.defineStatus(StatusProposta.ELEGIVEL);
        } catch (FeignException e) {
            proposta.defineStatus(StatusProposta.NAO_ELEGIVEL);
        }
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public String getIdProposta() {
        return idProposta;
    }
}
