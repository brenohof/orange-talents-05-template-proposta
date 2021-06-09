package br.com.zup.proposta.config.actuator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;

@Component
public class MyHealthIndicator implements HealthIndicator {

    @Value(value = "${analise.health}")
    private String analise_url;

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
        } catch (Exception e) {
            return 500;
        }
        return 0;
    }

}