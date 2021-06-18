package br.com.zup.proposta.cartao.bloqueio;

public class BloqueioRequest {
    private String sistemaResponsavel;

    public BloqueioRequest(String sistemaResponsavel) {
        this.sistemaResponsavel = sistemaResponsavel;
    }

    public static BloqueioRequest build(String sistemaResponsavel) {
        return new BloqueioRequest(sistemaResponsavel);
    }

    public String getSistemaResponsavel() {
        return sistemaResponsavel;
    }
}
