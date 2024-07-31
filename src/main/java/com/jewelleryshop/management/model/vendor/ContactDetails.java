package com.jewelleryshop.management.model.vendor;

import org.springframework.data.annotation.Id;

import com.jewelleryshop.management.model.enums.Designation;

import lombok.Data;

@Data
public class ContactDetails {
	
	private Designation designation;
	private String name;
	private String phoneNo;
	private String email; // Assuming you meant "email" instead of "emai"
	private String businessCardUrl; // Using String for URL
	private String profileImageUrl; // Using String for URL
}
