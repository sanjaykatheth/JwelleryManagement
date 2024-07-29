package com.jewelleryshop.management.model.vendor;

import lombok.Data;

@Data
public class BankDetails {
	private String id;
	private String bankName;
	private String ifscCode;
	private String accNo;
	private String reAccNo;
	
}
