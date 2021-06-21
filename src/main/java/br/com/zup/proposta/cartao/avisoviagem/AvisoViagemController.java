package br.com.zup.proposta.cartao.avisoviagem;

import br.com.zup.proposta.cartao.CartaoRepository;
import br.com.zup.proposta.core.error.ApiErroException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/cartoes")
public class AvisoViagemController {

    private EntityManager entityManager;
    private CartaoRepository cartaoRepository;
    private Logger logger = LoggerFactory.getLogger(AvisoViagemController.class);

    public AvisoViagemController(EntityManager entityManager,
                                 CartaoRepository cartaoRepository) {
        this.entityManager = entityManager;
        this.cartaoRepository = cartaoRepository;
    }

    @PostMapping("/{idCartao}/aviso-viagem")
    @Transactional
    public ResponseEntity<?> novoAvisoViagem(@PathVariable String idCartao,
                                             HttpServletRequest servletRequest,
                                             @Valid @RequestBody AvisoViagemRequest  request) {
        return cartaoRepository.findById(idCartao)
                .map(cartao -> {
                    String userAgent = servletRequest.getHeader("User-Agent");
                    String ip = servletRequest.getRemoteAddr();

                    AvisoViagem novoAvisoViagem = request.toModel(cartao, ip, userAgent);

                    entityManager.persist(novoAvisoViagem);
                    logger.info("Novo aviso de viagem foi persistido com id = " + novoAvisoViagem.getId());
                    return novoAvisoViagem.getId();
                })
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ApiErroException(HttpStatus.NOT_FOUND, "Cartão não encontrado."));
    }
}
