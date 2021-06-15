package br.com.zup.proposta.proposta.biometria;

import br.com.zup.proposta.proposta.cartao.Cartao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/biometrias")
public class BiometriaController {
    @Autowired
    private EntityManager entityManager;

    @PostMapping("/{idCartao}")
    @Transactional
    public ResponseEntity<?> cadastrar(@PathVariable String idCartao,
                                       @RequestBody @Valid BiometriaRequest request,
                                       UriComponentsBuilder uriComponentsBuilder) {
        Cartao cartao = entityManager.find(Cartao.class, idCartao);
        if (cartao == null)
            return ResponseEntity.notFound().build();

        Biometria biometria = request.toModel(cartao);
        entityManager.persist(biometria);

        URI urlNovaProposta = uriComponentsBuilder.path("/biometrias/{id}").build(biometria.getId());
        return ResponseEntity.created(urlNovaProposta).build();
    }
}
