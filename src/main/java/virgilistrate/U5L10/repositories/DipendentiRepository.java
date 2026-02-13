package virgilistrate.U5L10.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import virgilistrate.U5L10.entities.Dipendente;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DipendentiRepository extends JpaRepository<Dipendente, UUID> {
    Optional<Dipendente> findByEmail(String email);

    Optional<Dipendente> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
