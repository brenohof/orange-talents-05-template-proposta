package br.com.zup.proposta.cartao.bloqueio;

import br.com.zup.proposta.cartao.Cartao;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "bloqueios")
public class Bloqueio {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @OneToOne
    private Cartao cartao;
    @NotNull
    private String ipCliente;
    @NotNull
    private String userAgentCliente;
    @NotNull
    private LocalDateTime bloqueadoEm = LocalDateTime.now();

    @Deprecated
    public Bloqueio() {
    }

    public Bloqueio(Cartao cartao, String userAgent, String ip) {
        this.cartao = cartao;
        this.userAgentCliente = userAgent;
        this.ipCliente = ip;
    }

    public String getId() {
        return id;
    }
}
