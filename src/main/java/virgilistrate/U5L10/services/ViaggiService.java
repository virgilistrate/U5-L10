package virgilistrate.U5L10.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import virgilistrate.U5L10.entities.*;
import virgilistrate.U5L10.exceptions.BadRequestException;
import virgilistrate.U5L10.exceptions.NotFoundException;
import virgilistrate.U5L10.payloads.ViaggioDTO;
import virgilistrate.U5L10.payloads.ViaggioPayload;
import virgilistrate.U5L10.repositories.PrenotazioniRepository;
import virgilistrate.U5L10.repositories.ViaggiRepository;

import java.util.UUID;

@Service
@Slf4j
public class ViaggiService {

    private final ViaggiRepository viaggiRepository;
    private final DipendentiService dipendentiService;
    private final PrenotazioniRepository prenotazioniRepository;

    @Autowired
    public ViaggiService(ViaggiRepository viaggiRepository, DipendentiService dipendentiService, PrenotazioniRepository prenotazioniRepository) {
        this.viaggiRepository = viaggiRepository;
        this.dipendentiService = dipendentiService;
        this.prenotazioniRepository = prenotazioniRepository;
    }

    public Viaggio save(ViaggioDTO payload) {
        Viaggio newViaggio = new Viaggio(payload.destinazione(), payload.data(), payload.status());
        Viaggio saved = this.viaggiRepository.save(newViaggio);
        log.info("Il viaggio con id " + saved.getId() + " è stato salvato correttamente!");
        return saved;
    }

    public Page<Viaggio> findAll(int page, int size, String orderBy, String sortCriteria) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size,
                sortCriteria.equals("desc") ? Sort.by(orderBy).descending() : Sort.by(orderBy));

        return this.viaggiRepository.findAll(pageable);
    }

    public Viaggio findById(UUID id) {
        return this.viaggiRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Viaggio findByIdAndUpdate(UUID id, ViaggioPayload payload) {
        Viaggio found = this.findById(id);

        found.setDestinazione(payload.getDestinazione());
        found.setData(payload.getData());
        found.setStatus(payload.getStatus());

        Viaggio modified = this.viaggiRepository.save(found);
        log.info("Il viaggio con id " + modified.getId() + " è stato modificato correttamente!");
        return modified;
    }

    public void findByIdAndDelete(UUID id) {
        Viaggio found = this.findById(id);
        this.viaggiRepository.delete(found);
    }

    public Viaggio updateStatus(UUID viaggioId, ViaggioStatus newStatus) {
        Viaggio found = this.findById(viaggioId);
        found.setStatus(newStatus);
        Viaggio modified = this.viaggiRepository.save(found);
        log.info("Lo stato del viaggio con id " + modified.getId() + " è stato aggiornato a " + newStatus);
        return modified;
    }

    public Viaggio assegnaDipendente(UUID viaggioId, UUID dipendenteId) {
        Viaggio viaggio = this.findById(viaggioId);
        Dipendente dipendente = this.dipendentiService.findById(dipendenteId);


        boolean giaAssegnatoAltrove = this.viaggiRepository.existsByDipendente_IdAndData(dipendenteId, viaggio.getData());


        boolean giaPrenotatoStessaData = this.prenotazioniRepository.existsByDipendente_IdAndViaggio_Data(dipendenteId, viaggio.getData());

        if (giaAssegnatoAltrove || giaPrenotatoStessaData) {
            throw new BadRequestException("Il dipendente è già impegnato in un altro viaggio nella data " + viaggio.getData());
        }

        viaggio.setDipendente(dipendente);

        Viaggio modified = this.viaggiRepository.save(viaggio);
        log.info("Il dipendente " + dipendenteId + " è stato assegnato al viaggio " + viaggioId);
        return modified;
    }
}
