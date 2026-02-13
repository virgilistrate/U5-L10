package virgilistrate.U5L10.payloads;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import virgilistrate.U5L10.entities.ViaggioStatus;

import java.time.LocalDate;

public class ViaggioPayload {

    @NotBlank(message = "La destinazione è un campo obbligatorio")
    @Size(min = 2, max = 120, message = "La destinazione deve essere tra 2 e 120 caratteri")
    private String destinazione;

    @NotNull(message = "La data è obbligatoria")
    @FutureOrPresent(message = "La data del viaggio non può essere nel passato")
    private LocalDate data;

    @NotNull(message = "Lo stato è obbligatorio")
    private ViaggioStatus status;

    public ViaggioPayload(String destinazione, LocalDate data, ViaggioStatus status) {
        this.destinazione = destinazione;
        this.data = data;
        this.status = status;
    }

    public String getDestinazione() {
        return destinazione;
    }

    public LocalDate getData() {
        return data;
    }

    public ViaggioStatus getStatus() {
        return status;
    }
}
