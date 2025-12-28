package com.yutube.oauth2.resource;

import com.yutube.oauth2.requests.UserRequest;
import com.yutube.oauth2.response.UserResponse;
import com.yutube.oauth2.service.H2UserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserControllerV1 implements UserResourceV1{

    private final H2UserDetailsService userService;

    @Override
    public ResponseEntity<UserResponse> createUser(UserRequest request) {
        UserResponse createdUser = this.userService.createUser(request);
        URI location = URI.create(String.format("/users/%s", createdUser.uuid()));
        return ResponseEntity.created(location).body(createdUser);
    }

    @Override
    public ResponseEntity<UserResponse> getUserByUserId(UUID userId) {
        UserResponse user = userService.getUserByUserId(userId);
        return ResponseEntity.ok(user);
    }
}
