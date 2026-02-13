package virgilistrate.U5L10.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import virgilistrate.U5L10.entities.Viaggio;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface ViaggiRepository extends JpaRepository<Viaggio, UUID> {
    boolean existsByDipendente_IdAndData(UUID dipendenteId, LocalDate data);
}
