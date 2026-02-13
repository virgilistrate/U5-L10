package virgilistrate.U5L10.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import virgilistrate.U5L10.entities.Viaggio;
import virgilistrate.U5L10.exceptions.ValidationException;
import virgilistrate.U5L10.payloads.ViaggioDTO;
import virgilistrate.U5L10.payloads.ViaggioPayload;
import virgilistrate.U5L10.payloads.ViaggioStatusPayload;
import virgilistrate.U5L10.services.ViaggiService;

import java.util.List;
import java.util.UUID;



@RestController
@RequestMapping("/viaggi")
public class ViaggiController {

    private final ViaggiService viaggiService;

    @Autowired
    public ViaggiController(ViaggiService viaggiService) {
        this.viaggiService = viaggiService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Viaggio create(@RequestBody @Validated ViaggioDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();

            throw new ValidationException(errorsList);
        } else {
            return this.viaggiService.save(payload);
        }
    }

    @GetMapping
    public Page<Viaggio> findAll(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "data") String orderBy,
                                 @RequestParam(defaultValue = "asc") String sortCriteria) {
        return this.viaggiService.findAll(page, size, orderBy, sortCriteria);
    }

    @GetMapping("/{viaggioId}")
    public Viaggio findById(@PathVariable UUID viaggioId) {
        return this.viaggiService.findById(viaggioId);
    }

    @PutMapping("/{viaggioId}")
    public Viaggio findByIdAndUpdate(@PathVariable UUID viaggioId, @RequestBody @Validated ViaggioPayload payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        } else {
            return this.viaggiService.findByIdAndUpdate(viaggioId, payload);
        }
    }

    @DeleteMapping("/{viaggioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID viaggioId) {
        this.viaggiService.findByIdAndDelete(viaggioId);
    }

    @PatchMapping("/{viaggioId}/stato")
    public Viaggio updateStatus(@PathVariable UUID viaggioId, @RequestBody @Validated ViaggioStatusPayload payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        } else {
            return this.viaggiService.updateStatus(viaggioId, payload.status());
        }
    }

    @PatchMapping("/{viaggioId}/assegna/{dipendenteId}")
    public Viaggio assegnaDipendente(@PathVariable UUID viaggioId, @PathVariable UUID dipendenteId) {
        return this.viaggiService.assegnaDipendente(viaggioId, dipendenteId);
    }
}
