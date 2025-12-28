package com.yutube.oauth2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "oauth2_jwk")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwkKeyEntity {

    @Id
    private String keyId;

    @Lob
    @Column(nullable = false)
    private String publicKey;

    @Lob
    @Column(nullable = false)
    private String privateKey;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
