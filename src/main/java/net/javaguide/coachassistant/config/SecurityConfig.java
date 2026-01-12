package net.javaguide.coachassistant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import net.javaguide.coachassistant.security.JwtAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration; // 👈 Nouvel import
import org.springframework.web.cors.CorsConfigurationSource; // 👈 Nouvel import
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // 👈 Nouvel import
import java.util.List; // 👈 Nouvel import

@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter; // 👈 Injection

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Login/Register ouverts
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN") // Admin sécurisé
                        .anyRequest().authenticated() // Le reste fermé
                )
                // 👇 ON AJOUTE LE FILTRE ICI !
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // On définit les règles CORS globalement pour Spring Security
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 🌍 Autoriser toutes les origines (Vercel, localhost, etc.)
        // Pour la prod, idéalement on mettrait juste ton URL Vercel, mais "*" est plus simple pour debug
        configuration.setAllowedOrigins(List.of("*"));

        // 🚀 Autoriser toutes les méthodes HTTP
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 📨 Autoriser tous les en-têtes (Authorization, Content-Type, etc.)
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Appliquer à toutes les routes
        return source;
    }
}