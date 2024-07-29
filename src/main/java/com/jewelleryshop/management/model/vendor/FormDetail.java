package com.jewelleryshop.management.model.vendor;

import java.util.List;

import org.springframework.data.annotation.Id;

public class FormDetail {

	private String name;
	private String phoneNumber;
	private String aadhaarNumber;
	private String gstNo;
	private String address;
	private List<Client> clientList;
	private List<Branch> branchList;
}
