package br.com.zup.proposta.cartao.carteira_digital;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class CarteiraLegadoRequest {
    private String email;
    private String carteira;

    public CarteiraLegadoRequest(String email, String carteira) {
        this.email = email;
        this.carteira = carteira;
    }

    public static CarteiraLegadoRequest build(CarteiraRequest request, EmissorCarteira emissor) {
        return new CarteiraLegadoRequest(request.getEmail(), emissor.toString());
    }

    public String getEmail() {
        return email;
    }

    public String getCarteira() {
        return carteira;
    }
}
