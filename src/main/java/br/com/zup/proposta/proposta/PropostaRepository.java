package br.com.zup.proposta.proposta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PropostaRepository extends JpaRepository<Proposta, String> {
    Optional<Proposta> findByDocumento(String documento);
    List<Proposta> findByCartaoNullAndStatus(StatusProposta elegivel);
}
