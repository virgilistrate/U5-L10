package virgilistrate.U5L10.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import virgilistrate.U5L10.entities.*;
import virgilistrate.U5L10.exceptions.BadRequestException;
import virgilistrate.U5L10.exceptions.NotFoundException;
import virgilistrate.U5L10.payloads.PrenotazioneDTO;
import virgilistrate.U5L10.repositories.PrenotazioniRepository;
import virgilistrate.U5L10.repositories.ViaggiRepository;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class PrenotazioniService {

    private final PrenotazioniRepository prenotazioniRepository;
    private final ViaggiRepository viaggiRepository;
    private final DipendentiService dipendentiService;

    @Autowired
    public PrenotazioniService(PrenotazioniRepository prenotazioniRepository, ViaggiRepository viaggiRepository, DipendentiService dipendentiService) {
        this.prenotazioniRepository = prenotazioniRepository;
        this.viaggiRepository = viaggiRepository;
        this.dipendentiService = dipendentiService;
    }

    public Prenotazione save(PrenotazioneDTO payload) {
        Viaggio viaggio = this.viaggiRepository.findById(payload.viaggioId())
                .orElseThrow(() -> new NotFoundException(payload.viaggioId()));

        Dipendente dipendente = this.dipendentiService.findById(payload.dipendenteId());


        boolean haGiaPrenotatoStessaData = this.prenotazioniRepository.existsByDipendente_IdAndViaggio_Data(dipendente.getId(), viaggio.getData());
        boolean haGiaViaggioAssegnatoStessaData = this.viaggiRepository.existsByDipendente_IdAndData(dipendente.getId(), viaggio.getData());

        if (haGiaPrenotatoStessaData || haGiaViaggioAssegnatoStessaData) {
            throw new BadRequestException("Il dipendente è già impegnato con un altro viaggio nella data " + viaggio.getData());
        }



        Prenotazione newPren = new Prenotazione(viaggio, dipendente, payload.dataRichiesta(), payload.note(), payload.preferenze());
        Prenotazione saved = this.prenotazioniRepository.save(newPren);

        log.info("La prenotazione con id " + saved.getId() + " è stata salvata correttamente!");
        return saved;
    }

    public List<Prenotazione> findAll() {
        return this.prenotazioniRepository.findAll();
    }

    public Prenotazione findById(UUID id) {
        return this.prenotazioniRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public void findByIdAndDelete(UUID id) {
        Prenotazione found = this.findById(id);
        this.prenotazioniRepository.delete(found);
    }
}
