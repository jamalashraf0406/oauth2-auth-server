package com.yutube.oauth2.service;

import com.yutube.oauth2.entity.Role;
import com.yutube.oauth2.entity.User;
import com.yutube.oauth2.enums.RoleName;
import com.yutube.oauth2.exception.ResourceNotFoundException;
import com.yutube.oauth2.exception.UsernameExistException;
import com.yutube.oauth2.repository.UserRepository;
import com.yutube.oauth2.requests.UserRequest;
import com.yutube.oauth2.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class H2UserDetailsService implements UserDetailsManager {

    private final UserRepository userRepository;
    private final PasswordEncoder bcrytPasswordEncoder;
    private final RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading the user details...");

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("Username: %s not found!", username)));

        /*hasRole("ADMIN")
        hasAuthority("ROLE_ADMIN")*/

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(
                        user.getRoles().stream()
                                .map(r -> new SimpleGrantedAuthority(r.getName().name()))
                                .toList()
                )
                /*.roles(
                        user.getRoles().stream()
                                .map(r->r.getName().replace("ROLE_", ""))
                                .toArray(String[]::new))*/
                .disabled(!user.isEnabled())
                .build();
    }

    @Override
    public void createUser(UserDetails user) {
        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        this.createUser(
                new UserRequest(user.getUsername(),
                        user.getPassword(), user.isEnabled(), RoleName.fromString(roles.get(0))));
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public UserResponse createUser(UserRequest userRequest) {
        Optional<User> user = userRepository.findByUsername(userRequest.username());
        if (user.isPresent()) {
            throw new UsernameExistException("Username is already used");
        }

        Set<Role> roles = Set.of(roleService.createRole(userRequest.roleName()));

        String password = bcrytPasswordEncoder.encode(userRequest.password());
        User newUser = User.builder()
                .username(userRequest.username())
                .password(password)
                .roles(roles)
                .enabled(true)
                .build();

        newUser = userRepository.save(newUser);
        return new UserResponse(newUser.getUuid(), newUser.getUsername(), newUser.isEnabled());
    }

    public UserResponse getUserByUserId(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found"));
        return new UserResponse(user.getUuid(), user.getUsername(), user.isEnabled());
    }

    private Collection<? extends GrantedAuthority> mapRoles(Set<Role> roles) {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority(r.getName().name()))
                .toList();
    }

    private Set<Role> resolveRoles(Collection<? extends GrantedAuthority> authorities) {
        Set<Role> roles = new HashSet<>();

        for (GrantedAuthority authority : authorities) {
            RoleName roleName = RoleName.valueOf(authority.getAuthority());
            roles.add(Role.builder().name(roleName).build());
        }
        return roles;
    }
}
