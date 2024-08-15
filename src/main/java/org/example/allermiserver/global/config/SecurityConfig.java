package org.example.allermiserver.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.PostMapping;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests((authorize) ->
                authorize.requestMatchers("/**").permitAll()
        );
        http.formLogin((formLogin) -> formLogin.loginPage("/login")
                .defaultSuccessUrl("/"));
        http.logout(logout -> logout.logoutUrl("/logout"));

        return http.build();
    }

    @PostMapping("/auth/login")
    public String handleLogin(String username, String password) {
        // 직접 로그인 처리를 구현하는 경우
        // 일반적으로는 Spring Security에 의해 처리되므로 필요하지 않음
        return "redirect:/";
    }
}
