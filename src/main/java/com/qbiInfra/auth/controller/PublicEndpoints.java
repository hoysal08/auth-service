package com.qbiInfra.auth.controller;

import com.qbiInfra.auth.authUtils.JwtUtils;
import com.qbiInfra.auth.authUtils.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("public")
public class PublicEndpoints {

    @GetMapping("test")
    ResponseEntity<String> getPublic() {
        return ResponseEntity.ok("OK");
    }


    @GetMapping("/getUser")
    public ResponseEntity<User> getUser(@RequestHeader("Authorization") String authorizationHeader) {
        // Extract the token from the Authorization header
        String token = extractToken(authorizationHeader);

        // Validate the token
        if (JwtUtils.validateToken(token)) {
            // If the token is valid, return the user
            User user = JwtUtils.getUserFromToken(token);
            return ResponseEntity.ok(user);
        } else {
            // If the token is not valid, return 401 Unauthorized
            return ResponseEntity.status(401).build();
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extractToken(authorizationHeader);

        if (JwtUtils.validateToken(token)) {
            return ResponseEntity.ok(true);
        } else {

            return ResponseEntity.status(401).body(false);
        }
    }

    private String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
