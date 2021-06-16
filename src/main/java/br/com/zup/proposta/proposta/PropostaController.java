package br.com.zup.proposta.proposta;

import br.com.zup.proposta.proposta.analise.SolicitacaoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    @Autowired
    private PropostaRepository repository;

    @Value(value = "${analise.host}")
    private String SOLICITACAO_URL;

    @PostMapping
    @Transactional
    public ResponseEntity<?> criarProposta(@RequestBody @Valid PropostaRequest request,
                                           UriComponentsBuilder uriComponentsBuilder) {
        Optional<Proposta> optional = repository.findByDocumento(request.getDocumento());
        if (optional.isPresent())
            return ResponseEntity.unprocessableEntity().build();

        Proposta proposta = request.toModel();
        repository.save(proposta);

        SolicitacaoRequest.solicitar(proposta, SOLICITACAO_URL);

        URI urlNovaProposta = uriComponentsBuilder.path("/propostas/{id}").build(proposta.getId());
        return ResponseEntity.created(urlNovaProposta).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> consultaProposta(@PathVariable Long id) {
        Optional<Proposta> optional = repository.findById(id);
        if (!optional.isPresent())
            return ResponseEntity.notFound().build();

        Proposta proposta = optional.get();
        return ResponseEntity.ok(new PropostaResponse(proposta));
    }
}
