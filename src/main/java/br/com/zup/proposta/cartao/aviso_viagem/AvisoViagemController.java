package br.com.zup.proposta.cartao.aviso_viagem;

import br.com.zup.proposta.cartao.Cartao;
import br.com.zup.proposta.cartao.CartaoClient;
import br.com.zup.proposta.cartao.CartaoRepository;
import br.com.zup.proposta.core.error.ApiErroException;
import feign.FeignException;
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
    private CartaoClient cartaoClient;

    public AvisoViagemController(EntityManager entityManager,
                                 CartaoRepository cartaoRepository,
                                 CartaoClient cartaoClient) {
        this.entityManager = entityManager;
        this.cartaoRepository = cartaoRepository;
        this.cartaoClient = cartaoClient;
    }

    @PostMapping("/{idCartao}/aviso-viagem")
    @Transactional
    public ResponseEntity<?> novoAvisoViagem(@PathVariable String idCartao,
                                             HttpServletRequest servletRequest,
                                             @Valid @RequestBody AvisoViagemRequest  request) {
        return cartaoRepository.findById(idCartao)
                .map(cartao -> {
                    notificaAviso(request, cartao);
                    Long idAviso = geraAviso(servletRequest, request, cartao);
                    return idAviso;
                })
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ApiErroException(HttpStatus.NOT_FOUND, "Cartão não encontrado."));
    }

    private void notificaAviso(AvisoViagemRequest request, Cartao cartao) {
        try {
            AvisoLegadoRequest avisoLegadoRequest =
                    new AvisoLegadoRequest(request.getDestino(), request.getDataTermino());
            cartaoClient.avisarViagem(cartao.getNumero(), avisoLegadoRequest);
        } catch (FeignException e) {
            throw new ApiErroException(HttpStatus.INTERNAL_SERVER_ERROR, "Houve um problema ao avisar uma viagem.");
        }
    }

    private Long geraAviso(HttpServletRequest servletRequest, AvisoViagemRequest request, Cartao cartao) {
        String userAgent = servletRequest.getHeader("User-Agent");
        String ip = servletRequest.getRemoteAddr();

        AvisoViagem novoAvisoViagem = request.toModel(cartao, ip, userAgent);

        entityManager.persist(novoAvisoViagem);
        logger.info("Novo aviso de viagem foi persistido com id = " + novoAvisoViagem.getId());
        return novoAvisoViagem.getId();
    }
}
