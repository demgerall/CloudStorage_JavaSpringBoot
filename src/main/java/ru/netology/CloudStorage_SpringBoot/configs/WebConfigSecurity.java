package ru.netology.CloudStorage_SpringBoot.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.netology.CloudStorage_SpringBoot.security.jwt_security.JWTAuthenticationEntryPoint;
import ru.netology.CloudStorage_SpringBoot.security.jwt_security.JWTTokenFilter;

//  Настраивает Spring Security для веб-приложения

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebConfigSecurity {

    //  Настраивает цепочку фильтров безопасности
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JWTTokenFilter jwtTokenFilter, JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint) throws Exception {
        http
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/login", "/signup").permitAll()
                        .anyRequest().hasAnyRole("USER")
                )
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login"))
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    //  Создает менеджер аутентификации
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //  Создает провайдера аутентификации,
    //  используя DaoAuthenticationProvider (обычно для аутентификации с базой данных)
    //  и задает кодировщик паролей
    @Bean
    public DaoAuthenticationProvider authenticationProvider(org.springframework.security.core.userdetails.UserDetailsService userDetailsServiceImpl) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsServiceImpl);
        daoAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());
        return daoAuthenticationProvider;
    }

    //  Создает кодировщик паролей BCryptPasswordEncoder
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
