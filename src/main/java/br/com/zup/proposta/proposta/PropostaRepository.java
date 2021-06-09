package br.com.zup.proposta.proposta;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PropostaRepository extends CrudRepository<Proposta, Long> {
    Optional<Proposta> findByDocumento(String documento);
    List<Proposta> findByCartaoNullAndStatus(StatusProposta elegivel);
}
