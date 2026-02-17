package virgilistrate.U5L10.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DipendenteRegisterDTO(
        @NotBlank @Size(min = 3, max = 50) String username,
        @NotBlank @Size(min = 2, max = 30) String name,
        @NotBlank @Size(min = 2, max = 30) String surname,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6, message = "La password deve avere almeno 6 caratteri")
        String password
) {}
