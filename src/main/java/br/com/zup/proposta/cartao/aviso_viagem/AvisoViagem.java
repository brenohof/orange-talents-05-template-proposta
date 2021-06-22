package br.com.zup.proposta.cartao.aviso_viagem;

import br.com.zup.proposta.cartao.Cartao;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "aviso_viagens")
public class AvisoViagem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @NotNull
    private Cartao cartao;
    private @NotNull String ipDoCliente;
    private @NotNull String userAgentCliente;
    private @NotNull String destino;
    private @NotNull LocalDate dataTermino;
    private LocalDateTime avisadoEm = LocalDateTime.now();

    @Deprecated
    public AvisoViagem() {
    }

    public AvisoViagem(Cartao cartao, String ip, String userAgent, @NotBlank String destino, @Future @NotNull LocalDate dataTermino) {
        this.cartao = cartao;
        this.ipDoCliente = ip;
        this.userAgentCliente = userAgent;
        this.destino = destino;
        this.dataTermino = dataTermino;
    }

    public Long getId() {
        return id;
    }
}
