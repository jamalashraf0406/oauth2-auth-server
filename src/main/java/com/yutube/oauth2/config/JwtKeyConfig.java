package com.yutube.oauth2.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.yutube.oauth2.service.JwkKeyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;

@Configuration
public class JwtKeyConfig {

    @Bean
    public JWKSource<SecurityContext> jwkSource(JwkKeyService jwkKeyService) {
        return (selector, context) -> {
            JWKSet jwkSet = new JWKSet(jwkKeyService.getActiveKey());
            return selector.select(jwkSet);
        };
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }
}