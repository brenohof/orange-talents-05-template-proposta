package br.com.zup.proposta.proposta.biometria;

import br.com.zup.proposta.proposta.cartao.Cartao;
import br.com.zup.proposta.validator.Base64;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.util.Assert;

import javax.validation.constraints.NotBlank;

public class BiometriaRequest {
    @NotBlank @Base64
    public String biometria;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public BiometriaRequest(String biometria) {
        this.biometria = biometria;
    }

    public Biometria toModel(Cartao cartao) {
        Assert.isTrue(cartao!=null, "[BUG] Cartão não deve ser nulo.");
        return new Biometria(biometria, cartao);
    }
}
