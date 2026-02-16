package virgilistrate.U5L10.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import virgilistrate.U5L10.exceptions.UnauthorizedException;

import java.io.IOException;

@Component
public class JWTCheckerFilter extends OncePerRequestFilter {

    private final JWTTools jwtTools;

    @Autowired
    public JWTCheckerFilter(JWTTools jwtTools) {
        this.jwtTools = jwtTools;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // prende l'header "Authorization" dalla richiesta
        String authHeader = request.getHeader("Authorization");

        // se l'header non esiste oppure non inizia con "Bearer "
        // allora significa che il token non è stato inserito correttamente e la richiesta viene bloccata
        if (authHeader == null || !authHeader.startsWith("Bearer "))

            // toglie la parte "Bearer " e rimane solo il token vero e proprio
            throw new UnauthorizedException("Inserire il token nell'Authorization header nel formato corretto");

        // verifica se il token è valido (firma corretta, non scaduto, ecc.)
        // se non è valido, viene lanciata un'eccezione e la richiesta non va avanti
        String accessToken = authHeader.replace("Bearer ", "");

        // se il token è corretto , fa continuare la richiesta verso gli altri filtri o il controller
        jwtTools.verifyToken(accessToken);


        filterChain.doFilter(request, response);


    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {


        // serve per dire a Spring quando NON deve applicare questo filtro
        // in questo caso tutte le rotte che iniziano con /auth/ vengono escluse
        // (esempio: /auth/login o /auth/register, perché lì di solito il token ancora non c'è)
        return new AntPathMatcher().match("/auth/**", request.getServletPath());

    }
}