package br.com.zup.proposta.cartao.carteira_digital;

import br.com.zup.proposta.cartao.Cartao;
import org.springframework.util.Assert;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class CarteiraRequest {
    private @NotBlank @Email String email;
    private @NotBlank String carteira;

    public CarteiraRequest(String email, String carteira) {
        this.email = email;
        this.carteira = carteira;
    }

    public String getCarteira() {
        return carteira;
    }

    public String getEmail() {
        return email;
    }

    public CarteiraDigital toModel(Cartao cartao) {
        Assert.isTrue(cartao != null, "[BUG] Cartão não deveria ser nulo.");

        return new CarteiraDigital(email, carteira, cartao);
    }
}
