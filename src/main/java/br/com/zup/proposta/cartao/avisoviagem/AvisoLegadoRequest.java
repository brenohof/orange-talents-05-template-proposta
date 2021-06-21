package br.com.zup.proposta.cartao.avisoviagem;

import java.time.LocalDate;

public class AvisoLegadoRequest {
    private String destino;
    private LocalDate validoAte;

    public AvisoLegadoRequest(String destino, LocalDate validoAte) {
        this.destino = destino;
        this.validoAte = validoAte;
    }

    public String getDestino() {
        return destino;
    }

    public LocalDate getValidoAte() {
        return validoAte;
    }
}
