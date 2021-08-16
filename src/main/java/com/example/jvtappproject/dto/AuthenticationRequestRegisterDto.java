package com.example.jvtappproject.dto;

import lombok.Data;

import java.util.List;

@Data
public class AuthenticationRequestRegisterDto {
    private String username;
    private String fullname;
    private String email;
    private String password;
    private String gender;
}
