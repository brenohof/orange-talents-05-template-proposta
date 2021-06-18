package br.com.zup.proposta.cartao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cartoes",url = "${cartao.host}")
public interface CartaoClient {

    @RequestMapping(method = RequestMethod.GET)
    CartaoResponse cartao(@RequestParam Long idProposta);
}
