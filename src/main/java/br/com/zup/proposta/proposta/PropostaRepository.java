package br.com.zup.proposta.proposta;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {

    Optional<Proposta> findByDocumento(String documento);
    List<Proposta> findByCartaoNullAndStatus(StatusProposta elegivel);
}
