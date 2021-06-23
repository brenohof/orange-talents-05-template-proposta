package br.com.zup.proposta.cartao.biometria;

import br.com.zup.proposta.cartao.Cartao;
import br.com.zup.proposta.cartao.CartaoRepository;
import br.com.zup.proposta.core.error.ApiErroException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger logger = LoggerFactory.getLogger(BiometriaController.class);
    private EntityManager entityManager;
    private CartaoRepository cartaoRepository;

    public BiometriaController(EntityManager entityManager, CartaoRepository cartaoRepository) {
        this.entityManager = entityManager;
        this.cartaoRepository = cartaoRepository;
    }

    @PostMapping("/{idCartao}/biometrias")
    @Transactional
    public ResponseEntity<?> cadastrar(@PathVariable String idCartao,
                                       @RequestBody @Valid BiometriaRequest request,
                                       UriComponentsBuilder uriComponentsBuilder) {
        Cartao cartao = cartaoRepository.findById(idCartao)
                .orElseThrow(() -> new ApiErroException(HttpStatus.NOT_FOUND, "Cartão não encontrado."));

        Biometria biometria = request.toModel(cartao);
        entityManager.persist(biometria);
        logger.info("Biometria persisitida com id = " + biometria.getId());

        cartao.associaBiometria(biometria);
        logger.info("Cartão atualizado com a biometria associada");

        URI url = uriComponentsBuilder.path("/cartoes/{idCartao}/biometrias/{id}").build(idCartao, biometria.getId());
        return ResponseEntity.created(url).build();
    }
}
