package br.com.zup.proposta.core.error;

import java.util.Collection;

public class ErroResponse {
    private Collection<String> mensagens;

    public ErroResponse(Collection<String> mensagens) {
        this.mensagens = mensagens;
    }

    public Collection<String> getMensagens() {
        return mensagens;
    }
}
