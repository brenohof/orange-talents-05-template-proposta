package br.com.zup.proposta.cartao;

import br.com.zup.proposta.proposta.Proposta;
import br.com.zup.proposta.proposta.PropostaRepository;
import br.com.zup.proposta.proposta.StatusProposta;
import feign.FeignException.FeignServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartaoTask {

    private Logger logger = LoggerFactory.getLogger(CartaoTask.class);
    private PropostaRepository propostaRepository;
    private CartaoClient cartaoClient;

    public CartaoTask(PropostaRepository propostaRepository, CartaoClient cartaoClient) {
        this.propostaRepository = propostaRepository;
        this.cartaoClient = cartaoClient;
    }

    @Scheduled(fixedDelayString = "${periodicidade.consulta-cartao}")
    public void consultaCartao() {
        List<Proposta> propostas = propostaRepository.findByCartaoNullAndStatus(StatusProposta.ELEGIVEL);

        propostas.forEach(proposta -> {
            String idProposta = proposta.getId();
            try {
                CartaoResponse response = cartaoClient.cartao(idProposta);
                Cartao cartao = response.toModel(proposta);

                proposta.associaCartao(cartao);
                propostaRepository.save(proposta);

                logger.info("Cartão associado a proposta " + idProposta + "!");
            } catch (FeignServerException e) {
                logger.info("Não há cartão para a proposta " + idProposta);
            }
        });
    }
}
