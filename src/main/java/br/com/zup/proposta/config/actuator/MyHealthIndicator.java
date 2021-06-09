package br.com.zup.proposta.config.actuator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MyHealthIndicator implements HealthIndicator {

    Logger logger = LoggerFactory.getLogger(MyHealthIndicator.class);

    @Value(value = "${analise.health}")
    private String analise_url;

    @Value(value = "${cartao.health}")
    private String cartao_url;

    @Override
    public Health health() {
        int errorCode = check();
        if (errorCode != 0) {
            return Health.down().withDetail("Error Code", errorCode).build();
        }
        return Health.up().build();
    }

    private int check() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getForEntity(analise_url, String.class);
            logger.info("API de analise está funcionando corretamente.");
        } catch (Exception e) {
            logger.error("API de analise NÃO está funcionando corretamente.");
            return 500;
        }

        try {
            restTemplate.getForEntity(cartao_url, String.class);
            logger.info("API de cartão está funcionando corretamente.");
        } catch (Exception e) {
            logger.error("API de cartão NÃO está funcionando corretamente.");
            return 500;
        }

        return 0;
    }

}