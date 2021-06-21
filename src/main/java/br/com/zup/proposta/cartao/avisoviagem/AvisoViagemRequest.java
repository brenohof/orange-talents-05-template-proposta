package br.com.zup.proposta.cartao.avisoviagem;

import br.com.zup.proposta.cartao.Cartao;
import org.springframework.util.Assert;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class AvisoViagemRequest {
    private @NotBlank  String destino;
    private @Future @NotNull LocalDate dataTermino;

    public AvisoViagemRequest(String destino, LocalDate dataTermino) {
        this.destino = destino;
        this.dataTermino = dataTermino;
    }

    public AvisoViagem toModel(Cartao cartao, String ip, String userAgent) {
        Assert.isTrue(cartao != null, "[BUG] Cartão não deve ser nulo.");
        Assert.isTrue(ip != null, "[BUG] IP não deve ser nulo.");
        Assert.isTrue(userAgent != null, "[BUG] User-Agent não deve ser nulo.");
        return new AvisoViagem(cartao, ip, userAgent, destino, dataTermino);
    }
}
