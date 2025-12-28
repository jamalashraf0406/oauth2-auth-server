package com.yutube.oauth2.config;

import com.yutube.oauth2.service.H2UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig {

    private final H2UserDetailsService userDetailsManager;
    //private final PasswordEncoder bcryptPasswordEncoder;

    /*@Bean
    public DaoAuthProvider DaoAuthProvider() {

        DaoAuthProvider provider = new DaoAuthProvider();

        provider.setUserDetailsService(userDetailsManager);
        provider.setPasswordEncoder(bcryptPasswordEncoder);

        return provider;
    }*/

    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsManager);
        provider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(provider);
    }
}

