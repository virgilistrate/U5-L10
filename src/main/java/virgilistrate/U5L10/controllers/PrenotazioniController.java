package virgilistrate.U5L10.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import virgilistrate.U5L10.entities.Prenotazione;
import virgilistrate.U5L10.exceptions.ValidationException;
import virgilistrate.U5L10.payloads.PrenotazioneDTO;
import virgilistrate.U5L10.services.PrenotazioniService;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioniController {

    private final PrenotazioniService prenotazioniService;

    @Autowired
    public PrenotazioniController(PrenotazioniService prenotazioniService) {
        this.prenotazioniService = prenotazioniService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Prenotazione create(@RequestBody @Validated PrenotazioneDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();

            throw new ValidationException(errorsList);
        } else {
            return this.prenotazioniService.save(payload);
        }
    }

    @GetMapping
    public List<Prenotazione> findAll() {
        return this.prenotazioniService.findAll();
    }

    @GetMapping("/{prenotazioneId}")
    public Prenotazione findById(@PathVariable UUID prenotazioneId) {
        return this.prenotazioniService.findById(prenotazioneId);
    }

    @DeleteMapping("/{prenotazioneId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID prenotazioneId) {
        this.prenotazioniService.findByIdAndDelete(prenotazioneId);
    }
}
