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

    @PostMapping("/{idCartao}/carteiras")
    @Transactional
    public ResponseEntity<?> associarCarteiraPaypal(@PathVariable String idCartao,
                                                    @Valid @RequestBody CarteiraRequest request,
                                                    UriComponentsBuilder uriComponentsBuilder) {

        Cartao cartao = cartaoRepository.findById(idCartao)
                .orElseThrow(() -> new ApiErroException(HttpStatus.NOT_FOUND, "Cartão não encontrado."));

        CarteiraDigital carteiraDigital = carteiraRepository.findByCartaoAndEmissor(cartao, request.getCarteira());
        if (carteiraDigital != null)
            throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Esse cartão já foi vinculado a essa carteira.");

        notificarLegado(request, cartao);

        CarteiraDigital novaCarteiraDigital = request.toModel(cartao);
        carteiraRepository.save(novaCarteiraDigital);
        cartao.associaCarteira(novaCarteiraDigital);

        URI url = uriComponentsBuilder.path("/cartoes/{idCartao}/carteiras/{id}")
                .build(idCartao, novaCarteiraDigital.getId());
        return ResponseEntity.created(url).build();
    }

    private void notificarLegado(CarteiraRequest request, Cartao cartao) {
        try {
            cartaoClient.associarCarteira(cartao.getNumero(), request);
        } catch (FeignException e) {
            throw  new ApiErroException(HttpStatus.INTERNAL_SERVER_ERROR, "Houve um erro ao associar a carteira.");
        }
    }
}
