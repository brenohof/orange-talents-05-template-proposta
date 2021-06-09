package br.com.zup.proposta.proposta.analise;

import br.com.zup.proposta.proposta.Proposta;
import br.com.zup.proposta.proposta.StatusProposta;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class SolicitacaoRequest {
    private String documento;
    private String nome;
    private Long idProposta;

    public SolicitacaoRequest(Proposta proposta) {
        this.documento = proposta.getDocumento();
        this.nome = proposta.getNome();
        this.idProposta = proposta.getId();
    }

    public static void solicitar(Proposta proposta, String URL) {
        RestTemplate restTemplate = new RestTemplate();
        SolicitacaoRequest request = new SolicitacaoRequest(proposta);
        try {
            restTemplate.postForEntity(URL, request, String.class);
            proposta.defineStatus(StatusProposta.ELEGIVEL);
        } catch (HttpClientErrorException exception) {
            proposta.defineStatus(StatusProposta.NAO_ELEGIVEL);
        }
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public Long getIdProposta() {
        return idProposta;
    }
}
