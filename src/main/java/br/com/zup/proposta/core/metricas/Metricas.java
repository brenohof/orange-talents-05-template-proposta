package br.com.zup.proposta.core.metricas;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class Metricas {

    private final MeterRegistry meterRegistry;

    public Metricas(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public Counter contadorPropostas() {
        Collection<Tag> tags = new ArrayList<>();
        tags.add(Tag.of("emissora", "Mastercard"));
        tags.add(Tag.of("banco", "Itaú"));

        return this.meterRegistry.counter("proposta_criada", tags);
    }

    public Timer timerCriaProposta() {
        Collection<Tag> tags = new ArrayList<>();
        tags.add(Tag.of("emissora", "Mastercard"));
        tags.add(Tag.of("banco", "Itaú"));

        return this.meterRegistry.timer("cria_proposta", tags);
    }
}
