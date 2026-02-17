package virgilistrate.U5L10.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        // questa classe serve per personalizzare la sicurezza del progetto

        // Alcune impostzioni di default che possiamo cambiare:

        httpSecurity.formLogin(formLogin -> formLogin.disable());
        // 1. servere per disattivare l' autenticazione tramite form, che non serve visto che la facciamo nel frontend

        httpSecurity.csrf(csrf -> csrf.disable());

        // 2. serve per disattivare gli attacchi da CSRF , che non ci serve e ci da fastidio al frontend
        httpSecurity.sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // 3. ci serve per lavoare tra sessioni

        // 4. cambiare il comportamento di default di alcune imostazioni
        httpSecurity.authorizeHttpRequests(req -> req.requestMatchers("/**").permitAll());
      //  5. serve per non avere error 401 ad ogni richiesta ma di poterlo impostre noi quando farlo


        return httpSecurity.build();

    }
    @Bean
    public PasswordEncoder getBCrypt() {
        return new BCryptPasswordEncoder(12);
    }

}

