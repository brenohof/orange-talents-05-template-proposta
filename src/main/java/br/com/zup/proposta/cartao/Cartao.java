package br.com.zup.proposta.cartao;

import br.com.zup.proposta.proposta.Proposta;
import br.com.zup.proposta.cartao.biometria.Biometria;
import br.com.zup.proposta.cartao.bloqueio.Bloqueio;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cartoes")
public class Cartao implements Serializable {
    @Id @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @NotNull
    private String numero;
    @NotNull
    private LocalDateTime emitidoEm;
    @NotNull
    private String titular;
    @OneToOne @NotNull
    private Proposta proposta;
    @NotNull
    private BigDecimal limite;
    @OneToMany(mappedBy = "cartao", cascade = CascadeType.MERGE)
    private Set<Biometria> biometrias = new HashSet<>();
    @OneToOne(mappedBy = "cartao", cascade = CascadeType.MERGE)
    private Bloqueio bloqueio;
    @Enumerated(EnumType.STRING)
    private StatusCartao statusCartao = StatusCartao.LIBERADO;

    @Deprecated
    public Cartao() {}

    public Cartao(String numero, LocalDateTime emitidoEm, String titular, Proposta proposta, BigDecimal limite) {
        this.numero = numero;
        this.emitidoEm = emitidoEm;
        this.titular = titular;
        this.proposta = proposta;
        this.limite = limite;
    }

    public void associaBiometria(Biometria biometria) {
        Assert.isTrue(biometria!=null, "[BUG] Biometria não deveria ser nula.");

        biometrias.add(biometria);
    }

    public String getNumero() {
        return numero;
    }

    public boolean estaBloqueado() {
        return this.statusCartao == StatusCartao.BLOQUEADO ? true : false;
    }

    public void bloquear(Bloqueio bloqueio) {
        Assert.isTrue(bloqueio!=null, "[BUG] Bloqueio não deve estar nulo.");

        this.statusCartao = StatusCartao.BLOQUEADO;
        this.bloqueio = bloqueio;
    }
}
