package com.jewelleryshop.management.model.vendor;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class BankDetails {
	@Id
	private String id;
	private String bankName;
	private String ifscCode;
	private String accNo;
	private String reAccNo;
	
}
