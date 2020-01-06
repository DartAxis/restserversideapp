package ru.dartinc.restserversideapp1.dto;

import lombok.Data;

@Data
public class AuthenticationRequestDto {

    private String username;
    private String password;
}
