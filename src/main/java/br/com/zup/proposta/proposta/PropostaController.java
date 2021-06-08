package br.com.zup.proposta.proposta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    @Autowired
    private PropostaRepository repository;

    @PostMapping
    public ResponseEntity<?> criarProposta(@RequestBody @Valid PropostaRequest request,
                                           UriComponentsBuilder uriComponentsBuilder) {
        Optional<Proposta> optional = repository.findByDocumento(request.getDocumento());
        if (optional.isPresent())
            return ResponseEntity.unprocessableEntity().build();

        Proposta proposta = request.toModel();
        repository.save(proposta);

        URI urlNovaProposta = uriComponentsBuilder.path("/propostas/{id}").build(proposta.getId());
        return ResponseEntity.created(urlNovaProposta).build();
    }
}
