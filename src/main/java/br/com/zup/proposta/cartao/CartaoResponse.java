package br.com.zup.proposta.cartao;

import br.com.zup.proposta.proposta.Proposta;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CartaoResponse {
    private LocalDateTime emitidoEm;
    private String titular;
    private Long idProposta;
    private BigDecimal limite;
    private String id;

    public CartaoResponse(LocalDateTime emitidoEm, String titular, Long idProposta, BigDecimal limite, String id) {
        this.emitidoEm = emitidoEm;
        this.titular = titular;
        this.idProposta = idProposta;
        this.limite = limite;
        this.id = id;
    }

    public Cartao toModel(Proposta proposta) {
        Assert.isTrue(proposta != null, "[BUG] Essa proposta não existe!");
        return new Cartao(id, emitidoEm, titular, proposta, limite);
    }
}
