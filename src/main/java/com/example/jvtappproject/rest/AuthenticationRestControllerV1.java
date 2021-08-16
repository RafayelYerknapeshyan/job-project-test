package com.example.jvtappproject.rest;

import com.example.jvtappproject.dto.AuthenticationRequestLoginDto;
import com.example.jvtappproject.dto.AuthenticationRequestRegisterDto;
import com.example.jvtappproject.model.User;
import com.example.jvtappproject.security.jwt.JwtTokenProvider;
import com.example.jvtappproject.sevice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
//@RequestMapping(value = "/api/v1/auth/login")
public class AuthenticationRestControllerV1 {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private UserService userService;

    @Autowired
    public AuthenticationRestControllerV1(
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }
    @GetMapping(value = "/api/v1/auth/login")
    public ResponseEntity login(@RequestBody AuthenticationRequestLoginDto requestLoginDto) {
        try {
            String username = requestLoginDto.getUsername();
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,
//                    requestLoginDto.getPassword()));
            System.out.println("Test");
            User user = userService.findByUsername(username);

            if (user == null) {
                throw new UsernameNotFoundException("User with username: \" + username + \" not found");
            }

            String token = jwtTokenProvider.createToken(username, user.getRoles());
            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
