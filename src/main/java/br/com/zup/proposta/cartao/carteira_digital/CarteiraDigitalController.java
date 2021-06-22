package br.com.zup.proposta.cartao.carteira_digital;

import br.com.zup.proposta.cartao.Cartao;
import br.com.zup.proposta.cartao.CartaoClient;
import br.com.zup.proposta.cartao.CartaoRepository;
import br.com.zup.proposta.core.error.ApiErroException;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/cartoes")
public class CarteiraDigitalController {

    private CartaoRepository cartaoRepository;
    private CartaoClient cartaoClient;
    private CarteiraRepository carteiraRepository;

    public CarteiraDigitalController(CartaoRepository cartaoRepository,
                                     CartaoClient cartaoClient,
                                     CarteiraRepository carteiraRepository) {
        this.cartaoRepository = cartaoRepository;
        this.cartaoClient = cartaoClient;
        this.carteiraRepository = carteiraRepository;
    }

    @PostMapping("/{idCartao}/paypal")
    @Transactional
    public ResponseEntity<?> associarCarteiraPaypal(@PathVariable String idCartao,
                                                    @Valid @RequestBody CarteiraRequest request,
                                                    UriComponentsBuilder uriComponentsBuilder) {
        URI url = processa(idCartao, request, uriComponentsBuilder, EmissorCarteira.PAYPAL);
        return ResponseEntity.created(url).build();
    }

    @PostMapping("/{idCartao}/samsungpay")
    @Transactional
    public ResponseEntity<?> associarCarteiraSamsungPay(@PathVariable String idCartao,
                                                    @Valid @RequestBody CarteiraRequest request,
                                                    UriComponentsBuilder uriComponentsBuilder) {
        URI url = processa(idCartao, request, uriComponentsBuilder, EmissorCarteira.SAMSUNG_PAY);
        return ResponseEntity.created(url).build();
    }

    private URI processa(String idCartao, CarteiraRequest request,
                         UriComponentsBuilder uriComponentsBuilder,
                         EmissorCarteira emissor) {
        Cartao cartao = validarDados(idCartao, emissor);


        notificarLegado(CarteiraLegadoRequest.build(request, emissor), cartao);

        CarteiraDigital novaCarteiraDigital = request.toModel(cartao, emissor);
        carteiraRepository.save(novaCarteiraDigital);
        cartao.associaCarteira(novaCarteiraDigital);

        URI url = uriComponentsBuilder.path("/cartoes/{idCartao}/carteiras/{id}")
                .build(idCartao, novaCarteiraDigital.getId());
        return url;
    }

    private Cartao validarDados(String idCartao, EmissorCarteira emissor) {
        Cartao cartao = cartaoRepository.findById(idCartao)
                .orElseThrow(() -> new ApiErroException(HttpStatus.NOT_FOUND, "Cartão não encontrado."));

        CarteiraDigital carteiraDigital = carteiraRepository.findByCartaoAndEmissor(cartao, emissor);
        if (carteiraDigital != null)
            throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Esse cartão já foi vinculado a essa carteira.");
        return cartao;
    }

    private void notificarLegado(CarteiraLegadoRequest request, Cartao cartao) {
        try {
            cartaoClient.associarCarteira(cartao.getNumero(), request);
        } catch (FeignException e) {
            throw  new ApiErroException(HttpStatus.INTERNAL_SERVER_ERROR, "Houve um erro ao associar a carteira.");
        }
    }
}
