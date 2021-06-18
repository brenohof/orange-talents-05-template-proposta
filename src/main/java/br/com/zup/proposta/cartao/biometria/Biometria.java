package br.com.zup.proposta.cartao.biometria;

import br.com.zup.proposta.cartao.Cartao;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "biometrias")
public class Biometria {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @NotNull
    private String biometria;
    @NotNull
    private LocalDateTime associadaEm = LocalDateTime.now();
    @NotNull @ManyToOne
    private Cartao cartao;

    @Deprecated
    public Biometria() {
    }

    public Biometria(String biometria, Cartao cartao) {
        this.biometria = biometria;
        this.cartao = cartao;
    }

    public String getId() {
        return id;
    }
}
