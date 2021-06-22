package br.com.zup.proposta.cartao;

import br.com.zup.proposta.cartao.aviso_viagem.AvisoLegadoRequest;
import br.com.zup.proposta.cartao.bloqueio.BloqueioRequest;
import br.com.zup.proposta.cartao.carteira_digital.CarteiraLegadoRequest;
import br.com.zup.proposta.cartao.carteira_digital.CarteiraRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "cartoes", url = "${cartao.host}")
public interface CartaoClient {

    @GetMapping
    CartaoResponse cartao(@RequestParam Long idProposta);

    @PostMapping("/{id}/bloqueios")
    void bloquear(@PathVariable String id, @RequestBody BloqueioRequest request);

    @PostMapping("/{id}/avisos")
    void avisarViagem(@PathVariable String id, @RequestBody AvisoLegadoRequest request);

    @PostMapping("/{id}/carteiras")
    void associarCarteira(@PathVariable String id, @RequestBody CarteiraLegadoRequest request);
}
