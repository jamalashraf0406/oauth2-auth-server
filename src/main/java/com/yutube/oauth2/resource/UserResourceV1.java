package com.yutube.oauth2.resource;

import com.yutube.oauth2.requests.UserRequest;
import com.yutube.oauth2.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/users")
public interface UserResourceV1 {

    @PostMapping
    ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest);

    @GetMapping("/{userId}")
    ResponseEntity<UserResponse> getUserByUserId(@PathVariable("userId") UUID uuid);
}
