package br.com.zup.proposta.proposta;

public class PropostaResponse {
    private Long id;
    private String nome;
    private StatusProposta status;

    public PropostaResponse(Proposta proposta) {
        this.id = proposta.getId();
        this.nome = proposta.getNome();
        this.status = proposta.getStatus();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public StatusProposta getStatus() {
        return status;
    }
}
