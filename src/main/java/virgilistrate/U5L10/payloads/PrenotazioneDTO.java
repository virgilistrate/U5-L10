package virgilistrate.U5L10.payloads;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record PrenotazioneDTO(
        @NotNull(message = "Il viaggioId è obbligatorio")
        UUID viaggioId,
        @NotNull(message = "Il dipendenteId è obbligatorio")
        UUID dipendenteId,
        @NotNull(message = "La data di richiesta è obbligatoria")
        LocalDate dataRichiesta,
        String note,
        String preferenze
) {
}
