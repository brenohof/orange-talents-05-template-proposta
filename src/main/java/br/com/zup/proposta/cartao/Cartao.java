package br.com.zup.proposta.cartao;

import br.com.zup.proposta.cartao.carteira_digital.CarteiraDigital;
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
    private @NotNull String numero;
    private @NotNull LocalDateTime emitidoEm;
    private @NotNull String titular;
    @OneToOne
    private @NotNull Proposta proposta;
    private @NotNull BigDecimal limite;
    @OneToMany(mappedBy = "cartao", cascade = CascadeType.MERGE)
    private Set<Biometria> biometrias = new HashSet<>();
    @OneToOne(mappedBy = "cartao", cascade = CascadeType.MERGE)
    private Bloqueio bloqueio;
    @Enumerated(EnumType.STRING)
    private StatusCartao statusCartao = StatusCartao.LIBERADO;
    @OneToMany(mappedBy = "cartao", cascade = CascadeType.MERGE)
    private Set<CarteiraDigital> carteiras = new HashSet<>();

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

    public void associaCarteira(CarteiraDigital carteiraDigital) {
        Assert.isTrue(carteiraDigital!=null, "[BUG] Carteira não deveria ser nula.");

        carteiras.add(carteiraDigital);
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
