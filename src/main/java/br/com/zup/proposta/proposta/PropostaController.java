package br.com.zup.proposta.proposta;

import br.com.zup.proposta.proposta.analise.SolicitacaoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    @Autowired
    private PropostaRepository repository;

    @Value(value = "${solicitacao.host}")
    private String SOLICITACAO_URL;

    @PostMapping
    public ResponseEntity<?> criarProposta(@RequestBody @Valid PropostaRequest request,
                                           UriComponentsBuilder uriComponentsBuilder) {
        Optional<Proposta> optional = repository.findByDocumento(request.getDocumento());
        if (optional.isPresent())
            return ResponseEntity.unprocessableEntity().build();

        Proposta proposta = request.toModel();
        repository.save(proposta);

        SolicitacaoRequest.solicitar(proposta, SOLICITACAO_URL);
        repository.save(proposta);

        URI urlNovaProposta = uriComponentsBuilder.path("/propostas/{id}").build(proposta.getId());
        return ResponseEntity.created(urlNovaProposta).build();
    }
}
