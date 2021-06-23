package br.com.zup.proposta.cartao.bloqueio;

import br.com.zup.proposta.cartao.Cartao;
import br.com.zup.proposta.cartao.CartaoClient;
import br.com.zup.proposta.cartao.CartaoTask;
import br.com.zup.proposta.core.error.ApiErroException;
import feign.FeignException;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/cartoes")
public class BloqueioController {

    private Logger logger = LoggerFactory.getLogger(BloqueioController.class);
    private EntityManager entityManager;
    private CartaoClient cartaoClient;
    private Tracer tracer;

    public BloqueioController(EntityManager entityManager, CartaoClient cartaoClient, Tracer tracer) {
        this.entityManager = entityManager;
        this.cartaoClient = cartaoClient;
        this.tracer = tracer;
    }

    @PostMapping("/{idCartao}/bloqueios")
    @Transactional
    public ResponseEntity<?> bloquearCartao(@PathVariable String idCartao,
                                            HttpServletRequest request) {
        Span activeSpan = tracer.activeSpan();
        activeSpan.setTag("cartao.id", idCartao);
        activeSpan.setBaggageItem("cartao.id", idCartao);

        Cartao cartao = verificaDadosCartao(idCartao);

        notificaLegado(cartao);

        String userAgent = request.getHeader("User-Agent");
        String ipClient = request.getRemoteAddr();

        Bloqueio bloqueio = new Bloqueio(cartao, userAgent, ipClient);
        entityManager.persist(bloqueio);
        cartao.bloquear(bloqueio);

        logger.info("Novo bloqueio com id = " + bloqueio.getId());
        return ResponseEntity.ok().build();
    }

    private void notificaLegado(Cartao cartao) {
        try {
            cartaoClient.bloquear(cartao.getNumero(), BloqueioRequest.build("proposta"));
        } catch (FeignException e) {
            throw new ApiErroException(HttpStatus.INTERNAL_SERVER_ERROR, "Houve um erro ao processar o bloqueio.");
        }
    }

    private Cartao verificaDadosCartao(String idCartao) {
        Cartao cartao = entityManager.find(Cartao.class, idCartao);
        if (cartao == null)
            throw new ApiErroException(HttpStatus.NOT_FOUND, "Cartão não encontrado.");
        if (cartao.estaBloqueado())
            throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Este cartão já está bloqueado.");
        return cartao;
    }


}
