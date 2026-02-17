package virgilistrate.U5L10.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import virgilistrate.U5L10.entities.Dipendente;
import virgilistrate.U5L10.exceptions.UnauthorizedException;


import java.util.Date;
import java.util.UUID;

@Component
public class JWTTools {
    @Value("${jwt.secret}")
    private String secret;

  // 1. Metodo Builder per creare il token
    public String generateToken(Dipendente dipendente) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis())) // Metto come data di emmessione la data di adesso, (in millisecondi)
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 3)) // applico una scadenza che voglio , sempre in millisecondi ma moltiplicando per comodita
                // in questo caso dura 3 giorni


                .subject(String.valueOf(dipendente.getId())) // impostiamo il possessore del token, in questo caso il dipendente, di cui diamo l' id, nb. non mettere
                // nome o cognome o dati sensibili in generale


                .signWith(Keys.hmacShaKeyFor(secret.getBytes())) // faccio genereare il secret del token
                .compact();
    }

    // 2. Metodo di verifica del token detto parse
    public void verifyToken(String token) {

        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parse(token);
        // verifica che il token sia valido, o non valido
            // se e non valido, o e scaduto, o e stato manipolato

       // se non e valido:
        } catch (Exception ex) {
            throw new UnauthorizedException("Token scaduto / sbagliato, reifare il login");
        }


    }

    public UUID extractIdFromToken(String token) {
        return UUID.fromString(Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject());
    }
}