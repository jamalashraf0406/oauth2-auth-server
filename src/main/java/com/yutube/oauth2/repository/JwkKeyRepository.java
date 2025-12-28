package com.yutube.oauth2.repository;

import com.yutube.oauth2.entity.JwkKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwkKeyRepository extends JpaRepository<JwkKeyEntity, String> {

    Optional<JwkKeyEntity> findByActiveTrue();
}