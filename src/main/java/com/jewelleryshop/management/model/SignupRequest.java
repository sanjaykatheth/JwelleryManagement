package com.jewelleryshop.management.model;

import lombok.Data;

@Data
public class SignupRequest {
	private String userName;
	private String email;

	private String role;

	private String password;

}