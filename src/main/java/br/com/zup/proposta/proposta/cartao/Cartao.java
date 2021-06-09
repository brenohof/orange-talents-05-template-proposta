package br.com.zup.proposta.proposta.cartao;

import br.com.zup.proposta.proposta.Proposta;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cartoes")
public class Cartao {
    @Id @Column(unique = true)
    private String id;
    @NotNull
    private LocalDateTime emitidoEm;
    @NotNull
    private String titular;
    @OneToOne @NotNull
    private Proposta proposta;
    @NotNull
    private BigDecimal limite;

    @Deprecated
    public Cartao() {}

    public Cartao(String id, LocalDateTime emitidoEm, String titular, Proposta proposta, BigDecimal limite) {
        this.id = id;
        this.emitidoEm = emitidoEm;
        this.titular = titular;
        this.proposta = proposta;
        this.limite = limite;
    }
}
