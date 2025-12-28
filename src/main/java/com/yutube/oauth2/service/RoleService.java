package com.yutube.oauth2.service;

import com.yutube.oauth2.entity.Role;
import com.yutube.oauth2.enums.RoleName;
import com.yutube.oauth2.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    public Role createRole(RoleName roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() ->
                        roleRepository.save(
                                Role.builder()
                                        .name(roleName)
                                        .build()
                        ));
    }
}
