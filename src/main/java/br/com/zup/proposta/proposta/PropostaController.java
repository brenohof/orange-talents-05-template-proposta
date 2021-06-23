package br.com.zup.proposta.proposta;

import br.com.zup.proposta.core.error.ApiErroException;
import br.com.zup.proposta.proposta.analise.AnaliseClient;
import br.com.zup.proposta.proposta.analise.AnaliseRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    private PropostaRepository repository;
    private AnaliseClient analiseClient;
    private Logger logger = LoggerFactory.getLogger(PropostaController.class);

    public PropostaController(PropostaRepository repository, AnaliseClient analiseClient) {
        this.repository = repository;
        this.analiseClient = analiseClient;
    }

    @PostMapping
    public ResponseEntity<?> criarProposta(@RequestBody @Valid PropostaRequest request,
                                           UriComponentsBuilder uriComponentsBuilder) {
        repository.findByDocumento(request.getDocumento())
                .ifPresent(s -> {throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Já existe uma proposta com esse documento.");});

        Proposta proposta = request.toModel();
        repository.save(proposta);
        logger.info("Proposta persistida com id = " + proposta.getId());

        AnaliseRequest.solicitar(proposta, analiseClient);
        repository.save(proposta);
        logger.info("Proposta atualizada com o status = " + proposta.getStatus());

        URI url = uriComponentsBuilder.path("/propostas/{id}").build(proposta.getId());
        return ResponseEntity.created(url).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> consultaProposta(@PathVariable Long id) {
        return repository.findById(id)
                .map(PropostaResponse::new)
                .map(ResponseEntity::ok)
                .orElseThrow(() ->  new ApiErroException(HttpStatus.NOT_FOUND, "Proposta não encontrada."));
    }
}
