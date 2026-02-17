package virgilistrate.U5L10.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import virgilistrate.U5L10.entities.Dipendente;
import virgilistrate.U5L10.exceptions.BadRequestException;
import virgilistrate.U5L10.payloads.DipendenteDTO;
import virgilistrate.U5L10.payloads.LoginDTO;
import virgilistrate.U5L10.payloads.LoginResponseDTO;
import virgilistrate.U5L10.services.AuthService;
import virgilistrate.U5L10.services.DipendentiService;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final DipendentiService dipendentiService;

    public AuthController(AuthService authService, DipendentiService dipendentiService) {
        this.authService = authService;
        this.dipendentiService = dipendentiService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Validated LoginDTO body, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors()
                    .stream()
                    .map(err -> err.getDefaultMessage())
                    .toList();

            throw new BadRequestException(String.join(". ", errors));
        }

        String token = this.authService.checkCredentialsAndGenerateToken(body);
        return new LoginResponseDTO(token);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Dipendente register(@RequestBody @Validated DipendenteDTO payload, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors()
                    .stream()
                    .map(err -> err.getDefaultMessage())
                    .toList();

            throw new BadRequestException(String.join(". ", errors));
        }

        return this.dipendentiService.save(payload);
    }
}
