package com.yutube.oauth2.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.provisioning.UserDetailsManager;

import java.time.Duration;
import java.util.UUID;

@Configuration
public class InitializeData {

    @Bean
    public CommandLineRunner initData(
            RegisteredClientRepository clientRepository,
            UserDetailsManager userDetailsManager,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. Create a Test User
            if (!userDetailsManager.userExists("hamza0306")) {
                UserDetails user = User.builder()
                        .username("hamza0306")
                        .password("password123")
                        .roles("USER")
                        .build();
                userDetailsManager.createUser(user);
            }

            // 2. Create a Registered Client (The Application)
            if (clientRepository.findByClientId("video-client") == null) {
                RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientName("Video-Upload-Client")
                        .clientId("video-client")
                        .clientSecret(passwordEncoder.encode("secret"))
                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                        .redirectUri("http://localhost:7072/login/oauth2/code/video-client-oidc")
                        .postLogoutRedirectUri("http://localhost:7072/")
                        .scope(OidcScopes.OPENID)
                        .scope(OidcScopes.PROFILE)
                        .scope("video.upload")
                        .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofMinutes(30)).build())
                        .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                        .build();
                clientRepository.save(registeredClient);
            }
        };
    }
}
