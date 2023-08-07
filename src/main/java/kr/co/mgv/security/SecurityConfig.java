package kr.co.mgv.security;

import kr.co.mgv.user.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    private final AuthenticationService authenticationService;

    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                // todo
                .authorizeRequests((authorizeRequests) -> {
                    authorizeRequests.antMatchers("/admin/**").hasRole("ADMIN");
                    authorizeRequests.anyRequest().permitAll();
                })
                .httpBasic(Customizer.withDefaults()) // todo
                .formLogin(AbstractHttpConfigurer::disable) // todo
//                .loginPage("/user/login")
//                .usernameParameter("id")
//                .passwordParameter("password")
//                .loginProcessingUrl("/user/login")
//                .defaultSuccessUrl("/")
//                .failureUrl("/user/login?error=fail")
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true) // todo
                )
//            .and()
//                .exceptionHandling().authenticationEntryPoint((req, res, ex) -> res.sendRedirect("/user/login?error=denid"))
//            .and()
//                .exceptionHandling().authenticationEntryPoint((req, res, ex) -> res.sendRedirect("/user/login?error=forbidden"))
//            .and()
//                // auth service 등록
//                .userDetailsService(authenticationService)
                .build();
    }

    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // UserDetailService 반환값으로 authenticationService 반환
    @Bean
    public UserDetailsService userDetailsService() {
        return authenticationService;
    }

    // todo
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(authenticationService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // todo
    @Bean
    public AuthenticationManager customeAuthenticationManager() throws Exception {
        return new ProviderManager(Collections.singletonList(authenticationProvider()));
    }
}
