package br.com.zup.proposta.cartao.biometria;

import br.com.zup.proposta.cartao.Cartao;
import br.com.zup.proposta.core.error.ApiErroException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/cartoes")
public class BiometriaController {
    @Autowired
    private EntityManager entityManager;

    @PostMapping("/{idCartao}/biometrias")
    @Transactional
    public ResponseEntity<?> cadastrar(@PathVariable String idCartao,
                                       @RequestBody @Valid BiometriaRequest request,
                                       UriComponentsBuilder uriComponentsBuilder) {
        Cartao cartao = entityManager.find(Cartao.class, idCartao);
        if (cartao == null)
            throw new ApiErroException(HttpStatus.NOT_FOUND, "Cartão não encontrado.");

        Biometria biometria = request.toModel(cartao);
        entityManager.persist(biometria);
        cartao.associaBiometria(biometria);

        URI url = uriComponentsBuilder.path("/cartoes/{idCartao}/biometrias/{id}").build(idCartao, biometria.getId());
        return ResponseEntity.created(url).build();
    }
}
