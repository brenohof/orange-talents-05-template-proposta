package br.com.zup.proposta.proposta.analise;

import br.com.zup.proposta.proposta.StatusProposta;

public class SolicitacaoResponse {
    private String documento;
    private String nome;
    private Long idProposta;
    private StatusResultado resultadoSolicitacao;

    @Deprecated
    public SolicitacaoResponse() {}

    public SolicitacaoResponse(String documento, String nome, Long idProposta, StatusResultado resultadoSolicitacao) {
        this.documento = documento;
        this.nome = nome;
        this.idProposta = idProposta;
        this.resultadoSolicitacao = resultadoSolicitacao;
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

    public StatusResultado getResultadoSolicitacao() {
        return resultadoSolicitacao;
    }
}
