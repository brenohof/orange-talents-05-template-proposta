package br.com.zup.proposta.proposta;

import br.com.zup.proposta.cartao.Cartao;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
@Table(name = "propostas")
public class Proposta {
    @Id @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @NotNull @Column(unique = true)
    private String documento;
    @NotNull
    private String email;
    @NotNull
    private String nome;
    @NotNull
    private String endereco;
    @NotNull
    private BigDecimal salario;
    @Enumerated(EnumType.STRING)
    private StatusProposta status;
    @OneToOne(mappedBy = "proposta", cascade = CascadeType.MERGE)
    private Cartao cartao;

    @Deprecated
    public Proposta() {
    }

    public Proposta(@NotBlank String documento, @NotBlank @Email String email,
                    @NotBlank String nome, @NotBlank String endereco,
                    @NotNull @Positive BigDecimal salario) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
        this.cartao = null;
    }

    public String getId() {
        return this.id;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public void defineStatus(StatusProposta status) {
        this.status = status;
    }

    public void associaCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public StatusProposta getStatus() {
        return this.status;
    }
}
