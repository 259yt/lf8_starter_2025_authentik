package de.szut.lf8_starter.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // مهم لاختبارات POST/PUT ومع Postman
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // Swagger/OpenAPI
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // صحّة وخلافه (اختياري)
                        .requestMatchers("/actuator/**").permitAll()

                        // API يحتاج مصادقة
                        .requestMatchers("/api/**").authenticated()

                        // أي شيء آخر
                        .anyRequest().permitAll()
                )

                .oauth2ResourceServer(oauth2 -> oauth2.jwt(CUSTOMIZER));

        return http.build();
    }

    private static final Customizer<org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer<HttpSecurity>.JwtConfigurer>
            CUSTOMIZER = Customizer.withDefaults();
}
