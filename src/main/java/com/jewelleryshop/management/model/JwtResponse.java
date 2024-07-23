package com.jewelleryshop.management.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    private final List<String> roles;
    private String token;
    private Long id;
    private String username;
    private String email;
}