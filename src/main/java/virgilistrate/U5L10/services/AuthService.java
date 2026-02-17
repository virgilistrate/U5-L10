package virgilistrate.U5L10.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import virgilistrate.U5L10.entities.Dipendente;
import virgilistrate.U5L10.exceptions.UnauthorizedException;
import virgilistrate.U5L10.payloads.LoginDTO;
import virgilistrate.U5L10.repositories.DipendentiRepository;
import virgilistrate.U5L10.security.JWTTools;

@Service
public class AuthService {

    private final DipendentiRepository dipendentiRepository;
    private final JWTTools jwtTools;
    private final PasswordEncoder passwordEncoder;

    public AuthService(DipendentiRepository dipendentiRepository, JWTTools jwtTools, PasswordEncoder passwordEncoder) {
        this.dipendentiRepository = dipendentiRepository;
        this.jwtTools = jwtTools;
        this.passwordEncoder = passwordEncoder;
    }

    public String checkCredentialsAndGenerateToken(LoginDTO body) {
        Dipendente dipendente = dipendentiRepository.findByEmail(body.email())
                .orElseThrow(() -> new UnauthorizedException("Credenziali non valide"));

        if (!passwordEncoder.matches(body.password(), dipendente.getPassword())) {
            throw new UnauthorizedException("Credenziali non valide");
        }

        return jwtTools.generateToken(dipendente);
    }
}
