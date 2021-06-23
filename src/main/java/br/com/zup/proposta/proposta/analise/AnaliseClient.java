package br.com.zup.proposta.proposta.analise;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(name = "analise-propostas", url = "${api.analise}")
public interface AnaliseClient {

    @PostMapping
    void solicitarAnalise(@RequestBody AnaliseRequest request);
}
