package br.com.zup.proposta.proposta.cartao.bloqueio;

import br.com.zup.proposta.proposta.cartao.Cartao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/cartoes")
public class BloqueioController {
    
    @Autowired
    private EntityManager entityManager;
    
    @PostMapping("/{idCartao}/bloqueios")
    @Transactional
    public ResponseEntity<?> bloquearCartao(@PathVariable String idCartao,
                                            HttpServletRequest request) {
        Cartao cartao = entityManager.find(Cartao.class, idCartao);
        if (cartao == null)
            return ResponseEntity.notFound().build();
        if (cartao.estaBloqueado())
            return ResponseEntity.unprocessableEntity().build();

        String userAgent = request.getHeader("User-Agent");
        String ipClient = request.getRemoteAddr();
        
        Bloqueio bloqueio = new Bloqueio(cartao, userAgent, ipClient);
        entityManager.persist(bloqueio);
        cartao.bloquear(bloqueio);

        return ResponseEntity.ok().build();
    }
}
