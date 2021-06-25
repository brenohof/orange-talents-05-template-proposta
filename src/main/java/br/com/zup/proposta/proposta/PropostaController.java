package br.com.zup.proposta.proposta;

import br.com.zup.proposta.core.error.ApiErroException;
import br.com.zup.proposta.core.metricas.Metricas;
import br.com.zup.proposta.proposta.analise.AnaliseClient;
import br.com.zup.proposta.proposta.analise.AnaliseRequest;
import io.micrometer.core.instrument.MeterRegistry;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    private PropostaRepository repository;
    private AnaliseClient analiseClient;
    private Tracer tracer;
    private Metricas metricas;
    private Logger logger = LoggerFactory.getLogger(PropostaController.class);

    public PropostaController(PropostaRepository repository,
                              AnaliseClient analiseClient,
                              Tracer tracer,
                              Metricas metricas) {
        this.repository = repository;
        this.analiseClient = analiseClient;
        this.tracer = tracer;
        this.metricas = metricas;
    }

    @PostMapping
    public ResponseEntity<?> criarProposta(@RequestBody @Valid PropostaRequest request,
                                           UriComponentsBuilder uriComponentsBuilder) {
        Span activeSpan = tracer.activeSpan();
        activeSpan.setTag("user.email", request.getEmail());
        activeSpan.setBaggageItem("user.email", request.getEmail());
        activeSpan.log("Requisição para criação de uma nova proposta feita pelo titular = " + request.getNome());

        Long startTime = System.currentTimeMillis();

        repository.findByDocumento(request.getDocumento())
                .ifPresent(s -> {throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Já existe uma proposta com esse documento.");});

        Proposta proposta = request.toModel();
        repository.save(proposta);
        logger.info("Proposta persistida com id = " + proposta.getId());

        AnaliseRequest.solicitar(proposta, analiseClient);
        repository.save(proposta);
        logger.info("Proposta atualizada com o status = " + proposta.getStatus());

        metricas.contadorPropostas().increment();
        metricas.timerCriaProposta().record(System.currentTimeMillis() - startTime, TimeUnit.MILLISECONDS);

        URI url = uriComponentsBuilder.path("/propostas/{id}").build(proposta.getId());
        return ResponseEntity.created(url).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> consultaProposta(@PathVariable String id) {
        return repository.findById(id)
                .map(PropostaResponse::new)
                .map(ResponseEntity::ok)
                .orElseThrow(() ->  new ApiErroException(HttpStatus.NOT_FOUND, "Proposta não encontrada."));
    }
}
