package com.jewelleryshop.management.model.vendor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jewelleryshop.management.model.enums.Designation;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactDetails {
	
	private Designation designation;
	private String name;
	private String phoneNo;
	private String email; 
	private String businessCardUrl; 
	private String profileImageUrl; 
}
