package virgilistrate.U5L10.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import virgilistrate.U5L10.entities.Prenotazione;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface PrenotazioniRepository extends JpaRepository<Prenotazione, UUID> {


    boolean existsByDipendente_IdAndViaggio_Data(UUID dipendenteId, LocalDate data);


    boolean existsByDipendente_IdAndDataRichiesta(UUID dipendenteId, LocalDate dataRichiesta);
}
