package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.config;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authotizHttpRequests)->authotizHttpRequests
                        .requestMatchers("/dicegame/player/auth/**").permitAll()
                        .requestMatchers("/update").hasRole(Role.PLAYER.name())
                        .requestMatchers("/newgame").hasRole(Role.PLAYER.name())
                        .requestMatchers("/allgames").hasRole(Role.PLAYER.name())
                        .requestMatchers("/allgames").hasRole(Role.ADMIN.name())
                        .requestMatchers("/allplayers").hasRole(Role.ADMIN.name())
                        .requestMatchers("/delete").hasRole(Role.ADMIN.name())
                        .requestMatchers("/playersranking").hasRole(Role.ADMIN.name())
                        .requestMatchers("/playersranking").hasRole(Role.PLAYER.name())
                        .requestMatchers("/averagesuccess").hasRole(Role.ADMIN.name())
                        .requestMatchers("/bestplayer").hasRole(Role.ADMIN.name())
                        .requestMatchers("/worstplayer").hasRole(Role.ADMIN.name())
                        .anyRequest().authenticated())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


}
