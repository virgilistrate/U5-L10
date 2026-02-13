package virgilistrate.U5L10.payloads;

import jakarta.validation.constraints.NotNull;
import virgilistrate.U5L10.entities.ViaggioStatus;

public record ViaggioStatusPayload(
        @NotNull(message = "Lo stato è obbligatorio")
        ViaggioStatus status
) {
}
