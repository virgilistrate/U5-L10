package virgilistrate.U5L10.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DipendentePayload {

    @NotBlank(message = "Lo username è obbligatorio")
    @Size(min = 3, max = 50, message = "Lo username deve essere tra 3 e 50 caratteri")
    private String username;

    @NotBlank(message = "Il nome proprio è obbligatorio")
    @Size(min = 2, max = 30, message = "Il nome  deve essere tra i 2 e i 30 caratteri")
    private String name;

    @NotBlank(message = "Il cognome è obbligatorio")
    @Size(min = 2, max = 30, message = "Il cognome deve essere tra i 2 e i 30 caratteri")
    private String surname;

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "L'indirizzo email inserito non è  corretto!")
    private String email;

    public DipendentePayload(String username, String name, String surname, String email) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }
}
