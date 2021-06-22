package br.com.zup.proposta.cartao.carteira_digital;

import br.com.zup.proposta.cartao.Cartao;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "carteiras")
public class CarteiraDigital {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private @NotNull String email;
    private @NotNull String emissor;
    @ManyToOne
    private @NotNull Cartao cartao;

    @Deprecated
    public CarteiraDigital() {
    }

    public CarteiraDigital(String email, String emissor, Cartao cartao) {
        this.email = email;
        this.emissor = emissor;
        this.cartao = cartao;
    }

    public String getId() {
        return id;
    }
}
