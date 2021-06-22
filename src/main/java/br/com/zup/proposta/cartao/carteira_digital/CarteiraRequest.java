package br.com.zup.proposta.cartao.carteira_digital;

import br.com.zup.proposta.cartao.Cartao;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.util.Assert;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class CarteiraRequest {
    private @NotBlank @Email String email;

    @JsonCreator
    public CarteiraRequest(String email) {
        this.email = email;
    }

    public CarteiraDigital toModel(Cartao cartao, EmissorCarteira emissorCarteira) {
        Assert.isTrue(cartao != null, "[BUG] Cartão não deveria ser nulo.");
        Assert.isTrue(emissorCarteira!= null, "[BUG] Emissor não deve ser nulo.");

        return new CarteiraDigital(email, emissorCarteira, cartao);
    }

    public String getEmail() {
        return email;
    }
}
