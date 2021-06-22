package br.com.zup.proposta.cartao.carteira_digital;

import br.com.zup.proposta.cartao.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarteiraRepository extends JpaRepository<CarteiraDigital, String> {
    public CarteiraDigital findByCartaoAndEmissor(Cartao cartao, EmissorCarteira emissor);
}
