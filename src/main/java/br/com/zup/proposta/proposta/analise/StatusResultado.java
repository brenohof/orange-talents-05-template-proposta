package br.com.zup.proposta.proposta.analise;

import br.com.zup.proposta.proposta.StatusProposta;

public enum StatusResultado {
    COM_RESTRICAO, SEM_RESTRICAO;

    StatusProposta normaliza() {
        if (this.equals(COM_RESTRICAO))
            return StatusProposta.NAO_ELEGIVEL;

        return StatusProposta.ELEGIVEL;
    }
}
