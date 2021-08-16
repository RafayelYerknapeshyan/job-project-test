package com.example.jvtappproject.rest;

import com.example.jvtappproject.dto.AuthenticationRequestRegisterDto;
import com.example.jvtappproject.model.Gender;
import com.example.jvtappproject.model.Role;
import com.example.jvtappproject.model.User;
import com.example.jvtappproject.repository.RoleRepository;
import com.example.jvtappproject.security.jwt.JwtTokenProvider;
import com.example.jvtappproject.sevice.UserService;
import com.example.jvtappproject.sevice.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthenticationRestControllerV2 {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private UserService userService;

    @Autowired
    public AuthenticationRestControllerV2(
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @RequestMapping (value = "/api/v2/auth/register")
    public ResponseEntity register(@RequestBody AuthenticationRequestRegisterDto requestRegisterDto) {
        System.out.println("New User");
        String username = requestRegisterDto.getUsername();

        User user = new User();
        user.setUsername(requestRegisterDto.getUsername());
        String[] name = requestRegisterDto.getFullname().split(" ");
        user.setFirstName(name[0]);
        user.setLastName(name[1]);
        user.setEmail(requestRegisterDto.getEmail());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode(requestRegisterDto.getPassword());
        System.out.println(password);
        user.setPassword(password);
        String gender = requestRegisterDto.getGender();
        if(gender.equals("Female")) {
                user.setGender(Gender.FEMALE);
            } else if(gender.equals("Male")) {
                user.setGender(Gender.MALE);
            } else  user.setGender(Gender.UNSPECIFIED);


        System.out.println("user registered");
        userService.register(user);


//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestRegisterDto.getPassword()));
//            user = userService.findByUsername(username);
//
//            if (user == null) {
//                throw new UsernameNotFoundException("User with username: \" + username + \" not found");
//            }
//
//            String token = jwtTokenProvider.createToken(username, user.getRoles());
//            Map<Object, Object> response = new HashMap<>();
//            response.put("username", username);
//            response.put("token", token);
//            return ResponseEntity.ok(response);
//        } catch (AuthenticationException e) {
//            throw new BadCredentialsException("Invalid username or password");
//        }
        Map<Object, Object> response = new HashMap<>();
        response.put("username", username);
        return ResponseEntity.ok(response);
    }
}
