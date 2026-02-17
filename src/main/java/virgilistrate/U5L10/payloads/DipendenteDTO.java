package virgilistrate.U5L10.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DipendenteDTO(
        @NotBlank(message = "Lo username è obbligatorio")
        @Size(min = 3, max = 50, message = "Lo username deve essere tra 3 e 50 caratteri")
        String username,
        @NotBlank(message = "Il nome è obbligatorio")
        @Size(min = 2, max = 30, message = "Il nome  deve essere tra 2 e 30 caratteri")
        String name,
        @NotBlank(message = "Il cognome è obbligatorio")
        @Size(min = 2, max = 30, message = "Il cognome deve essere tra 2 e 30 caratteri")
        String surname,
        @NotBlank(message = "L'email è obbligatoria")
        @Email(message = "L'indirizzo email inserito non è corretto!")
        String email,
        @NotBlank(message = "la password è obbligatoria")
        String password
) {
}
