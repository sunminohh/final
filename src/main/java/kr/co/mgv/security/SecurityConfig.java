package kr.co.mgv.security;

import kr.co.mgv.user.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

//    private final AuthenticationService authenticationService;

    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin()
                .loginPage("/user/login")
                .usernameParameter("id")
                .passwordParameter("password")
                .loginProcessingUrl("/user/login")
                .defaultSuccessUrl("/")
                .failureUrl("/user/login?error=fail")
            .and()
                .logout()
                .logoutUrl("user/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
            .and()
                .exceptionHandling().authenticationEntryPoint((req, res, ex) -> res.sendRedirect("/user/login?error=denid"))
            .and()
                .exceptionHandling().authenticationEntryPoint((req, res, ex) -> res.sendRedirect("/user/login?error=forbidden"))
            .and()
                // auth service 등록
//                .userDetailsService(authenticationService)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
