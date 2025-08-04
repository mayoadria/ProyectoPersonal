package adria.mayo.proyectopersonal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain webChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                auth -> auth
                        .requestMatchers("/registrar/**","/login", "/validar", "/",
                                "/cataleg","/css/**","/js/**","/Imagenes/**").permitAll()
                        .anyRequest().authenticated()

        ).formLogin(form -> form
                        .loginPage("/login")                     // <- tu página personalizada
                        .loginProcessingUrl("/login")            // <- Spring intercepta POST /login
                        .usernameParameter("nomUsuari")          // <- el name del input
                        .passwordParameter("password")           // <- el name del input
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                ).exceptionHandling(exception -> exception
                        .accessDeniedPage("/acceso-denegado")
                );

        return http.build();
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
