package com.yutube.oauth2.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

//@Component
@Slf4j
public class DaoAuthProvider extends DaoAuthenticationProvider {

    @Override
    protected void additionalAuthenticationChecks(
            UserDetails userDetails,
            UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {

        super.additionalAuthenticationChecks(userDetails, authentication);

        if (!userDetails.isEnabled()) {
            throw new DisabledException("User account is disabled");
        }

        // Example: add custom rule
        if (authentication.getCredentials() == null) {
            throw new BadCredentialsException("Credentials are missing");
        }

        // Example: login audit
        logLoginAttempt(userDetails.getUsername());
    }

    private void logLoginAttempt(String username) {
        log.info("Login attempt by user: " + username);
    }
}
