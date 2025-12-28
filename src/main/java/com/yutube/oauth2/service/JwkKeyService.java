package com.yutube.oauth2.service;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.RSAKey;
import com.yutube.oauth2.entity.JwkKeyEntity;
import com.yutube.oauth2.repository.JwkKeyRepository;
import com.yutube.oauth2.utils.KeyUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class JwkKeyService {

    private final JwkKeyRepository jwkKeyRepository;

    /**
     * Used for token signing
     */
    public RSAKey getActiveKey() {
        return jwkKeyRepository.findByActiveTrue()
                .map(this::toRsaKey)
                .orElseGet(this::generateAndSaveKey);
    }

    /**
     * Used for JWKS endpoint (old + active keys)
     */
    public List<RSAKey> loadAllKeys() {
        return jwkKeyRepository.findAll()
                .stream()
                .map(this::toRsaKey)
                .toList();
    }

    /**
     * Rotate key (recommended via scheduler)
     */
    public void rotateKey() {
        jwkKeyRepository.findByActiveTrue()
                .ifPresent(key -> {
                    key.setActive(false);
                    jwkKeyRepository.save(key);
                });

        generateAndSaveKey();
    }

    private RSAKey generateAndSaveKey() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair keyPair = generator.generateKeyPair();

            String kid = UUID.randomUUID().toString();

            JwkKeyEntity entity = new JwkKeyEntity(
                    kid,
                    encode(keyPair.getPublic().getEncoded()),
                    encode(keyPair.getPrivate().getEncoded()),
                    true,
                    LocalDateTime.now()
            );

            jwkKeyRepository.save(entity);

            return buildRsaKey(kid, keyPair);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate JWK", e);
        }
    }

    private RSAKey toRsaKey(JwkKeyEntity entity) {
        try {
            RSAPublicKey publicKey =
                    (RSAPublicKey) KeyUtil.parsePublicKey(entity.getPublicKey());
            RSAPrivateKey privateKey =
                    (RSAPrivateKey) KeyUtil.parsePrivateKey(entity.getPrivateKey());

            return new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID(entity.getKeyId())
                    .algorithm(JWSAlgorithm.RS256)
                    .build();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse RSA key", e);
        }
    }

    private RSAKey buildRsaKey(String kid, KeyPair keyPair) {
        return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .keyID(kid)
                .algorithm(JWSAlgorithm.RS256)
                .build();
    }

    private String encode(byte[] value) {
        return Base64.getEncoder().encodeToString(value);
    }
}
